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

package examples.concepts.draganddrop

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  init {
    showGameScene(DragAndDropScene())
    show()
  }
}

class DragAndDropScene :
    BoardGameScene(width = 445, height = 300, background = ColorVisual(Color(0x0F141F))) {

  private val redToken =
      TokenView(
          posX = 40,
          posY = 40,
          width = 70,
          height = 70,
          visual = ColorVisual(Color(0xEF4444)).apply { style.borderRadius = BorderRadius.FULL })

  private val greenToken =
      TokenView(
          posX = 40,
          posY = 190,
          width = 70,
          height = 70,
          visual = ColorVisual(Color(0xC6FF6E)).apply { style.borderRadius = BorderRadius.FULL })

  private val redArea =
      Area<GameComponentView>(
          posX = 310,
          posY = 25,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xEF4444)).apply { transparency = 0.3 })

  private val greenArea =
      Area<GameComponentView>(
          posX = 310,
          posY = 175,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xC6FF6E)).apply { transparency = 0.3 })

  init {
    connect(redToken, redArea)
    connect(greenToken, greenArea)
    addComponents(redToken, greenToken, redArea, greenArea)
  }

  private fun connect(token: TokenView, area: Area<GameComponentView>) {
    token.isDraggable = true
    token.onDragGestureStarted = { token.scale(1.2) }
    token.onDragGestureEnded = { _, success ->
      token.scale(1.0)
      if (success) {
        token.isDraggable = false
      }
    }

    area.dropAcceptor = { event -> event.draggedComponent == token }
    area.onDragDropped = {
      token.removeFromParent()
      token.reposition(15, 15)
      area.add(token)
    }
  }
}
