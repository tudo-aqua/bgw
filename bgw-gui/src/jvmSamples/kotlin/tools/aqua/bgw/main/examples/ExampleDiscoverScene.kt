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

package tools.aqua.bgw.main.examples

import PropData
import jsonMapper
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.mapper.AnimationMapper
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual

// The classes below mirror the scenes of the "Dice Duel" game that is built step by step in the
// Discover guides. The complete, runnable game (including the game logic) lives in
// bgw-examples/bgw-docs-examples. Keep the layouts in sync.

/** Menu of the Dice Duel game. */
internal class DiceDuelMenuScene :
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

/** Game scene of the Dice Duel game (layout only). */
internal class DiceDuelGameScene :
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

  init {
    addComponents(scoreLabel, playerLabel, opponentLabel, playerDice, opponentDice, rollButton)
  }
}

/** Provides the live previews for the Discover guides. */
internal class ExampleDiscoverScene {
  val map = mutableMapOf<String, String>()

  init {
    val gameScene = DiceDuelGameScene()

    serialize(
        "emptyGameScene",
        gameScene =
            BoardGameScene(width = 890, height = 600, background = ColorVisual(Color(0x161D29))))
    // Menu scenes are always rendered above a game scene, so all previews share the same size.
    val emptyGameScene = {
      BoardGameScene(width = 890, height = 600, background = ColorVisual(Color(0x161D29)))
    }
    serialize(
        "emptyMenuScene",
        menuScene = MenuScene(width = 400, height = 280, background = ColorVisual(Color(0x0F141F))),
        gameScene = emptyGameScene())
    serialize("menuScene", menuScene = DiceDuelMenuScene(), gameScene = emptyGameScene())
    serialize("gameScene", gameScene = gameScene)
    serialize("menuOverGameScene", menuScene = DiceDuelMenuScene(), gameScene = DiceDuelGameScene())

    serialize(
        "gameSceneLabels",
        gameScene =
            DiceDuelGameScene().apply {
              clearComponents()
              addComponents(scoreLabel, playerLabel, opponentLabel)
            })

    serialize(
        "gameSceneDice",
        gameScene =
            DiceDuelGameScene().apply {
              clearComponents()
              addComponents(playerDice, opponentDice)
            })

    serialize(
        "gameSceneWithoutButton",
        gameScene =
            DiceDuelGameScene().apply {
              clearComponents()
              addComponents(scoreLabel, playerLabel, opponentLabel, playerDice, opponentDice)
            })

    // The dice have to be the first components, the animation is attached to them in the preview.
    val rollScene = DiceDuelGameScene()
    serialize(
        "rollScene",
        gameScene =
            rollScene.apply {
              clearComponents()
              addComponents(
                  playerDice, opponentDice, scoreLabel, playerLabel, opponentLabel, rollButton)
            })
    serializeAnimation(
        "rollAnimation",
        ParallelAnimation(
            DiceAnimation(dice = rollScene.playerDice, toSide = 5, duration = 1500),
            DiceAnimation(dice = rollScene.opponentDice, toSide = 2, duration = 1500)))
  }

  private fun serialize(
      name: String,
      menuScene: MenuScene? = null,
      gameScene: BoardGameScene? = null
  ) {
    val appData = SceneMapper.map(menuScene = menuScene, gameScene = gameScene)
    map["${this::class.qualifiedName}.$name"] = jsonMapper.encodeToString(PropData(appData))
  }

  private fun serializeAnimation(name: String, animation: Animation) {
    val animationData = AnimationMapper.map(animation)
    map["${this::class.qualifiedName}.$name"] = jsonMapper.encodeToString(PropData(animationData))
  }
}
