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

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.Message
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.request.Request
import tools.aqua.bgw.net.common.response.*
import java.net.URI

/**
 * [WebSocketClient] for network communication in BGW applications. Handles sending data to the
 * remote server and marshals incoming messages to the appropriate event handlers in the
 * [BoardGameClient].
 *
 * @param uri The server uri containing host, port and endpoint.
 * @param playerName The player name.
 * @param secret The server secret.
 * @property callback Callback to the [BoardGameClient] for message marshalling.
 */
class BGWWebSocketClient(
    uri: URI,
    playerName: String,
    secret: String,
    private val callback: BoardGameClient
) : WebSocketClient(uri) {

  /** Object mapper instance for JSON serialization. */
  private val mapper = ObjectMapper().registerModule(kotlinModule())

  init {
    addHeader("PlayerName", playerName)
    addHeader("SoPraSecret", secret)
  }

  // region Send
  /**
   * Sends a [Request] to all connected players.
   *
   * @param message The [Request] instance.
   */
  fun sendRequest(message: Request) {
    send(mapper.writeValueAsString(message))
  }

  /**
   * Sends a [GameActionMessage] to all connected players.
   *
   * @param payload The [GameActionMessage] payload.
   */
  fun sendGameActionMessage(payload: GameAction) {
    send(
        mapper.writeValueAsString(
            GameActionMessage(
                mapper.writeValueAsString(payload), payload.toString(), ""))) // TODO: Enter sender
  }
  // endregion

  // region Callbacks
  /**
   * Called after an opening handshake has been performed and the given websocket is ready to be
   * written on.
   *
   * @param handshakedata The handshake of the websocket instance
   */
  override fun onOpen(handshakedata: ServerHandshake?) {
    callback.onOpen()
  }

  /**
   * Called after the websocket connection has been closed.
   *
   * @param code The codes can be looked up here: [org.java_websocket.framing.CloseFrame].
   * @param reason Additional information string.
   * @param remote Returns whether the closing of the connection was initiated by the remote host.
   */
  override fun onClose(code: Int, reason: String?, remote: Boolean) {
    callback.onClose(code = code, reason = reason ?: "n/a", remote)
  }

  /**
   * Called when an error occurred. If an error causes the websocket connection to fail
   * #onClose(int, String?, boolean) will be called additionally. This method will be called
   * primarily because of IO or protocol errors.
   *
   * @param ex The exception causing this error
   */
  override fun onError(ex: Exception?) {
    callback.onError(throwable = ex ?: NullPointerException("Exception itself is null."))
  }

  /**
   * Callback for messages received from the remote host.
   *
   * @param message The message that was received.
   */
  override fun onMessage(message: String?) {
    try {
      messageMapping(mapper.readValue(message, Message::class.java))
    } catch (ise: IllegalArgumentException) {
      ise.printStackTrace()
      onError(ise)
    } catch (jse: JsonProcessingException) {
      jse.printStackTrace()
      onError(jse)
    }
  }

  /** Maps incoming messages onto appropriate handlers. */
  private fun messageMapping(message: Message) {
    require(message !is Request) { "Client received a request" }

    when (message) {
      is GameActionMessage ->
          callback.onGameActionReceived(
              mapper.readValue(message.payload, GameAction::class.java),
              message.sender) // TODO: Mapping to correct class
      is GameActionResponse -> callback.onGameActionResponse(message)
      is CreateGameResponse -> callback.onCreateGameResponse(message)
      is JoinGameResponse -> callback.onJoinGameResponse(message)
      is LeaveGameResponse -> callback.onLeaveGameResponse(message)

      is UserJoinedNotification -> callback.onUserJoined(message)
      is UserDisconnectedNotification -> callback.onUserLeft(message)
    }
  }
  // endregion
}
