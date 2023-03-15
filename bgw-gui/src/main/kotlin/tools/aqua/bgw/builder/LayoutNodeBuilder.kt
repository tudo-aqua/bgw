/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import javafx.scene.Node
import javafx.scene.layout.Pane as FXPane
import javafx.scene.layout.Region
import tools.aqua.bgw.builder.NodeBuilder.buildChildren
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.GridPane.Companion.COLUMN_WIDTH_AUTO
import tools.aqua.bgw.components.layoutviews.GridPane.Companion.ROW_HEIGHT_AUTO
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.util.ComponentViewGrid

/** LayoutNodeBuilder. Factory for all BGW layout components. */
object LayoutNodeBuilder {
  /** Switches between LayoutComponents. */
  internal fun buildLayoutView(
      scene: Scene<out ComponentView>,
      layoutViewView: LayoutView<out ComponentView>
  ): Region =
      when (layoutViewView) {
        is GridPane<*> -> buildGrid(scene, layoutViewView)
        is Pane<*> -> buildPane(scene, layoutViewView)
      }

  private fun buildPane(scene: Scene<out ComponentView>, pane: Pane<out ComponentView>): Region =
      FXPane().apply {
        pane.observableComponents.setGUIListenerAndInvoke(emptyList()) { oldValue, _ ->
          buildChildren(scene, pane.observableComponents, oldValue.toSet())
        }
      }

  /** Builds [GridPane]. */
  private fun buildGrid(
      scene: Scene<out ComponentView>,
      gridView: GridPane<out ComponentView>
  ): Region =
      FXPane().apply { gridView.updateGui = { refreshGrid(scene, gridView) }.also { it.invoke() } }

  /** Refreshes [GridPane]. */
  private fun FXPane.refreshGrid(
      scene: Scene<out ComponentView>,
      gridView: GridPane<out ComponentView>
  ) {
    val grid: ComponentViewGrid<out ComponentView> = gridView.grid

    children.clear()

    // Build Nodes
    val nodes = ArrayList<Triple<Pair<Int, Int>, ComponentView, Node>>()
    grid.getColumns().forEachIndexed { colIndex, col ->
      for (rowIndex in col.indices) {
        val gameComponent = col[rowIndex] ?: continue

        nodes.add(
            Triple(
                Pair(colIndex, rowIndex),
                gameComponent,
                NodeBuilder.build(scene = scene, componentView = gameComponent)))
      }
    }

    // Calculate row heights
    gridView.renderedRowHeights =
        DoubleArray(grid.rows) {
          var height = grid.getRowHeight(it)

          if (height == ROW_HEIGHT_AUTO) {
            height =
                grid.getRow(it).filterNotNull().maxOfOrNull { entry ->
                  entry.let { t -> t.layoutBounds.height + t.posY }
                }
                    ?: 0.0
          }

          height
        }

    // Calculate column widths
    gridView.renderedColWidths =
        DoubleArray(grid.columns) {
          var width = grid.getColumnWidth(it)

          if (width == COLUMN_WIDTH_AUTO) {
            width =
                grid.getColumn(it).filterNotNull().maxOfOrNull { entry ->
                  entry.let { t -> t.layoutBounds.width + t.posX }
                }
                    ?: 0.0
          }

          width
        }

    gridView.width =
        gridView.renderedColWidths.sum() + (gridView.renderedColWidths.size - 1) * gridView.spacing
    gridView.height =
        gridView.renderedRowHeights.sum() +
            (gridView.renderedRowHeights.size - 1) * gridView.spacing

    nodes.forEach { triple ->
      refreshGridNode(gridView, triple)
      triple.second.heightProperty.guiListener = { _, _ -> refreshGrid(scene, gridView) }
      triple.second.widthProperty.guiListener = { _, _ -> refreshGrid(scene, gridView) }
    }
  }

  /** Refreshes grid node. */
  private fun FXPane.refreshGridNode(
      gridView: GridPane<out ComponentView>,
      triple: Triple<Pair<Int, Int>, ComponentView, Node>
  ) {

    val colIndex = triple.first.first
    val rowIndex = triple.first.second
    val component = triple.second
    val node = triple.third
    val posX =
        (0 until colIndex).sumOf { gridView.renderedColWidths[it] } + colIndex * gridView.spacing
    val posY =
        (0 until rowIndex).sumOf { gridView.renderedRowHeights[it] } + rowIndex * gridView.spacing

    children.add(
        node.apply {
          val nodeWidth = component.layoutBounds.width
          val nodeHeight = component.layoutBounds.height

          // Calculate delta due to scale and rotation
          val deltaX = (nodeWidth - component.width) / 2
          val deltaY = (nodeHeight - component.height) / 2

          // Calculate anchor point for flush TOP_LEFT placement
          val anchorX = posX + component.posX + deltaX
          val anchorY = posY + component.posY + deltaY

          // Account for centering
          val centerMode = gridView.getCellCenterMode(columnIndex = colIndex, rowIndex = rowIndex)
          val remainingSpaceX = gridView.renderedColWidths[colIndex] - nodeWidth - component.posX
          val remainingSpaceY = gridView.renderedRowHeights[rowIndex] - nodeHeight - component.posY

          layoutX = anchorX + remainingSpaceX * centerMode.horizontalAlignment.positionMultiplier
          layoutY = anchorY + remainingSpaceY * centerMode.verticalAlignment.positionMultiplier
        })
  }
}
