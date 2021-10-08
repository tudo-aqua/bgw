package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple

class RefreshViewController(private val viewController: SudokuViewController) : Refreshable {
	override fun refreshInit(initialSudoku: Sudoku) {
		val grid = viewController.sudokuGameScene.sudokuGrid
		
		initialSudoku.grid.forEachIndexed { box, boxArray ->
			boxArray.forEachIndexed { row, rowArray ->
				rowArray.forEachIndexed { col, cell ->
					grid.setFixed(box, row, col, cell.value)
				}
			}
		}
	}
	
	override fun refreshTimer(time: String) {
		BoardGameApplication.runOnGUIThread {
			viewController.sudokuGameScene.timer.text = time
		}
	}
	
	override fun refreshSetValue(tuple : SudokuTuple) {
		viewController.sudokuGameScene.sudokuGrid[tuple.box, tuple.row, tuple.col] = tuple.value
	}
	
	override fun refreshHint(tuple: Collection<SudokuTuple>) {
		viewController.sudokuGameScene.sudokuGrid.showHint(tuple)
	}
	
	override fun refreshWon() {
		println("You won!")
	}
}