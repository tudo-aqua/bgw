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

package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.examples.maumau.view.scenes.*
import tools.aqua.bgw.util.BidirectionalMap

/** Main view controller. */
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {

  /** The main menu scene. */
  private val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()

  /** The host game menu scene. */
  private val mauMauHostGameMenuScene: MauMauHostGameMenuScene = MauMauHostGameMenuScene()

  /** The join game menu scene. */
  private val mauMauJoinGameMenuScene: MauMauJoinGameMenuScene = MauMauJoinGameMenuScene()

  /** The waiting for opponent menu scene. */
  val mauMauWaitForOpponentMenuScene: MauMauWaitForOpponentMenuScene =
      MauMauWaitForOpponentMenuScene()

  /** The player won menu scene. */
  val mauMauPlayerWonMenuScene: MauMauPlayerWonScene = MauMauPlayerWonScene()

  /** The main game scene. */
  val mauMauGameScene: MauMauGameScene = MauMauGameScene()

  /** Refresh view controller instance for the [LogicController] callbacks. */
  private val refreshViewController: RefreshViewController = RefreshViewController(this)

  /** Logic controller instance. */
  val logicController: LogicController = LogicController(refreshViewController)

  /** CardMap mapping entity cards onto view components. */
  val cardMap: BidirectionalMap<MauMauCard, CardView> = BidirectionalMap()

  init {
    registerGameEvents()
    registerMainMenuEvents()
    registerPlayerWonMenuEvents()
    registerHostMenuEvents()
    registerJoinMenuEvents()
    showGameScene(mauMauGameScene)
    showMenuScene(mauMauMenuScene)

    onWindowClosed = { logicController.networkService.close() }

    show()
  }

  /** Registers events in the main game scene. */
  private fun registerGameEvents() {
    mauMauGameScene.mainMenuButton.onMouseClicked = { showMenuScene(mauMauMenuScene) }

    // Register hint button to calculate hint for current player
    mauMauGameScene.hintButton.onMouseClicked = { logicController.showHint() }

    // Set onClick handler for draw stack
    mauMauGameScene.drawStack.onMouseClicked = {
      if (!logicController.game.drawStack.isEmpty())
          logicController.drawCard(isCurrentPlayer = true, advance = true)
    }

    // Set drag drop acceptor and handler for game stack
    mauMauGameScene.gameStack.dropAcceptor = this::tryElementDropped
    mauMauGameScene.gameStack.onDragDropped = this::elementDropped

    // Set onClick handler for jack selection
    mauMauGameScene.buttonDiamonds.onMousePressed = {
      logicController.selectSuit(CardSuit.DIAMONDS, true)
    }
    mauMauGameScene.buttonHearts.onMousePressed = {
      logicController.selectSuit(CardSuit.HEARTS, true)
    }
    mauMauGameScene.buttonSpades.onMousePressed = {
      logicController.selectSuit(CardSuit.SPADES, true)
    }
    mauMauGameScene.buttonClubs.onMousePressed = {
      logicController.selectSuit(CardSuit.CLUBS, true)
    }
  }

  /**
   * Calculates whether the dragged card may be played.
   *
   * @param event Drag event.
   *
   * @return `true` if playing the dragged card is a valid move
   */
  private fun tryElementDropped(event: DragEvent): Boolean {
    if (event.draggedComponent !is CardView) return false

    return logicController.checkRules(cardMap.backward(event.draggedComponent as CardView))
  }

  /**
   * Plays dragged card after successful drop.
   *
   * @param event Drag event.
   */
  private fun elementDropped(event: DragEvent) {
    logicController.playCard(
        card = cardMap.backward(event.draggedComponent as CardView),
        animated = false,
        isCurrentPlayer = true)
  }

  /** Registers events in the main menu scene. */
  private fun registerMainMenuEvents() {
    mauMauMenuScene.continueGameButton.onMouseClicked = { hideMenuScene() }

    mauMauMenuScene.newLocalGameButton.onMouseClicked = {
      logicController.isOnline = false
      logicController.isHost = true

      logicController.newGame()
      hideMenuScene()
    }

    mauMauMenuScene.hostGameButton.onMouseClicked = {
      showMenuScene(mauMauHostGameMenuScene)
      logicController.isOnline = true
      logicController.isHost = true
    }

    mauMauMenuScene.joinGameButton.onMouseClicked = {
      showMenuScene(mauMauJoinGameMenuScene)
      logicController.isOnline = true
      logicController.isHost = false
    }

    mauMauWaitForOpponentMenuScene.startGameButton.onMouseClicked = {
      hideMenuScene()
      logicController.newGame(logicController.game)
      mauMauGameScene.startAnimation()
    }

    mauMauMenuScene.exitButton.onMouseClicked = { exit() }
  }

  /** Registers events in the player won menu scene. */
  private fun registerPlayerWonMenuEvents() {
    mauMauPlayerWonMenuScene.newGameButton.onMouseClicked = {
      logicController.newGame(logicController.game)
      hideMenuScene()
      mauMauGameScene.unlock()
    }

    mauMauPlayerWonMenuScene.exitButton.onMouseClicked = { exit() }
  }

  /** Registers events in the host game menu scene. */
  private fun registerHostMenuEvents() {
    mauMauHostGameMenuScene.hostGameButton.onMouseClicked = {
      val address = mauMauHostGameMenuScene.addressText.text.trim()
      val secret = mauMauHostGameMenuScene.secretText.text.trim()
      val name = mauMauHostGameMenuScene.nameText.text.trim()
      val sessionID = mauMauHostGameMenuScene.sessionIDText.text.trim()

      logicController.networkService.hostGame(address, secret, name, sessionID)
      logicController.game.players[0].name = name
    }

    mauMauHostGameMenuScene.backButton.onMouseClicked = { showMenuScene(mauMauMenuScene) }
  }

  /** Registers events in the join game menu scene. */
  private fun registerJoinMenuEvents() {
    mauMauJoinGameMenuScene.joinGameButton.onMouseClicked = {
      val address = mauMauJoinGameMenuScene.addressText.text.trim()
      val secret = mauMauJoinGameMenuScene.secretText.text.trim()
      val name = mauMauJoinGameMenuScene.nameText.text.trim()
      val sessionID = mauMauJoinGameMenuScene.sessionIDText.text.trim()

      logicController.networkService.joinGame(address, secret, name, sessionID)
      logicController.game.players[0].name = name
    }

    mauMauJoinGameMenuScene.backButton.onMouseClicked = { showMenuScene(mauMauMenuScene) }
  }
}
