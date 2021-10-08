package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple
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
		else
			get(box % 3, box / 3)?.get(col, row)?.clear()
	}
	
	operator fun set(box: Int, row: Int, col: Int, value: Int?) {
		getCell(box, row, col).value = value
	}
	
	operator fun get(box: Int, row: Int, col: Int): Int? = getCell(box, row, col).value
	
	fun showHint(tuple: Collection<SudokuTuple>) {
		tuple.forEach {
			getCell(it.box, it.row, it.col).visual = ColorVisual(255,150,150)
		}
	}
	
	fun clearHints() {
		forEach { box -> box.component?.forEach { cell ->
				cell.component?.visual = ColorVisual.WHITE
			}
		}
	}
	
	fun getCell(box: Int, row: Int, col: Int) : SudokuCell =
		get(box % 3, box / 3)?.get(col, row)!!
}