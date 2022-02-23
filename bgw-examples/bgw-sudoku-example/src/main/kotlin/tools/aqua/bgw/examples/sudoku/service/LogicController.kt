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

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.streams.toList
import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.entity.Settings
import tools.aqua.bgw.examples.sudoku.entity.Sudoku
import tools.aqua.bgw.examples.sudoku.service.SudokuChecker.Companion.checkSudoku
import tools.aqua.bgw.examples.sudoku.view.Refreshable

/** Controller managing game actions. */
class LogicController(private val view: Refreshable) {

  /** Settings instance. */
  val settings: Settings = Settings()

  /** current sudoku instance. */
  var sudoku: Sudoku = Sudoku()

  /** Timer started time in millis. */
  private var startTime: Long = 0L

  /** Whether the timer is running. */
  private var timerRunning: Boolean = false

  init {
    Timer().apply {
      scheduleAtFixedRate(delay = 0, period = 100) {
        if (timerRunning) {
          val millis = System.currentTimeMillis() - startTime

          view.refreshTimer(
              String.format(
                  "%02d:%02d:%02d",
                  TimeUnit.MILLISECONDS.toHours(millis),
                  TimeUnit.MILLISECONDS.toMinutes(millis) -
                      TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                  TimeUnit.MILLISECONDS.toSeconds(millis) -
                      TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))))
        }
      }
    }
  }

  /**
   * Creates a new game for given difficulty.
   *
   * @param difficulty Selected difficulty.
   */
  fun newGame(difficulty: Difficulty) {
    sudoku = getRandomSudoku(difficulty)
    startTime = System.currentTimeMillis()
    timerRunning = true

    settings.currentDifficulty = difficulty
    view.refreshInit(sudoku)
  }

  /**
   * Returns a random sudoku for given difficulty.
   *
   * @param difficulty Selected difficulty.
   */
  private fun getRandomSudoku(difficulty: Difficulty): Sudoku {
    val formatted = Array(9) { Array(3) { Array(3) { Sudoku.SudokuCell() } } }
    val sudokuInput =
        checkNotNull(
            javaClass
                .classLoader
                .getResourceAsStream(difficulty.file)
                ?.bufferedReader()
                ?.lines()
                ?.toList()
                ?.randomOrNull())

    check(sudokuInput.length == 81)

    sudokuInput.toCharArray().forEachIndexed { index, token ->
      val box = index / 9
      val row = index % 9 / 3
      val col = index % 3
      val value = token.digitToInt()

      if (value != 0) formatted[box][row][col] = Sudoku.SudokuCell(value = value, isFixed = true)
    }

    return Sudoku(formatted)
  }

  /**
   * Sets a new value for the selected cell.
   *
   * @param box Box index.
   * @param row Row index.
   * @param col Column index.
   * @param value Value to set or `null` to clear cell.
   */
  fun setValue(box: Int, row: Int, col: Int, value: Int? = null) {
    if (sudoku.grid[box][row][col].isFixed) return

    sudoku[box, row, col] = value
    view.refreshSetValue(box, row, col, value)

    showErrors()
  }

  /** Checks for errors in current sudoku and refreshes GUI. */
  fun showErrors() {
    val isFull = SudokuChecker.checkFull(sudoku)
    if (isFull || settings.instantCheck.value) {
      val errors = checkSudoku(sudoku)

      if (errors.isNotEmpty()) view.refreshHint(errors) else if (isFull) view.refreshWon()
    }
  }

  /** Calculates and shows error hints. */
  fun requestHint() {
    view.refreshHint(checkSudoku(sudoku))
  }
}
