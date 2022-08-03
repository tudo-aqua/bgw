/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

package tools.aqua.bgw.layoutelements.grid

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Alignment

/** Test base for Grid. */
open class GridPaneTestBase {

  /** Alignments in grid. */
  private lateinit var alignments: Array<Array<Alignment>>

  /** 3 labels. */
  private val labels: Array<Label> = Array(3) { Label() }

  /** 3 buttons. */
  private val buttons: Array<Button> = Array(3) { Button() }

  /** 3 colorPickers. */
  private val colorPickers: Array<ColorPicker> = Array(3) { ColorPicker() }

  /** The grid. */
  protected lateinit var grid: GridPane<UIComponent>

  /** Initial grid contents. */
  protected val contents: Array<Array<out UIComponent>> = arrayOf(labels, buttons, colorPickers)

  /** Initializes grid before each test. */
  @BeforeEach
  fun setUp() {
    grid = GridPane(rows = 3, columns = 3)
    alignments = Array(3) { Array(3) { Alignment.CENTER } }
    alignments[0][0] = Alignment.CENTER_LEFT
    alignments[0][1] = Alignment.BOTTOM_RIGHT
    alignments[1][1] = Alignment.TOP_RIGHT
    alignments[1][2] = Alignment.CENTER_RIGHT

    for (i in 0..2) {
      for (j in 0..2) {
        grid[i, j] = contents[i][j]
        grid.setCellCenterMode(i, j, alignments[i][j])
      }
    }
  }

  /**
   * Asserts given column and row ranges with given offsets to contain the initial values.
   *
   * @param columns Columns to be checked.
   * @param rows Rows to be checked.
   * @param columnBias Column offset when looking up initial values.
   * @param rowBias Row offset when looking up initial values.
   */
  protected fun testUnchanged(
      columns: IntRange = 0..2,
      rows: IntRange = 0..2,
      columnBias: Int = 0,
      rowBias: Int = 0
  ) {
    for (i in columns) {
      for (j in rows) {
        assertEquals(contents[i + columnBias][j + rowBias], grid[i, j])
        assertEquals(alignments[i + columnBias][j + rowBias], grid.getCellCenterMode(i, j))
      }
    }
  }

  /**
   * Asserts given column and row range to be all null.
   *
   * @param columns Columns to be checked.
   * @param rows Rows to be checked.
   */
  protected fun testNull(columns: IntRange = 0..2, rows: IntRange = 0..2) {
    for (i in columns) {
      for (j in rows) {
        assertNull(grid[i, j])
      }
    }
  }

  /**
   * Asserts grid dimensions to equal given row and column count.
   *
   * @param columns Expected column count.
   * @param rows Expected column count.
   *
   * @return 'true' iff [columns] == #columns && [rows] == #rows.
   */
  protected fun checkSize(columns: Int = 3, rows: Int = 3) {
    // Check rows and columns size
    assertEquals(columns, grid.columns)
    assertEquals(rows, grid.rows)

    // Check rowHeights Array
    for (i in 0 until rows) {
      grid.getRowHeight(i)
    }
    assertThrows(IllegalArgumentException::class.java) { grid.getRowHeight(rows) }

    // Check columnWidths Array
    for (i in 0 until columns) {
      grid.getColumnWidth(i)
    }
    assertThrows(IllegalArgumentException::class.java) { grid.getColumnWidth(columns) }

    // Check cellCenterMode Array
    for (i in 0 until rows) {
      for (j in 0 until columns) {
        grid.getCellCenterMode(rowIndex = i, columnIndex = j)
      }
    }
    assertThrows(IllegalArgumentException::class.java) {
      grid.getCellCenterMode(rowIndex = 0, columnIndex = columns)
    }
    assertThrows(IllegalArgumentException::class.java) {
      grid.getCellCenterMode(rowIndex = rows, columnIndex = 0)
    }
    assertThrows(IllegalArgumentException::class.java) {
      grid.getCellCenterMode(rowIndex = rows, columnIndex = columns)
    }
  }
}
