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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "TooManyFunctions")

package tools.aqua.bgw.components.layoutviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.DEFAULT_GRID_SPACING
import tools.aqua.bgw.util.ComponentViewGrid
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.GridIteratorElement
import tools.aqua.bgw.visual.Visual

/**
 * Defines a [LayoutView] that orders components in a grid structure.
 *
 * @constructor Creates a [GridPane] with given [rows] and [columns].
 *
 * @param posX Horizontal coordinate for this [GridPane]. Default: 0.
 * @param posY Vertical coordinate for this [GridPane]. Default: 0.
 * @param columns Initial column count.
 * @param rows Initial row count.
 * @param spacing Spacing between rows and columns. Default: [DEFAULT_GRID_SPACING].
 * @param layoutFromCenter Whether the [GridPane] should anchor in the center (`true`) or top-Left (`false`).
 * Default: `true`.
 * @param visual Initial visual for this [GridPane]. Default: [Visual.EMPTY].
 */
open class GridPane<T : ComponentView>(
	posX: Number = 0,
	posY: Number = 0,
	columns: Int,
	rows: Int,
	spacing: Number = DEFAULT_GRID_SPACING,
	layoutFromCenter: Boolean = true,
	visual: Visual = Visual.EMPTY
) : LayoutView<T>(height = 0, width = 0, posX = posX, posY = posY, visual = visual),
	Iterable<GridIteratorElement<T>> {
	
	internal var updateGui : (() -> Unit)? = null
	
	internal val grid: ComponentViewGrid<T> = ComponentViewGrid(rows = rows, columns = columns)
	internal var renderedRowHeights = DoubleArray(rows) { 0.0 }
	internal var renderedColWidths = DoubleArray(columns) { 0.0 }
	
	/**
	 * Current column count.
	 */
	val columns: Int
		get() = grid.columns
	
	/**
	 * Current row count.
	 */
	val rows: Int
		get() = grid.rows

	/**
	 * Current spacing.
	 */
	var spacing: Double = spacing.toDouble()
		set(value) {
			require(value >= 0) { "Spacing has to be positive or zero" }
			
			field = value
			updateGui?.invoke()
		}
	
	init {
		this.layoutFromCenter = layoutFromCenter
	}
	
	/**
	 * Returns [ComponentView] in specified cell. Returns `null` if there was no component.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 */
	operator fun get(columnIndex: Int, rowIndex: Int): T? =
		grid[columnIndex, rowIndex]
	
	/**
	 * Sets content of desired grid cell.
	 * Overrides existing component in this cell.
	 * Pass `null` to remove a component.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 * @param component [ComponentView] to be added to the specified cell.
	 */
	operator fun set(columnIndex: Int, rowIndex: Int, component: T?) {
		grid[columnIndex, rowIndex]?.apply {
			this.widthProperty.internalListener = null
			this.heightProperty.internalListener = null
			this.posXProperty.internalListener = null
			this.posYProperty.internalListener = null
			this.parent = null
		}
		
		grid[columnIndex, rowIndex] = component?.apply {
			this.widthProperty.internalListener = { _, _ -> updateGui?.invoke() }
			this.heightProperty.internalListener = { _, _ -> updateGui?.invoke() }
			this.posXProperty.internalListener = { _, _ -> posXProperty.setSilent(0.0) /*updateGui?.invoke()*/ }
			this.posYProperty.internalListener = { _, _ -> posYProperty.setSilent(0.0) /*updateGui?.invoke()*/ }
			this.parent = this@GridPane
		}
		
		//TODO: Remove hard reset of position
		component?.posXProperty?.setSilent(0.0)
		component?.posYProperty?.setSilent(0.0)
		updateGui?.invoke()
	}
	
	/**
	 * Returns centering mode as an [Alignment] of the specified cell.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 */
	fun getCellCenterMode(columnIndex: Int, rowIndex: Int): Alignment =
		grid.getCellCenterMode(columnIndex = columnIndex, rowIndex = rowIndex)
	
	/**
	 * Sets centering mode of desired grid cell with given [Alignment].
	 * Overrides existing mode in this cell.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 * @param value New centering mode to be set for the specified cell.
	 */
	fun setCellCenterMode(columnIndex: Int, rowIndex: Int, value: Alignment) {
		grid.setCellCenterMode(columnIndex = columnIndex, rowIndex = rowIndex, alignment = value)
		updateGui?.invoke()
	}
	
	/**
	 * Sets centering mode of desired column in grid with given [Alignment].
	 * Overrides existing mode in the whole column.
	 *
	 * @param columnIndex Column index in grid.
	 * @param value New centering mode to be set for the whole column.
	 */
	fun setColumnCenterMode(columnIndex: Int, value: Alignment) {
		grid.setColumnCenterMode(columnIndex = columnIndex, alignment = value)
		updateGui?.invoke()
	}
	
	/**
	 * Sets centering mode of desired row in grid with given [Alignment].
	 * Overrides existing mode in the whole row.
	 *
	 * @param rowIndex Row index in grid.
	 * @param value New centering mode to be set for the whole row.
	 */
	fun setRowCenterMode(rowIndex: Int, value: Alignment) {
		grid.setRowCenterMode(rowIndex = rowIndex, alignment = value)
		updateGui?.invoke()
	}
	
	/**
	 * Sets centering mode of all cells in the grid with given [Alignment].
	 * Overrides existing modes of all cells.
	 *
	 * @param value New centering mode to be set for all cells.
	 */
	fun setCenterMode(value: Alignment) {
		grid.setCenterMode(alignment = value)
		updateGui?.invoke()
	}
	
	/**
	 * Returns the set column width for the given column.
	 *
	 * @param columnIndex Target column.
	 *
	 * @see setColumnWidth
	 * @see setColumnWidths
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun getColumnWidth(columnIndex: Int): Double = grid.getColumnWidth(columnIndex)
	
	/**
	 * Manually set column width of one column.
	 * Overrides automatic resizing based on content from this column.
	 *
	 * @param columnIndex Target column.
	 * @param columnWidth New column width. Use [COLUMN_WIDTH_AUTO] to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException If value was negative.
	 *
	 * @see setColumnWidths
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setColumnWidth(columnIndex: Int, columnWidth: Number) {
		grid.setColumnWidth(columnIndex = columnIndex, columnWidth = columnWidth.toDouble())
		updateGui?.invoke()
	}
	
	/**
	 * Manually set column width of all columns.
	 * Overrides automatic resizing based on content from this column.
	 *
	 * @param columnWidths New column widths. Array index 0 get applied for the first column etc.
	 * Use [COLUMN_WIDTH_AUTO] to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException If Array size does not match column count or values were negative.
	 *
	 * @see setColumnWidth
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setColumnWidths(columnWidths: DoubleArray) {
		grid.setColumnWidths(columnWidths = columnWidths)
		updateGui?.invoke()
	}
	
	/**
	 * Restores automatic resizing behavior for desired column.
	 *
	 * @param columnIndex Target column.
	 *
	 * @see setColumnWidth
	 * @see setColumnWidths
	 * @see setAutoColumnWidths
	 */
	fun setAutoColumnWidth(columnIndex: Int) {
		grid.setColumnWidth(columnIndex = columnIndex, columnWidth = COLUMN_WIDTH_AUTO)
		updateGui?.invoke()
	}
	
	/**
	 * Restores automatic resizing behavior for all columns.
	 *
	 * @see setColumnWidth
	 * @see setColumnWidths
	 * @see setAutoColumnWidth
	 */
	fun setAutoColumnWidths() {
		grid.setColumnWidths(columnWidths = DoubleArray(columns) { COLUMN_WIDTH_AUTO })
		updateGui?.invoke()
	}
	
	/**
	 * Returns the set row height for the given row.
	 *
	 * @param rowIndex Target row.
	 *
	 * @see setRowHeight
	 * @see setRowHeights
	 * @see setAutoRowHeight
	 * @see setAutoRowHeights
	 */
	fun getRowHeight(rowIndex: Int): Double = grid.getRowHeight(rowIndex)
	
	/**
	 * Manually set row height of one row.
	 * Overrides automatic resizing based on content from this row.
	 *
	 * @param rowIndex Target row.
	 * @param rowHeight New row height. Use [ROW_HEIGHT_AUTO] to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException If value was negative.
	 *
	 * @see setRowHeights
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setRowHeight(rowIndex: Int, rowHeight: Number) {
		grid.setRowHeight(rowIndex = rowIndex, rowHeight = rowHeight.toDouble())
		updateGui?.invoke()
	}
	
	/**
	 * Manually set row height of all rows.
	 * Overrides automatic resizing based on content from this row.
	 *
	 * @param rowHeights New row heights. Array index 0 get applied for the first row etc.
	 * Use [ROW_HEIGHT_AUTO] to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException If Array size does not match row count or values were negative.
	 *
	 * @see setRowHeight
	 * @see setAutoRowHeight
	 * @see setAutoRowHeights
	 */
	fun setRowHeights(rowHeights: DoubleArray) {
		grid.setRowHeights(rowHeights = rowHeights)
		updateGui?.invoke()
	}
	
	/**
	 * Restores automatic resizing behavior for desired row.
	 *
	 * @param rowIndex Target row.
	 *
	 * @see setRowHeight
	 * @see setRowHeights
	 * @see setAutoColumnWidths
	 */
	fun setAutoRowHeight(rowIndex: Int) {
		grid.setRowHeight(rowIndex = rowIndex, rowHeight = ROW_HEIGHT_AUTO)
		updateGui?.invoke()
	}
	
	/**
	 * Restores automatic resizing behavior for all rows.
	 *
	 * @see setRowHeight
	 * @see setRowHeights
	 * @see setAutoRowHeight
	 */
	fun setAutoRowHeights() {
		grid.setRowHeights(rowHeights = DoubleArray(rows) { ROW_HEIGHT_AUTO })
		updateGui?.invoke()
	}
	
	/**
	 * Grows grid by specified dimensions, filling new cells with `null` values.
	 *
	 * New rows and columns get automatic resizing behaviour, specified as [COLUMN_WIDTH_AUTO] and [ROW_HEIGHT_AUTO].
	 * Therefore, new empty rows and columns get rendered with height and width 0.0
	 * e.g. invisible if not specified otherwise.
	 *
	 * @param left Column count to be added to the left.
	 * @param right Column count to be added to the right.
	 * @param top Row count to be added on the top.
	 * @param bottom Row count to be added on the bottom.
	 *
	 * @return `true` if the grid has changed by this operation, `false` otherwise.
	 *
	 * @throws IllegalArgumentException If any value passed was negative.
	 *
	 * @see trim
	 * @see removeEmptyColumns
	 * @see removeEmptyRows
	 *
	 * @see addColumns
	 * @see removeColumn
	 * @see addRows
	 * @see removeRow
	 */
	fun grow(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0): Boolean {
		val result = grid.grow(left = left, right = right, top = top, bottom = bottom)
		
		if (result)
			updateGui?.invoke()
		
		return result
	}
	
	/**
	 * Removes all empty outer rows and columns
	 * e.g. all rows and columns counted from left, right, top and bottom that have no views in their cells.
	 *
	 * That means after calling the first and last row
	 * as well as the first and last column have at least one component in their cells.
	 *
	 * Attributes [rows] and [columns] get updated according to new dimensions.
	 *
	 * If the grid was empty the grid gets trimmed to size 0x0.
	 *
	 * @return `true` if the grid has changed by this operation, `false` otherwise.
	 *
	 * @see grow
	 * @see removeEmptyColumns
	 * @see removeEmptyRows
	 *
	 * @see addColumns
	 * @see removeColumn
	 * @see addRows
	 * @see removeRow
	 */
	fun trim(): Boolean {
		val result = grid.trim()
		
		if (result)
			updateGui?.invoke()
		
		return result
	}
	
	/**
	 * Adds the desired amount of columns between column <b>columnIndex - 1</b> and <b>columnIndex</b>.
	 * New columns get `null`-initialized.
	 *
	 * @param columnIndex Index on which the new column should be added
	 * @param count Column count to be added. Default: 1
	 *
	 * @see addRows
	 * @see removeColumn
	 */
	fun addColumns(columnIndex: Int, count: Int = 1) {
		grid.addColumns(columnIndex = columnIndex, count = count)
		updateGui?.invoke()
	}
	
	/**
	 * Removes the desired column in the grid. Removes all, views in the column.
	 * If there is no column left in the grid, it gets trimmed to size 0x0.
	 *
	 * @param columnIndex Index of the column to be deleted.
	 *
	 * @see addColumns
	 * @see removeEmptyColumns
	 */
	fun removeColumn(columnIndex: Int) {
		grid.removeColumn(columnIndex = columnIndex)
		updateGui?.invoke()
	}
	
	/**
	 * Removes all empty columns e.g. all rows that have no views in their cells.
	 *
	 * That means after calling every column has at least one component in its cells.
	 *
	 * Attribute [columns] get updated according to new dimension.
	 *
	 * If the grid was empty the grid gets trimmed to size 0x0.
	 *
	 * @see addRows
	 * @see removeRow
	 */
	fun removeEmptyColumns() {
		grid.removeEmptyColumns()
		updateGui?.invoke()
	}
	
	/**
	 * Adds the desired amount of rows between row <b>rowIndex</b> and <b>rowIndex + 1</b>.
	 * New rows get `null`-initialized.
	 *
	 * @param rowIndex Index after which the new row should be added
	 * @param count Count of rows to be added. Default: 1
	 *
	 * @see addColumns
	 * @see removeRow
	 */
	fun addRows(rowIndex: Int, count: Int = 1) {
		grid.addRows(rowIndex = rowIndex, count = count)
		updateGui?.invoke()
	}
	
	/**
	 * Removes the desired row in the grid. Removes all, views in the row.
	 *
	 * If there is no row left in the grid, it gets trimmed to size 0x0.
	 *
	 * @param rowIndex Index of the row te be deleted.
	 *
	 * @see addRows
	 * @see removeEmptyRows
	 */
	fun removeRow(rowIndex: Int) {
		grid.removeRow(rowIndex = rowIndex)
		updateGui?.invoke()
	}
	
	/**
	 * Removes all empty rows e.g. all rows that have no views in their cells.
	 *
	 * That means after calling every row has at least one component in its cells.
	 *
	 * Attribute [rows] gets updated according to new dimension.
	 *
	 * If the grid was empty the grid gets trimmed to size 0x0.
	 *
	 * @see addRows
	 * @see removeRow
	 */
	fun removeEmptyRows() {
		grid.removeEmptyRows()
		updateGui?.invoke()
	}
	
	/**
	 * Removes component from grid.
	 *
	 * @param component Child to be removed.
	 */
	override fun removeChild(component: ComponentView) {
		val componentTriple = grid.find { it.component == component }
		
		if (componentTriple != null) {
			this[componentTriple.columnIndex, componentTriple.rowIndex] = null
			component.parent = null
		}
		updateGui?.invoke()
	}
	
	/**
	 * Function returning a contained child's coordinates within this [GridPane] relative to the top left corner.
	 *
	 * @param child Child to find.
	 *
	 * @return coordinate of given child in this [GridPane].
	 */
	override fun getChildPosition(child: ComponentView): Coordinate? =
		grid.filter { it.component == child }.map {
			getRelativeChildOffset(it)
		}.firstOrNull()
	
	/**
	 * Function returning a contained child's coordinates within this [GridPane] relative to the top left corner with
	 * scale.
	 *
	 * @param child Child to find.
	 *
	 * @return coordinate of given child in this [GridPane].
	 */
	override fun getActualChildPosition(child: ComponentView): Coordinate? =
		grid.filter { it.component == child }.map {
			val offset = getRelativeChildOffset(it)
			Coordinate(
				xCoord = offset.xCoord * scaleX,
				yCoord = offset.yCoord * scaleY
			)
		}.firstOrNull()
	
	private fun getRelativeChildOffset(it : GridIteratorElement<T>): Coordinate {
		val cols = renderedColWidths.toMutableList().subList(0, it.columnIndex)
		val rows = renderedRowHeights.toMutableList().subList(0, it.rowIndex)
		
		return Coordinate(
			xCoord = cols.sum() + cols.size * spacing,
			yCoord = rows.sum() + rows.size * spacing
		)
	}
	
	/**
	 * Returns an [Iterator] over the grid components.
	 *
	 * Iteration is columns-first which means that the iterator starts at cell [0,0] and then proceeds iterating through
	 * the first row from left to right. When reaching the end of a row it proceeds to the next one resetting the column
	 * pointer to 0.
	 *
	 * [Iterator.hasNext] Returns `true` if at least one cell is left to iterate.
	 *
	 * [Iterator.next] Returns a data object [GridIteratorElement] that contains the current row and column as well as
	 * the component itself. Refer to the [GridIteratorElement] documentation for further information.
	 *
	 * @return Iterator over the grid components.
	 */
	override fun iterator(): Iterator<GridIteratorElement<T>> = grid.iterator()
	
	companion object {
		/**
		 * Constant for automatic column width.
		 */
		const val COLUMN_WIDTH_AUTO: Double = -1.0
		
		/**
		 * Constant for automatic row height.
		 */
		const val ROW_HEIGHT_AUTO: Double = -1.0
	}
}