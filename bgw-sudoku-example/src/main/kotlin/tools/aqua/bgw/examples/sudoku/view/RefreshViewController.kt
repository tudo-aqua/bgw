package tools.aqua.bgw.examples.sudoku.view

class RefreshViewController(private val viewController: SudokuViewController) : Refreshable {
	override fun refreshInit(initialSudoku: Array<Array<Array<Int?>>>) {
		val grid = viewController.sudokuGameScene.sudokuGrid
		
		initialSudoku.forEachIndexed { box, boxArray ->
			boxArray.forEachIndexed { row, rowArray ->
				rowArray.forEachIndexed { col, value ->
					grid.setFixed(box, row, col, value)
				}
			}
		}
	}
	
	override fun refreshSetValue(box: Int, row: Int, col: Int, value: Int?) {
		viewController.sudokuGameScene.sudokuGrid[box, row, col] = value
	}
}