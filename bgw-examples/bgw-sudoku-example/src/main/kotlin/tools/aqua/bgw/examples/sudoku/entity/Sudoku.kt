package tools.aqua.bgw.examples.sudoku.entity

/**
 * Sudoku entity
 *
 * @param grid The grid representation.
 */
class Sudoku(
	val grid: Array<Array<Array<SudokuCell>>> = Array(9) { Array(3) { Array(3) { SudokuCell() } } }
) {
	
	/**
	 * Sets a cells value.
	 *
	 * @param box Box index.
	 * @param row Row index.
	 * @param col Column index.
	 * @param value Value to set or `null` to clear cell.
	 */
	operator fun set(box: Int, row: Int, col: Int, value: Int?) {
		checkBounds(box, row, col, value)
		
		grid[box][row][col].value = value
	}
	
	/**
	 * Returns a cells value.
	 *
	 * @param box Box index.
	 * @param row Row index.
	 * @param col Column index.
	 *
	 * @return The cells value or `null` if the cell is empty
	 */
	operator fun get(box: Int, row: Int, col: Int): Int? {
		checkBounds(box, row, col)
		
		return grid[box][row][col].value
	}
	
	companion object {
		/**
		 * Checks bounds for cell access.
		 *
		 * @param box Box index. Must be in 0..8.
		 * @param row Row index. Must be in 0..2.
		 * @param col Column index. Must be in 0..2.
		 * @param value Value to set or `null` to clear cell. Must be in 1..9 or `null`.
		 */
		fun checkBounds(box: Int = 0, row: Int = 0, col: Int = 0, value: Int? = null) {
			require(box in (0..8)) { "Parameter box is out of bounds: $box" }
			require(row in (0..2)) { "Parameter row is out of bounds: $row" }
			require(col in (0..2)) { "Parameter col is out of bounds: $col" }
			require(value == null || value in (1..9)) { "Parameter value is out of bounds: $col" }
		}
	}
	
	/**
	 * Sudoku cell data class holding cell's value and fixed status.
	 *
	 * @param value Current value of this cell or `null` if empty.
	 * @param isFixed Whether this is a fixed digit.
	 */
	data class SudokuCell(var value: Int? = null, var isFixed: Boolean = false)
	
	/**
	 * SudokuTuple data class containing cell's value and position in the grid.
	 *
	 * @param box Box index.
	 * @param row Row index.
	 * @param col Column index.
	 * @param value Value to set or `null` to clear cell.
	 */
	data class SudokuTuple(val box : Int, val row : Int, val col : Int, val value : Int? = null) {
		init {
			checkBounds(box, row, col, value)
		}
	}
}