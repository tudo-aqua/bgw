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

/** Test removeColumn function on Grid. */
class RemoveColumnTest : GridPaneTestBase() {

  /** Remove column at start. */
  @Test
  @DisplayName("Remove column at start")
  fun testRemoveColumnAtStart() {
    grid.removeColumn(0)

    // Column 0-1 must contain former columns 1-2
    testUnchanged(columns = 0..1, columnBias = 1)

    // Grid size should be updated
    checkSize(2, 3)
  }

  /** Remove column at end. */
  @Test
  @DisplayName("Remove column at end")
  fun testRemoveColumnAtEnd() {
    grid.removeColumn(2)

    // Column 0-1 must remain unchanged
    testUnchanged(columns = 0..1)

    // Grid size should be updated
    checkSize(2, 3)
  }

  /** Remove column in middle. */
  @Test
  @DisplayName("Remove column in middle")
  fun testRemoveColumnInMiddle() {
    grid.removeColumn(1)

    // Column 0 unchanged
    testUnchanged(columns = 0..0)

    // Column 1 contains former column 2
    testUnchanged(columns = 1..1, columnBias = 1)

    checkSize(2, 3)
  }

  /** Trim on last column removed. */
  @Test
  @DisplayName("Trim on last column removed")
  fun testTrimOnRemoveAllColumns() {
    grid.removeColumn(0)
    grid.removeColumn(0)
    grid.removeColumn(0)

    checkSize(0, 0)
  }
}
