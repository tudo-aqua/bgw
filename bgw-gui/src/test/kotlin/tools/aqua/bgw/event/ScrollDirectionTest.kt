/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.event.KeyCode.*

/** Tests for [KeyCode]. */
class ScrollDirectionTest {

  /** Tests multiply. */
  @Test
  @DisplayName("Test multiply")
  fun testMultiply() {
    assertEquals(2.0, ScrollDirection.UP * 2)
    assertEquals(-2.0, ScrollDirection.DOWN * 2)
  }

  /** Tests of. */
  @Test
  @DisplayName("Test of")
  fun testOf() {
    assertEquals(ScrollDirection.UP, ScrollDirection.of(2))
    assertEquals(ScrollDirection.UP, ScrollDirection.of(0))
    assertEquals(ScrollDirection.DOWN, ScrollDirection.of(-2))
  }
}
