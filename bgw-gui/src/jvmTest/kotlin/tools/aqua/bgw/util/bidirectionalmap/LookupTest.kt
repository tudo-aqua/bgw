/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Test lookup function in BidirectionalMap. */
class LookupTest : BidirectionalMapTestBase() {

  /** Test forward lookup on existing element. */
  @Test
  @DisplayName("Test forward lookup on existing element")
  fun testForwardExisting() {
    assertEquals(1, map.forward(0))
    assertEquals(1, map.forwardOrNull(0))
  }

  /** Test backward lookup on existing element. */
  @Test
  @DisplayName("Test backward lookup on existing element")
  fun testBackwardExisting() {
    assertEquals(0, map.backward(1))
    assertEquals(0, map.backwardOrNull(1))
  }

  /** Test forward lookup on non-existing element. */
  @Test
  @DisplayName("Test forward lookup on non-existing element")
  fun testForwardNonExisting() {
    assertThrows<NoSuchElementException> { map.forward(5) }
    assertEquals(null, map.forwardOrNull(5))
  }

  /** Test backward lookup on non-existing element. */
  @Test
  @DisplayName("Test backward lookup on non-existing element")
  fun testBackwardNonExisting() {
    assertThrows<NoSuchElementException> { map.backward(5) }
    assertEquals(null, map.backwardOrNull(5))
  }

  /** Test keysForward of map. */
  @Test
  @DisplayName("Test keysForward of map")
  fun testKeysForward() {
    val keys = map.keysForward
    assertEquals(2, keys.size)
    assertTrue(keys.contains(0))
    assertTrue(keys.contains(2))
  }

  /** Test keysBackward of map. */
  @Test
  @DisplayName("Test keysBackward of map")
  fun testKeysBackward() {
    val keys = map.keysBackward
    assertEquals(2, keys.size)
    assertTrue(keys.contains(1))
    assertTrue(keys.contains(3))
  }
}
