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

import java.net.URI
import tools.aqua.bgw.net.common.gamemessage.EndGameMessage
import tools.aqua.bgw.net.common.gamemessage.GameActionMessage
import tools.aqua.bgw.net.common.gamemessage.InitializeGameMessage
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.request.CreateGameMessage
import tools.aqua.bgw.net.common.request.JoinGameMessage
import tools.aqua.bgw.net.common.request.LeaveGameMessage
import tools.aqua.bgw.net.common.response.*

/**
 * [BoardGameClient] for network communication in BGW applications. Inherit from this class and
 * override it's open functions. By default, these do nothing if not overridden.
 *
 * @param IG Generic for the [InitializeGameMessage].
 * @param GA Generic for the [GameActionMessage].
 * @param EG Generic for the [EndGameMessage].
 * @property playerName The player name.
 * @param host The server ip or hostname.
 * @param port The server port.
 * @param endpoint The server endpoint.
 * @param secret The server secret.
 * @param initGameClass The [InitializeGameMessage] class.
 * @param gameActionClass The [GameActionMessage] class.
 * @param endGameClass The [EndGameMessage] class.
 */
open class BoardGameClient<IG, GA, EG>
protected constructor(
    val playerName: String,
    host: String,
    port: Int,
    endpoint: String = "chat",
    secret: String,
    initGameClass: Class<IG>,
    gameActionClass: Class<GA>,
    endGameClass: Class<EG>,
) {

  /** WebSocketClient handling network communication. */
  private val wsClient: BGWWebSocketClient<IG, GA, EG>

  init {
    @Suppress("LeakingThis")
    wsClient =
        BGWWebSocketClient(
            uri = URI.create("ws://$host:$port/$endpoint"),
            playerName = playerName,
            secret = secret,
            callback = this,
            initGameClass = initGameClass,
            gameActionClass = gameActionClass,
            endGameClass = endGameClass)
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

  // region Send messages
  /**
   * Sends a [GameActionMessage] to all connected players.
   *
   * @param payload The [GameActionMessage] payload.
   */
  fun sendGameActionMessage(payload: GA) {
    wsClient.sendGameActionMessage(payload)
  }

  /**
   * Sends an [InitializeGameMessage] to all connected players.
   *
   * @param payload The [InitializeGameMessage] payload.
   */
  fun sendInitializeGameMessage(payload: IG) {
    wsClient.sendInitializeGameMessage(payload)
  }

  /**
   * Sends an [EndGameMessage] to all connected players.
   *
   * @param payload The [EndGameMessage] payload.
   */
  fun sendEndGameMessage(payload: EG) {
    wsClient.sendEndGameMessage(payload)
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
  open fun onGameActionReceived(message: GA, sender: String) {}

  /**
   * Called when server sent a [InitializeGameResponse] after a [InitializeGameMessage] was sent.
   *
   * @param response The [InitializeGameResponse] received from the server.
   */
  open fun onInitializeGameResponse(response: InitializeGameResponse) {}

  /**
   * Called when an opponent sent a [InitializeGameMessage].
   *
   * @param message The [InitializeGameMessage] received from the opponent.
   * @param sender The opponents identification.
   */
  open fun onInitializeGameReceived(message: IG, sender: String) {}

  /**
   * Called when server sent a [EndGameResponse] after a [EndGameMessage] was sent.
   *
   * @param response The [EndGameResponse] received from the server.
   */
  open fun onEndGameResponse(response: EndGameResponse) {}

  /**
   * Called when an opponent sent a [EndGameMessage].
   *
   * @param message The [EndGameMessage] received from the opponent.
   * @param sender The opponents identification.
   */
  open fun onEndGameReceived(message: EG, sender: String) {}
  // endregion

}
