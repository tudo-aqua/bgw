/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.sudoku.entity

/**
 * Sudoku entity.
 *
 * @property grid The grid representation.
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
      require(box in 0..8) { "Parameter box is out of bounds: $box" }
      require(row in 0..2) { "Parameter row is out of bounds: $row" }
      require(col in 0..2) { "Parameter col is out of bounds: $col" }
      require(value == null || value in 1..9) { "Parameter value is out of bounds: $col" }
    }
  }

  /**
   * Sudoku cell data class holding cell's value and fixed status.
   *
   * @property value Current value of this cell or `null` if empty.
   * @property isFixed Whether this is a fixed digit.
   */
  data class SudokuCell(var value: Int? = null, var isFixed: Boolean = false)

  /**
   * SudokuTuple data class containing cell's value and position in the grid.
   *
   * @property box Box index.
   * @property row Row index.
   * @property col Column index.
   * @property value Value to set or `null` to clear cell.
   */
  data class SudokuTuple(val box: Int, val row: Int, val col: Int, val value: Int? = null) {
    init {
      checkBounds(box, row, col, value)
    }
  }
}
