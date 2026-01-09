/*
 * Copyright 2022-2026 The BoardGameWork Authors
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

package examples.concepts.animation

import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

fun main() {
  AnimationExample()
}

class AnimationExample : BoardGameApplication("Animation example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

  private val imageFront: ImageVisual =
      ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 200)
  private val imageBack: ImageVisual =
      ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 800)
  private val dieVisuals: MutableList<Visual> =
      mutableListOf(
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 0, offsetY = 0),
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 200, offsetY = 0),
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 400, offsetY = 0),
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 600, offsetY = 0),
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 800, offsetY = 0),
          ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 1000, offsetY = 0),
      )
  private val randomCardFaces: MutableList<Visual> =
      mutableListOf(
          ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 0, offsetY = 0),
          ImageVisual(
              path = "card_deck.png", width = 130, height = 200, offsetX = 130, offsetY = 0),
          ImageVisual(
              path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 0),
          ImageVisual(
              path = "card_deck.png", width = 130, height = 200, offsetX = 390, offsetY = 0),
          ImageVisual(
              path = "card_deck.png", width = 130, height = 200, offsetX = 520, offsetY = 0),
          ImageVisual(
              path = "card_deck.png", width = 130, height = 200, offsetX = 650, offsetY = 0),
      )

  private val buttonDelay: Button =
      Button(posX = 900, posY = 800, text = "Delay").apply { visual = ColorVisual.WHITE }
  private val buttonMovement: Button =
      Button(posX = 500, posY = 700, text = "Move").apply { visual = ColorVisual.WHITE }
  private val buttonRotation: Button =
      Button(posX = 650, posY = 700, text = "Rotate").apply { visual = ColorVisual.WHITE }
  private val buttonOpacity: Button =
      Button(posX = 800, posY = 700, text = "Fade").apply { visual = ColorVisual.WHITE }
  private val buttonStretch: Button =
      Button(posX = 950, posY = 700, text = "Scale").apply { visual = ColorVisual.WHITE }
  private val buttonFlip: Button =
      Button(posX = 1100, posY = 700, text = "Flip").apply { visual = ColorVisual.WHITE }
  private val buttonRandomize: Button =
      Button(posX = 1250, posY = 700, text = "Randomize").apply { visual = ColorVisual.WHITE }
  private val buttonDie: Button =
      Button(posX = 1400, posY = 700, text = "Roll").apply { visual = ColorVisual.WHITE }

  private val area =
      Area<GameComponentView>(
          posX = 500, posY = 450, width = 300, height = 200, visual = ColorVisual.BLUE)

  private val cardMovement: CardView =
      CardView(posX = 50, posY = 0, front = imageBack, back = imageFront).apply {
        isDraggable = true
      }
  private val cardRotation: CardView =
      CardView(posX = 650, posY = 450, front = imageFront, back = imageBack)
  private val cardOpacity: CardView =
      CardView(posX = 800, posY = 450, front = imageFront, back = imageBack).apply { opacity = 0.5 }
  private val cardStretch: CardView =
      CardView(posX = 950, posY = 450, front = imageFront, back = imageBack).apply { scaleY = 0.5 }
  private val cardFlip: CardView =
      CardView(posX = 1100, posY = 450, front = imageFront, back = imageBack)
  private val cardRandomize: CardView =
      CardView(posX = 1250, posY = 450, front = imageFront, back = imageBack)
  private val die: DiceView = DiceView(posX = 1400, posY = 500, visuals = dieVisuals)

  init {
    buttonDelay.onMouseClicked = {
      gameScene.lock()
      gameScene.playAnimation(
          DelayAnimation(duration = 2000).apply {
            onFinished = {
              gameScene.unlock()
              showDialog(Dialog(DialogType.NONE, "", "", ""))
            }
          })
    }

    buttonMovement.onMouseClicked = {
      gameScene.playAnimation(
          SequentialAnimation(
              SequentialAnimation(
                  RotationAnimation(componentView = cardMovement, byAngle = 45.0, duration = 2000),
                  MovementAnimation(componentView = cardMovement, byY = -50, duration = 2000)),
              ParallelAnimation(
                  MovementAnimation(componentView = cardRotation, byX = -50, duration = 1000),
                  RotationAnimation(componentView = cardRotation, byAngle = -45.0, duration = 2000),
              )))
    }
    buttonRotation.onMouseClicked = {
      gameScene.playAnimation(
          RotationAnimation(componentView = cardRotation, byAngle = 420.0, duration = 1000))
    }
    buttonOpacity.onMouseClicked = {
      gameScene.playAnimation(
          FadeAnimation(
              componentView = cardRotation, fromOpacity = 0.5, toOpacity = 0.0, duration = 1000))
    }
    buttonStretch.onMouseClicked = {
      gameScene.playAnimation(
          ParallelAnimation(
              RotationAnimation(componentView = cardMovement, byAngle = 45.0, duration = 500)
                  .apply { onFinished = { cardStretch.scaleY = 1.0 } },
              ScaleAnimation(componentView = cardStretch, byScaleY = 0.5, duration = 1000)))
    }

    buttonFlip.onMouseClicked = {
      gameScene.playAnimation(
          FlipAnimation(
                  gameComponentView = cardFlip,
                  fromVisual =
                      if (cardFlip.currentSide == CardView.CardSide.FRONT) cardFlip.frontVisual
                      else cardFlip.backVisual,
                  toVisual = ColorVisual.RED,
                  duration = 1000)
              .apply {
                onFinished = {
                  println(
                      "Cardside ${cardFlip.currentSide} (${cardFlip.frontVisual} | ${cardFlip.backVisual})")
                }
              })
    }
    buttonRandomize.onMouseClicked = {
      gameScene.playAnimation(
          RandomizeAnimation(
              gameComponentView = cardRandomize,
              visuals = randomCardFaces,
              toVisual = cardFlip.frontVisual,
              duration = 1000,
              speed = 50))
    }
    buttonDie.onMouseClicked = {
      gameScene.playAnimation(
          DiceAnimation(dice = die, toSide = 3, duration = 1000, speed = 50).apply {
            onFinished = { die.currentSide = 3 }
          })
    }

    area.add(cardMovement.apply { rotation = 45.0 })
    area.rotation = 70.0

    gameScene.addComponents(
        buttonDelay,
        buttonMovement,
        buttonRotation,
        buttonOpacity,
        buttonStretch,
        buttonFlip,
        buttonRandomize,
        buttonDie,
        area,
        cardRotation,
        cardOpacity,
        cardStretch,
        cardFlip,
        cardRandomize,
        die)
    showGameScene(gameScene)
    show()
  }
}
