/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.maumau.service.network

import tools.aqua.bgw.examples.maumau.entity.*
import tools.aqua.bgw.examples.maumau.main.GAME_ID
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.examples.maumau.service.network.NetworkSerialization.serialize
import tools.aqua.bgw.examples.maumau.service.network.NetworkSerialization.serializeGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauEndGameAction
import tools.aqua.bgw.examples.maumau.service.network.messages.MauMauGameAction

/**
 * Service for handling network communication. Offline games may be implemented by setting [client]
 * to 'null'.
 */
class NetworkService(private val logicController: LogicController) {
  /** Network client. Nullable for offline games. */
  private var client: MauMauBoardGameClient? = null

  // region Connection
  /**
   * Connects to server and starts a new game session.
   *
   * @param address Server address and port.
   * @param secret Server secret.
   * @param name Player name.
   * @param sessionID Session ID to host.
   */
  fun hostGame(address: String, secret: String, name: String, sessionID: String) {
    if (!connect(address, secret, name)) return

    if (sessionID.isEmpty()) client?.createGame(GAME_ID, "Welcome!")
    else client?.createGame(GAME_ID, sessionID, "Welcome!")
  }

  /**
   * Connects to server and joins a game session.
   *
   * @param address Server address and port.
   * @param secret Server secret.
   * @param name Player name.
   * @param sessionID Session ID to join to.
   */
  fun joinGame(address: String, secret: String, name: String, sessionID: String) {
    if (sessionID.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "SessionID is empty", message = "Please fill in the sessionID field.")
      return
    }

    if (!connect(address, secret, name)) return

    client?.joinGame(sessionID, "greeting")
  }

  /**
   * Connects to server.
   *
   * @param address Server address and port in format "127.0.0.1:8080"
   * @param secret Network secret.
   * @param name Player name.
   */
  private fun connect(address: String, secret: String, name: String): Boolean {
    if (address.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Address is empty", message = "Please fill in the address field.")
      return false
    }

    if (secret.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Secret is empty", message = "Please fill in the secret field.")
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
            host = address,
            secret = secret,
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
    client?.sendGameActionMessage(NetworkSerialization.serializeInitMessage(game))
  }

  /** Send game stack shuffled message to connected opponent. */
  fun sendStackShuffled(game: MauMauGame) {
    client?.sendGameActionMessage(NetworkSerialization.serializeStacksShuffledMessage(game))
  }

  /** Send [GameActionType.DRAW] action to connected opponent. */
  fun sendCardDrawn() {
    client?.sendGameActionMessage(
        MauMauGameAction(action = serializeGameAction(GameActionType.DRAW)))
  }

  /** Send [GameActionType.REQUEST_DRAW_TWO] action to connected opponent. */
  fun sendDrawTwoRequest() {
    client?.sendGameActionMessage(
        MauMauGameAction(action = serializeGameAction(GameActionType.REQUEST_DRAW_TWO)))
  }

  /** Send [GameActionType.PLAY] action to connected opponent. */
  fun sendCardPlayed(card: MauMauCard) {
    client?.sendGameActionMessage(
        MauMauGameAction(
            action = serializeGameAction(GameActionType.PLAY), card = card.serialize()))
  }

  /** Send [GameActionType.REQUEST_SUIT] action to connected opponent. */
  fun sendSuitSelected(suit: CardSuit) {
    client?.sendGameActionMessage(
        MauMauGameAction(
            action = serializeGameAction(GameActionType.REQUEST_SUIT),
            card = MauMauCard(cardValue = CardValue.ACE, cardSuit = suit).serialize()))
  }

  /** Sends [GameActionType.END_TURN] to connected opponent. */
  fun sendEndTurn() {
    client?.sendGameActionMessage(
        MauMauGameAction(action = serializeGameAction(GameActionType.END_TURN)))
  }

  /** Sends End game message to connected opponent. */
  fun sendEndGame() {
    client?.sendGameActionMessage(MauMauEndGameAction(client?.playerName ?: "Your Opponent"))
  }

  /** Disconnects from the server and closes web socket client. */
  fun close() {
    if (client?.isOpen == true) client?.disconnect()
  }
  // endregion
}
