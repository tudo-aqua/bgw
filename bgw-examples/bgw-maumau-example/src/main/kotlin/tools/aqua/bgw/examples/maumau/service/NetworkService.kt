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
import tools.aqua.bgw.examples.maumau.service.messages.GameActionMessage
import tools.aqua.bgw.examples.maumau.service.messages.GameOverMessage
import java.net.InetAddress

/** Service for handling network communication. */
class NetworkService(private val logicController: LogicController) {
  /** Network client. Nullable for offline games. */
  private var client: NetworkClientService? = null

  // region Connection
  /**
   * Connects to server and starts a new game session.
   *
   * @param address Server address and port.
   * @param name Player name.
   * @param sessionID Session ID to host.
   */
  fun tryHostGame(address: String, name: String, sessionID: String) {
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
    val split = address.split(":")

    client =
        NetworkClientService(
                playerName = name,
                host = split[0],
                port = split[1].toInt(),
                logicController = logicController,
            )
            .apply { connect() }

    return true // TODO: Check status after bgw-net error handling upgrade
  }
  // endregion

  // region Send actions
  /** Send initialize game message to connected opponent. */
  fun sendInit(game: MauMauGame) {
    client?.sendInitializeGameMessage(Serialization.serializeInitMessage(game))
  }

  /** Send [GameAction.DRAW] action to connected opponent. */
  fun sendCardDrawn() {
    client?.sendGameActionMessage(GameActionMessage(gameAction = GameAction.DRAW))
  }

  /** Send [GameAction.REQUEST_DRAW_TWO] action to connected opponent. */
  fun sendDrawTwoRequest() {
    client?.sendGameActionMessage(GameActionMessage(gameAction = GameAction.REQUEST_DRAW_TWO))
  }

  /** Send [GameAction.PLAY] action to connected opponent. */
  fun sendCardPlayed(card: MauMauCard) {
    client?.sendGameActionMessage(GameActionMessage(gameAction = GameAction.PLAY, card = card))
  }

  /** Send [GameAction.REQUEST_SUIT] action to connected opponent. */
  fun sendSuitSelected(suit: CardSuit) {
    client?.sendGameActionMessage(
        GameActionMessage(
            gameAction = GameAction.REQUEST_SUIT,
            card = MauMauCard(cardValue = CardValue.ACE, cardSuit = suit)))
  }

  /** Sends [GameAction.END_TURN] to connected opponent. */
  fun sendEndTurn() {
    client?.sendGameActionMessage(GameActionMessage(gameAction = GameAction.END_TURN))
  }

  fun sendEndGame() {
    client?.sendEndGameMessage(GameOverMessage("Looser!"))
  }
  // endregion

  // region Helper
  /**
   * Checks [address], [name] and [gameID] for not being empty, [address] to be parsable to an ip
   * and port and [gameID] for being a positive integer.
   */
  fun validateInputs(address: String, name: String, gameID: String): Boolean {
    if (address.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Address is empty", message = "Please fill in the address field.")
    }

    val split = address.split(":")
    if (split.size != 2 || !validateIP(split[0]) || !validatePort(split[1])) {
      logicController.view.showConnectWarningDialog(
          title = "Address invalid",
          message = "Address is invalid. Must be in format 127.0.0.1:8080")
    }

    if (name.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "Name is empty", message = "Please fill in the name field.")
      return false
    }

    if (gameID.isEmpty()) {
      logicController.view.showConnectWarningDialog(
          title = "gameID is empty", message = "Please fill in the gameID field.")
      return false
    }

    return true
  }

  /**
   * Tries parsing [ip] into a hostname.
   *
   * @return 'true' if InetAddress.getByName returns a valid connection.
   */
  private fun validateIP(ip: String): Boolean {
    val converted: InetAddress?
    try {
      converted = InetAddress.getByName(ip)
    } catch (_: Exception) {
      return false
    }
    return converted != null
  }

  /** Tries parsing [port] into an ip port. */
  private fun validatePort(port: String): Boolean = (port.toIntOrNull() ?: false) in 1..65_534
  // endregion
}
