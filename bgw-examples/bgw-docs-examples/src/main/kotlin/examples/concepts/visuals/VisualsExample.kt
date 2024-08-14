/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package examples.concepts.visuals

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.*

fun main() {
  VisualsExample()
}

class VisualsExample : BoardGameApplication("Visuals example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

  private val token: TokenView =
      TokenView(posX = 860, posY = 400, height = 200, width = 200, visual = Visual.EMPTY)
  private val buttonColor: Button =
      Button(posX = 600, posY = 700, text = "ColorVisual").apply { visual = ColorVisual.LIGHT_GRAY }
  private val buttonImage: Button =
      Button(posX = 800, posY = 700, text = "ImageVisual").apply { visual = ColorVisual.LIGHT_GRAY }
  private val buttonText: Button =
      Button(posX = 1000, posY = 700, text = "TextVisual").apply { visual = ColorVisual.LIGHT_GRAY }
  private val buttonCompound: Button =
      Button(posX = 1200, posY = 700, text = "Compound").apply { visual = ColorVisual.LIGHT_GRAY }

  init {
    buttonColor.onMouseClicked = { token.visual = ColorVisual.GREEN }
    buttonImage.onMouseClicked = { token.visual = ImageVisual("Die.png") }
    buttonText.onMouseClicked = {
      token.visual =
          TextVisual(
              "Roll", Font(size = 20, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD))
    }
    buttonCompound.onMouseClicked = {
      token.visual =
          CompoundVisual(
              ImageVisual("Die.png"),
              ColorVisual.GREEN.apply { transparency = 0.4 },
              TextVisual(
                  "Roll", Font(size = 40, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD)))
    }

    gameScene.addComponents(token, buttonColor, buttonImage, buttonText, buttonCompound)
    showGameScene(gameScene)
    show()
  }
}
