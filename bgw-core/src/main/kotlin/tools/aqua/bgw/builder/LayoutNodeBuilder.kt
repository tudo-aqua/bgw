/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.aqua.bgw.builder

import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.ElementPane
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.layoutviews.LayoutElement
import tools.aqua.bgw.util.ElementViewGrid

/**
 * LayoutNodeBuilder.
 * Factory for all BGW layout elements.
 */
internal class LayoutNodeBuilder {
	companion object {
		/**
		 * Switches between LayoutElements.
		 */
		internal fun buildLayoutElement(
			scene: Scene<out ElementView>,
			layoutElementView: LayoutElement<out ElementView>
		): Region =
			when (layoutElementView) {
				is GridLayoutView<*> ->
					buildGrid(scene, layoutElementView)
				is ElementPane<*> ->
					buildElementPane(scene, layoutElementView)
			}

		private fun buildElementPane(scene: Scene<out ElementView>, elementPane: ElementPane<out ElementView>): Region =
			Pane().apply {
				elementPane.observableElements.setGUIListenerAndInvoke {
					refreshElementPane(scene, elementPane)
				}
			}

		private fun Pane.refreshElementPane(scene: Scene<out ElementView>, elementPane: ElementPane<out ElementView>) {
			children.clear()
			children.addAll(elementPane.observableElements.map { NodeBuilder.build(scene, it) })
		}

		/**
		 * Builds [GridLayoutView].
		 */
		private fun buildGrid(scene: Scene<out ElementView>, gridView: GridLayoutView<out ElementView>): Region =
			Pane().apply {
				gridView.setGUIListenerAndInvoke { refreshGrid(scene, gridView) }
			}
		
		/**
		 * Refreshes [GridLayoutView].
		 */
		private fun Pane.refreshGrid(scene: Scene<out ElementView>, gridView: GridLayoutView<out ElementView>) {
			val grid: ElementViewGrid<out ElementView> = gridView.grid
			
			children.clear()
			
			//Build Nodes
			val nodes = ArrayList<Triple<Pair<Int, Int>, ElementView, Node>>()
			grid.getColumns().forEachIndexed { colIndex, col ->
				for (rowIndex in col.indices) {
					val gameElementView = col[rowIndex] ?: continue
					
					nodes.add(
						Triple(
							Pair(colIndex, rowIndex),
							gameElementView,
							NodeBuilder.build(scene = scene, elementView = gameElementView)
						)
					)
				}
			}
			
			gridView.renderedRowHeights = DoubleArray(grid.rows) {
				grid.getRow(it).maxOf { entry -> entry?.let { t -> t.height + t.posY } ?: 0.0 }
			}
			gridView.renderedColWidths = DoubleArray(grid.columns) {
				grid.getColumn(it).maxOf { entry -> entry?.let { t -> t.width + t.posY } ?: 0.0 }
			}
			
			//TODO: Disable auto resizing if desired
			gridView.width = gridView.renderedColWidths.sum() + (gridView.renderedColWidths.size - 1) * gridView.spacing
			gridView.height =
				gridView.renderedRowHeights.sum() + (gridView.renderedRowHeights.size - 1) * gridView.spacing
			
			nodes.forEach { triple ->
				val colIndex = triple.first.first
				val rowIndex = triple.first.second
				val element = triple.second
				val node = triple.third
				val posX = (0 until colIndex).sumOf { gridView.renderedColWidths[it] } + colIndex * gridView.spacing
				val posY = (0 until rowIndex).sumOf { gridView.renderedRowHeights[it] } + rowIndex * gridView.spacing
				
				children.add(node.apply {
					layoutX = posX + element.posX
					layoutY = posY + element.posY
				})
			}
		}
	}
}