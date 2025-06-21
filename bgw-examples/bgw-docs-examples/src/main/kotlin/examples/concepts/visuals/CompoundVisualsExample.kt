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

package examples.concepts.visuals

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  CompoundVisualsExample()
}

class CompoundVisualsExample : BoardGameApplication("CompoundVisuals example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

  private val gridView =
      GridPane<TokenView>(
          columns = 3,
          rows = 3,
          posX = 1920 / 2,
          posY = 1080 / 2,
          spacing = 2,
      )

  init {
    gameScene.addComponents(gridView)

    for (i in 0 until gridView.columns) {
      for (j in 0 until gridView.rows) {
        val tokenView =
            TokenView(posX = 0, posY = 0, width = 50, height = 50, visual = ColorVisual.RED)

        tokenView.onMouseClicked = { event ->
          tokenView.visual =
              listOf(
                      ColorVisual.GREEN,
                      ColorVisual.BLUE,
                      ColorVisual.YELLOW,
                      ColorVisual.PURPLE,
                      ColorVisual.ORANGE,
                      ColorVisual.CYAN)
                  .random()
            gridView.set(i,j, null)
        }
        gridView[i, j] = tokenView
      }
    }

    showGameScene(gameScene)
    show()
  }
}
