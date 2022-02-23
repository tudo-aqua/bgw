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

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class AddColumnTest : GridPaneTestBase() {

  @Test
  @DisplayName("Add column at start")
  fun testAddColumnAtStart() {
    grid.addColumns(0, 1)

    // First column must be empty
    testNull(columns = 0..0)

    // Column 1-3 must contain former columns 0-2
    testUnchanged(columns = 1..3, columnBias = -1)

    // Grid size should be updated
    checkSize(4, 3)
  }

  @Test
  @DisplayName("Add column at end")
  fun testAddColumnAtEnd() {
    grid.addColumns(3, 1)

    // Columns 0-2 unchanged
    testUnchanged()

    // Last column must be empty
    testNull(columns = 3..3)

    // Grid size should be updated
    checkSize(4, 3)
  }

  @Test
  @DisplayName("Add multiple columns in middle")
  fun testAddMultipleColumnsInMiddle() {
    grid.addColumns(2, 3)

    // Column 0-1 unchanged
    testUnchanged(columns = 0..1)

    // Columns 2-4 must be empty
    testNull(columns = 2..4)

    // Column 5 contains former column 2
    testUnchanged(columns = 5..5, columnBias = -3)

    checkSize(6, 3)
  }
}
