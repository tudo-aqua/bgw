/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Test addRow function on Grid. */
class AddRowTest : GridPaneTestBase() {

  /** Add row at start. */
  @Test
  @DisplayName("Add row at start")
  fun testAddRowAtStart() {
    grid.addRows(0)

    // First row must be empty
    testNull(rows = 0..0)

    // Rows 1-3 must contain former rows 0-2
    testUnchanged(rows = 1..3, rowBias = -1)

    // Grid size should be updated
    checkSize(3, 4)
  }

  /** Add row at end. */
  @Test
  @DisplayName("Add row at end")
  fun testAddRowAtEnd() {
    grid.addRows(3)

    // Rows 0-2 unchanged
    testUnchanged()

    // Last row must be empty
    testNull(rows = 3..3)

    // Grid size should be updated
    checkSize(3, 4)
  }

  /** Add multiple rows in middle. */
  @Test
  @DisplayName("Add multiple rows in middle")
  fun testAddMultipleRowsInMiddle() {
    grid.addRows(2, 3)

    // Row 0-1 unchanged
    testUnchanged(rows = 0..1)

    // Rows 2-4 must be empty
    testNull(rows = 2..4)

    // Row 5 contains former row 2
    testUnchanged(rows = 5..5, rowBias = -3)

    checkSize(3, 6)
  }

  /** Add row out of bounds on the top. */
  @Test
  @DisplayName("Add row out of bounds on the top")
  fun testAddRowOutOfBoundsTop() {
    assertThrows<IllegalArgumentException> { grid.addRows(-1) }

    // Grid size should not be updated
    checkSize(3, 3)
  }

  /** Add row out of bounds on the bottom. */
  @Test
  @DisplayName("Add row out of bounds on the bottom")
  fun testAddRowOutOfBoundsBottom() {
    assertThrows<IllegalArgumentException> { grid.addRows(4) }

    // Grid size should not be updated
    checkSize(3, 3)
  }
}
