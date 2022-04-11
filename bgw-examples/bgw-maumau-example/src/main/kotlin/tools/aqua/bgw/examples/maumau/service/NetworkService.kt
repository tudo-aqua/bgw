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

import tools.aqua.bgw.examples.maumau.entity.*
import tools.aqua.bgw.examples.maumau.main.GAME_ID
import tools.aqua.bgw.examples.maumau.service.messages.MauMauEndGameAction
import tools.aqua.bgw.examples.maumau.service.messages.MauMauGameAction
import tools.aqua.bgw.net.client.BoardGameClient

/** Service for handling network communication. */
class NetworkService(private val logicController: LogicController) {
  /** Network client. Nullable for offline games. */
  private var client: MauMauBoardGameClient? = null

  // region Connection
  /**
   * Connects to server and starts a new game session.
   *
   * @param address Server address and port.
   * @param name Player name.
   * @param sessionID Session ID to host.
   */
  fun tryHostGame(address: String, name: String, sessionID: String) {
    if (sessionID.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "SessionID is empty", message = "Please fill in the sessionID field.")
      return
    }

    if (!tryConnect(address, name)) return

    client?.createGame(GAME_ID, sessionID)
  }

  /**
   * Connects to server and joins a game session.
   *
   * @param address Server address and port.
   * @param name Player name.
   * @param sessionID Session ID to join to.
   */
  fun tryJoinGame(address: String, name: String, sessionID: String) {
    if (sessionID.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "SessionID is empty", message = "Please fill in the sessionID field.")
      return
    }

    if (!tryConnect(address, name)) return

    client?.joinGame(sessionID, "greeting")
  }

  /**
   * Connects to server.
   *
   * @param address Server address and port in format "127.0.0.1:8080"
   * @param name Player name.
   */
  private fun tryConnect(address: String, name: String): Boolean {
    if (address.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Address is empty", message = "Please fill in the address field.")
      return false
    }

    val split = address.split(":")
    if (split.size != 2) {
      logicController.view.showConnectWarningDialog(
          title = "Address invalid",
          message = "Address is invalid. Must be in format 127.0.0.1:8080")
      return false
    }

    val parsedHost = BoardGameClient.parseAndValidateIP(split[0])
    if (parsedHost == null) {
      logicController.view.showConnectWarningDialog(
          title = "Hostname invalid",
          message = "Hostname is invalid. Must be IP address or valid hostname.")
      return false
    }

    val parsedPort = BoardGameClient.parseAndValidatePort(split[1])
    if (parsedPort == null) {
      logicController.view.showConnectWarningDialog(
          title = "Port invalid",
          message = "Port is invalid. Must be an integer between 1 and 65534.")
      return false
    }

    if (name.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Name is empty", message = "Please fill in the name field.")
      return false
    }

    val newClient =
        MauMauBoardGameClient(
            playerName = name,
            host = parsedHost,
            port = parsedPort,
            logicController = logicController,
        )

    return if (newClient.connect()) {
      this.client = newClient
      true
    } else {
      false
    }
  }
  // endregion

  // region Send actions
  /** Send initialize game message to connected opponent. */
  fun sendInit(game: MauMauGame) {
    client?.sendGameActionMessage(Serialization.serializeInitMessage(game))
  }

  /** Send game stack shuffled message to connected opponent. */
  fun sendStackShuffled(game: MauMauGame) {
    client?.sendGameActionMessage(Serialization.serializeStacksShuffledMessage(game))
  }

  /** Send [GameActionType.DRAW] action to connected opponent. */
  fun sendCardDrawn() {
    client?.sendGameActionMessage(MauMauGameAction(gameAction = GameActionType.DRAW))
  }

  /** Send [GameActionType.REQUEST_DRAW_TWO] action to connected opponent. */
  fun sendDrawTwoRequest() {
    client?.sendGameActionMessage(MauMauGameAction(gameAction = GameActionType.REQUEST_DRAW_TWO))
  }

  /** Send [GameActionType.PLAY] action to connected opponent. */
  fun sendCardPlayed(card: MauMauCard) {
    client?.sendGameActionMessage(MauMauGameAction(gameAction = GameActionType.PLAY, card = card))
  }

  /** Send [GameActionType.REQUEST_SUIT] action to connected opponent. */
  fun sendSuitSelected(suit: CardSuit) {
    client?.sendGameActionMessage(
        MauMauGameAction(
            gameAction = GameActionType.REQUEST_SUIT,
            card = MauMauCard(cardValue = CardValue.ACE, cardSuit = suit)))
  }

  /** Sends [GameActionType.END_TURN] to connected opponent. */
  fun sendEndTurn() {
    client?.sendGameActionMessage(MauMauGameAction(gameAction = GameActionType.END_TURN))
  }

  /** Sends End game message to connected opponent. */
  fun sendEndGame() {
    client?.sendGameActionMessage(MauMauEndGameAction("Looser!"))
  }
  // endregion
}
