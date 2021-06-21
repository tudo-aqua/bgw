@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.IObservable
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.ElementViewGrid

/**
 * Defines an GameElementContainerView that orders elements in a grid structure.
 *
 * @param rows Initial row count.
 * @param columns Initial column count.
 * @param spacing Spacing between rows and columns. Default: 0.0
 */
open class GridLayoutView<T : ElementView>(
	rows: Int,
	columns: Int,
	spacing: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	layoutFromCenter: Boolean = true
) : LayoutElement<T>(posX = posX, posY = posY), IObservable {
	
	internal val grid: ElementViewGrid<T> = ElementViewGrid(rows = rows, columns = columns)
	internal var renderedRowHeights = DoubleArray(rows) { 0.0 }
	internal var renderedColWidths = DoubleArray(columns) { 0.0 }
	
	/**
	 * Current row count.
	 */
	var rows = rows
		private set
	
	/**
	 * Current column count.
	 */
	var columns = columns
		private set
	
	/**
	 * Current spacing.
	 */
	var spacing: Double = spacing.toDouble()
		set(value) {
			require(value >= 0) { "Spacing has to be positive or zero" }
			
			field = value
			notifyChange()
		}
	
	init {
		this.layoutFromCenter = layoutFromCenter
	}
	
	/**
	 * Notifies UI to update view component.
	 */
	override fun update() {
		notifyChange()
	}
	
	/**
	 * Returns ElementView in specified cell. Returns NULL if there was no element.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 */
	operator fun get(columnIndex: Int, rowIndex: Int) =
		grid[columnIndex, rowIndex]
	
	/**
	 * Sets content of desired grid cell.
	 * Overrides existing element in this cell.
	 * Pass null to remove an element.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 * @param element ElementView to be added to the specified cell.
	 */
	operator fun set(columnIndex: Int, rowIndex: Int, element: T?) {
		grid[columnIndex, rowIndex]?.internalListener = null
		grid[columnIndex, rowIndex] = element.also { it?.internalListener = {} } //FIXME why is this here?
		notifyChange()
	}
	
	/**
	 * Returns centering mode of the specified cell.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 */
	fun getCellCenterMode(columnIndex: Int, rowIndex: Int): Alignment =
		grid.getCellCenterMode(columnIndex = columnIndex, rowIndex = rowIndex)
	
	/**
	 * Sets centering mode of desired grid cell.
	 * Overrides existing mode in this cell.
	 *
	 * @param columnIndex Column index in grid.
	 * @param rowIndex Row index in grid.
	 * @param value New Centering mode to be set for the specified cell.
	 */
	fun setCellCenterMode(columnIndex: Int, rowIndex: Int, value: Alignment) {
		grid.setCellCenterMode(columnIndex = columnIndex, rowIndex = rowIndex, value = value)
		notifyChange()
	}
	
	/**
	 * Sets centering mode of desired column in grid.
	 * Overrides existing mode in the whole column.
	 *
	 * @param columnIndex Column index in grid.
	 * @param value New Centering mode to be set for the whole column.
	 */
	fun setCellCenterMode(columnIndex: Int, value: Alignment) {
		grid.setColumnCenterMode(columnIndex = columnIndex, value = value)
		notifyChange()
	}
	
	/**
	 * Sets centering mode of desired row in grid.
	 * Overrides existing mode in the whole row.
	 *
	 * @param rowIndex Row index in grid.
	 * @param value New Centering mode to be set for the whole row.
	 */
	fun setRowCenterMode(rowIndex: Int, value: Alignment) {
		grid.setRowCenterMode(rowIndex = rowIndex, value = value)
		notifyChange()
	}
	
	/**
	 * Sets centering mode of all cells in the grid.
	 * Overrides existing modes of all cells.
	 *
	 * @param value New Centering mode to be set for all cells.
	 */
	fun setCenterMode(value: Alignment) {
		grid.setCenterMode(value = value)
		notifyChange()
	}
	
	/**
	 * Manually set column width of one column.
	 * Overrides automatic resizing based on content from this column.
	 *
	 * @param columnIndex Target column.
	 * @param columnWidth New column width. Use COLUMN_WIDTH_AUTO to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException Thrown if value was negative.
	 *
	 * @see setColumnWidths
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setColumnWidth(columnIndex: Int, columnWidth: Double) {
		grid.setColumnWidth(columnIndex = columnIndex, columnWidth = columnWidth)
		notifyChange()
	}
	
	/**
	 * Manually set column width of all columns.
	 * Overrides automatic resizing based on content from this column.
	 *
	 * @param columnWidths New column widths. Array index 0 get applied for the first column etc.
	 * Use COLUMN_WIDTH_AUTO to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException Thrown if Array size does not match column count or values were negative.
	 *
	 * @see setColumnWidth
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setColumnWidths(columnWidths: DoubleArray) {
		grid.setColumnWidths(columnWidths = columnWidths)
		notifyChange()
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
		notifyChange()
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
		notifyChange()
	}
	
	/**
	 * Manually set row height of one row.
	 * Overrides automatic resizing based on content from this row.
	 *
	 * @param rowIndex Target row.
	 * @param rowHeight New row height. Use ROW_HEIGHT_AUTO to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException Thrown if value was negative.
	 *
	 * @see setRowHeights
	 * @see setAutoColumnWidth
	 * @see setAutoColumnWidths
	 */
	fun setRowHeight(rowIndex: Int, rowHeight: Double) {
		grid.setRowHeight(rowIndex = rowIndex, rowHeight = rowHeight)
		notifyChange()
	}
	
	/**
	 * Manually set row height of all rows.
	 * Overrides automatic resizing based on content from this row.
	 *
	 * @param rowHeights New row heights. Array index 0 get applied for the first row etc.
	 * Use ROW_HEIGHT_AUTO to restore automatic resizing behaviour.
	 *
	 * @throws IllegalArgumentException Thrown if Array size does not match row count or values were negative.
	 *
	 * @see setRowHeight
	 * @see setAutoRowHeight
	 * @see setAutoRowHeights
	 */
	fun setRowHeights(rowHeights: DoubleArray) {
		grid.setRowHeights(rowHeights = rowHeights)
		notifyChange()
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
		notifyChange()
	}
	
	/**
	 * Restores automatic resizing behavior for all rows.
	 *
	 * @see setRowHeight
	 * @see setRowHeights
	 * @see setAutoRowHeight
	 */
	fun setAutoRowHeights() {
		grid.setColumnWidths(columnWidths = DoubleArray(columns) { COLUMN_WIDTH_AUTO })
		notifyChange()
	}
	
	/**
	 * Grows grid by specified dimensions, filling new cells with NULL values.
	 * New rows and columns get automatic resizing behaviour, specified as COLUMN_WIDTH_AUTO and ROW_HEIGHT_AUTO.
	 * Therefore new empty rows and columns get rendered with height and width 0.0
	 * e.g. invisible if not specified otherwise.
	 *
	 * @param left column count to be added to the left.
	 * @param right column count to be added to the right.
	 * @param top row count to be added on the top.
	 * @param bottom row count to be added on the bottom.
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
	fun grow(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
		grid.grow(left = left, right = right, top = top, bottom = bottom)
		columns = grid.columns
		rows = grid.rows
	}
	
	/**
	 * Removes all empty outer rows and columns
	 * e.g. all rows and columns counted from left, right, top and bottom that have no views in their cells.
	 * That means after calling the first and last row
	 * as well as the first and last column have at least one element in their cells.
	 * Attributes "rows" and "columns" get updated according to new dimensions.
	 * If the grid was empty the grid gets trimmed to size 0x0.
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
	fun trim() {
		grid.trim()
		columns = grid.columns
		rows = grid.rows
	}
	
	/**
	 * Adds the desired amount of columns between column <b>columnIndex</b> and <b>columnIndex + 1</b>.
	 * New columns get NULL-initialized.
	 *
	 * @param columnIndex index after which the new column should be added
	 * @param count Column count to be added. Default: 1
	 *
	 * @see addRows
	 * @see removeColumn
	 */
	fun addColumns(columnIndex: Int, count: Int = 1) {
		grid.addColumns(columnIndex = columnIndex, count = count)
		notifyChange()
	}
	
	/**
	 * Removes the desired column in the grid. Removes all, views in the column.
	 * If there is no column left in the grid, it gets trimmed to size 0x0.
	 *
	 * @param columnIndex Index of the column te be deleted.
	 *
	 * @see addColumns
	 * @see removeEmptyColumns
	 */
	fun removeColumn(columnIndex: Int) {
		grid.removeColumn(columnIndex = columnIndex)
		notifyChange()
	}
	
	/**
	 * Removes all empty columns e.g. all rows that have no views in their cells.
	 * That means after calling every row has at least one element in its cell.
	 * Attribute "rows" get updated according to new dimension.
	 * If the grid was empty the grid gets trimmed to size 0x0.
	 *
	 * @see addRows
	 * @see removeRow
	 */
	fun removeEmptyColumns() {
		grid.removeEmptyColumns()
		notifyChange()
	}
	
	/**
	 * Adds the desired amount of rows between row <b>rowIndex</b> and <b>rowIndex + 1</b>.
	 * New rows get NULL-initialized.
	 *
	 * @param rowIndex index after which the new row should be added
	 * @param count Row count to be added. Default: 1
	 *
	 * @see addColumns
	 * @see removeRow
	 */
	fun addRows(rowIndex: Int, count: Int = 1) {
		grid.addRows(rowIndex = rowIndex, count = count)
		notifyChange()
	}
	
	/**
	 * Removes the desired row in the grid. Removes all, views in the row.
	 * If there is no row left in the grid, it gets trimmed to size 0x0.
	 *
	 * @param rowIndex Index of the row te be deleted.
	 *
	 * @see addRows
	 * @see removeEmptyRows
	 */
	fun removeRow(rowIndex: Int) {
		grid.removeRow(rowIndex = rowIndex)
		notifyChange()
	}
	
	/**
	 * Removes all empty rows e.g. all rows that have no views in their cells.
	 * That means after calling every row has at least one element in its cell.
	 * Attribute "rows" get updated according to new dimension.
	 * If the grid was empty the grid gets trimmed to size 0x0.
	 *
	 * @see addRows
	 * @see removeRow
	 */
	fun removeEmptyRows() {
		grid.removeEmptyRows()
		notifyChange()
	}
	
	/**
	 * {@inheritDoc}.
	 */
	override fun removeChild(child: ElementView) {
		val elementTriple = grid.find { it.third == child }
		
		if (elementTriple != null) {
			this[elementTriple.first, elementTriple.second] = null
			child.parent = null
		}
	}
	
	/**
	 * {@inheritDoc}.
	 */
	override fun getChildPosition(child: ElementView): Coordinate? =
		grid.filter { it.third == child }.map {
			val cols = renderedColWidths.toMutableList().subList(0, it.first)
			val offsetX = cols.sum() + cols.size * spacing
			
			val rows = renderedRowHeights.toMutableList().subList(0, it.second)
			val offsetY = rows.sum() + rows.size * spacing
			
			Coordinate(offsetX + child.posX, offsetY + child.posY)
		}.firstOrNull()
	
	companion object {
		/**
		 * Constant for auto column width.
		 */
		const val COLUMN_WIDTH_AUTO = -1.0
		
		/**
		 * Constant for auto row height.
		 */
		const val ROW_HEIGHT_AUTO = -1.0
	}
}