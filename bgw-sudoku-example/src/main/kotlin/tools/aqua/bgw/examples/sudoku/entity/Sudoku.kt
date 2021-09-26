package tools.aqua.bgw.examples.sudoku.entity

class Sudoku(
	val grid: Array<Array<Array<SudokuCell>>> = Array(9) { Array(3) { Array(3) { SudokuCell() } } }
) {
	
	operator fun set(box: Int, row: Int, col: Int, value: Int?) {
		grid[box][row][col].value = value
	}
	
	operator fun get(box: Int, row: Int, col: Int): Int? = grid[box][row][col].value
	
	class SudokuCell(var value: Int? = null, var isFixed: Boolean = false)
}