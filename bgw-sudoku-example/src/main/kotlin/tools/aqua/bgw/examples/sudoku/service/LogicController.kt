package tools.aqua.bgw.examples.sudoku.service

import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple
import tools.aqua.bgw.examples.sudoku.service.SudokuChecker.Companion.checkSudoku
import tools.aqua.bgw.examples.sudoku.view.Refreshable
import kotlin.streams.toList

/**
 * Controller managing game actions.
 */
class LogicController(private val view: Refreshable) {
	
	var sudoku : Sudoku = Sudoku()
	
	fun newGame(difficulty: Difficulty) {
		sudoku = getRandomSudoku(difficulty)
		view.refreshInit(sudoku)
	}
	
	private fun getRandomSudoku(difficulty: Difficulty) : Sudoku {
		val formatted = Array(9) { Array(3) { Array(3) { Sudoku.SudokuCell() } } }
		val sudokuInput = checkNotNull(readRandomSudoku(difficulty))
		
		check(sudokuInput.length == 81)
		
		sudokuInput.toCharArray().forEachIndexed { index, token ->
			val box = index / 9
			val row = index % 9 / 3
			val col = index % 3
			val value = token.digitToInt()
			
			if(value != 0)
				formatted[box][row][col] = Sudoku.SudokuCell(value = value, isFixed = true)
		}
		
		return Sudoku(formatted)
	}
	
	private fun readRandomSudoku(difficulty: Difficulty) =
		javaClass.classLoader.getResourceAsStream(difficulty.file)?.bufferedReader()?.lines()?.toList()?.randomOrNull()
	
	fun setValue(tuple : SudokuTuple) {
		if(sudoku.grid[tuple.box][tuple.row][tuple.col].isFixed)
			return
		
		sudoku[tuple.box, tuple.row, tuple.col] = tuple.value
		view.refreshSetValue(tuple)
		
		if(SudokuChecker.checkFull(sudoku)) {
			val errors = checkSudoku(sudoku)
			if(errors.isNotEmpty())
				view.refreshHint(errors)
		}
	}
	
	fun requestHint() {
		view.refreshHint(checkSudoku(sudoku))
	}
}