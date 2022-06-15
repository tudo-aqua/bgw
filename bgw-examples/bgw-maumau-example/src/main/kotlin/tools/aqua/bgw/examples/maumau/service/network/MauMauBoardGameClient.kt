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

@file:Suppress("UnusedPrivateMember")

package tools.aqua.bgw.examples.maumau.service.network

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.maumau.entity.GameActionType
import tools.aqua.bgw.examples.maumau.entity.MauMauPlayer
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.examples.maumau.service.network.NetworkSerialization.deserialize
import tools.aqua.bgw.examples.maumau.service.network.NetworkSerialization.deserializeGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauEndGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauInitGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauShuffleStackGameAction
import tools.aqua.bgw.examples.maumau.view.Refreshable
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.*

/**
 * [BoardGameClient] implementation for network communication.
 *
 * @param playerName Your player name.
 * @param host Host address.
 * @param secret Network secret.
 * @property logicController [LogicController] instance for refreshes.
 */
class MauMauBoardGameClient(
    playerName: String,
    host: String,
    secret: String,
    val logicController: LogicController,
) :
    BoardGameClient(
        playerName = playerName,
        host = host,
        secret = secret,
        networkLoggingBehavior = NetworkLogging.VERBOSE) {

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

  override fun onPlayerJoined(notification: PlayerJoinedNotification) {
    BoardGameApplication.runOnGUIThread {
      logicController.game.players[1] = MauMauPlayer(notification.sender)
      logicController.view.onUserJoined(notification.sender)
    }
  }

  override fun onPlayerLeft(notification: PlayerLeftNotification) {
    BoardGameApplication.runOnGUIThread { logicController.view.onUserLeft(notification.sender) }
  }
  // endregion

  // region Game messages
  /** GameActionReceiver for [MauMauInitGameAction]. */
  @GameActionReceiver
  private fun onInitReceived(message: MauMauInitGameAction, sender: String) {
    BoardGameApplication.runOnGUIThread {
      logicController.initGame(message)
      view.onInitializeGameReceived()
    }
  }

  /** GameActionReceiver for [MauMauInitGameAction]. */
  @GameActionReceiver
  private fun onShuffleDrawStackReceived(message: MauMauShuffleStackGameAction, sender: String) {
    BoardGameApplication.runOnGUIThread {
      logicController.shuffleStack(message)
      view.onShuffleStackReceived()
    }
  }

  /** GameActionReceiver for [MauMauGameAction]. */
  @GameActionReceiver
  private fun onGameActionReceived(message: MauMauGameAction, sender: String) {
    BoardGameApplication.runOnGUIThread {
      when (deserializeGameAction(message.action)) {
        // Enemy has played a card
        GameActionType.PLAY -> {
          checkNotNull(message.card)
          logicController.playCard(
              card = message.card.deserialize(), animated = true, isCurrentPlayer = false)
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
          checkNotNull(message.card)
          logicController.selectSuit(
              suit = message.card.deserialize().cardSuit, isCurrentPlayer = false)
        }

        // Enemy has ended his turn
        GameActionType.END_TURN -> {
          view.refreshEndTurn()
        }
      }
    }
  }

  /** GameActionReceiver for [MauMauEndGameAction]. */
  @GameActionReceiver
  private fun onEndGameReceived(message: MauMauEndGameAction, sender: String) {
    BoardGameApplication.runOnGUIThread { view.refreshEndGame(sender) }
  }
  // endregion
}
