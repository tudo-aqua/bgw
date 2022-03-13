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

package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.maumau.entity.GameActionType
import tools.aqua.bgw.examples.maumau.entity.MauMauPlayer
import tools.aqua.bgw.examples.maumau.main.NETWORK_SECRET
import tools.aqua.bgw.examples.maumau.service.messages.MauMauEndGameMessage
import tools.aqua.bgw.examples.maumau.service.messages.MauMauGameAction
import tools.aqua.bgw.examples.maumau.service.messages.MauMauInitMessage
import tools.aqua.bgw.examples.maumau.view.Refreshable
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.UserDisconnectedNotification
import tools.aqua.bgw.net.common.notification.UserJoinedNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus

/**
 * [BoardGameClient] implementation for network communication.
 *
 * @param playerName Your player name.
 * @param host Host address or name
 * @param port Destination port.
 * @property logicController [LogicController] instance for refreshes.
 */
class NetworkClientService(
    playerName: String,
    host: String,
    port: Int,
    val logicController: LogicController,
) :
    BoardGameClient(
        playerName = playerName,
        secret = NETWORK_SECRET,
        host = host,
        port = port,
    ) {

  /** [Refreshable] instance. */
  /** [Refreshable] instance. */
  val view: Refreshable = logicController.view

  // region Connect response
  override fun onCreateGameResponse(response: CreateGameResponse) {
    BoardGameApplication.runOnGUIThread {
      when (response.status) {
        CreateGameResponseStatus.SUCCESS -> view.onCreateGameSuccess()
        CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
            view.onCreateGameError("You are already in a game.")
        CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS ->
            view.onCreateGameError("Session id already exists.")
        CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> error(response)
        CreateGameResponseStatus.SERVER_ERROR -> view.onServerError()
      }
    }
  }

  override fun onJoinGameResponse(response: JoinGameResponse) {
    BoardGameApplication.runOnGUIThread {
      when (response.status) {
        JoinGameResponseStatus.SUCCESS -> view.onJoinGameSuccess()
        JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME ->
            view.onCreateGameError("You are already in a game.")
        JoinGameResponseStatus.INVALID_SESSION_ID -> view.onCreateGameError("Session id invalid.")
        JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN ->
            view.onJoinGameError("Player name is already taken.")
        JoinGameResponseStatus.SERVER_ERROR -> view.onServerError()
      }
    }
  }

  override fun onUserJoined(notification: UserJoinedNotification) {
    BoardGameApplication.runOnGUIThread {
      logicController.game.players[1] = MauMauPlayer(notification.sender)
      logicController.view.onUserJoined(notification.sender)
    }
  }

  override fun onUserLeft(notification: UserDisconnectedNotification) {
    BoardGameApplication.runOnGUIThread { logicController.view.onUserLeft(notification.sender) }
  }
  // endregion

  // region Game messages
  @GameActionReceiver
  fun onGameActionReceived(message: MauMauGameAction, sender: String) {
    BoardGameApplication.runOnGUIThread {
      when (GameActionType.valueOf(message.action)) {
        // Enemy has played a card
        GameActionType.PLAY -> {
          logicController.playCard(
              card = Serialization.deserializeMauMauCard(message.card),
              animated = true,
              isCurrentPlayer = false)
        }

        // Enemy has drawn a card
        GameActionType.DRAW -> {
          logicController.drawCard(isCurrentPlayer = false, advance = false)
        }

        // Enemy has played a seven effect and requests to draw two
        GameActionType.REQUEST_DRAW_TWO -> {
          logicController.drawCard(isCurrentPlayer = true, advance = false)
          logicController.drawCard(isCurrentPlayer = true, advance = false)
        }

        // Enemy has played a jack and request suit
        GameActionType.REQUEST_SUIT -> {
          logicController.selectSuit(
              suit = Serialization.deserializeMauMauCard(message.card).cardSuit,
              isCurrentPlayer = false)
        }

        // Enemy has ended his turn
        GameActionType.END_TURN -> {
          view.refreshEndTurn()
        }
      }
    }
  }

  @GameActionReceiver
  private fun onGameActionReceived(message: MauMauInitMessage, sender: String) {
    BoardGameApplication.runOnGUIThread {
      logicController.initGame(message)
      view.onInitializeGameReceived()
    }
  }

  @GameActionReceiver
  private fun onGameActionReceived(message: MauMauEndGameMessage, sender: String) {
    BoardGameApplication.runOnGUIThread { view.refreshEndGame(sender) }
  }
  // endregion
}
