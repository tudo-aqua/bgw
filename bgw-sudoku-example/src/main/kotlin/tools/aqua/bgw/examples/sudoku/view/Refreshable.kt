package tools.aqua.bgw.examples.sudoku.view

interface Refreshable {
	fun refreshInit(initialSudoku: Array<Array<Array<Int?>>>)
	fun refreshSetValue(box: Int, row: Int, col: Int, value: Int?)
}