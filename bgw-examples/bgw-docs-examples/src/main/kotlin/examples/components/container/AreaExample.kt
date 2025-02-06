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

package examples.components.container

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  AreaExample()
}

class AreaExample : BoardGameApplication("Area example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val numberOfComponentsLabel: Label = Label(width = 400, posX = 50, posY = 50)
  private val area: Area<TokenView> = Area(100, 400, 50, 100, ColorVisual.DARK_GRAY)

  private val greenToken: TokenView = TokenView(visual = ColorVisual.GREEN)
  private val redToken: TokenView = TokenView(visual = ColorVisual.RED)

  init {
    area.onAdd = {
      this.resize(100, 100)
      numberOfComponentsLabel.text =
          "Number of components in this area: ${area.numberOfComponents()}"
    }
    area.onRemove = { this.rotation += 45 }

    area.add(greenToken)
    area.add(redToken, 0)

    area.remove(redToken)

    gameScene.addComponents(area, numberOfComponentsLabel)
    showGameScene(gameScene)
    show()
  }
}
