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

package tools.aqua.bgw.observable.property

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty

/** Test [LimitedDoubleProperty] functions. */
class LimitedDoublePropertyTest : PropertyTestBase() {

  /** Test upperBound < lowerBound. */
  @Test
  @DisplayName("Test upperBound < lowerBound")
  fun testBoundsWrongOrderEqual() {
    assertThrows<IllegalArgumentException> { LimitedDoubleProperty(10, 5, 0) }
  }

  /** Test upperBound = lowerBound. */
  @Test
  @DisplayName("Test upperBound = lowerBound")
  fun testBoundsEqual() {
    val newProperty = LimitedDoubleProperty(10, 10, 10)
    newProperty.value = 10.0
  }

  /** Test initial value out of bounds. */
  @Test
  @DisplayName("Test initial value out of bounds")
  fun testInitialValueOutOfBounds() {
    assertThrows<IllegalArgumentException> { LimitedDoubleProperty(0, 10, 15) }
  }

  /** Notify Unchanged invoking all listener. */
  @Test
  @DisplayName("Notify Unchanged invoking all listener")
  fun testSetOutOfBounds() {
    assertThrows<IllegalArgumentException> { property.value = -7.0 }
  }
}
