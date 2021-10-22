package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.sudoku.entity.Sudoku

class RefreshViewController(private val viewController: SudokuViewController) : Refreshable {
	/**
	 * Refresh to init new game.
	 *
	 * @param initialSudoku New sudoku
	 */
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
	
	/**
	 * Refresh to set a digit.
	 *
	 * @param box Box index.
	 * @param row Row index.
	 * @param col Column index.
	 * @param value Value to set or `null` to clear cell.
	 */
	override fun refreshSetValue(box: Int, row: Int, col: Int, value: Int?) {
		viewController.sudokuGameScene.sudokuGrid[box, row, col] = value
	}
	
	/**
	 * Refresh to show error hints.
	 *
	 * @param errors Errors to show in grid.
	 */
	override fun refreshHint(errors: Collection<Sudoku.SudokuTuple>) {
		viewController.sudokuGameScene.sudokuGrid.showHint(errors)
	}
	
	/**
	 * Refresh to update timer label.
	 *
	 * @param time New time.
	 */
	override fun refreshTimer(time: String) {
		BoardGameApplication.runOnGUIThread {
			viewController.sudokuGameScene.timer.text = time
		}
	}
	
	/**
	 * Refresh to show that game is finished.
	 */
	override fun refreshWon() {
		viewController.sudokuGameScene.sudokuGrid.showWon()
	}
}