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

package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.examples.sudoku.entity.Sudoku

interface Refreshable {
  /**
   * Refresh to init new game.
   *
   * @param initialSudoku New sudoku
   */
  fun refreshInit(initialSudoku: Sudoku)

  /**
   * Refresh to set a digit.
   *
   * @param box Box index.
   * @param row Row index.
   * @param col Column index.
   * @param value Value to set or `null` to clear cell.
   */
  fun refreshSetValue(box: Int, row: Int, col: Int, value: Int? = null)

  /**
   * Refresh to show error hints.
   *
   * @param errors Errors to show in grid.
   */
  fun refreshHint(errors: Collection<Sudoku.SudokuTuple>)

  /**
   * Refresh to update timer label.
   *
   * @param time New time.
   */
  fun refreshTimer(time: String)

  /** Refresh to show that game is finished. */
  fun refreshWon()
}
