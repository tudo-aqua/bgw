/*
 * Copyright 2022-2023 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress(
    "unused", "MemberVisibilityCanBePrivate", "GlobalCoroutineUsage", "EXPERIMENTAL_IS_NOT_ENABLED")

package tools.aqua.bgw.net.client

import com.fasterxml.jackson.databind.JsonMappingException
import java.lang.reflect.Method
import java.net.URI
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.jvm.javaMethod
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.annotations.GameActionClassProcessor.getAnnotatedClasses
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.annotations.GameActionReceiverProcessor.getAnnotatedReceivers
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.notification.SpectatorJoinedNotification
import tools.aqua.bgw.net.common.request.CreateGameMessage
import tools.aqua.bgw.net.common.request.JoinGameMessage
import tools.aqua.bgw.net.common.request.LeaveGameMessage
import tools.aqua.bgw.net.common.request.SpectatorJoinGameMessage
import tools.aqua.bgw.net.common.response.*

/**
 * [BoardGameClient] for network communication in BGW applications. Inherit from this class and
 * override its open functions. By default, these do nothing if not overridden.
 *
 * @property playerName The player name.
 * @property host The server ip or hostname.
 * @param secret The server secret.
 * @param networkLoggingBehavior The desired network logging verbosity. Default:
 * [NetworkLogging.NO_LOGGING].
 */
@Suppress("LeakingThis")
open class BoardGameClient
protected constructor(
    val playerName: String,
    val host: String,
    secret: String,
    networkLoggingBehavior: NetworkLogging = NetworkLogging.NO_LOGGING
) {

  /** WebSocketClient handling network communication. */
  private val wsClient: BGWWebSocketClient

  /** All classes annotated with @GameActionClass. */
  private var gameActionClasses: Set<Class<out GameAction>>? = null

  /** Mapper for incoming message handlers. */
  private var gameActionReceivers: Map<Class<out GameAction>, Method>? = null

  /** Coroutines job for initializing annotation processing. */
  private val initializationJob: Job

  /** Queue for incoming [GameActionMessage]s. */
  private val msgQueue: ConcurrentLinkedQueue<GameActionMessage> = ConcurrentLinkedQueue()

  /** Mapping for [GameActionMessage]s onto receiver function and deserialized payload. */
  private val msgMapping: MutableMap<GameActionMessage, Pair<Method, GameAction>> = mutableMapOf()

  /** Mutex for [msgQueue] modifications. */
  private val latch: Any = Any()

  /** NetworkLogger instance for traffic logging. */
  private val logger: NetworkLogger = NetworkLogger(networkLoggingBehavior)

  /** Returns the current state of connection. */
  val isOpen: Boolean
    get() = wsClient.isOpen

  init {
    logger.info("Initializing BoardGameClient on host $host.")

    wsClient =
        BGWWebSocketClient(
            uri = URI.create("ws://$host"),
            playerName = playerName,
            secret = secret,
            callback = this,
            logger = logger)

    logger.debug("Initializing annotated receiver functions.")

    initializationJob =
        wsClient.scope.launch {
          val annotatedClasses = getAnnotatedClasses()
          val annotatedFunctions =
              getAnnotatedReceivers(this@BoardGameClient::class.java, annotatedClasses)

          gameActionClasses = annotatedClasses
          gameActionReceivers = annotatedFunctions

          logger.debug("Found the following GameActionClasses:")
          annotatedClasses.forEach { logger.debug(it.name) }

          logger.debug("Found the following GameActionReceivers:")
          annotatedFunctions.forEach { logger.debug(it.value.name) }
        }
  }

  // region Connect / Disconnect
  /**
   * Connects to the remote server, blocking.
   *
   * @return Returns whether connection could be established.
   *
   * @throws IllegalStateException If client is already connected to a host.
   */
  fun connect(): Boolean =
      try {
        checkConnected(false)

        logger.info("Connecting to ${wsClient.uri}.")

        val result = wsClient.connectBlocking()

        logger.debug(
            "Connection call succeeded without interruption ${if(result) "and" else "but"} returned $result.")

        result
      } catch (e: InterruptedException) {
        logger.error(
            "Attempt to connect to ${wsClient.uri} failed with an InterruptedException.", e)
        false
      }

  /**
   * Disconnects from the remote server.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun disconnect() {
    try {
      checkConnected(true)

      logger.info("Disconnecting.")

      wsClient.closeBlocking()

      logger.debug("Disconnection call succeeded without interruption.")
    } catch (e: InterruptedException) {
      logger.error("Attempt to disconnect failed with an InterruptedException.", e)
    }
  }
  // endregion

  // region Create / Join / Leave game
  /**
   * Creates a new game session on the server by sending a [CreateGameMessage].
   *
   * @param gameID ID of the current game to be used.
   * @param sessionID Unique id for the new session to be created on the server.
   * @param greetingMessage Greeting message to be broadcast to all players joining this session.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun createGame(gameID: String, sessionID: String, greetingMessage: String) {
    checkConnected(expectedState = true)

    require(sessionID.isNotBlank()) { "Session ID was blank, did not create game." }

    logger.info(
        "Requesting creation of new game with ID \"$gameID\", " +
            "sessionID \"$sessionID\" and greeting message \"$greetingMessage\".")

    wsClient.sendRequest(CreateGameMessage(gameID, sessionID.trim(), greetingMessage))
  }

  /**
   * Creates a new game session on the server by sending a [CreateGameMessage]. The session ID will
   * be set automatically and returned through the [onCreateGameResponse].
   *
   * @param gameID ID of the current game to be used.
   * @param greetingMessage Greeting message to be broadcast to all players joining this session.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun createGame(gameID: String, greetingMessage: String) {
    checkConnected(expectedState = true)

    logger.info(
        "Requesting creation of new game with ID \"$gameID\", " +
            "auto sessionID and greeting message \"$greetingMessage\".")

    wsClient.sendRequest(CreateGameMessage(gameID, null, greetingMessage))
  }

  /**
   * Joins an existing game session on the server by sending a [JoinGameMessage].
   *
   * @param sessionID Unique id for the existing session to join to.#
   * @param greetingMessage Greeting message to be broadcast to all other players in this session.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun joinGame(sessionID: String, greetingMessage: String) {
    checkConnected(expectedState = true)

    logger.info(
        "Requesting joining to sessionID $sessionID. Greeting message is: $greetingMessage.")
    wsClient.sendRequest(JoinGameMessage(sessionID, greetingMessage))
  }

  /**
   * Joins an existing game session as a spectator on the server by sending a
   * [SpectatorJoinGameMessage].
   *
   * @param sessionID Unique id for the existing session to join to.#
   * @param greetingMessage Greeting message to be broadcast to all other players in this session.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun spectateGame(sessionID: String, greetingMessage: String) {
    checkConnected(expectedState = true)

    logger.info(
        "Requesting joining to sessionID $sessionID as spectator. Greeting message is: $greetingMessage.")
    wsClient.sendRequest(SpectatorJoinGameMessage(sessionID, greetingMessage))
  }

  /**
   * Leaves the current game session by sending a [LeaveGameMessage].
   *
   * @param goodbyeMessage Goodbye message to be broadcast to all other players in this session.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun leaveGame(goodbyeMessage: String) {
    checkConnected(expectedState = true)

    logger.info("Leaving game. Goodbye message is: $goodbyeMessage.")
    wsClient.sendRequest(LeaveGameMessage(goodbyeMessage))
  }
  // endregion

  // region Game messages
  /**
   * Sends a [GameActionMessage] to all connected players.
   *
   * @param payload The [GameActionMessage] payload.
   *
   * @throws IllegalStateException If client is not connected to a host.
   */
  fun sendGameActionMessage(payload: GameAction) {
    checkConnected(expectedState = true)

    logger.info("Sending GameActionMessage ${payload.javaClass.name}")
    wsClient.sendGameActionMessage(payload)
  }
  // endregion

  // region Interface functions
  /** Called after an opening handshake has been performed. */
  open fun onOpen() {}

  /**
   * Called after the websocket connection has been closed.
   *
   * @param code The codes can be looked up here: [org.java_websocket.framing.CloseFrame].
   * @param reason Additional information string.
   * @param remote Returns whether the closing of the connection was initiated by the remote host.
   */
  open fun onClose(code: Int, reason: String, remote: Boolean) {}

  /**
   * Called when an error occurred. By default, this method throws the incoming exception. Override
   * to implement error handling.
   *
   * @throws Throwable Throws [throwable].
   */
  open fun onError(throwable: Throwable) {
    throw throwable
  }

  /**
   * Called when a player joined the session and the server sent a [PlayerJoinedNotification].
   *
   * @param notification The [PlayerJoinedNotification] received from the server.
   */
  open fun onPlayerJoined(notification: PlayerJoinedNotification) {}

  /**
   * Called when a spectator joined the session and the server sent a [SpectatorJoinedNotification].
   *
   * @param notification The [SpectatorJoinedNotification] received from the server.
   */
  open fun onSpectatorJoined(notification: SpectatorJoinedNotification) {}

  /**
   * Called when a player left the session and the server sent a [PlayerLeftNotification].
   *
   * @param notification The [PlayerLeftNotification] received from the server.
   */
  open fun onPlayerLeft(notification: PlayerLeftNotification) {}

  /**
   * Called when server sent a [CreateGameResponse] after a [CreateGameMessage] was sent.
   *
   * @param response The [CreateGameResponse] received from the server.
   */
  open fun onCreateGameResponse(response: CreateGameResponse) {}

  /**
   * Called when server sent a [JoinGameResponse] after a [JoinGameMessage] was sent.
   *
   * @param response The [JoinGameResponse] received from the server.
   */
  open fun onJoinGameResponse(response: JoinGameResponse) {}

  /**
   * Called when server sent a [SpectatorJoinGameResponse] after a [SpectatorJoinGameMessage] was
   * sent.
   *
   * @param response The [SpectatorJoinGameResponse] received from the server.
   */
  open fun onSpectatorJoinGameResponse(response: SpectatorJoinGameResponse) {}

  /**
   * Called when server sent a [LeaveGameResponse] after a [LeaveGameMessage] was sent.
   *
   * @param response The [LeaveGameResponse] received from the server.
   */
  open fun onLeaveGameResponse(response: LeaveGameResponse) {}

  /**
   * Called when server sent a [GameActionResponse] after a [GameActionMessage] was sent.
   *
   * @param response The [GameActionResponse] received from the server.
   */
  open fun onGameActionResponse(response: GameActionResponse) {}

  /**
   * Called when an opponent sent a [GameActionMessage]. This method is supposed to act as a
   * fallback solution if not for all Subclasses of [GameAction] a dedicated handler was registered
   * using [GameActionReceiver] annotation.
   *
   * Register a message receiver for a message type 'ExampleGameAction' by declaring a function
   *
   * fun yourFunctionName(yourParameterName1 : ExampleGameAction, yourParameterName2: String) { }
   *
   * The BGW framework will automatically choose a function to invoke based on the declared
   * parameter types.
   *
   * @param message The [GameAction] received from the opponent.
   * @param sender The opponents identification.
   */
  open fun onGameActionReceived(message: GameAction, sender: String) {
    logger.err(
        "An incoming GameAction has been handled by the fallback function. Override onGameActionReceived or create" +
            " dedicated handler for message type ${message.javaClass.canonicalName}.")
  }
  // endregion

  // region Helper
  /**
   * Invokes dedicated annotated receiver function for [message] parameter or fallback if not found.
   *
   * @param message The [GameActionMessage] received from the opponent that is to be delegated.
   */
  internal fun invokeAnnotatedReceiver(message: GameActionMessage) {
    msgQueue.add(message)

    wsClient.scope.launch {
      if (!initializationJob.isCompleted) {
        logger.debug("Initialization of annotated receivers has not finished yet. Joining thread.")
        initializationJob.join()
      }

      val targetClasses = checkNotNull(gameActionClasses)
      val targetReceivers = checkNotNull(gameActionReceivers)

      for (target in targetClasses) {
        try {
          // Try de-serializing payload into target type
          val payload = wsClient.mapper.readValue(message.payload, target)

          // Find receiver method for target, or catchall fallback if none specified
          val method =
              targetReceivers.getOrDefault(target, targetReceivers[GameAction::class.java])
                  ?: checkNotNull(this@BoardGameClient::onGameActionReceived.javaMethod)

          // Invoke receiver
          synchronized(this@BoardGameClient) {
            msgMapping[message] = Pair(method, payload)
            invokeMessageQueue()
          }

          return@launch
        } catch (_: JsonMappingException) {}
      }

      logger.err(
          "Received GameActionMessage $message but no target class was Found. " +
              "Create class annotated @GameActionClass extending GameAction in your classpath.")

      synchronized(this@BoardGameClient) {
        msgQueue.remove(message)
        invokeMessageQueue()
      }
    }
  }

  /**
   * Invokes all receiver methods in [msgQueue] that have been finished until the first still
   * working.
   */
  private fun invokeMessageQueue() {
    while (msgQueue.isNotEmpty()) {
      val msg = msgQueue.peek()
      val method = msgMapping.getOrDefault(msg, null) ?: return

      method.first.invoke(this@BoardGameClient, method.second, msg.sender)
      msgQueue.poll()
    }
  }

  /**
   * Checks for connected state.
   *
   * @param expectedState The expected connection state.
   *
   * @throws IllegalStateException If BoardGameClient is not connected.
   */
  private fun checkConnected(expectedState: Boolean) {
    check(isOpen == expectedState) {
      "This BoardGameClient is ${if (expectedState) "not" else "already"} connected to a host."
    }
  }
  // endregion
}
