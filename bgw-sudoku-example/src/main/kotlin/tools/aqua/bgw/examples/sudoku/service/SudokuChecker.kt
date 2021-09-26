package tools.aqua.bgw.examples.sudoku.service

import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple

class SudokuChecker {
	companion object {
		fun checkSudoku(sudoku: Sudoku): Set<SudokuTuple> = checkBoxes(sudoku) + checkRows(sudoku) + checkCols(sudoku)
		
		private fun checkBoxes(sudoku: Sudoku): Set<SudokuTuple> {
			val errors = mutableSetOf<SudokuTuple>()
			
			sudoku.grid.forEachIndexed { box, boxArray ->
				val digits = mutableListOf<SudokuTuple>()
				
				boxArray.forEachIndexed { row, rowArray ->
					rowArray.forEachIndexed { col, cell ->
						digits.add(SudokuTuple(box, row, col, cell.value))
					}
				}
				
				errors.addAll(checkDuplicates(digits))
			}
			
			return errors
		}
		
		private fun checkRows(sudoku: Sudoku): Set<SudokuTuple> {
			val errors = mutableSetOf<SudokuTuple>()
			
			for (i in 0..2) {
				for (row in 0..2) {
					val digits = mutableListOf<SudokuTuple>()
					
					for (box in IntRange(3 * i, 3 * i + 2)) {
						for (col in 0..2) {
							digits.add(SudokuTuple(box, row, col, sudoku[box, row, col]))
						}
					}
					
					errors.addAll(checkDuplicates(digits))
				}
			}
			
			return errors
		}
		
		private fun checkCols(sudoku: Sudoku): Set<SudokuTuple> {
			val errors = mutableSetOf<SudokuTuple>()
			
			for (i in 0..2) {
				for (col in 0..2) {
					val digits = mutableListOf<SudokuTuple>()
					
					for (box in listOf(i, i+3, i+6)) {
						for (row in 0..2) {
							digits.add(SudokuTuple(box, row, col, sudoku[box, row, col]))
						}
					}
					
					errors.addAll(checkDuplicates(digits))
				}
			}
			
			return errors
		}
		
		private fun checkDuplicates(digits : List<SudokuTuple>) : Set<SudokuTuple> {
			val errors = mutableSetOf<SudokuTuple>()
			
			(digits subtract digits.distinctBy { it.value }).forEach {
				if (it.value != null)
					errors.addAll(digits.filter { t -> it.value == t.value })
			}
			
			return errors
		}
		
		fun checkFull(sudoku: Sudoku): Boolean =
			!sudoku.grid.any { box -> box.any { row -> row.any { col -> col.value == null } } }
	}
}