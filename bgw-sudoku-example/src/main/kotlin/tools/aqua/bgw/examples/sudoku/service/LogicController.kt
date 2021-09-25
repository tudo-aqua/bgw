package tools.aqua.bgw.examples.sudoku.service

import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.view.Refreshable
import kotlin.streams.toList

/**
 * Controller managing game actions.
 */
class LogicController(private val view: Refreshable) {
	fun newGame(difficulty: Difficulty) {
		view.refreshInit(getRandomSudoku(difficulty))
	}
	
	private fun getRandomSudoku(difficulty: Difficulty) : Array<Array<Array<Int?>>> {
		val formatted = Array(9) { Array(3) { Array<Int?>(3) { null }}}
		val sudokuInput = checkNotNull(readRandomSudoku(difficulty))
		
		check(sudokuInput.length == 81)
		
		sudokuInput.toCharArray().forEachIndexed { index, token ->
			val box = index / 9
			val row = index % 9 / 3
			val col = index % 3
			val value = token.digitToInt()
			
			formatted[box][row][col] = if(value == 0) null else value
		}
		
		return formatted
	}
	
	private fun readRandomSudoku(difficulty: Difficulty) =
		javaClass.classLoader.getResourceAsStream(difficulty.file)?.bufferedReader()?.lines()?.toList()?.randomOrNull()
	
	fun setValue(box: Int, row: Int, col: Int, value: Int?) {
		view.refreshSetValue(box, row, col, value)
	}
}