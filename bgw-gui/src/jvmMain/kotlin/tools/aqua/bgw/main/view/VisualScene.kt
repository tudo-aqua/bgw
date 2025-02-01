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

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

internal class VisualScene : BoardGameScene() {
  val card =
      CardView(
              width = 150,
              height = 150,
              posX = 50,
              posY = 0,
              front =
                  ImageVisual(
                      "assets/1.jpg",
                  ),
              back = ColorVisual.BLUE)
          .apply {
            currentSide = CardView.CardSide.FRONT
            onMouseClicked = { frontVisual = ImageVisual("assets/2.jpg") }
          }

  val card2 =
      CardView(
          width = 150,
          height = 150,
          posX = 50,
          posY = 0,
          front = CompoundVisual(ColorVisual.RED, ImageVisual("icon.png", offsetX = 40)))

  val token1 =
      TokenView(
          width = 50,
          height = 100,
          posX = 50,
          posY = 0,
          visual = CompoundVisual(ColorVisual.GREEN, ImageVisual("icon.png", offsetX = 40)))

  val list =
      ListView<GameComponentView>(
              width = 300,
              height = 250,
              posX = 0,
              posY = 250,
              visual = ColorVisual.GREEN,
              formatFunction = { it::class.simpleName.toString() },
              selectionMode = SelectionMode.MULTIPLE,
              selectionBackground = ColorVisual.RED,
              font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
          )
          .apply {
            items.add(card2)
            items.add(token1)
          }

  val button =
      Button(posX = 800, posY = 800, visual = ColorVisual.BLUE).apply {
        onMouseClicked = {
          list.selectAll()
          println(list.selectedIndices.toList())
        }
      }

  val column1 =
      TableColumn<GameComponentView>(
          title = "Width",
          width = 150,
          formatFunction = { it.width.toString() },
          font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
      )

  val column2 =
      TableColumn<GameComponentView>(
          title = "Height", width = 750, formatFunction = { it.height.toString() })

  val table =
      TableView<GameComponentView>(
              width = 700,
              height = 250,
              posX = 400,
              posY = 250,
              visual = Visual.EMPTY,
              selectionMode = SelectionMode.SINGLE,
              selectionBackground = ColorVisual.RED,
              columns = mutableListOf(column1, column2))
          .apply {
            items.add(card2)
            items.add(token1)
          }

  val label =
      Label(
          posX = 400,
          posY = 250,
          width = 150,
          height = 50,
          alignment = Alignment.BOTTOM_LEFT,
          isWrapText = false,
          visual = ColorVisual.YELLOW)

  init {
    addComponents(list, label, table, card)
    list.selectIndex(1)

    list.selectedItems.addListener { _, newValue -> println(newValue) }
  }
}
