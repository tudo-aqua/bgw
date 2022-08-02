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

package tools.aqua.bgw.examples.maumau.view

import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.main.CARDS_FILE
import tools.aqua.bgw.examples.maumau.main.IMG_HEIGHT
import tools.aqua.bgw.examples.maumau.main.IMG_WIDTH
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual

/** ViewController for refreshes. */
class RefreshViewController(private val viewController: MauMauViewController) : Refreshable {

  /** texture map for cards. */
  private val image: BufferedImage =
      ImageIO.read(this::class.java.classLoader.getResource(CARDS_FILE))

  /** Current hint overlay. */
  private var hintOverlay: ColorVisual? = null

  // region interface functions
  /** Refreshes after card was drawn. */
  override fun refreshCardDrawn(card: MauMauCard, isCurrentPlayer: Boolean) {
    // find hand to refresh
    val playerHandView =
        if (isCurrentPlayer) viewController.mauMauGameScene.currentPlayerHand
        else viewController.mauMauGameScene.otherPlayerHand

    // transfer card
    val cardView = viewController.cardMap.forward(card)
    viewController.mauMauGameScene.drawStack.remove(cardView)
    playerHandView.add(cardView)

    // show card front if player is currently active
    if (isCurrentPlayer) cardView.showFront()

    // update label
    viewController.mauMauGameScene.drawStackInfo.textProperty.value =
        viewController.logicController.game.drawStack.size().toString()

    // add event handlers to drawn card
    if (isCurrentPlayer) cardView.addInteraction() else cardView.addSneakInteraction()

    // hide suit selection
    showJackEffectSelection(false)

    // clear overlay
    hintOverlay?.transparency = 0.0
  }

  /** Refreshes after card was played. */
  override fun refreshCardPlayed(card: MauMauCard, animated: Boolean, isCurrentPlayer: Boolean) {
    val cardView = viewController.cardMap.forward(card)
    val hand =
        if (isCurrentPlayer) viewController.mauMauGameScene.currentPlayerHand
        else viewController.mauMauGameScene.otherPlayerHand

    // remove event handlers
    cardView.removeInteraction()
    cardView.showFront()

    // hide suit selection
    showJackEffectSelection(false)

    // animate card
    if (animated) {
      val anim =
          MovementAnimation.toComponentView(
              componentView = cardView,
              toComponentViewPosition = viewController.mauMauGameScene.gameStack,
              scene = viewController.mauMauGameScene,
              duration = 500)

      anim.onFinished =
          {
            hand.remove(cardView)
            viewController.mauMauGameScene.gameStack.add(cardView)

            cardView.posX = 0.0
            cardView.posY = 0.0

            // update label
            viewController.mauMauGameScene.gameStackInfo.textProperty.value =
                viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
          }

      viewController.mauMauGameScene.lock()
      viewController.mauMauGameScene.playAnimation(anim)
    } else {
      hand.remove(cardView)
      viewController.mauMauGameScene.gameStack.push(cardView)

      cardView.posX = 0.0
      cardView.posY = 0.0

      // update label
      viewController.mauMauGameScene.gameStackInfo.textProperty.value =
          viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()
    }

    // Clear overlay
    hintOverlay?.transparency = 0.0
  }

  /** Refreshes after game stack was shuffled back. */
  override fun refreshGameStackShuffledBack() {
    viewController.mauMauGameScene.gameStack.apply {
      val saved = pop()
      clear()
      push(saved)
    }

    viewController.mauMauGameScene.drawStack.addAll(
        viewController.logicController.game.drawStack.cards
            .map { viewController.cardMap.forward(it) }
            .onEach {
              it.removeInteraction()
              it.showBack()
            })
  }

  /** Refreshes when player may take another turn. */
  override fun refreshPlayAgain() {
    viewController.mauMauGameScene.unlock()
  }

  /** Shows jack selection. */
  override fun showJackEffectSelection() {
    showJackEffectSelection(true)
  }

  /** Shows hint to draw a card. */
  override fun refreshHintDrawCard() {
    hintOverlay =
        ((viewController.mauMauGameScene.drawStack.components.last().visual as CompoundVisual)
                .children.last() as
                ColorVisual)
            .apply {
              color = Color.YELLOW
              transparency = 0.5
            }
  }

  /**
   * Shows hint to play a card.
   *
   * @param card Card to play.
   */
  override fun refreshHintPlayCard(card: MauMauCard) {
    hintOverlay =
        ((viewController.cardMap.forward(card).visual as CompoundVisual).children.last() as
                ColorVisual)
            .apply {
              color = Color.YELLOW
              transparency = 0.5
            }
  }

  /** Ends game. */
  override fun refreshEndGame(playerWon: String) {
    viewController.showMenuScene(
        scene = viewController.mauMauPlayerWonMenuScene.apply { this.playerWon = playerWon },
        fadeTime = 2000)
  }

  /** Locks gameScene. */
  override fun refreshAdvanceOnlinePlayer() {
    viewController.mauMauGameScene.lock()
  }

  override fun refreshSwapPlayers() {
    viewController.mauMauGameScene.lock()

    val delay =
        DelayAnimation(1000).apply {
          onFinished =
              {
                // swap playerHands
                //				val tmp = viewController.mauMauGameScene.currentPlayerHand
                //				viewController.mauMauGameScene.currentPlayerHand =
                // viewController.mauMauGameScene.otherPlayerHand
                //				viewController.mauMauGameScene.otherPlayerHand = tmp
                //
                //				//swap hand positions
                //				val tmpPosY = viewController.mauMauGameScene.currentPlayerHand.posY
                //				viewController.mauMauGameScene.currentPlayerHand.posY =
                // viewController.mauMauGameScene.otherPlayerHand.posY
                //				viewController.mauMauGameScene.otherPlayerHand.posY = tmpPosY
                //
                //				//add interaction and show front for all cards in currentPlayerHand
                //				viewController.mauMauGameScene.currentPlayerHand.components.forEach { t ->
                //					t.addInteraction()
                //					t.showFront()
                //					t.onMouseEntered = null
                //					t.onMouseExited = null
                //				}
                //
                //				//add interaction and show back for all cards in otherPlayerHand
                //				viewController.mauMauGameScene.otherPlayerHand.components.forEach { t ->
                //					t.removeInteraction()
                //					t.showBack()
                //					t.onMouseEntered = {
                //						viewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.backVisual,
                // t.frontVisual))
                //					}
                //					t.onMouseExited = {
                //						viewController.mauMauGameScene.playAnimation(FlipAnimation(t, t.frontVisual,
                // t.backVisual))
                //					}
                //				}
                //
                //				viewController.mauMauGameScene.currentPlayerHand.rotation += 180.0
                //				viewController.mauMauGameScene.otherPlayerHand.rotation += 180.0
                refreshHands()
                viewController.mauMauGameScene.unlock()
              }
        }

    viewController.mauMauGameScene.lock()
    viewController.mauMauGameScene.playAnimation(delay)
  }

  /** Refresh selected suit and hides jack selection. */
  override fun refreshSuitSelected() {
    viewController.mauMauGameScene.gameStackInfo.text =
        viewController.logicController.game.nextSuit.toString()
    showJackEffectSelection(false)
  }

  /** Indicates refreshes all components. */
  override fun refreshAll() {
    val game = viewController.logicController.game
    val cardBack = ImageVisual(image, IMG_WIDTH, IMG_HEIGHT, 2 * IMG_WIDTH, 4 * IMG_HEIGHT)

    // Generate cards
    viewController.cardMap.clear()
    for (card in game.mauMauCards) {
      val cardFront =
          ImageVisual(
              image,
              IMG_WIDTH,
              IMG_HEIGHT,
              card.cardValue.ordinal * IMG_WIDTH,
              card.cardSuit.ordinal * IMG_HEIGHT,
          )

      val cardView =
          CardView(
                  height = 200,
                  width = 130,
                  front = CompoundVisual(cardFront, ColorVisual.TRANSPARENT),
                  back = CompoundVisual(cardBack, ColorVisual.TRANSPARENT))
              .apply { name = card.toString() }

      viewController.cardMap.add(card, cardView)
    }

    // Add components to stacks
    viewController.mauMauGameScene.drawStack.clear()
    viewController.mauMauGameScene.drawStack.addAll(
        game.drawStack.cards.asReversed().map { card ->
          viewController.cardMap.forward(card).apply { showBack() }
        })

    viewController.mauMauGameScene.gameStack.clear()
    viewController.mauMauGameScene.gameStack.addAll(
        game.gameStack.cards.asReversed().map { card ->
          viewController.cardMap.forward(card).apply { showFront() }
        })

    // update labels
    viewController.mauMauGameScene.drawStackInfo.textProperty.value =
        viewController.logicController.game.drawStack.size().toString()

    viewController.mauMauGameScene.gameStackInfo.textProperty.value =
        viewController.logicController.game.gameStack.cards.peek().cardSuit.toString()

    // Add elements to hands
    refreshHands()

    // hide suit selection
    showJackEffectSelection(false)
  }

  private fun refreshHands() {
    val game = viewController.logicController.game
    viewController.mauMauGameScene.currentPlayerHand.clear()
    viewController.mauMauGameScene.otherPlayerHand.clear()

    for (i in 0 until game.players[0].hand.cards.size) {
      viewController.mauMauGameScene.currentPlayerHand.add(
          viewController.cardMap.forward(game.players[0].hand.cards[i]).apply { showFront() })
    }
    for (i in 0 until game.players[1].hand.cards.size) {
      viewController.mauMauGameScene.otherPlayerHand.add(
          viewController.cardMap.forward(game.players[1].hand.cards[i]).apply { showBack() })
    }

    viewController.mauMauGameScene.currentPlayerName.text = game.players[0].name
    viewController.mauMauGameScene.otherPlayerName.text = game.players[1].name

    // Add EventHandler for cards on Hand
    viewController.mauMauGameScene.currentPlayerHand.components.forEach {
      it.addInteraction()
      it.removeSneakInteraction()
    }
    viewController.mauMauGameScene.otherPlayerHand.components.forEach {
      it.removeInteraction()
      it.addSneakInteraction()
    }
  }

  /**
   * Shows a warning dialog for wrong inputs in host/join menu.
   *
   * @param title Title line.
   * @param message Message to display.
   */
  override fun showConnectWarningDialog(title: String, message: String) {
    viewController.showDialog(
        Dialog(
            dialogType = DialogType.WARNING,
            title = title,
            header = title,
            message = message,
        ))
  }

  override fun onCreateGameSuccess(sessionID: String?) {
    viewController.showDialogNonBlocking(
        Dialog(DialogType.INFORMATION, "Session ID", "Session ID", "Session ID is $sessionID"))
    viewController.showMenuScene(
        viewController.mauMauWaitForOpponentMenuScene.also { it.startAnimation() })
  }

  override fun onJoinGameSuccess() {
    viewController.showMenuScene(
        viewController.mauMauWaitForOpponentMenuScene.also { it.startAnimation() })
  }

  override fun onCreateGameError(message: String) {
    viewController.showDialog(
        Dialog(
            dialogType = DialogType.ERROR,
            title = "Error creating game",
            header = "Error creating game",
            message = message))
  }

  override fun onJoinGameError(message: String) {
    viewController.showDialog(
        Dialog(
            dialogType = DialogType.ERROR,
            title = "Error joining game",
            header = "Error joining game",
            message = message))
  }

  override fun onInitializeGameReceived() {
    viewController.apply {
      mauMauGameScene.startAnimation()
      mauMauGameScene.lock()
      hideMenuScene()
    }
  }

  override fun onShuffleStackReceived() {
    viewController.apply {
      // Add components to stacks
      mauMauGameScene.drawStack.clear()
      mauMauGameScene.drawStack.addAll(
          logicController.game.drawStack.cards.asReversed().map { card ->
            cardMap.forward(card).apply { showBack() }
          })

      mauMauGameScene.gameStack.clear()
      mauMauGameScene.gameStack.addAll(
          logicController.game.gameStack.cards.asReversed().map { card ->
            cardMap.forward(card).apply { showFront() }
          })

      // update labels
      mauMauGameScene.drawStackInfo.textProperty.value =
          logicController.game.drawStack.size().toString()
      mauMauGameScene.gameStackInfo.textProperty.value =
          logicController.game.gameStack.cards.peek().cardSuit.toString()
    }
  }

  override fun refreshEndTurn() {
    viewController.mauMauGameScene.unlock()
  }

  override fun onUserJoined(sender: String) {
    viewController.mauMauWaitForOpponentMenuScene.onOpponentConnected(sender)
  }

  override fun onUserLeft(sender: String) {
    println("$sender left.")
  }

  override fun onServerError() {
    error("Server error")
  }

  // endregion

  // region extension functions
  /** Adds interactivity to card. */
  private fun CardView.addInteraction() {
    /*onMouseEntered = {
    	viewController.gameScene.playAnimation(
    		MovementAnimation(
    			this,
    			fromY = 0.0,
    			toY = -50.0,
    			duration = 500
    		)
    	)
    }
    onMouseExited = {
    	viewController.gameScene.playAnimation(
    		MovementAnimation(
    			this,
    			fromY = -50.0,
    			toY = 0.0,
    			duration = 500
    		)
    	)
    }*/
    isDraggable = true

    var overlay: ColorVisual? = null
    onDragGestureStarted =
        {
          overlay =
              ((viewController.mauMauGameScene.gameStack.components.last().visual as CompoundVisual)
                      .children.last() as
                      ColorVisual)
                  .apply {
                    color =
                        if (viewController.logicController.checkRules(
                            viewController.cardMap.backward(this@addInteraction)))
                            Color.GREEN
                        else Color.RED

                    transparency = 0.5
                  }
        }
    onDragGestureEnded = { _, _ -> overlay?.transparency = 0.0 }
    onMouseClicked =
        {
          viewController.logicController.playCard(
              card = viewController.cardMap.backward(this), animated = true, isCurrentPlayer = true)
        }
  }

  /** Removes interactivity from card. */
  private fun CardView.removeInteraction() {
    isDraggable = false
    onMouseClicked = null
    onMouseEntered = null
    onMouseExited = null
  }

  /** Removes sneak peek interactivity to card. */
  private fun CardView.addSneakInteraction() {
    onMouseEntered =
        {
          viewController.mauMauGameScene.playAnimation(FlipAnimation(this, backVisual, frontVisual))
        }
    onMouseExited =
        {
          viewController.mauMauGameScene.playAnimation(FlipAnimation(this, frontVisual, backVisual))
        }
  }

  /** Removes sneak peek interactivity from card. */
  private fun CardView.removeSneakInteraction() {
    onMouseEntered = null
    onMouseExited = null
  }
  // endregion

  // region helper functions
  /** Shows or hides jack selection. */
  private fun showJackEffectSelection(visible: Boolean) {
    viewController.mauMauGameScene.buttonDiamonds.isVisible = visible
    viewController.mauMauGameScene.buttonHearts.isVisible = visible
    viewController.mauMauGameScene.buttonSpades.isVisible = visible
    viewController.mauMauGameScene.buttonClubs.isVisible = visible
  }
  // endregion
}
