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

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual

fun main() {
  HexgridExample()
}

class HexgridExample : BoardGameApplication("Hexagon Grid example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  val offsetHexagonGrid =
      HexagonGrid<HexagonView>(
          posX = 0, posY = 0, coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET)

  val axialHexagonGrid =
      HexagonGrid<HexagonView>(
          posX = 0, posY = 0, coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL)

  val offsetPane =
      Pane<ComponentView>(posX = 0, posY = 0, width = 1920 / 2, height = 1080).apply {
        add(offsetHexagonGrid)
      }

  val offsetCameraPane =
      CameraPane(
              posX = 0,
              posY = 0,
              width = 1920 / 2,
              height = 1080,
              target = offsetPane,
              limitBounds = false)
          .apply { interactive = true }

  val axialPane =
      Pane<ComponentView>(posX = 0, posY = 0, width = 1920 / 2, height = 1080).apply {
        add(axialHexagonGrid)
      }

  val axialCameraPane =
      CameraPane(
              posX = 1920 / 2,
              posY = 0,
              width = 1920 / 2,
              height = 1080,
              target = axialPane,
              limitBounds = false)
          .apply { interactive = true }

  init {
    /* Create a 4x5 grid of hexagons */
    for (x in 0..3) {
      for (y in 0..4) {
        val hexagon =
            HexagonView(
                visual =
                    CompoundVisual(
                        ColorVisual(Color(0xfa6c56)),
                        TextVisual(text = "$x, $y", font = Font(10.0, Color(0x0f141f)))),
                size = 30)

        offsetHexagonGrid[x, y] = hexagon
      }
    }

    /* Create three rings of hexagons */
    for (row in -2..2) {
      for (col in -2..2) {
        /* Only add hexagons that would fit in a circle */
        if (row + col in -2..2) {
          val hexagon =
              HexagonView(
                  visual =
                      CompoundVisual(
                          ColorVisual(Color(0xc6ff6e)),
                          TextVisual(text = "$col, $row", font = Font(10.0, Color(0x0f141f)))),
                  size = 30)

          axialHexagonGrid[col, row] = hexagon
        }
      }
    }

    offsetPane.width = offsetHexagonGrid.actualWidth
    offsetPane.height = offsetHexagonGrid.actualHeight

    axialPane.width = axialHexagonGrid.actualWidth
    axialPane.height = axialHexagonGrid.actualHeight

    gameScene.addComponents(offsetCameraPane, axialCameraPane)
    showGameScene(gameScene)
    show()
  }
}
