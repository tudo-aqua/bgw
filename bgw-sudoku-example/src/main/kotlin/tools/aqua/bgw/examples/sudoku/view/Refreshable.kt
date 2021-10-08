package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.examples.sudoku.entity.Sudoku

interface Refreshable {
	/**
	 * Refresh to init new game.
	 *
	 * @param initialSudoku New sudoku
	 */
	fun refreshInit(initialSudoku: Sudoku)
	
	/**
	 * Refresh to set a digit.
	 *
	 * @param box Box index.
	 * @param row Row index.
	 * @param col Column index.
	 * @param value Value to set or `null` to clear cell.
	 */
	fun refreshSetValue(box: Int, row: Int, col: Int, value: Int? = null)
	
	/**
	 * Refresh to show error hints.
	 *
	 * @param errors Errors to show in grid.
	 */
	fun refreshHint(errors : Collection<Sudoku.SudokuTuple>)
	
	/**
	 * Refresh to update timer label.
	 *
	 * @param time New time.
	 */
	fun refreshTimer(time: String)
	
	/**
	 * Refresh to show that game is finished.
	 */
	fun refreshWon()
}