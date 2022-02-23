/*
 * Copyright 2022 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.examples.sudoku.service

import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.Sudoku.SudokuTuple

object SudokuChecker {
  /**
   * Checks given sudoku for errors and returns them.
   *
   * @param sudoku Sudoku to check.
   *
   * @return Set of errors in given sudoku.
   */
  fun checkSudoku(sudoku: Sudoku): Set<SudokuTuple> =
      checkBoxes(sudoku) + checkRows(sudoku) + checkCols(sudoku)

  /**
   * Checks given sudoku for errors in boxes and returns them.
   *
   * @param sudoku Sudoku to check.
   *
   * @return Set of errors in given sudoku.
   */
  private fun checkBoxes(sudoku: Sudoku): Set<SudokuTuple> {
    val errors = mutableSetOf<SudokuTuple>()

    sudoku.grid.forEachIndexed { box, boxArray ->
      val digits = mutableListOf<SudokuTuple>()

      boxArray.forEachIndexed { row, rowArray ->
        rowArray.forEachIndexed { col, cell -> digits.add(SudokuTuple(box, row, col, cell.value)) }
      }

      errors.addAll(checkDuplicates(digits))
    }

    return errors
  }

  /**
   * Checks given sudoku for errors in rows and returns them.
   *
   * @param sudoku Sudoku to check.
   *
   * @return Set of errors in given sudoku.
   */
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

  /**
   * Checks given sudoku for errors in columns and returns them.
   *
   * @param sudoku Sudoku to check.
   *
   * @return Set of errors in given sudoku.
   */
  private fun checkCols(sudoku: Sudoku): Set<SudokuTuple> {
    val errors = mutableSetOf<SudokuTuple>()

    for (i in 0..2) {
      for (col in 0..2) {
        val digits = mutableListOf<SudokuTuple>()

        for (box in listOf(i, i + 3, i + 6)) {
          for (row in 0..2) {
            digits.add(SudokuTuple(box, row, col, sudoku[box, row, col]))
          }
        }

        errors.addAll(checkDuplicates(digits))
      }
    }

    return errors
  }

  /**
   * Checks for duplicate digits in list and returns them.
   *
   * @param digits List of digits.
   *
   * @return Set of duplicate digits in given list.
   */
  private fun checkDuplicates(digits: List<SudokuTuple>): Set<SudokuTuple> {
    val errors = mutableSetOf<SudokuTuple>()

    (digits subtract digits.distinctBy { it.value }.toSet()).forEach {
      if (it.value != null) errors.addAll(digits.filter { t -> it.value == t.value })
    }

    return errors
  }

  /**
   * Checks whether given sudoku is full.
   *
   * @return `true` if all cells contain a digit, `false` otherwise.
   */
  fun checkFull(sudoku: Sudoku): Boolean =
      !sudoku.grid.any { box -> box.any { row -> row.any { col -> col.value == null } } }
}
