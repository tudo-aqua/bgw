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

package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.entity.Sudoku.SudokuTuple
import tools.aqua.bgw.visual.ColorVisual

/**
 * Sudoku grid containing 9 [SudokuBox]es.
 *
 * @param posX Horizontal coordinate for this [SudokuGrid].
 * @param posY Vertical coordinate for this [SudokuGrid].
 * @param size The size of this [SudokuGrid] as square.
 * @param majorSpacing Spacing between boxes.
 * @param minorSpacing Spacing between cells.
 */
class SudokuGrid(
    posX: Number,
    posY: Number,
    size: Number,
    majorSpacing: Number,
    minorSpacing: Number
) :
    GridPane<SudokuBox>(
        posX = posX,
        posY = posY,
        columns = 3,
        rows = 3,
        spacing = majorSpacing,
        layoutFromCenter = true,
        visual = ColorVisual.BLACK) {
  /** Currently selected cell. */
  var selectedCell: SudokuCell? = null

  /** Event raised upon selection of a cell. */
  private var selectedEvent: ((CellSelectedEvent) -> Unit)? = null

  init {
    val cellSize = size.toDouble() / 9

    for (i in 0..8) {
      this[i % 3, i / 3] =
          SudokuBox(i, cellSize, minorSpacing).apply {
            selectedEvent = {
              selectedCell?.deselect()
              selectedCell = it.cell
              this@SudokuGrid.selectedEvent?.invoke(it)
            }
          }
    }
  }

  /**
   * Sets a cell's value and marks it as fixed. Changes to black font.
   *
   * @param box Box index. Must be in 0..8.
   * @param row Row index. Must be in 0..2.
   * @param col Column index. Must be in 0..2.
   * @param value Value to set or `null` to clear cell. Must be in 1..9 or `null`.
   */
  fun setFixed(box: Int, row: Int, col: Int, value: Int?) {
    Sudoku.checkBounds(box = box, row = row, col = col, value = value)

    if (value != null) get(box % 3, box / 3)?.get(col, row)?.setFixedValue(value)
    else get(box % 3, box / 3)?.get(col, row)?.clear()
  }

  /**
   * Sets a cell's value.
   *
   * @param box Box index. Must be in 0..8.
   * @param row Row index. Must be in 0..2.
   * @param col Column index. Must be in 0..2.
   * @param value Value to set or `null` to clear cell. Must be in 1..9 or `null`.
   */
  operator fun set(box: Int, row: Int, col: Int, value: Int?) {
    Sudoku.checkBounds(box = box, row = row, col = col, value = value)

    getCell(box, row, col).value = value
  }

  /**
   * Returns a cell's value.
   *
   * @param box Box index. Must be in 0..8.
   * @param row Row index. Must be in 0..2.
   * @param col Column index. Must be in 0..2.
   *
   * @return The cell's value.
   */
  operator fun get(box: Int, row: Int, col: Int): Int? {
    Sudoku.checkBounds(box = box, row = row, col = col)

    return getCell(box, row, col).value
  }

  /**
   * Returns a [SudokuCell].
   *
   * @param box Box index. Must be in 0..8.
   * @param row Row index. Must be in 0..2.
   * @param col Column index. Must be in 0..2.
   *
   * @return The [SudokuCell].
   */
  fun getCell(box: Int, row: Int, col: Int): SudokuCell {
    Sudoku.checkBounds(box = box, row = row, col = col)

    return requireNotNull(get(box % 3, box / 3)?.get(col, row))
  }

  /**
   * Marks given cells in red.
   *
   * @param cells Cells to be marked.
   */
  fun showHint(cells: Collection<SudokuTuple>) {
    cells.forEach { getCell(it.box, it.row, it.col).visual = ColorVisual(errorColor) }
  }

  /** Marks all cells in green. */
  fun showWon() {
    forEach { box ->
      box.component?.forEach { cell -> cell.component?.visual = ColorVisual(wonColor) }
    }
  }

  /** Clears all hints. */
  fun clearHints() {
    forEach { box -> box.component?.forEach { cell -> cell.component?.visual = ColorVisual.WHITE } }
    selectedCell?.visual = ColorVisual(selectedColor)
  }
}
