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

package tools.aqua.bgw.core.alignment

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment

/** Test [Alignment] conversion into [HorizontalAlignment] and [VerticalAlignment]. */
class AlignmentConversionTest {

  /** Test the horizontalAlignment field of Alignment. */
  @Test
  @DisplayName("Test the horizontalAlignment field of Alignment")
  fun toHorizontal() {
    assertEquals(HorizontalAlignment.LEFT, Alignment.TOP_LEFT.horizontalAlignment)
    assertEquals(HorizontalAlignment.RIGHT, Alignment.TOP_RIGHT.horizontalAlignment)
    assertEquals(HorizontalAlignment.CENTER, Alignment.TOP_CENTER.horizontalAlignment)
    assertEquals(HorizontalAlignment.LEFT, Alignment.BOTTOM_LEFT.horizontalAlignment)
    assertEquals(HorizontalAlignment.RIGHT, Alignment.BOTTOM_RIGHT.horizontalAlignment)
    assertEquals(HorizontalAlignment.CENTER, Alignment.BOTTOM_CENTER.horizontalAlignment)
    assertEquals(HorizontalAlignment.LEFT, Alignment.CENTER_LEFT.horizontalAlignment)
    assertEquals(HorizontalAlignment.RIGHT, Alignment.CENTER_RIGHT.horizontalAlignment)
    assertEquals(HorizontalAlignment.CENTER, Alignment.CENTER.horizontalAlignment)
  }

  /** Test the verticalAlignment field of Alignment. */
  @Test
  @DisplayName("Test the verticalAlignment field of Alignment")
  fun toVertical() {
    assertEquals(VerticalAlignment.TOP, Alignment.TOP_LEFT.verticalAlignment)
    assertEquals(VerticalAlignment.TOP, Alignment.TOP_RIGHT.verticalAlignment)
    assertEquals(VerticalAlignment.TOP, Alignment.TOP_CENTER.verticalAlignment)
    assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_LEFT.verticalAlignment)
    assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_RIGHT.verticalAlignment)
    assertEquals(VerticalAlignment.BOTTOM, Alignment.BOTTOM_CENTER.verticalAlignment)
    assertEquals(VerticalAlignment.CENTER, Alignment.CENTER_LEFT.verticalAlignment)
    assertEquals(VerticalAlignment.CENTER, Alignment.CENTER_RIGHT.verticalAlignment)
    assertEquals(VerticalAlignment.CENTER, Alignment.CENTER.verticalAlignment)
  }
}
