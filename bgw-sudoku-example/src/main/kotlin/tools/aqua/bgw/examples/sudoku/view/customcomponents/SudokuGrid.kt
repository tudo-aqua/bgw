package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.visual.ColorVisual

class SudokuGrid(
	posX: Number,
	posY: Number,
	size: Number,
	majorSpacing: Number,
	minorSpacing: Number
) : GridPane<SudokuBox>(
	posX = posX,
	posY = posY,
	columns = 3,
	rows = 3,
	spacing = majorSpacing,
	layoutFromCenter = true,
	visual = ColorVisual.BLACK
) {
	
	var selectedCell : SudokuCell? = null
	
	var selectedEvent: ((CellSelectedEvent) -> Unit)? = null
	
	init {
		val cellSize = size.toDouble() / 9
		
		for (i in 0..8) {
			this[i % 3, i / 3] = SudokuBox(i, cellSize, minorSpacing).apply {
				selectedEvent = {
					selectedCell?.deselect()
					selectedCell = it.cell
					this@SudokuGrid.selectedEvent?.invoke(it)
				}
			}
		}
	}
	
	fun setFixed(box: Int, row: Int, col: Int, value: Int?) {
		if(value != null)
			get(box % 3, box / 3)?.get(col, row)?.setFixedValue(value)
	}
	
	operator fun set(box: Int, row: Int, col: Int, value: Int?) {
		get(box % 3, box / 3)?.get(row, col)?.value = value
	}
	
	operator fun get(box: Int, row: Int, col: Int, value: Int?): Int? =
		get(box % 3, box / 3)?.get(col, row)?.value
}