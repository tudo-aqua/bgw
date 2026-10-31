/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package examples.discover

import kotlin.random.Random
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual

/**
 * The complete "Dice Duel" game from the Discover guides.
 *
 * The scenes in `bgw-gui` (`ExampleDiscoverScene`) mirror the layout of these scenes to render the
 * live previews of the guides. Keep both in sync.
 */
fun main() {
  DiceDuelApplication
}

class DiceDuelMenuScene :
    MenuScene(width = 400, height = 280, background = ColorVisual(Color(0x0F141F))) {

  val titleLabel =
      Label(
          posX = 0,
          posY = 30,
          width = 400,
          height = 60,
          text = "Dice Duel",
          font = Font(size = 36, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD))

  val startButton =
      Button(
          posX = 80,
          posY = 110,
          width = 240,
          height = 56,
          text = "Start game",
          font = Font(size = 20, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
          visual = ColorVisual(Color(0xC6FF6E)).apply { style.borderRadius = BorderRadius.MEDIUM })

  val quitButton =
      Button(
          posX = 80,
          posY = 190,
          width = 240,
          height = 56,
          text = "Quit",
          font = Font(size = 20, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
          visual = ColorVisual(Color(0xFA6C56)).apply { style.borderRadius = BorderRadius.MEDIUM })

  init {
    addComponents(titleLabel, startButton, quitButton)
  }
}

class DiceDuelGameScene :
    BoardGameScene(width = 890, height = 600, background = ColorVisual(Color(0x161D29))) {

  private fun face(value: Int) =
      CompoundVisual(
          ColorVisual(Color.WHITE).apply { style.borderRadius = BorderRadius.LARGE },
          TextVisual(
              text = value.toString(),
              font = Font(size = 84, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD)))

  val playerDice =
      DiceView(
          posX = 245, posY = 240, width = 160, height = 160, visuals = List(6) { face(it + 1) })

  val opponentDice =
      DiceView(
          posX = 485, posY = 240, width = 160, height = 160, visuals = List(6) { face(it + 1) })

  val playerLabel =
      Label(
          posX = 245,
          posY = 190,
          width = 160,
          height = 40,
          text = "You",
          font = Font(size = 24, color = Color(0x6DBEFF)))

  val opponentLabel =
      Label(
          posX = 485,
          posY = 190,
          width = 160,
          height = 40,
          text = "Opponent",
          font = Font(size = 24, color = Color(0xFA6C56)))

  val scoreLabel =
      Label(
          posX = 245,
          posY = 100,
          width = 400,
          height = 70,
          text = "0 : 0",
          font = Font(size = 56, color = Color(0xFFC656), fontWeight = Font.FontWeight.BOLD))

  val rollButton =
      Button(
          posX = 345,
          posY = 440,
          width = 200,
          height = 60,
          text = "Roll",
          font = Font(size = 22, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
          visual = ColorVisual(Color(0xBB6DFF)).apply { style.borderRadius = BorderRadius.MEDIUM })

  /** Gets invoked with `true` if the player won the game and `false` if the opponent did. */
  var onGameOver: ((Boolean) -> Unit)? = null

  init {
    addComponents(scoreLabel, playerLabel, opponentLabel, playerDice, opponentDice, rollButton)
  }

  fun showNewGame() {
    scoreLabel.text = "0 : 0"
    playerDice.currentSide = 0
    opponentDice.currentSide = 0
  }

  fun showRoll(
      playerSide: Int,
      opponentSide: Int,
      playerPoints: Int,
      opponentPoints: Int,
      playerWon: Boolean?
  ) {
    rollButton.isDisabled = true

    playAnimation(
        ParallelAnimation(
                DiceAnimation(dice = playerDice, toSide = playerSide, duration = 1500),
                DiceAnimation(dice = opponentDice, toSide = opponentSide, duration = 1500))
            .apply {
              onFinished = {
                scoreLabel.text = "$playerPoints : $opponentPoints"
                rollButton.isDisabled = false
                playerWon?.let { onGameOver?.invoke(it) }
              }
            })
  }
}

object DiceDuelApplication :
    BoardGameApplication(windowTitle = "Dice Duel", width = 890, height = 600) {

  private val menuScene = DiceDuelMenuScene()
  private val gameScene = DiceDuelGameScene()
  private val gameService = DiceDuelService(gameScene)

  init {
    menuScene.startButton.onMouseClicked = {
      gameService.startGame()
      hideMenuScene()
    }
    menuScene.quitButton.onMouseClicked = { exit() }

    gameScene.rollButton.onMouseClicked = { gameService.rollDice() }
    gameScene.onGameOver = { playerWon ->
      val dialog =
          Dialog(
              dialogType = DialogType.INFORMATION,
              title = "Game over",
              header = if (playerWon) "You win!" else "You lose!",
              message = "Do you want to play another round?")
      dialog.onButtonClicked = { showMenuScene(menuScene) }
      showDialog(dialog)
    }

    showGameScene(gameScene)
    showMenuScene(menuScene)
    show()
  }
}

// region Game logic
// Not part of the guides: the rules of the game are independent of the GUI.

class DiceDuelService(private val scene: DiceDuelGameScene) {
  private var playerPoints = 0
  private var opponentPoints = 0

  fun startGame() {
    playerPoints = 0
    opponentPoints = 0
    scene.showNewGame()
  }

  fun rollDice() {
    val playerSide = Random.nextInt(6)
    val opponentSide = Random.nextInt(6)
    when {
      playerSide > opponentSide -> playerPoints++
      opponentSide > playerSide -> opponentPoints++
    }
    val playerWon =
        when {
          playerPoints == WINNING_SCORE -> true
          opponentPoints == WINNING_SCORE -> false
          else -> null
        }
    scene.showRoll(playerSide, opponentSide, playerPoints, opponentPoints, playerWon)
  }

  private companion object {
    const val WINNING_SCORE = 3
  }
}
// endregion
