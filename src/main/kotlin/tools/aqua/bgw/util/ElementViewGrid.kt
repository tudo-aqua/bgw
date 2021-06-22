@file:Suppress("MemberVisibilityCanBePrivate", "unused", "Unchecked_Cast", "DuplicatedCode")

package tools.aqua.bgw.util

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView.Companion.COLUMN_WIDTH_AUTO
import tools.aqua.bgw.elements.layoutviews.GridLayoutView.Companion.ROW_HEIGHT_AUTO

//TODO: Avoid duplicate code on require statements
/**
 * ElementViewGrid.
 */
internal class ElementViewGrid<T : ElementView>(
	rows: Int,
	columns: Int
) : Iterable<Triple<Int, Int, T?>> {
	
	var rows = rows
		private set
	
	var columns = columns
		private set
	
	private var grid = Array(columns) { Array<ElementView?>(rows) { null } }
	private var centeringModes = Array(columns) { Array(rows) { Alignment.CENTER } }
	
	private var rowHeights = DoubleArray(rows) { -1.0 }
	private var columnWidths = DoubleArray(columns) { -1.0 }
	
	operator fun get(columnIndex: Int, rowIndex: Int): T? = grid[columnIndex][rowIndex] as? T
	
	operator fun set(columnIndex: Int, rowIndex: Int, value: T?) {
		require(columnIndex in 0 until columns && rowIndex in 0 until rows) {
			"Indices exceed grid bounds."
		}
		
		grid[columnIndex][rowIndex] = value
	}
	
	fun getCellCenterMode(columnIndex: Int, rowIndex: Int): Alignment = centeringModes[columnIndex][rowIndex]
	
	fun setCellCenterMode(columnIndex: Int, rowIndex: Int, value: Alignment) {
		require(columnIndex in 0 until columns && rowIndex in 0 until rows) {
			"Indices exceed grid bounds."
		}
		
		centeringModes[columnIndex][rowIndex] = value
	}
	
	fun setColumnCenterMode(columnIndex: Int, value: Alignment) {
		require(columnIndex in 0 until columns) {
			"Column index exceed grid bounds."
		}
		
		for (i in 0 until rows)
			centeringModes[columnIndex][i] = value
	}
	
	fun setRowCenterMode(rowIndex: Int, value: Alignment) {
		require(rowIndex in 0 until rows) {
			"Row index exceed grid bounds."
		}
		
		for (i in 0 until columns)
			centeringModes[i][rowIndex] = value
	}
	
	fun setCenterMode(value: Alignment) {
		for (x in 0 until columns)
			for (y in 0 until rows)
				centeringModes[x][y] = value
	}
	
	fun getRow(rowIndex: Int): List<T?> = List(columns) { grid[it][rowIndex] as? T }
	
	fun getRows(): List<List<T?>> = (0 until rows).map { getRow(it) }
	
	fun getColumn(columnIndex: Int): List<T?> = grid[columnIndex].toList() as List<T?>
	
	fun getColumns(): List<List<T?>> = (0 until columns).map { getColumn(it) }
	
	fun getColumnWidth(columnIndex: Int): Double {
		require(columnIndex in columnWidths.indices) {
			"ColumnIndex out of grid range."
		}
		
		return columnWidths[columnIndex]
	}
	
	fun setColumnWidth(columnIndex: Int, columnWidth: Double) {
		require(columnIndex in columnWidths.indices) {
			"ColumnIndex out of grid range."
		}
		require(columnWidth >= 0 || columnWidth == COLUMN_WIDTH_AUTO) {
			"Parameter must be positive or COLUMN_WIDTH_AUTO."
		}
		
		columnWidths[columnIndex] = columnWidth
	}
	
	fun setColumnWidths(columnWidths: DoubleArray) {
		require(columnWidths.size == this.columnWidths.size) {
			"Array size does not match grid range."
		}
		require(columnWidths.all { it >= 0 || it == COLUMN_WIDTH_AUTO }) {
			"Parameter values must all be positive or COLUMN_WIDTH_AUTO."
		}
		
		this.columnWidths = columnWidths
	}
	
	fun getRowHeight(rowIndex: Int): Double {
		require(rowIndex in rowHeights.indices) {
			"ColumnIndex out of grid range."
		}
		
		return rowHeights[rowIndex]
	}
	
	fun setRowHeight(rowIndex: Int, rowHeight: Double) {
		require(rowIndex in rowHeights.indices) {
			"ColumnIndex out of grid range."
		}
		require(rowHeight >= 0 || rowHeight == ROW_HEIGHT_AUTO) {
			"Parameter must be positive or ROW_HEIGHT_AUTO."
		}
		
		rowHeights[rowIndex] = rowHeight
	}
	
	fun setRowHeights(rowHeights: DoubleArray) {
		require(rowHeights.size == this.rowHeights.size) {
			"Array size does not match grid range."
		}
		require(rowHeights.all { it >= 0 || it == ROW_HEIGHT_AUTO }) {
			"Parameter values must all be positive or ROW_HEIGHT_AUTO."
		}
		
		this.rowHeights = rowHeights
	}
	
	fun grow(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) {
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
	}
	
	fun trim() {
		trimColumns()
		trimRows()
	}
	
	fun trimColumns() {
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
			return
		}
		
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
	
	fun trimRows() {
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
			return
		}
		
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
	
	fun addColumns(columnIndex: Int, count: Int) {
		require(count >= 0) {
			"Parameter count must be positive."
		}
		require(columnIndex in 0..columns) {
			"Column index out of grid range."
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
	
	fun addRows(rowIndex: Int, count: Int) {
		require(count >= 0) {
			"Parameter count must be positive."
		}
		require(rowIndex in 0..rows) {
			"Row index out of grid range."
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
	}//Todo: definitely needs a test
	
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
	
	private fun initEmpty() {
		grid = Array(0) { Array(0) { null } }
		centeringModes = Array(0) { Array(0) { Alignment.CENTER } }
		columnWidths = DoubleArray(0) { -1.0 }
		rowHeights = DoubleArray(0) { -1.0 }
		columns = 0
		rows = 0
	}
	
	override fun iterator(): Iterator<Triple<Int, Int, T?>> = GridIterator()
	
	override fun toString(): String = "#Rows: " + rows + "\n" +
			"#Columns: " + columns + "\n" +
			grid.joinToString(separator = "\n")
			{ cols ->
				cols.joinToString(prefix = "[", postfix = "]")
				{ if (it == null) "0" else "X" }
			}
	
	internal inner class GridIterator : Iterator<Triple<Int, Int, T?>> {
		
		private var currRow = 0
		private var currCol = 0
		
		override fun hasNext(): Boolean = currCol < grid.size && currRow < grid[currCol].size //TODO: TEST!!!!!
		
		override fun next(): Triple<Int, Int, T?> {
			if (currCol >= grid.size)
				throw NoSuchElementException()
			
			val res: Triple<Int, Int, T?> = Triple(currCol, currRow, grid[currCol][currRow] as? T)
			
			currRow = ++currRow % grid[currCol].size
			
			if (currRow == 0)
				currCol++
			
			return res
		}
	}
	
	inline fun forEachNotNull(action: (Triple<Int, Int, T>) -> Unit) {
		for (element in this.filter { it.third != null }) action(element as Triple<Int, Int, T>)
	}
	
	inline fun forEachItemNotNull(action: (T) -> Unit) {
		for (element in this.mapNotNull { it.third }) action(element)
	}
}