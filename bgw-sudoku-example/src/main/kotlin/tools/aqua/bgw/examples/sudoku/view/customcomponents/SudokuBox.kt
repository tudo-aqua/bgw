package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.visual.Visual

class SudokuBox(boxIndex : Int, size : Number, spacing : Number) : GridPane<SudokuCell>(
	posX = 0,
	posY = 0,
	columns = 3,
	rows = 3,
	spacing = spacing,
	layoutFromCenter = true,
	visual = Visual.EMPTY) {
	
	var selectedEvent: ((CellSelectedEvent) -> Unit)? = null
	
	init {
		for(i in 0..2){
			for (j in 0..2) {
				this[i,j] = SudokuCell(boxIndex, j, i, size).apply {
					selectedEvent = { this@SudokuBox.selectedEvent?.invoke(it) }
				}
			}
		}
	}
}