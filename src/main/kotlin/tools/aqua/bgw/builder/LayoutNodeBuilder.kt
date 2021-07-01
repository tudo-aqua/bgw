package tools.aqua.bgw.builder

import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.util.ElementViewGrid

/**
 * LayoutNodeBuilder.
 * Factory for all BGW layout elements.
 */
internal class LayoutNodeBuilder {
	companion object {
		//region grid
		internal fun buildGrid(scene: Scene<out ElementView>, gridView: GridLayoutView<out ElementView>): Region =
			Pane().apply {
				gridView.setGUIListenerAndInvoke { refreshGrid(scene, gridView) }
			}
		
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
		//endregion
	}
}