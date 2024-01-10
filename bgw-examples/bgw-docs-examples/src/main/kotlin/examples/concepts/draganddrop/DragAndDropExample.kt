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

package examples.concepts.draganddrop

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
  private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

  private val redArea: Area<TokenView> =
      Area(height = 50, width = 50, posX = 200, posY = 20, visual = ColorVisual(255, 0, 0, 100))

  private val greenArea: Area<TokenView> =
      Area(height = 50, width = 50, posX = 200, posY = 200, visual = ColorVisual(0, 255, 0, 100))

  init {
    redToken.isDraggable = true
    redToken.onDragGestureEnded = { _, success ->
      if (success) {
        redToken.isDraggable = false
      }
    }

    greenToken.isDraggable = true
    greenToken.onDragGestureEnded = { _, success ->
      if (success) {
        greenToken.isDraggable = false
      }
    }

    redArea.dropAcceptor = { dragEvent ->
      when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == redToken
        else -> false
      }
    }
    redArea.onDragDropped = { dragEvent ->
      redArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
    }

    greenArea.dropAcceptor = { dragEvent ->
      when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == greenToken
        else -> false
      }
    }
    greenArea.onDragDropped = { dragEvent ->
      greenArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
    }

    gameScene.addComponents(redToken, greenToken, redArea, greenArea)
    showGameScene(gameScene)
    show()
  }
}
