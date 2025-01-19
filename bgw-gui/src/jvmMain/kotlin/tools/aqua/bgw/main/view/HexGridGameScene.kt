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
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

internal class HexGridGameScene : BoardGameScene() {
  private val hexGrid =
      HexagonGrid<HexagonView>(
          width = 500,
          height = 500,
          posX = 0,
          posY = 0,
          coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET,
          visual = Visual.EMPTY,
          orientation = HexOrientation.POINTY_TOP)

  private val satchel =
      Satchel<HexagonView>(
              posX = 1100,
              posY = 0,
              width = 100,
              height = 100,
              // visual =
              // ImageVisual("https://static.vecteezy.com/system/resources/previews/010/256/326/non_2x/premium-flat-icon-of-game-bag-vector.jpg")
              )
          .apply {
            dropAcceptor = { it.draggedComponent is HexagonView }
            onDragDropped = { placeInSatchel(it.draggedComponent as HexagonView) }
          }

  private val singleHex =
      HexagonView(
              posX = 1100,
              posY = 0,
              visual = ColorVisual.BLUE.copy().apply { style.borderRadius = BorderRadius(4) },
              size = 50)
          .apply {
            onWheel = {
              println(
                  "Scrolled ${it.direction} Alt: ${it.isAltDown} Shift: ${it.isShiftDown} Ctrl: ${it.isControlDown}")
            }
          }

  val paneDot =
      Label(
          posX = -3,
          posY = -3,
          width = 6,
          height = 6,
          visual = ColorVisual.BLUE.copy().apply { style.borderRadius = BorderRadius.FULL })

  val paneDot2 =
      Label(
          posX = 200 - 3,
          posY = 100 - 3,
          width = 6,
          height = 6,
          visual = ColorVisual.MAGENTA.copy().apply { style.borderRadius = BorderRadius.FULL })

  private val targetPane =
      Pane<ComponentView>(
              width = 1920, height = 1080, posX = 0, posY = 0, visual = ImageVisual("assets/3.jpg"))
          .apply {
            add(hexGrid)
            add(paneDot)
            add(paneDot2)
          }

  private val cameraPane =
      CameraPane(
              width = 1200,
              height = 800,
              target =
                  Pane<ComponentView>(
                      width = 1920,
                      height = 1080,
                      posX = 0,
                      posY = 0,
                      visual = ImageVisual("assets/3.jpg")),
              posX = 200,
              posY = 100,
              visual = ColorVisual.YELLOW,
              limitBounds = true)
          .apply {
            interactive = true

            onZoomed = { println("Zoomed to $it") }
          }

  val centerDot =
      Label(
          posX = cameraPane.width / 2 - 2 + cameraPane.posX,
          posY = cameraPane.height / 2 - 2 + cameraPane.posY,
          width = 4,
          height = 4,
          visual = ColorVisual.RED.copy().apply { style.borderRadius = BorderRadius.FULL })

  val panButton =
      Button(
              posX = 5,
              posY = 1020,
              width = 100,
              height = 50,
              text = "Pan By",
              visual = ColorVisual(Color(0, 255, 0)))
          .apply { onMouseClicked = { println(hexGrid.components[50]) } }

  val panZeroButton =
      Button(
              posX = 105,
              posY = 1020,
              width = 100,
              height = 50,
              text = "Pan",
              visual = ColorVisual(Color(0, 125, 0)))
          .apply { onMouseClicked = { cameraPane.pan(0, 0, smooth = false) } }

  val zoomButton =
      Button(
              posX = 205,
              posY = 1020,
              width = 100,
              height = 50,
              text = "Zoom",
              visual = ColorVisual(Color(0, 255, 0)))
          .apply { onMouseClicked = { Application.showGameScene(Application.cardLayoutScene) } }

  private val hexPointy =
      HexagonView(
          posX = 800,
          posY = 200,
          visual = ColorVisual(Color(255, 0, 0)),
          size = 50,
          orientation = HexOrientation.POINTY_TOP)
  private val hexFlat =
      HexagonView(
          posX = 900,
          posY = 200,
          visual = ColorVisual(Color(0, 255, 0)),
          size = 50,
          orientation = HexOrientation.FLAT_TOP)

  fun placeOnHexGrid(hexagon: HexagonView) {
    hexGrid[10, 0] =
        HexagonView(
                visual = ColorVisual(Color(255, 0, 0)),
                size = 50,
                orientation = HexOrientation.POINTY_TOP)
            .apply {
              onMouseClicked = {
                println("Before: ${(visual as ColorVisual).color.toHex()}")
                visual = ColorVisual.GREEN
                println("After: ${(visual as ColorVisual).color.toHex()}")
              }
            }
    targetPane.width = hexGrid.width
    targetPane.height = hexGrid.height
  }

  fun placeInSatchel(hexagon: HexagonView) {
    hexGrid.remove(hexagon)
    satchel.add(hexagon)
  }

  val hexMap = BidirectionalMap<Pair<Int, Int>, HexagonView>()

  fun buildHexGrid() {
    for (q in -1..1) {
      for (r in -1..1) {
        if (q + r >= -1 && q + r <= 1) {

          val hexagon =
              HexagonView(visual = ColorVisual.LIGHT_GRAY, size = 40).apply {
                onMouseClicked = {
                  visual = ColorVisual.CYAN
                  size = 100.0
                }
              }
          hexGrid[q, r] = hexagon
        }
      }
    }

    targetPane.width = hexGrid.width
    targetPane.height = hexGrid.height
  }

  fun refreshHexGrid() {
    buildHexGrid()
  }

  init {
    buildHexGrid()

    targetPane.add(singleHex)
    addComponents(targetPane, panButton, panZeroButton, zoomButton, centerDot)
  }
}
