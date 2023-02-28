/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

/** Test removeRow function on Grid. */
class RemoveRowTest : GridPaneTestBase() {

  /** Remove row at start. */
  @Test
  @DisplayName("Remove row at start")
  fun testRemoveRowAtStart() {
    grid.removeRow(0)

    // Row 0-1 must contain former rows 1-2
    testUnchanged(rows = 0..1, rowBias = 1)

    // Grid size should be updated
    checkSize(3, 2)
  }

  /** Remove row at end. */
  @Test
  @DisplayName("Remove row at end")
  fun testRemoveRowAtEnd() {
    grid.removeRow(2)

    // Row 0-1 must remain unchanged
    testUnchanged(rows = 0..1)

    // Grid size should be updated
    checkSize(3, 2)
  }

  /** Remove row in middle. */
  @Test
  @DisplayName("Remove row in middle")
  fun testRemoveRowInMiddle() {
    grid.removeRow(1)

    // Row 0 unchanged
    testUnchanged(rows = 0..0)

    // Row 1 contains former row 2
    testUnchanged(rows = 1..1, rowBias = 1)

    checkSize(3, 2)
  }

  /** Trim on last row removed. */
  @Test
  @DisplayName("Trim on last row removed")
  fun testTrimOnRemoveAllRows() {
    grid.removeRow(0)
    grid.removeRow(0)
    grid.removeRow(0)

    checkSize(0, 0)
  }
}
