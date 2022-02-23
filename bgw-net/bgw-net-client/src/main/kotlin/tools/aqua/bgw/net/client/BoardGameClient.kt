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

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import tools.aqua.bgw.net.common.*
import java.net.URI

open class BoardGameClient<IG, GA, EG> protected constructor(
    val playerName: String,
    secret: String,
    private val initGameClass: Class<IG>,
    private val gameActionClass: Class<GA>,
    private val endGameClass: Class<EG>,
    host: String,
    port: Int,
    endpoint: String = "chat",
) {

  private val mapper = ObjectMapper().registerModule(kotlinModule())

  private val wsClient: WebSocketClient =
      MyWebSocketClient(URI.create("ws://$host:$port/$endpoint"))

  init {
    wsClient.addHeader("PlayerName", playerName)
    wsClient.addHeader("SoPraSecret", secret)
  }

  // region Connect / Disconnect
  // TODO: isConnected flag
  fun connect() {
    wsClient.connectBlocking()
  }

  fun disconnect() {
    wsClient.closeBlocking()
  }

  open fun onError(throwable: Throwable) {
    throw throwable
  }

  open fun onOpen() {}

  open fun onClose(code: Int, reason: String, remote: Boolean) {}
  // endregion

  // region Create / Join / Leave game
  fun createGame(gameID: String, sessionID: String) {
    val message: Message = CreateGameMessage(gameID, sessionID)
    wsClient.send(mapper.writeValueAsString(message))
  }

  fun joinGame(sessionID: String, greetingMessage: String) {
    val message: Message = JoinGameMessage(sessionID, greetingMessage)
    wsClient.send(mapper.writeValueAsString(message))
  }

  fun leaveGame(goodbyeMessage: String) {
    val message: Message = LeaveGameMessage(goodbyeMessage)
    wsClient.send(mapper.writeValueAsString(message))
  }

  open fun onCreateGameResponse(response: CreateGameResponse) {}

  open fun onJoinGameResponse(response: JoinGameResponse) {}

  open fun onLeaveGameResponse(response: LeaveGameResponse) {}

  open fun onUserJoined(notification: UserJoinedNotification) {}

  open fun onUserLeft(notification: UserDisconnectedNotification) {}
  // endregion

  // region Send messages
  fun sendGameActionMessage(payload: GA) {
    send(
        mapper.writeValueAsString(
            GameActionMessage(mapper.writeValueAsString(payload), payload.toString(), "")))
  }

  fun sendInitializeGameMessage(payload: IG) {
    send(
        mapper.writeValueAsString(
            InitializeGameMessage(mapper.writeValueAsString(payload), payload.toString(), "")))
  }

  fun sendEndGameMessage(payload: EG) {
    send(
        mapper.writeValueAsString(
            EndGameMessage(mapper.writeValueAsString(payload), payload.toString(), "")))
  }

  private fun send(message: String) {
    wsClient.send(message)
  }

  open fun onGameActionResponse(response: GameActionResponse) {}

  open fun onGameActionReceived(message: GA, sender: String) {}

  open fun onInitializeGameResponse(response: InitializeGameResponse) {}

  open fun onInitializeGameReceived(message: IG, sender: String) {}

  open fun onEndGameResponse(response: EndGameResponse) {}

  open fun onEndGameReceived(message: EG, sender: String) {}
  // endregion

  private inner class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
    override fun onOpen(handshakedata: ServerHandshake?) {
      this@BoardGameClient.onOpen()
    }

    override fun onMessage(message: String?) {
      try {
        val bgwMessage: Message = mapper.readValue(message, Message::class.java)
        messageMapping(bgwMessage)
      } catch (e: Exception) {
        onError(e)
      }
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
      this@BoardGameClient.onClose(code, reason ?: "n/a", remote)
    }

    override fun onError(ex: Exception?) {
      this@BoardGameClient.onError(ex!!)
    }

    private fun messageMapping(message: Message) {
      when (message) {
        is Request -> throw Exception("Client received a request")
        is InitializeGameMessage ->
            onInitializeGameReceived(
                mapper.readValue(message.payload, initGameClass), message.sender)
        is EndGameMessage ->
            onEndGameReceived(mapper.readValue(message.payload, endGameClass), message.sender)
        is GameActionMessage ->
            onGameActionReceived(mapper.readValue(message.payload, gameActionClass), message.sender)
        is InitializeGameResponse -> onInitializeGameResponse(message)
        is EndGameResponse -> onEndGameResponse(message)
        is CreateGameResponse -> onCreateGameResponse(message)
        is GameActionResponse -> onGameActionResponse(message)
        is JoinGameResponse -> onJoinGameResponse(message)
        is LeaveGameResponse -> onLeaveGameResponse(message)
        is UserJoinedNotification -> onUserJoined(message)
        is UserDisconnectedNotification -> onUserLeft(message)
      }
    }
  }
}
