/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.ColorVisual

/**
 * A box of the sudoku containing 9 [SudokuCell]s.
 *
 * @property boxIndex Index of the box. Must be in 0..8.
 * @property rowIndex Index of the row. Must be in 0..2.
 * @property colIndex Index of the column. Must be in 0..2.
 * @param size Size of this cell.
 */
class SudokuCell(val boxIndex: Int, val rowIndex: Int, val colIndex: Int, size: Number) :
    Label(width = size, height = size, font = blueFont, text = "", visual = ColorVisual.WHITE) {

  /** Whether this is a fixed digit. */
  var isFixed: Boolean = false

  /** [Property] for the [value] of this cell. */
  private val valueProperty =
      Property<Int?>(null).apply { this.addListener { _, nV -> text = nV?.toString().orEmpty() } }

  /** Value of this cell. */
  var value: Int?
    get() = valueProperty.value
    set(value) {
      if (isFixed) return

      valueProperty.value = value
    }

  /** Event raised upon selection. */
  var selectedEvent: ((CellSelectedEvent) -> Unit)? = null

  init {
    Sudoku.checkBounds(box = boxIndex, row = rowIndex, col = colIndex)

    onMouseClicked = { select() }
  }

  /** Sets value and marks it as fixed. Changes to black font. */
  fun setFixedValue(value: Int) {
    text = value.toString()
    font = blackFont
    isFixed = true
  }

  /** Clears value and marks it as non-fixed. Changes to blue font. */
  fun clear() {
    text = ""
    font = blueFont
    isFixed = false
  }

  /** Selects this cell. */
  fun select() {
    selectedEvent?.invoke(CellSelectedEvent(this))
    visual = ColorVisual(selectedColor)
  }

  /** Deselects this cell. */
  fun deselect() {
    visual = ColorVisual.WHITE
  }
}
