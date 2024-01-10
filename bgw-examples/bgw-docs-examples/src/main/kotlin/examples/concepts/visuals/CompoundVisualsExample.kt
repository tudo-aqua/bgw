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
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual

fun main() {
  CompoundVisualsExample()
}

class CompoundVisualsExample : BoardGameApplication("CompoundVisuals example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

  private val token1: TokenView =
      TokenView(
          posX = 660,
          posY = 400,
          height = 200,
          width = 130,
          visual = CompoundVisual(ColorVisual.YELLOW, TextVisual(text = "Hint")))
  private val token2: TokenView =
      TokenView(
          posX = 860,
          posY = 400,
          height = 200,
          width = 130,
          visual =
              CompoundVisual(
                  ImageVisual(
                      path = "card_deck.png",
                      width = 130,
                      height = 200,
                      offsetX = 260,
                      offsetY = 200),
                  TextVisual(text = "3 of Diamonds")))
  private val token3: TokenView =
      TokenView(
          posX = 1060,
          posY = 400,
          height = 200,
          width = 130,
          visual =
              CompoundVisual(
                  ImageVisual(
                      path = "card_deck.png",
                      width = 130,
                      height = 200,
                      offsetX = 260,
                      offsetY = 200),
                  ColorVisual.GREEN.apply { transparency = 0.2 }))

  init {
    gameScene.addComponents(token1, token2, token3)
    showGameScene(gameScene)
    show()
  }
}
