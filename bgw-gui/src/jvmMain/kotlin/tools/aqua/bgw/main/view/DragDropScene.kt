/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

internal class DragDropScene : BoardGameScene() {
  private val source =
      Pane<ComponentView>(width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY).apply {
        zIndex = 1
      }

  private val target =
      Pane<ComponentView>(
              posX = 500, posY = 500, width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY)
          .apply {
            dropAcceptor = { it.draggedComponent is HexagonView }
            onDragDropped = {
              source.remove(it.draggedComponent)
              add(it.draggedComponent)
              (it.draggedComponent as TokenView).isDraggable = false
            }
          }

  private val randomPane =
      Pane<ComponentView>(
          posX = 500, posY = 0, width = 500, height = 500, visual = ColorVisual.BLUE)

  private val token =
      TokenView(posX = 20, posY = 100, visual = ColorVisual.RED, width = 100, height = 100).apply {
        isDraggable = true
        onDragGestureStarted = {}

        onDragGestureEnded = { event, success ->
          println("Drag gesture ended: $success")
          println(event.draggedComponent)
          println(event.dragTargets)
        }

        onMouseEntered = { posY += 20 }

        onMouseExited = { posY -= 20 }
      }

  init {
    source.add(token)
    addComponents(source, target, randomPane)
  }
}
