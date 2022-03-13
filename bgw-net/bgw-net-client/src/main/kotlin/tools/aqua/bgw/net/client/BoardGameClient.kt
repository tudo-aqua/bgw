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

import java.lang.reflect.Method
import java.net.URI
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.annotations.GameActionReceiverProcessor.getAnnotatedReceivers
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.request.CreateGameMessage
import tools.aqua.bgw.net.common.request.JoinGameMessage
import tools.aqua.bgw.net.common.request.LeaveGameMessage
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.GameActionResponse
import tools.aqua.bgw.net.common.response.JoinGameResponse
import tools.aqua.bgw.net.common.response.LeaveGameResponse

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
    secret: String
) {

  /** WebSocketClient handling network communication. */
  private val wsClient: BGWWebSocketClient

  private val gameActionReceivers: Map<Class<out GameAction>, Method>

  init {
    @Suppress("LeakingThis")
    wsClient =
        BGWWebSocketClient(
            uri = URI.create("ws://$host:$port/$endpoint"),
            playerName = playerName,
            secret = secret,
            callback = this)

    gameActionReceivers = getAnnotatedReceivers(this::class.java)
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
  fun sendGameActionMessage(payload: GameAction) { // TODO: ANY
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
  open fun <T : GameAction> onGameActionReceived(message: T, sender: String) {} // TODO: ANY
  // endregion
}
