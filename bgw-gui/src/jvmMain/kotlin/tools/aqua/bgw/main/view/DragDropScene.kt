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

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.ColorVisual

internal class DragDropScene : BoardGameScene() {
  private val source =
      Area<GameComponentView>(width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY).apply {
        zIndex = 1
        isDraggable = true

        dropAcceptor = { it.draggedComponent is TokenView || it.draggedComponent is Area<*> }
        onDragDropped = {
          println("You got fooled")
          this.visual = ColorVisual.GREEN
        }
      }

  private val target =
      Pane<GameComponentView>(
              posX = 500, posY = 500, width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY)
          .apply {
            dropAcceptor = { it.draggedComponent is TokenView || it.draggedComponent is Area<*> }
            onDragDropped = {
              if (it.draggedComponent is TokenView) {
                source.remove(it.draggedComponent as TokenView)
                add(it.draggedComponent as TokenView)
                (it.draggedComponent as TokenView).isDraggable = false
              } else if (it.draggedComponent is Area<*>) {
                val area = it.draggedComponent as Area<GameComponentView>
                val components = area.components
                components.forEach { component ->
                  if (component is TokenView) {
                    area.remove(component)
                    add(component)
                    component.isDraggable = false
                  }
                }
              }
            }
          }

  private val randomPane =
      Pane<GameComponentView>(
          posX = 500, posY = 0, width = 500, height = 500, visual = ColorVisual.BLUE)

  private val token =
      TokenView(posX = 20, posY = 100, visual = ColorVisual.RED, width = 100, height = 100).apply {
        isDraggable = true
        onDragGestureStarted = {}

        onDragGestureEnded = { event, success ->
          println("Drag gesture ended: $success")
          println(event.draggedComponent)
          println(event.dropTarget)
        }

        onMouseEntered = { posY += 20 }

        onMouseExited = { posY -= 20 }
      }

  val hiddenToken =
      TokenView(
          width = 50,
          height = 50,
          visual = ColorVisual(Color(0x6dbeff)).apply { style.borderRadius = BorderRadius.FULL })

  val satchel =
      Satchel<TokenView>(
              posX = 800,
              posY = 800,
              width = 100,
              height = 100,
              visual = ColorVisual(Color(0x0f141f)))
          .apply { add(hiddenToken) }

  init {
    source.add(token)
    addComponents(satchel)
  }
}
