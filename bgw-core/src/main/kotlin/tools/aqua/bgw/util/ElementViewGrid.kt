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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "Unchecked_Cast", "DuplicatedCode")

package tools.aqua.bgw.util

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView.Companion.COLUMN_WIDTH_AUTO
import tools.aqua.bgw.elements.layoutviews.GridLayoutView.Companion.ROW_HEIGHT_AUTO

/**
 * Internal class ElementViewGrid boxing grid structure.
 */
internal class ElementViewGrid<T : ElementView>(
	rows: Int,
	columns: Int
) : Iterable<GridIteratorElement<T>> {
	
	//region Attributes
	/**
	 * Current number of rows.
	 */
	var rows = rows
		private set
	
	/**
	 * Current number of columns.
	 */
	var columns = columns
		private set
	
	/**
	 * Grid array containing contents.
	 */
	private var grid = Array(columns) { Array<ElementView?>(rows) { null } }
	
	/**
	 * Grid array containing centering behaviors for individual cells.
	 */
	private var centeringModes = Array(columns) { Array(rows) { Alignment.CENTER } }
	
	/**
	 * Row heights.
	 */
	private var rowHeights = DoubleArray(rows) { -1.0 }
	
	/**
	 * Column widths.
	 */
	private var columnWidths = DoubleArray(columns) { -1.0 }
	//endregion
	
	//region Get/Set operators
	/**
	 * Returns grid cell content.
	 *
	 * @param columnIndex column index of cell.
	 * @param rowIndex row index of cell.
	 */
	operator fun get(columnIndex: Int, rowIndex: Int): T? = grid[columnIndex][rowIndex] as? T
	
	
	/**
	 * Sets grid cell content.
	 *
	 * @param columnIndex column index of cell.
	 * @param rowIndex row index of cell.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range.
	 */
	operator fun set(columnIndex: Int, rowIndex: Int, value: T?) {
		require(columnIndex in 0 until columns && rowIndex in 0 until rows) {
			"Indices exceed grid bounds."
		}
		
		grid[columnIndex][rowIndex] = value
	}
	//endregion
	
	//region Get/Set center modes
	/**
	 * Returns grid cell centering mode.
	 *
	 * @param columnIndex column index of cell.
	 * @param rowIndex row index of cell.
	 */
	fun getCellCenterMode(columnIndex: Int, rowIndex: Int): Alignment = centeringModes[columnIndex][rowIndex]
	
	/**
	 * Sets grid cell centering mode.
	 *
	 * @param columnIndex column index of cell.
	 * @param rowIndex row index of cell.
	 *
	 * @throws IllegalArgumentException if [columnIndex] or [rowIndex] is out of grid range.
	 */
	fun setCellCenterMode(columnIndex: Int, rowIndex: Int, alignment: Alignment) {
		require(columnIndex in 0 until columns && rowIndex in 0 until rows) {
			"Indices exceed grid bounds."
		}
		
		centeringModes[columnIndex][rowIndex] = alignment
	}
	
	/**
	 * Sets grid centering mode for whole column.
	 *
	 * @param columnIndex column index.
	 * @param alignment new alignment.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range.
	 */
	fun setColumnCenterMode(columnIndex: Int, alignment: Alignment) {
		require(columnIndex in 0 until columns) {
			"Column index exceed grid bounds."
		}
		
		for (i in 0 until rows)
			centeringModes[columnIndex][i] = alignment
	}
	
	/**
	 * Sets grid centering mode for whole row.
	 *
	 * @param rowIndex row index.
	 * @param alignment new alignment.
	 *
	 * @throws IllegalArgumentException if [rowIndex] is out of grid range.
	 */
	fun setRowCenterMode(rowIndex: Int, alignment: Alignment) {
		require(rowIndex in 0 until rows) {
			"Row index exceed grid bounds."
		}
		
		for (i in 0 until columns)
			centeringModes[i][rowIndex] = alignment
	}
	
	/**
	 * Sets grid centering mode for all cells.
	 *
	 * @param alignment new alignment.
	 */
	fun setCenterMode(alignment: Alignment) {
		for (x in 0 until columns)
			for (y in 0 until rows)
				centeringModes[x][y] = alignment
	}
	//endregion
	
	//region Get/Set values
	/**
	 * Returns whole row as [List].
	 *
	 * @param rowIndex row index.
	 */
	fun getRow(rowIndex: Int): List<T?> = List(columns) { grid[it][rowIndex] as? T }
	
	/**
	 * Returns [List] all all rows as another [List].
	 */
	fun getRows(): List<List<T?>> = (0 until rows).map { getRow(it) }
	
	/**
	 * Returns whole column as [List].
	 *
	 * @param columnIndex column index.
	 */
	fun getColumn(columnIndex: Int): List<T?> = grid[columnIndex].toList() as List<T?>
	
	/**
	 * Returns [List] all all columns as another [List].
	 */
	fun getColumns(): List<List<T?>> = (0 until columns).map { getColumn(it) }
	//endregion
	
	//region Get/Set column width/row height
	/**
	 * Returns preferred column width ([COLUMN_WIDTH_AUTO] for auto).
	 *
	 * @param columnIndex column index.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range,.
	 */
	fun getColumnWidth(columnIndex: Int): Double {
		require(columnIndex in columnWidths.indices) {
			"ColumnIndex out of grid range."
		}
		
		return columnWidths[columnIndex]
	}
	
	/**
	 * Sets preferred column width ([COLUMN_WIDTH_AUTO] for auto).
	 *
	 * @param columnIndex column index.
	 * @param columnWidth new column width.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range or [columnWidth] is negative.
	 */
	fun setColumnWidth(columnIndex: Int, columnWidth: Double) {
		require(columnIndex in columnWidths.indices) {
			"ColumnIndex out of grid range."
		}
		require(columnWidth >= 0 || columnWidth == COLUMN_WIDTH_AUTO) {
			"Parameter must be positive or COLUMN_WIDTH_AUTO."
		}
		
		columnWidths[columnIndex] = columnWidth
	}
	
	/**
	 * Sets preferred column width ([COLUMN_WIDTH_AUTO] for auto) for all columns.
	 *
	 * @param columnWidths new column widths.
	 *
	 * @throws IllegalArgumentException if size of [columnWidths] does not match grid size
	 * or any columnWidth is negative.
	 */
	fun setColumnWidths(columnWidths: DoubleArray) {
		require(columnWidths.size == this.columnWidths.size) {
			"Array size does not match grid range."
		}
		require(columnWidths.all { it >= 0 || it == COLUMN_WIDTH_AUTO }) {
			"Parameter values must all be positive or COLUMN_WIDTH_AUTO."
		}
		
		this.columnWidths = columnWidths
	}
	
	/**
	 * Returns preferred row height ([ROW_HEIGHT_AUTO] for auto).
	 *
	 * @param rowIndex row index.
	 *
	 * @throws IllegalArgumentException if [rowIndex] is out of grid range.
	 */
	fun getRowHeight(rowIndex: Int): Double {
		require(rowIndex in rowHeights.indices) {
			"ColumnIndex out of grid range."
		}
		
		return rowHeights[rowIndex]
	}
	
	/**
	 * Sets preferred row height ([ROW_HEIGHT_AUTO] for auto).
	 *
	 * @param rowIndex row index.
	 * @param rowHeight new row height.
	 *
	 * @throws IllegalArgumentException if [rowIndex] is out of grid range or [rowHeight] is negative.
	 */
	fun setRowHeight(rowIndex: Int, rowHeight: Double) {
		require(rowIndex in rowHeights.indices) {
			"ColumnIndex out of grid range."
		}
		require(rowHeight >= 0 || rowHeight == ROW_HEIGHT_AUTO) {
			"Parameter must be positive or ROW_HEIGHT_AUTO."
		}
		
		rowHeights[rowIndex] = rowHeight
	}
	
	/**
	 * Sets preferred row height ([ROW_HEIGHT_AUTO] for auto) for all rows.
	 *
	 * @param rowHeights new row heights.
	 *
	 * @throws IllegalArgumentException if size of [rowHeights] does not match grid size
	 * or any rowHeight is negative.
	 */
	fun setRowHeights(rowHeights: DoubleArray) {
		require(rowHeights.size == this.rowHeights.size) {
			"Array size does not match grid range."
		}
		require(rowHeights.all { it >= 0 || it == ROW_HEIGHT_AUTO }) {
			"Parameter values must all be positive or ROW_HEIGHT_AUTO."
		}
		
		this.rowHeights = rowHeights
	}
	//endregion
	
	//region Grow/Trim
	/**
	 * Extends grid to given directions.
	 *
	 * @param left amount of columns to add to the left.
	 * @param right amount of columns to add to the right.
	 * @param top amount of rows to add on the top.
	 * @param bottom amount of rows to add on the bottom.
	 *
	 * @throws IllegalArgumentException if any parameter is negative.
	 */
	fun grow(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0): Boolean {
		if (left == 0 && right == 0 && top == 0 && bottom == 0)
			return false
		
		require(left >= 0 && right >= 0 && top >= 0 && bottom >= 0) {
			"All parameters must be positive."
		}
		
		val newGrid = Array(columns + left + right) { Array<ElementView?>(rows + top + bottom) { null } }
		val newCenteringModes = Array(columns + left + right) { Array(rows + top + bottom) { Alignment.CENTER } }
		
		for (x in 0 until columns) {
			for (y in 0 until rows) {
				newGrid[x + left][y + top] = grid[x][y]
				newCenteringModes[x + left][y + top] = centeringModes[x][y]
			}
		}
		
		grid = newGrid
		centeringModes = newCenteringModes
		rows += top + bottom
		columns += left + right
		rowHeights = DoubleArray(left) { ROW_HEIGHT_AUTO } + rowHeights + DoubleArray(right) { ROW_HEIGHT_AUTO }
		columnWidths = DoubleArray(left) { COLUMN_WIDTH_AUTO } + columnWidths + DoubleArray(right) { COLUMN_WIDTH_AUTO }
		
		return true
	}
	
	/**
	 * Removes all empty outer rows and columns.
	 *
	 * @return `true` if the grid has been changes by this operation, `false` otherwise.
	 */
	fun trim(): Boolean = trimColumns() || trimRows()
	
	/**
	 * Removes all empty outer columns.
	 *
	 * @return `true` if the grid has been changes by this operation, `false` otherwise.
	 */
	fun trimColumns(): Boolean {
		val oldColumns = columns
		var firstColumn = -1
		var lastColumn = -1
		
		for (index in 0 until columns) {
			if (getColumn(index).any { it != null }) {
				firstColumn = index
				break
			}
		}
		
		//Check if array contained only null entries
		if (firstColumn == -1) {
			columns = 0
			rows = 0
			grid = Array(columns) { Array(rows) { null } }
			centeringModes = Array(columns) { Array(rows) { Alignment.CENTER } }
		} else {
			for (index in columns - 1 downTo firstColumn) {
				if (getColumn(index).any { it != null }) {
					lastColumn = index
					break
				}
			}
			
			columns = lastColumn - firstColumn + 1
			
			grid = Array(columns) { grid[it + firstColumn] }
			centeringModes = Array(columns) { centeringModes[it + firstColumn] }
			columnWidths = DoubleArray(columns) { columnWidths[it + firstColumn] }
		}
		
		return columns != oldColumns
	}
	
	/**
	 * Removes all empty outer rows.
	 *
	 * @return `true` if the grid has been changes by this operation, `false` otherwise.
	 */
	fun trimRows(): Boolean {
		val oldRows = rows
		var firstRow = -1
		var lastRow = -1
		
		for (index in 0 until rows) {
			if (getRow(index).any { it != null }) {
				firstRow = index
				break
			}
		}
		
		//Check if array contained only null entries
		if (firstRow == -1) {
			columns = 0
			rows = 0
			grid = Array(columns) { Array(rows) { null } }
			centeringModes = Array(columns) { Array(rows) { Alignment.CENTER } }
		} else {
			
			for (index in rows - 1 downTo firstRow) {
				if (getRow(index).any { it != null }) {
					lastRow = index
					break
				}
			}
			
			assert(lastRow > firstRow)
			
			rows = lastRow - firstRow + 1
			grid = Array(columns) { x -> Array(rows) { y -> grid[x][y + firstRow] } }
			centeringModes = Array(columns) { x -> Array(rows) { y -> centeringModes[x][y + firstRow] } }
			rowHeights = DoubleArray(rows) { rowHeights[it + firstRow] }
		}
		
		return rows != oldRows
	}
	//endregion
	
	//region Add/Remove columns and rows
	/**
	 * Inserts the given amount of columns at the given position.
	 *
	 * @param columnIndex index after which the columns should be inserted.
	 * @param count amount of columns to insert.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range or [count] is negative.
	 */
	fun addColumns(columnIndex: Int, count: Int) {
		require(columnIndex in 0..columns) {
			"Column index out of grid range."
		}
		require(count >= 0) {
			"Parameter count must be positive."
		}
		
		val newGrid = Array(columns + count) { Array<ElementView?>(rows) { null } }
		val newCenteringModes = Array(columns + count) { Array(rows) { Alignment.CENTER } }
		
		for (x in 0 until columnIndex) {
			for (y in 0 until rows) {
				newGrid[x][y] = grid[x][y]
				newCenteringModes[x][y] = centeringModes[x][y]
			}
		}
		
		for (x in columnIndex until columns) {
			for (y in 0 until rows) {
				newGrid[x + count][y] = grid[x][y]
				newCenteringModes[x + count][y] = centeringModes[x][y]
			}
		}
		
		val newColumnWidths = DoubleArray(columns + count) { COLUMN_WIDTH_AUTO }
		columnWidths.copyInto(
			destination = newColumnWidths,
			startIndex = 0,
			endIndex = columnIndex
		)
		columnWidths.copyInto(
			destination = newColumnWidths,
			destinationOffset = columnIndex + count,
			startIndex = columnIndex,
			endIndex = columns
		)
		columnWidths = newColumnWidths
		columns += count
		grid = newGrid
		centeringModes = newCenteringModes
	}
	
	/**
	 * Removes column at given index.
	 *
	 * @param columnIndex index of column to be deleted.
	 *
	 * @throws IllegalArgumentException if [columnIndex] is out of grid range.
	 */
	fun removeColumn(columnIndex: Int) {
		require(columnIndex in 0 until columns) {
			"Column index out of grid range."
		}
		
		if (columns == 1) {
			initEmpty()
			return
		}
		
		val newGrid = Array(columns - 1) { Array<ElementView?>(rows) { null } }
		val newCenteringModes = Array(columns - 1) { Array(rows) { Alignment.CENTER } }
		
		for (x in 0 until columnIndex) {
			for (y in 0 until rows) {
				newGrid[x][y] = grid[x][y]
				newCenteringModes[x][y] = centeringModes[x][y]
			}
		}
		
		for (x in columnIndex + 1 until columns) {
			for (y in 0 until rows) {
				newGrid[x - 1][y] = grid[x][y]
				newCenteringModes[x - 1][y] = centeringModes[x][y]
			}
		}
		
		val newColumnWidths = DoubleArray(columns - 1) { COLUMN_WIDTH_AUTO }
		columnWidths.copyInto(
			destination = newColumnWidths,
			startIndex = 0,
			endIndex = columnIndex
		)
		columnWidths.copyInto(
			destination = newColumnWidths,
			destinationOffset = columnIndex,
			startIndex = columnIndex + 1,
			endIndex = columns
		)
		columnWidths = newColumnWidths
		columns--
		grid = newGrid
		centeringModes = newCenteringModes
	}
	
	/**
	 * Removes all empty columns.
	 */
	fun removeEmptyColumns() {
		val columnIndices = grid.indices.filter { grid[it].any { e -> e != null } }.toIntArray()
		
		if (columnIndices.isEmpty()) {
			initEmpty()
			return
		}
		
		grid = Array(columnIndices.size) { grid[columnIndices[it]] }
		centeringModes = Array(columnIndices.size) { centeringModes[columnIndices[it]] }
		columnWidths = DoubleArray(columnIndices.size) { columnWidths[columnIndices[it]] }
		columns = columnIndices.size
	}
	
	/**
	 * Inserts the given amount of rows at the given position.
	 *
	 * @param rowIndex index after which the rows should be inserted.
	 * @param count amount of columns to insert.
	 *
	 * @throws IllegalArgumentException if [rowIndex] is out of grid range or [count] is negative.
	 */
	fun addRows(rowIndex: Int, count: Int) {
		require(rowIndex in 0..rows) {
			"Row index out of grid range."
		}
		require(count >= 0) {
			"Parameter count must be positive."
		}
		
		val newGrid = Array(columns) { Array<ElementView?>(rows + count) { null } }
		val newCenteringModes = Array(columns) { Array(rows + count) { Alignment.CENTER } }
		
		for (x in 0 until columns) {
			for (y in 0 until rowIndex) {
				newGrid[x][y] = grid[x][y]
				newCenteringModes[x][y] = centeringModes[x][y]
			}
		}
		
		for (x in 0 until columns) {
			for (y in rowIndex until rows) {
				newGrid[x][y + count] = grid[x][y]
				newCenteringModes[x][y + count] = centeringModes[x][y]
			}
		}
		val newRowHeights = DoubleArray(rows + count) { ROW_HEIGHT_AUTO }
		rowHeights.copyInto(
			destination = newRowHeights,
			startIndex = 0,
			endIndex = rowIndex
		)
		rowHeights.copyInto(
			destination = newRowHeights,
			destinationOffset = rowIndex + count,
			startIndex = rowIndex,
			endIndex = rows
		)
		rowHeights = newRowHeights
		rows += count
		grid = newGrid
		centeringModes = newCenteringModes
	}
	
	/**
	 * Removes row at given index.
	 *
	 * @param rowIndex index of row to be deleted.
	 *
	 * @throws IllegalArgumentException if [rowIndex] is out of grid range.
	 */
	fun removeRow(rowIndex: Int) {
		require(rowIndex in 0 until rows) {
			"Row index out of grid range."
		}
		
		if (rows == 1) {
			initEmpty()
			return
		}
		
		val newGrid = Array(columns) { Array<ElementView?>(rows - 1) { null } }
		val newCenteringModes = Array(columns) { Array(rows - 1) { Alignment.CENTER } }
		
		for (x in 0 until columns) {
			for (y in 0 until rowIndex) {
				newGrid[x][y] = grid[x][y]
				newCenteringModes[x][y] = centeringModes[x][y]
			}
		}
		
		for (x in 0 until columns) {
			for (y in rowIndex + 1 until rows) {
				newGrid[x][y - 1] = grid[x][y]
				newCenteringModes[x][y - 1] = centeringModes[x][y]
			}
		}
		
		val newRowHeights = DoubleArray(rows - 1) { ROW_HEIGHT_AUTO }
		rowHeights.copyInto(
			destination = newRowHeights,
			startIndex = 0,
			endIndex = rowIndex
		)
		rowHeights.copyInto(
			destination = newRowHeights,
			destinationOffset = rowIndex,
			startIndex = rowIndex + 1,
			endIndex = rows
		)
		rowHeights = newRowHeights
		rows--
		grid = newGrid
		centeringModes = newCenteringModes
	}
	
	/**
	 * Removes all empty rows.
	 */
	fun removeEmptyRows() {
		val rowIndices = (0 until rows).filter { j -> (0 until columns).any { i -> grid[i][j] != null } }.toIntArray()
		
		if (rowIndices.isEmpty()) {
			initEmpty()
			return
		}
		
		grid = Array(columns) { x -> Array(rowIndices.size) { y -> grid[x][rowIndices[y]] } }
		centeringModes = Array(columns) { x -> Array(rowIndices.size) { y -> centeringModes[x][rowIndices[y]] } }
		rowHeights = DoubleArray(rowIndices.size) { rowHeights[rowIndices[it]] }
		rows = rowIndices.size
	}
	//endregion
	
	//region Helper
	/**
	 * Creates an empty grid of size 0x0.
	 */
	private fun initEmpty() {
		grid = Array(0) { Array(0) { null } }
		centeringModes = Array(0) { Array(0) { Alignment.CENTER } }
		columnWidths = DoubleArray(0) { COLUMN_WIDTH_AUTO }
		rowHeights = DoubleArray(0) { ROW_HEIGHT_AUTO }
		columns = 0
		rows = 0
	}
	
	/**
	 * Inline forEach method only returning cells with non-null content.
	 *
	 * @see GridIterator
	 * @see GridIteratorElement
	 */
	inline fun forEachNotNull(action: (GridIteratorElement<T>) -> Unit) {
		for (element in this.filter { it.element != null }) action(element)
	}
	
	/**
	 * Inline forEach method only returning non-null cell values.
	 *
	 * @see GridIterator
	 */
	inline fun forEachItemNotNull(action: (T) -> Unit) {
		for (element in this.mapNotNull { it.element }) action(element)
	}
	
	/**
	 * Returns a [GridIterator] for this grid.
	 */
	override fun iterator(): Iterator<GridIteratorElement<T>> = GridIterator()
	
	/**
	 * Prints grid as "x" and "o", where "x" is a cell containing a value and "o" an empty cell.
	 */
	override fun toString(): String = "#Rows: " + rows + "\n" +
			"#Columns: " + columns + "\n" +
			grid.joinToString(separator = "\n")
			{ cols ->
				cols.joinToString(prefix = "[", postfix = "]")
				{ if (it == null) "0" else "X" }
			}
	//endregion
	
	/**
	 * An iterator over a ElementViewGrid. Allows to sequentially access the elements.
	 */
	internal inner class GridIterator : Iterator<GridIteratorElement<T>> {
		/**
		 * Current row position.
		 */
		private var currRow = 0
		
		/**
		 * Current column position.
		 */
		private var currCol = 0
		
		/**
		 * Returns ``rue` if the iteration has more elements.
		 */
		override fun hasNext(): Boolean = currCol < grid.size && currRow < grid[currCol].size
		
		/**
		 * Returns the next element in the iteration.
		 */
		override fun next(): GridIteratorElement<T> {
			if (currCol >= grid.size)
				throw NoSuchElementException()
			
			val res = GridIteratorElement(currCol, currRow, grid[currCol][currRow] as? T)
			
			currRow = ++currRow % grid[currCol].size
			
			if (currRow == 0)
				currCol++
			
			return res
		}
	}
}