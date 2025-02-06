/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.maumau.view.scenes

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.main.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/** Main game scene for MauMau. */
class MauMauGameScene : BoardGameScene(background = ImageVisual(BG_FILE)) {
  // region Player hands
  /** Player one's hand. */
  var currentPlayerHand: LinearLayout<CardView> =
      LinearLayout(
          height = 220,
          width = 800,
          posX = 560,
          posY = 750,
          spacing = -50,
          alignment = Alignment.CENTER,
          visual = ColorVisual(255, 255, 255, 50))

  /** Player two's hand. */
  var otherPlayerHand: LinearLayout<CardView> =
      LinearLayout<CardView>(
              height = 220,
              width = 800,
              posX = 560,
              posY = 50,
              spacing = -50,
              alignment = Alignment.CENTER,
              visual = ColorVisual(255, 255, 255, 50))
          .apply { rotation = 180.0 }

  /** Player one's name. */
  var currentPlayerName: Label =
      Label(
          height = 220,
          width = 200,
          posX = 350,
          posY = 750,
          alignment = Alignment.CENTER_RIGHT,
          font = Font(size = 16, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD))

  /** Player two's name. */
  var otherPlayerName: Label =
      Label(
          height = 220,
          width = 200,
          posX = 350,
          posY = 50,
          alignment = Alignment.CENTER_RIGHT,
          font = Font(size = 16, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD))
  // endregion

  // region Stacks
  /** The draw stack. */
  val drawStack: CardStack<CardView> =
      CardStack(
          height = 200,
          width = 130,
          posX = 750,
          posY = 360,
          visual = ColorVisual(255, 255, 255, 50))

  /** The game stack. */
  val gameStack: CardStack<CardView> =
      CardStack(
          height = 200,
          width = 130,
          posX = 1040,
          posY = 360,
          visual = ColorVisual(255, 255, 255, 50))

  /** The draw stack info label. */
  val drawStackInfo: Label = Label(height = 40, width = 130, posX = 750, posY = 320)

  /** The game stack info label. */
  val gameStackInfo: Label = Label(height = 40, width = 130, posX = 1040, posY = 320)

  /** The label indication waiting for the opponent's turn. */
  private val waitForOpponentLabel: Label =
      Label(
              height = 50,
              width = (gameStack.posX + gameStack.width) - drawStack.posX,
              posX = drawStack.posX,
              posY = 620,
              font = Font(size = 26, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD))
          .apply { isVisible = false }
  // endregion stacks

  // region Jack selection
  /** Button "CLUBS" for jack selection. */
  val buttonClubs: Button =
      Button(
          height = 200,
          width = 130,
          posX = 820,
          posY = 250,
          visual =
              ImageVisual(
                  CARDS_FILE,
                  IMG_WIDTH,
                  IMG_HEIGHT,
                  CardValue.ACE.ordinal * IMG_WIDTH,
                  CardSuit.CLUBS.ordinal * IMG_HEIGHT))

  /** Button "SPADES" for jack selection. */
  val buttonSpades: Button =
      Button(
          height = 200,
          width = 130,
          posX = 970,
          posY = 250,
          visual =
              ImageVisual(
                  CARDS_FILE,
                  IMG_WIDTH,
                  IMG_HEIGHT,
                  CardValue.ACE.ordinal * IMG_WIDTH,
                  CardSuit.SPADES.ordinal * IMG_HEIGHT))

  /** Button "HEARTS" for jack selection. */
  val buttonHearts: Button =
      Button(
          height = 200,
          width = 130,
          posX = 820,
          posY = 470,
          visual =
              ImageVisual(
                  CARDS_FILE,
                  IMG_WIDTH,
                  IMG_HEIGHT,
                  CardValue.ACE.ordinal * IMG_WIDTH,
                  CardSuit.HEARTS.ordinal * IMG_HEIGHT))

  /** Button "DIAMONDS" for jack selection. */
  val buttonDiamonds: Button =
      Button(
          height = 200,
          width = 130,
          posX = 970,
          posY = 470,
          visual =
              ImageVisual(
                  CARDS_FILE,
                  IMG_WIDTH,
                  IMG_HEIGHT,
                  CardValue.ACE.ordinal * IMG_WIDTH,
                  CardSuit.DIAMONDS.ordinal * IMG_HEIGHT))
  // endregion

  /** Hint button. */
  val hintButton: Button =
      Button(
          height = 80,
          width = 80,
          posX = 1430,
          posY = 820,
          font = Font(20.0, fontStyle = FontStyle.ITALIC, color = Color.WHITE),
          visual = ImageVisual(LIGHT_BULB_FILE))

  /** Main menu button. */
  val mainMenuButton: Button =
      Button(
          height = 100,
          width = 200,
          posX = 20,
          posY = 20,
          text = "Main menu",
          font = Font(20.0, fontStyle = FontStyle.ITALIC, color = Color.WHITE),
          visual = ImageVisual(BUTTON_BG_FILE))

  private var dots = 1
  private val timer = Timer(true)

  init {
    buttonClubs.isVisible = false
    buttonSpades.isVisible = false
    buttonHearts.isVisible = false
    buttonDiamonds.isVisible = false

    addComponents(
        drawStack,
        gameStack,
        currentPlayerHand,
        otherPlayerHand,
        currentPlayerName,
        otherPlayerName,
        drawStackInfo,
        gameStackInfo,
        waitForOpponentLabel,
        buttonDiamonds,
        buttonHearts,
        buttonSpades,
        buttonClubs,
        hintButton,
        mainMenuButton)

    onLockChanged = { nV -> waitForOpponentLabel.isVisible = nV }
  }

  /** Starts the dot animation. */
  fun startAnimation() {
    timer.scheduleAtFixedRate(delay = 0, period = 500) {
      BoardGameApplication.runOnGUIThread {
        waitForOpponentLabel.text = "Waiting for opponents turn" + ".".repeat(dots)
        dots %= 3
        dots++
      }
    }
  }
}
