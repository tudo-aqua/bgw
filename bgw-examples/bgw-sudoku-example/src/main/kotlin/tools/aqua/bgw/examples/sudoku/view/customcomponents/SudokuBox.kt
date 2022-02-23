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
import tools.aqua.bgw.visual.Visual

/**
 * A box of the sudoku containing 9 [SudokuCell]s.
 *
 * @param boxIndex Index of this box. Must be in 0..8.
 * @param size Size of this box.
 * @param spacing Spacing between cells.
 */
class SudokuBox(boxIndex: Int, size: Number, spacing: Number) :
    GridPane<SudokuCell>(
        posX = 0,
        posY = 0,
        columns = 3,
        rows = 3,
        spacing = spacing,
        layoutFromCenter = true,
        visual = Visual.EMPTY) {
  
  /**
   * Event when selecting a sudoku cell.
   */
  var selectedEvent: ((CellSelectedEvent) -> Unit)? = null

  init {
    Sudoku.checkBounds(box = boxIndex)

    for (i in 0..2) {
      for (j in 0..2) {
        this[i, j] =
            SudokuCell(boxIndex, j, i, size).apply {
              selectedEvent = { this@SudokuBox.selectedEvent?.invoke(it) }
            }
      }
    }
  }
}
