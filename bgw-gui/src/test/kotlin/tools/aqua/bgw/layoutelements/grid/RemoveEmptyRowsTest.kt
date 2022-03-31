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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Test remove empty rows function in Grid. */
class RemoveEmptyRowsTest : GridPaneTestBase() {

  /** Remove empty rows from full grid. */
  @Test
  @DisplayName("Remove empty rows from full grid")
  fun testRemoveEmptyRowsOnFullGrid() {
    grid.removeEmptyRows()

    // Grid unchanged
    checkSize()
    testUnchanged()
  }

  /** Remove empty rows from partially full grid. */
  @Test
  @DisplayName("Remove empty rows from partially full grid")
  fun testRemoveEmptyRowsOnPartiallyFullGrid() {
    grid[1, 2] = null
    grid[2, 2] = null
    grid.removeEmptyRows()

    checkSize()

    // Row 0-1 unchanged
    testUnchanged(rows = 0..1)

    // Row 2 unchanged
    assertEquals(contents[0][2], grid[0, 2])
    assertEquals(null, grid[1, 2])
    assertEquals(null, grid[2, 2])
  }

  /** Remove empty first row. */
  @Test
  @DisplayName("Remove empty first row")
  fun testRemoveEmptyFirstRow() {
    grid[0, 0] = null
    grid[1, 0] = null
    grid[2, 0] = null
    grid.removeEmptyRows()

    checkSize(3, 2)

    // Rows 0-1 contain former rows 1-2
    testUnchanged(rows = 0..1, rowBias = 1)
  }

  /** Remove empty last row. */
  @Test
  @DisplayName("Remove empty last row")
  fun testRemoveEmptyLastRow() {
    grid[0, 2] = null
    grid[1, 2] = null
    grid[2, 2] = null
    grid.removeEmptyRows()

    checkSize(3, 2)

    // Rows 0-1 unchanged
    testUnchanged(rows = 0..1)
  }

  /** Remove empty middle row. */
  @Test
  @DisplayName("Remove empty middle row")
  fun testRemoveEmptyMiddleRow() {
    grid[0, 1] = null
    grid[1, 1] = null
    grid[2, 1] = null
    grid.removeEmptyRows()

    checkSize(3, 2)

    // Row 0 unchanged
    testUnchanged(rows = 0..0)

    // Row 1 contains former row 2
    testUnchanged(rows = 1..1, rowBias = 1)
  }

  /** Remove empty rows from empty grid. */
  @Test
  @DisplayName("Remove empty rows from empty grid")
  fun testRemoveEmptyRowsFromEmptyGrid() {
    for (i in 0..2) {
      for (j in 0..2) {
        grid[i, j] = null
      }
    }

    grid.removeEmptyRows()

    checkSize(0, 0)
  }
}
