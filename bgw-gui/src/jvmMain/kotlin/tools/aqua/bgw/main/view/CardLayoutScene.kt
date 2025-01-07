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

import kotlin.random.Random
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.visual.ColorVisual

internal class CardLayoutScene : BoardGameScene() {

  val cardStack =
      CardStack<CardView>(
              width = 300,
              height = 200,
              posX = 900,
              posY = 100,
              alignment = Alignment.CENTER,
              visual = ColorVisual.LIGHT_GRAY)
          .apply {
            repeat(5) { i ->
              val card =
                  CardView(
                      posX = 0,
                      posY = 0,
                      width = i,
                      height = 200,
                      front =
                          ColorVisual(
                              Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))))
              println(card.width)
              this.add(card)
            }

            println(this.iterator().asSequence().map { it.width }.toList())
            println(this.peek().width)
          }

  val button =
      Button(
              width = 200,
              height = 200,
              posX = 700,
              posY = 700,
              text = "Add Card",
              visual = ColorVisual(Color(255, 0, 0)))
          .apply {
            dropAcceptor = { it.draggedComponent is CardView && it.draggedComponent != this }

            onDragDropped = { event -> println("Dropped ${event.draggedComponent} on $this") }
          }

  val layout =
      LinearLayout<CardView>(
              width = 150,
              height = 800,
              posX = 100,
              posY = 100,
              orientation = Orientation.VERTICAL,
              alignment = Alignment.BOTTOM_CENTER,
              visual = ColorVisual.LIGHT_GRAY,
              spacing = 15)
          .apply {
            onMouseClicked = { event ->
              if (event.button == MouseButtonType.LEFT_BUTTON) {
                this.add(
                    CardView(
                            posX = 0,
                            posY = 0,
                            width = 100,
                            height = 200,
                            front =
                                ColorVisual(
                                    Color(
                                        Random.nextInt(255),
                                        Random.nextInt(255),
                                        Random.nextInt(255))))
                        .apply {
                          isDraggable = true

                          dropAcceptor = {
                            it.draggedComponent is CardView && it.draggedComponent != this
                          }

                          onDragDropped = { event ->
                            println("Dropped ${event.draggedComponent} on $this")
                          }
                        })
              } else if (event.button == MouseButtonType.RIGHT_BUTTON) {
                this.remove(this.components.last())
              }
            }
          }

  init {
    addComponents(layout, button, cardStack)
    repeat(5) {
      val card =
          CardView(
                  posX = 0,
                  posY = 0,
                  width = 100,
                  height = 200,
                  front =
                      ColorVisual(
                          Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))))
              .apply {
                isDraggable = true

                dropAcceptor = { it.draggedComponent is CardView && it.draggedComponent != this }

                onDragDropped = { event -> println("Dropped ${event.draggedComponent} on $this") }
              }
      layout.add(card)
    }
  }
}
