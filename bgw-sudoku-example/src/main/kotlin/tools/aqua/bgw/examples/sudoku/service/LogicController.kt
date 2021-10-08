package tools.aqua.bgw.examples.sudoku.service

import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.entity.Settings
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple
import tools.aqua.bgw.examples.sudoku.service.SudokuChecker.Companion.checkSudoku
import tools.aqua.bgw.examples.sudoku.view.Refreshable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.streams.toList

/**
 * Controller managing game actions.
 */
class LogicController(private val view: Refreshable) {
	
	var sudoku: Sudoku = Sudoku()
	val settings: Settings = Settings()
	
	/**
	 * Timer
	 */
	private var startTime: Long = 0L
	private var timerRunning: Boolean = false
	private val timer: Timer = Timer().apply {
		scheduleAtFixedRate(
			delay = 0,
			period = 100
		) {
			if (timerRunning) {
				val millis = System.currentTimeMillis() - startTime
				
				view.refreshTimer(
					String.format(
						"%02d:%02d:%02d",
						TimeUnit.MILLISECONDS.toHours(millis),
						TimeUnit.MILLISECONDS.toMinutes(millis) -
								TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
						TimeUnit.MILLISECONDS.toSeconds(millis) -
								TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
					)
				)
			}
		}
	}
	
	fun newGame(difficulty: Difficulty) {
		sudoku = getRandomSudoku(difficulty)
		startTime = System.currentTimeMillis()
		timerRunning = true
		view.refreshInit(sudoku)
	}
	
	private fun getRandomSudoku(difficulty: Difficulty): Sudoku {
		val formatted = Array(9) { Array(3) { Array(3) { Sudoku.SudokuCell() } } }
		val sudokuInput = checkNotNull(readRandomSudoku(difficulty))
		
		check(sudokuInput.length == 81)
		
		sudokuInput.toCharArray().forEachIndexed { index, token ->
			val box = index / 9
			val row = index % 9 / 3
			val col = index % 3
			val value = token.digitToInt()
			
			if (value != 0)
				formatted[box][row][col] = Sudoku.SudokuCell(value = value, isFixed = true)
		}
		
		return Sudoku(formatted)
	}
	
	private fun readRandomSudoku(difficulty: Difficulty) =
		javaClass.classLoader.getResourceAsStream(difficulty.file)?.bufferedReader()?.lines()?.toList()?.randomOrNull()
	
	fun setValue(tuple: SudokuTuple) {
		if (sudoku.grid[tuple.box][tuple.row][tuple.col].isFixed)
			return
		
		sudoku[tuple.box, tuple.row, tuple.col] = tuple.value
		view.refreshSetValue(tuple)
		
		checkSudoku()
	}
	
	fun checkSudoku() {
		val isFull = SudokuChecker.checkFull(sudoku)
		if (isFull || settings.instantCheck.value) {
			val errors = checkSudoku(sudoku)
			
			if (errors.isNotEmpty())
				view.refreshHint(errors)
			else if (errors.isEmpty() && isFull)
				view.refreshWon()
		}
	}
	
	fun requestHint() {
		view.refreshHint(checkSudoku(sudoku))
	}
}