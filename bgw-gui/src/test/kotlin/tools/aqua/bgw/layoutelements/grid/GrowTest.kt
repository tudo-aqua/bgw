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
import org.junit.jupiter.api.assertThrows

/** Test grow function on Grid. */
class GrowTest : GridPaneTestBase() {

  /** Grow to the left. */
  @Test
  @DisplayName("Grow to the left")
  fun testGrowLeft() {
    grid.grow(2, 0, 0, 0)

    checkSize(5, 3)

    testUnchanged(columns = 2..4, columnBias = -2)
  }

  /** Grow to the right. */
  @Test
  @DisplayName("Grow to the right")
  fun testGrowRight() {
    grid.grow(0, 2, 0, 0)

    checkSize(5, 3)

    testUnchanged()
  }

  /** Grow on the top. */
  @Test
  @DisplayName("Grow on the top")
  fun testGrowTop() {
    grid.grow(0, 0, 2, 0)

    checkSize(3, 5)

    testUnchanged(rows = 2..4, rowBias = -2)
  }

  /** Grow on the bottom. */
  @Test
  @DisplayName("Grow on the bottom")
  fun testGrowBottom() {
    grid.grow(0, 0, 0, 2)

    checkSize(3, 5)

    testUnchanged()
  }

  /** Grow on all sides. */
  @Test
  @DisplayName("Grow on all sides")
  fun testGrowAllSides() {
    grid.grow(1, 1, 1, 1)

    checkSize(5, 5)

    testUnchanged(columns = 1..3, rows = 1..3, columnBias = -1, rowBias = -1)
  }

  /** Don't grow by passing 0. */
  @Test
  @DisplayName("Don't grow by passing 0")
  fun testDontGrow() {
    grid.grow(0, 0, 0, 0)

    checkSize()
    testUnchanged()

    grid.grow()

    checkSize()
    testUnchanged()
  }

  /** Don't grow to the left for negative parameter. */
  @Test
  @DisplayName("Don't grow to the left for negative parameter")
  fun testDontGrowLeftForNegativeParameter() {
    assertThrows<IllegalArgumentException> { grid.grow(-1, 0, 0, 0) }

    checkSize()
    testUnchanged()
  }

  /** Don't grow to the right for negative parameter. */
  @Test
  @DisplayName("Don't grow to the right for negative parameter")
  fun testDontGrowRightForNegativeParameter() {
    assertThrows<IllegalArgumentException> { grid.grow(0, -1, 0, 0) }

    checkSize()
    testUnchanged()
  }

  /** Don't grow on the top for negative parameter. */
  @Test
  @DisplayName("Don't grow on the top for negative parameter")
  fun testDontGrowTopForNegativeParameter() {
    assertThrows<IllegalArgumentException> { grid.grow(0, 0, -1, 0) }

    checkSize()
    testUnchanged()
  }

  /** Don't grow on the bottom for negative parameter. */
  @Test
  @DisplayName("Don't grow on the bottom for negative parameter")
  fun testDontGrowBottomForNegativeParameter() {
    assertThrows<IllegalArgumentException> { grid.grow(0, 0, 0, -1) }

    checkSize()
    testUnchanged()
  }
}
