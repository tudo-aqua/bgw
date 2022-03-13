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
  private val mapper = ObjectMapper().registerModule(kotlinModule())

  private fun WebSocketSession.sendMessage(message: Message) =
      sendMessage(TextMessage(mapper.writeValueAsString(message)))

  private fun Game.broadcastMessage(sender: Player, msg: Message) {
    for (session in (players - sender).map(Player::session)) {
      session.sendMessage(msg)
    }
  }

  fun handleMessage(session: WebSocketSession, messageString: String) {
    val message: Message = mapper.readValue(messageString, Message::class.java)
    println(message)
    when (message) {
      is Response -> throw UnsupportedOperationException()
      is Notification -> throw UnsupportedOperationException()
      is GameActionMessage -> handleGameMessage(session, message)
      is CreateGameMessage -> handleCreateGameMessage(session, message)
      is JoinGameMessage -> handleJoinGameMessage(session, message)
      is LeaveGameMessage -> handleLeaveGameMessage(session, message)
    }
  }

  private fun handleGameMessage(wsSession: WebSocketSession, gameMessage: GameActionMessage) {
    val player = wsSession.player
    val game = player.game
    var errors: Optional<List<String>> = Optional.empty()
    val status =
        if (game != null)
            try {
              errors = validationService.validate(gameMessage, game.gameID)
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
              payload = gameMessage.payload,
              prettyPrint = gameMessage.prettyPrint,
              sender = player.name))
    }
  }

  private fun handleCreateGameMessage(
      wsSession: WebSocketSession,
      createGameMessage: CreateGameMessage
  ) {
    val player = wsSession.player
    val createGameResponseStatus =
        if (!schemasByGameRepository.existsById(createGameMessage.gameID))
            CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST
        else gameService.createGame(createGameMessage.gameID, createGameMessage.sessionID, player)
    wsSession.sendMessage(CreateGameResponse(createGameResponseStatus))
  }

  private fun handleJoinGameMessage(wsSession: WebSocketSession, joinGameMessage: JoinGameMessage) {
    val player = wsSession.player
    val joinGameResponseStatus = gameService.joinGame(player, joinGameMessage.sessionID)
    wsSession.sendMessage(JoinGameResponse(joinGameResponseStatus))
    if (joinGameResponseStatus == JoinGameResponseStatus.SUCCESS) {
      val notification = UserJoinedNotification(joinGameMessage.greeting, player.name)
      gameService.getBySessionID(joinGameMessage.sessionID)?.broadcastMessage(player, notification)
    }
  }

  private fun handleLeaveGameMessage(
      wsSession: WebSocketSession,
      leaveGameMessage: LeaveGameMessage
  ) {
    val player = wsSession.player
    val game = player.game
    val leaveGameResponseStatus = gameService.leaveGame(player)
    wsSession.sendMessage(LeaveGameResponse(leaveGameResponseStatus))
    if (leaveGameResponseStatus == LeaveGameResponseStatus.SUCCESS) {
      val notification = UserDisconnectedNotification(leaveGameMessage.goodbyeMessage, player.name)
      val message = mapper.writeValueAsString(notification)
      game?.let {
        it.players.map(Player::session).forEach { session ->
          session.sendMessage(TextMessage(message))
        }
      }
    }
  }

  fun broadcastNotification(game: Game, msg: Notification) {
    for (session in game.players.map(Player::session)) {
      session.sendMessage(msg)
    }
  }
}
