/*
 * Copyright 2022 The BoardGameWork Authors
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

package tools.aqua.bgw.net.client

import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.request.CreateGameMessage
import tools.aqua.bgw.net.common.request.JoinGameMessage
import tools.aqua.bgw.net.common.request.LeaveGameMessage
import tools.aqua.bgw.net.common.response.*
import java.lang.reflect.Method
import java.net.URI


/**
 * [BoardGameClient] for network communication in BGW applications. Inherit from this class and
 * override it's open functions. By default, these do nothing if not overridden.
 *
 * @param playerName The player name.
 * @param host The server ip or hostname.
 * @param port The server port.
 * @param endpoint The server endpoint.
 * @param secret The server secret.
 */
open class BoardGameClient
protected constructor(
    playerName: String,
    host: String,
    port: Int,
    endpoint: String = "chat",
    secret: String,
    val schemas : Set<Class<out GameAction>>
) {

  /** WebSocketClient handling network communication. */
  private val wsClient: BGWWebSocketClient

  init {
    @Suppress("LeakingThis")
    wsClient =
        BGWWebSocketClient(
            uri = URI.create("ws://$host:$port/$endpoint"),
            playerName = playerName,
            secret = secret,
            callback = this)


  }











  fun testReflection() {
    println("Test reflection 1")

    val methods = getAnnotatedReceivers()
    methods.forEach { (k, v) -> println("$k -> $v")  }

    println("End Test reflection")
  }


  private fun getAnnotatedReceivers(): Map<Class<out GameAction>, Method> {
    val map = mutableMapOf<Class<out GameAction>, Method>()
    val annotatedMethods = mutableListOf<Method>()
    var clazz : Class<*> = this::class.java

    //Retrieve all annotated methods
    while (clazz != Any::class.java) {
      for (method in clazz.declaredMethods) {
        if (method.isAnnotationPresent(GameActionReceiver::class.java)) {
          annotatedMethods.add(method)
        }
      }
      clazz = clazz.superclass
    }

    //Create mapping
    for(method in annotatedMethods) {
      val params : Array<Class<*>> = method.parameterTypes

      //Check parameter count
      if(params.size != 2) {
        System.err.println("Found function ${method.name} annotated with @GameActionReceiver that does not declare the expected parameter count. Ignoring.")
        continue
      }

      //Check first parameter is subclass of GameAction
      val receiver = params[0]
      if(!GameAction::class.java.isAssignableFrom(receiver)) {
        System.err.println("Found function ${method.name} annotated with @GameActionReceiver with first parameter not conforming to GameAction which was expected. Ignoring.")
        continue
      }

      //Check second parameter is String
      if(!String::class.java.isAssignableFrom(params[1])) {
        System.err.println("Found function ${method.name} annotated with @GameActionReceiver with second parameter not conforming to String which was expected. Ignoring.")
        continue
      }

      //Check first parameter is in schemas set
      val schema = matchSchema(receiver)
      if(schema == null) {
        System.err.println("Found function ${method.name} annotated with @GameActionReceiver with first parameter not in schema list passed to BoardGameClient constructor. Ignoring.")
        continue
      }

      //Check for duplicate method
      if(map.containsKey(schema)) {
        System.err.println("Multiple functions annotated with @GameActionReceiver found that declare receiver parameter $receiver. Ignoring duplicate.")
        continue
      }

      map[schema] = method
    }
    return map
  }

  private fun matchSchema(type : Class<*>) : Class<out GameAction>? {
    for(schema in schemas) {
      if (schema.isAssignableFrom(type)) {
        return schema
      }
    }
    return null
  }














  // region Connect / Disconnect
  /** Connects to the remote server, blocking. */
  // TODO: isConnected flag
  fun connect() {
    wsClient.connectBlocking()
  }

  /** Disconnects from the remote server. */
  fun disconnect() {
    wsClient.closeBlocking()
  }

  /**
   * Called when an error occurred. By default, this method throws the incoming exception. Override
   * to implement error handling.
   *
   * @throws Throwable Throws [throwable].
   */
  open fun onError(throwable: Throwable) {
    throw throwable
  }

  /** Called after an opening handshake has been performed and the [BoardGameClient] send data. */
  open fun onOpen() {}

  /**
   * Called after the websocket connection has been closed.
   *
   * @param code The codes can be looked up here: [org.java_websocket.framing.CloseFrame].
   * @param reason Additional information string.
   * @param remote Returns whether the closing of the connection was initiated by the remote host.
   */
  open fun onClose(code: Int, reason: String, remote: Boolean) {}
  // endregion

  // region Create / Join / Leave game
  /**
   * Creates a new game session on the server by sending a [CreateGameMessage].
   *
   * @param gameID ID of the current game to be used.
   * @param sessionID Unique id for the new session to be created on the server.
   */
  fun createGame(gameID: String, sessionID: String) {
    wsClient.sendRequest(CreateGameMessage(gameID, sessionID))
  }

  /**
   * Joins an existing game session on the server by sending a [JoinGameMessage].
   *
   * @param sessionID Unique id for the existing session to join to.#
   * @param greetingMessage Greeting message to be broadcast to all other players in this session.
   */
  fun joinGame(sessionID: String, greetingMessage: String) {
    wsClient.sendRequest(JoinGameMessage(sessionID, greetingMessage))
  }

  /**
   * Leaves the current game session by sending a [LeaveGameMessage].
   *
   * @param goodbyeMessage Goodbye message to be broadcast to all other players in this session.
   */
  fun leaveGame(goodbyeMessage: String) {
    wsClient.sendRequest(LeaveGameMessage(goodbyeMessage))
  }

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
   * Called when server sent a [LeaveGameResponse] after a [LeaveGameMessage] was sent.
   *
   * @param response The [LeaveGameResponse] received from the server.
   */
  open fun onLeaveGameResponse(response: LeaveGameResponse) {}

  /**
   * Called when a user joined the session and the server sent a [UserJoinedNotification].
   *
   * @param notification The [UserJoinedNotification] received from the server.
   */
  open fun onUserJoined(notification: UserJoinedNotification) {}

  /**
   * Called when a user left the session and the server sent a [UserDisconnectedNotification].
   *
   * @param notification The [UserDisconnectedNotification] received from the server.
   */
  open fun onUserLeft(notification: UserDisconnectedNotification) {}
  // endregion

  // region Game messages
  /**
   * Sends a [GameActionMessage] to all connected players.
   *
   * @param payload The [GameActionMessage] payload.
   */
  fun sendGameActionMessage(payload: GameAction) { //TODO: ANY
    wsClient.sendGameActionMessage(payload)
  }

  /**
   * Called when server sent a [GameActionResponse] after a [GameActionMessage] was sent.
   *
   * @param response The [GameActionResponse] received from the server.
   */
  open fun onGameActionResponse(response: GameActionResponse) {}

  /**
   * Called when an opponent sent a [GameActionMessage].
   *
   * @param message The [GameActionMessage] received from the opponent.
   * @param sender The opponents identification.
   */
   open fun <T : GameAction> onGameActionReceived(message: T, sender: String) {} //TODO: ANY
  // endregion
}
