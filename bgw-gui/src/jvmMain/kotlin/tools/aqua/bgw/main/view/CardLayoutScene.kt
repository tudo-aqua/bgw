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
import tools.aqua.bgw.visual.CompoundVisual

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
            val card =
                CardView(
                    posX = 0,
                    posY = 0,
                    width = 100,
                    height = 200,
                    front =
                        CompoundVisual(
                            ColorVisual(Color(255, 0, 0)),
                        ))
            this.add(card)

            onMouseClicked = {
              (card.visual as CompoundVisual).children =
                  listOf(
                      *((card.visual as CompoundVisual).children.toTypedArray()),
                      ColorVisual(Color(0, 255, 0)).apply { transparency = 0.1 })
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
              text = "Open Dialog",
              visual = ColorVisual(Color(255, 0, 0)))
          .apply {
            onMouseClicked = {
              Application.showMenuScene(Application.menuScene)
              //              val dialog =
              //                  FileDialog(
              //                      mode = FileDialogMode.OPEN_MULTIPLE_FILES,
              //                      title = "Open BGW File",
              //                      initialDirectoryPath = "F:\\Test",
              //                      initialFileName = "test.bgw",
              //                      extensionFilters =
              //                          listOf(
              //                              ExtensionFilter("Images", "png", "jpg", "jpeg"),
              //                              ExtensionFilter("BoardGameWork File", "bgw"),
              //                              ExtensionFilter("All Files", "*")))
              //
              //              Application.showFileDialog(dialog)
              //
              //              dialog.onPathsSelected = { paths -> println("Selected paths: $paths")
              // }
              //
              //              dialog.onSelectionCancelled = { println("Selection cancelled") }
            }
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
                this.visual = ColorVisual(Color(0, 255, 0))
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
