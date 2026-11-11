/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.util

import kotlin.test.Test
import kotlin.test.assertContentEquals
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.visual.Visual

class ComponentViewGridTest {

  @Test
  fun autoSizesOfColumnsWithoutRowsAreZero() {
    val grid = GridPane<TokenView>(columns = 3, rows = 0)

    assertContentEquals(doubleArrayOf(0.0, 0.0, 0.0), grid.grid.getColumnWidths())
    assertContentEquals(doubleArrayOf(), grid.grid.getRowHeights())
  }

  @Test
  fun autoSizesOfRowsWithoutColumnsAreZero() {
    val grid = GridPane<TokenView>(columns = 0, rows = 2)

    assertContentEquals(doubleArrayOf(), grid.grid.getColumnWidths())
    assertContentEquals(doubleArrayOf(0.0, 0.0), grid.grid.getRowHeights())
  }

  @Test
  fun removingAllRowsAndAddingColumnsKeepsSizesComputable() {
    val grid = GridPane<TokenView>(columns = 2, rows = 1)
    grid[0, 0] = TokenView(width = 40, height = 30, visual = Visual.EMPTY)

    grid.removeRow(0)
    grid.addColumns(0, 2)

    assertContentEquals(doubleArrayOf(0.0, 0.0), grid.grid.getColumnWidths())
  }

  @Test
  fun autoSizesUseLargestComponent() {
    val grid = GridPane<TokenView>(columns = 2, rows = 2)
    grid[0, 0] = TokenView(width = 40, height = 30, visual = Visual.EMPTY)
    grid[0, 1] = TokenView(width = 60, height = 10, visual = Visual.EMPTY)

    assertContentEquals(doubleArrayOf(60.0, 0.0), grid.grid.getColumnWidths())
    assertContentEquals(doubleArrayOf(30.0, 10.0), grid.grid.getRowHeights())
  }
}
