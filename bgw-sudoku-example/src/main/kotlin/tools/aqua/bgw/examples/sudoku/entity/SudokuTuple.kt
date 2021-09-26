package tools.aqua.bgw.examples.sudoku.entity

data class SudokuTuple(val box : Int, val row : Int, val col : Int, val value : Int? = null)