package tools.aqua.bgw.util

import tools.aqua.bgw.util.ElementViewGrid.GridIterator

/**
 * Data class containing meta info about current grid element returned by its [GridIterator].
 *
 * @param columnIndex current column index.
 * @param rowIndex current row index.
 * @param element current element or null if there is no element present in this cell.
 */
data class GridIteratorElement<T>(
	val columnIndex: Int,
	val rowIndex: Int,
	val element: T?
)