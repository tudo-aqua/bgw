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

package tools.aqua.bgw.net.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import java.util.*
import org.atmosphere.annotation.AnnotationUtil.logger
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.common.Message
import tools.aqua.bgw.net.common.message.*
import tools.aqua.bgw.net.common.notification.Notification
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.request.CreateGameMessage
import tools.aqua.bgw.net.common.request.JoinGameMessage
import tools.aqua.bgw.net.common.request.LeaveGameMessage
import tools.aqua.bgw.net.common.response.*
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository
import tools.aqua.bgw.net.server.player
import tools.aqua.bgw.net.server.service.validation.JsonSchemaNotFoundException
import tools.aqua.bgw.net.server.service.validation.ValidationService

/** This service handles the text messages received by the web socket server. */
@Service
class MessageService(
    private val gameService: GameService,
    private val validationService: ValidationService,
    private val schemasByGameRepository: SchemasByGameRepository,
) {
  //region Handle messages
  /**
   * Handles incoming messages.
   *
   * @param session Sender [WebSocketSession].
   * @param messageString The message.
   *
   * @throws UnsupportedOperationException For unsupported message types.
   */
  @Throws(UnsupportedOperationException::class)
  fun handleMessage(session: WebSocketSession, messageString: String): Unit =
    when (val message = mapper.readValue(messageString, Message::class.java)) {
      is GameActionMessage -> handleGameMessage(session, message)
      is CreateGameMessage -> handleCreateGameMessage(session, message)
      is JoinGameMessage -> handleJoinGameMessage(session, message)
      is LeaveGameMessage -> handleLeaveGameMessage(session, message)
      is Notification -> throw UnsupportedOperationException("Server received notification.")
      is Response -> throw UnsupportedOperationException("Server received response.")
      else -> throw UnsupportedOperationException("Unsupported message type.")
    }

  /**
   * Handles incoming [GameActionMessage].
   *
   * @param session The [WebSocketSession].
   * @param msg The [GameActionMessage] that was received.
   */
  private fun handleGameMessage(session: WebSocketSession, msg: GameActionMessage) {
    val player = session.player
    val game = player.game
    var errors: Optional<List<String>> = Optional.empty()
    val status =
        if (game != null)
            try {
              errors = validationService.validate(msg, game.gameID)
              if (errors.isEmpty) GameActionResponseStatus.SUCCESS
              else GameActionResponseStatus.INVALID_JSON
            } catch (exception: JsonSchemaNotFoundException) {
              GameActionResponseStatus.SERVER_ERROR
            }
        else GameActionResponseStatus.NO_ASSOCIATED_GAME

    player.session.sendMessage(GameActionResponse(status, errors.orElseGet { emptyList() }))

    if (status == GameActionResponseStatus.SUCCESS) {
      game?.broadcastMessage(
          player,
          GameActionMessage(
              payload = msg.payload,
              prettyPrint = msg.prettyPrint,
              sender = player.name))
    }
  }

  /**
   * Handles incoming [CreateGameMessage].
   *
   * @param session The [WebSocketSession].
   * @param msg The [CreateGameMessage] that was received.
   */
  private fun handleCreateGameMessage(session: WebSocketSession, msg: CreateGameMessage) {
    val player = session.player
    val createGameResponseStatus =
        if (schemasByGameRepository.findAll().none { it.gameID == msg.gameID })
            CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST
        else gameService.createGame(msg.gameID, msg.sessionID, player)

    session.sendMessage(CreateGameResponse(createGameResponseStatus))
  }

  /**
   * Handles incoming [JoinGameMessage].
   *
   * @param session The [WebSocketSession].
   * @param msg The [JoinGameMessage] that was received.
   */
  private fun handleJoinGameMessage(session: WebSocketSession, msg: JoinGameMessage) {
    val player = session.player
    val joinGameResponseStatus = gameService.joinGame(player, msg.sessionID)

    session.sendMessage(JoinGameResponse(joinGameResponseStatus))

    if (joinGameResponseStatus == JoinGameResponseStatus.SUCCESS) {
      val notification = UserJoinedNotification(msg.greeting, player.name)

      logger.info("Starting broadcast join message")
      gameService.getBySessionID(msg.sessionID)?.broadcastMessage(player, notification)
    }
  }

  /**
   * Handles incoming [LeaveGameMessage].
   *
   * @param session The [WebSocketSession].
   * @param msg The [LeaveGameMessage] that was received.
   */
  private fun handleLeaveGameMessage(session: WebSocketSession, msg: LeaveGameMessage) {
    val player = session.player
    val game = player.game
    val leaveGameResponseStatus = gameService.leaveGame(player)

    session.sendMessage(LeaveGameResponse(leaveGameResponseStatus))

    if (leaveGameResponseStatus == LeaveGameResponseStatus.SUCCESS) {
      val notification = UserDisconnectedNotification(msg.goodbyeMessage, player.name)
      val message = mapper.writeValueAsString(notification)
      game?.let {
        it.players.map(Player::session).forEach { session ->
          session.sendMessage(TextMessage(message))
        }
      }
    }
  }
  //endregion

  //region Send messages
  companion object {
    /** Object mapper for Json (de)serialization */
    private val mapper = ObjectMapper().registerModule(kotlinModule())

    internal fun Game.broadcastMessage(sender: Player, msg: Message) {
      for (session in (players - sender).map(Player::session)) {
        logger.info("Broadcast $msg to $session")
        session.sendMessage(msg)
      }
    }

    private fun WebSocketSession.sendMessage(message: Message) =
      sendMessage(TextMessage(mapper.writeValueAsString(message)))
  }
  //endregion
}
