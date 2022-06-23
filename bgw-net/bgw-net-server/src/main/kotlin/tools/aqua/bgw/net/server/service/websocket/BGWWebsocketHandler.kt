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

package tools.aqua.bgw.net.server.service.websocket

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import tools.aqua.bgw.net.server.service.GameService
import tools.aqua.bgw.net.server.service.MessageService
import tools.aqua.bgw.net.server.service.PlayerService

/**
 * [WebSocketHandler] for BGW-Net applications.
 *
 * @property playerService Auto-Wired [PlayerService].
 * @property messageService Auto-Wired [MessageService].
 * @property gameService Auto-Wired [GameService].
 */
@Component
class BGWWebsocketHandler(
    private val playerService: PlayerService,
    private val messageService: MessageService,
    private val gameService: GameService,
) : TextWebSocketHandler() {

  private val logger = LoggerFactory.getLogger(javaClass)

  /** Logs the transport error. */
  override fun handleTransportError(session: WebSocketSession, throwable: Throwable) {
    logger.info(
        "A transport error occurred for user with session id ${session.id}. Error Message: ${throwable.localizedMessage}")
    throw throwable
  }

  /** Creates a new player and associates it with the [WebSocketSession]. */
  override fun afterConnectionEstablished(session: WebSocketSession) {
    playerService.createPlayer(session)
    logger.info("User with session id ${session.id} connected")
    logger.info("Connected players:" + playerService.getAll())
  }

  /**
   * First removes the player associated with the [WebSocketSession] from its game. Then deletes the
   * player.
   */
  override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
    /*val player = session.player

    gameService.leaveGame(player)

    player.game?.broadcastMessage(player, PlayerLeftNotification("disconnected", player.name))

    playerService.deletePlayer(session)
    logger.info("User with session id ${session.id} disconnected")
    logger.info("Connected players:" + playerService.getAll())*/
  }

  /** Delegates the handling of the message payload to [messageService]. */
  override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
    logger.info("Received message ${message.payload}")
    messageService.handleMessage(session, message.payload)
  }
}
