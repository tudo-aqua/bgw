/*
 * Copyright 2022-2025 The BoardGameWork Authors
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
import java.net.URI
import kotlinx.coroutines.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.Message
import tools.aqua.bgw.net.common.message.GameActionMessage
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.notification.SpectatorJoinedNotification
import tools.aqua.bgw.net.common.request.Request
import tools.aqua.bgw.net.common.response.*

/**
 * [WebSocketClient] for network communication in BGW applications. Handles sending data to the
 * remote server and marshals incoming messages to the appropriate event handlers in the
 * [BoardGameClient].
 *
 * @property uri The server uri containing host, port and endpoint.
 * @property playerName The player name.
 * @property secret The server secret.
 * @property callback Callback to the [BoardGameClient] for message marshalling.
 * @property logger Network logger instance.
 */
internal class BGWWebSocketClient(
    private val uri: URI,
    private val playerName: String,
    private val secret: String,
    private val callback: BoardGameClient,
    private val logger: NetworkLogger
) : WebSocketClient(uri) {

  /** Object mapper instance for JSON serialization. */
  internal val mapper = ObjectMapper().registerModule(kotlinModule())

  /** Message coroutine scope. */
  internal val scope = CoroutineScope(Dispatchers.Default)

  init {
    addHeader("PlayerName", playerName)
    addHeader("NetworkSecret", secret)
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
    val payloadJson = mapper.writeValueAsString(payload)
    val msg = GameActionMessage(payloadJson, payload.toString(), playerName)

    logger.debug("Sending GameAction as JSON: $payloadJson")

    send(mapper.writeValueAsString(msg))
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
    logger.info("Connection is now open.")

    scope.launch { callback.onOpen() }
  }

  /**
   * Called after the websocket connection has been closed.
   *
   * @param code The codes can be looked up here: [org.java_websocket.framing.CloseFrame].
   * @param reason Additional information string.
   * @param remote Returns whether the closing of the connection was initiated by the remote host.
   */
  override fun onClose(code: Int, reason: String?, remote: Boolean) {
    logger.info("Connection is now closed.")
    logger.debug(
        "Status code is: $code. The connection was closed with the following reason: $reason. " +
            "The closing was initiated by the ${ if(remote) "remote" else "local"} host.")

    scope.launch { callback.onClose(code = code, reason = reason ?: "n/a", remote) }
  }

  /**
   * Called when an error occurred. If an error causes the websocket connection to fail
   * #onClose(int, String?, boolean) will be called additionally. This method will be called
   * primarily because of IO or protocol errors.
   *
   * @param ex The exception causing this error
   */
  override fun onError(ex: Exception?) {
    logger.error("An uncaught error occurred.", ex)

    scope.launch {
      callback.onError(throwable = ex ?: NullPointerException("Exception itself is null."))
    }
  }

  /**
   * Callback for messages received from the remote host.
   *
   * @param message The message that was received.
   */
  override fun onMessage(message: String?) {
    logger.info("Received message: $message")
    try {
      val msg = mapper.readValue(message, Message::class.java)

      logger.debug("Received message of type $msg")

      scope.launch { messageMapping(msg) }
    } catch (ise: IllegalArgumentException) {
      onError(ise)
    } catch (jse: JsonProcessingException) {
      onError(jse)
    }
  }

  /** Maps incoming messages onto appropriate handlers. */
  private fun messageMapping(message: Message) {
    require(message !is Request) { "Client received a request" }

    when (message) {
      is GameActionMessage -> {
        logger.debug("Received GameActionMessage. Invoking annotated receiver function.")
        callback.invokeAnnotatedReceiver(message)
      }
      is GameActionResponse -> {
        logger.debug("Received GameActionResponse. Invoking handler for onGameActionResponse.")
        callback.onGameActionResponse(message)
      }
      is CreateGameResponse -> {
        logger.debug("Received CreateGameResponse. Invoking handler for onCreateGameResponse.")
        callback.onCreateGameResponse(message)
      }
      is JoinGameResponse -> {
        logger.debug("Received JoinGameResponse. Invoking handler for onJoinGameResponse.")
        callback.onJoinGameResponse(message)
      }
      is SpectatorJoinGameResponse -> {
        logger.debug(
            "Received SpectatorJoinGameResponse. Invoking handler for onSpectatorJoinGameResponse.")
        callback.onSpectatorJoinGameResponse(message)
      }
      is LeaveGameResponse -> {
        logger.debug("Received LeaveGameResponse. Invoking handler for onLeaveGameResponse.")
        callback.onLeaveGameResponse(message)
      }
      is PlayerJoinedNotification -> {
        logger.debug("Received PlayerJoinedNotification. Invoking handler for onPlayerJoined.")
        callback.onPlayerJoined(message)
      }
      is SpectatorJoinedNotification -> {
        logger.debug(
            "Received SpectatorJoinedNotification. Invoking handler for onSpectatorJoined.")
        callback.onSpectatorJoined(message)
      }
      is PlayerLeftNotification -> {
        logger.debug("Received PlayerLeftNotification. Invoking handler for onPlayerLeft.")
        callback.onPlayerLeft(message)
      }
    }
  }
  // endregion
}
