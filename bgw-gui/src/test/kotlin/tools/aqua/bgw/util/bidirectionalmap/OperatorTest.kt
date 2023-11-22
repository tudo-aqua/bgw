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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Test set and get function in BidirectionalMap. */
class OperatorTest : BidirectionalMapTestBase() {
  /** Test set a new pair. */
  @Test
  @DisplayName("Test set new pair")
  fun testSetNew() {
    assertEquals(2, map.size)
    map[4] = 5
    assertEquals(3, map.size)
    assertTrue(map.contains(4, 5))
    assertTrue(map.containsBackward(5))
    assertTrue(map.containsForward(4))
  }

  /** Test set already existing domain key. */
  @Test
  @DisplayName("Test set already existing domain key")
  fun testSetAlreadyExistingDomainKey() {
    assertTrue(map.containsForward(0))
    assertEquals(2, map.size)

    map[0] = 5

    assertEquals(2, map.size)
    assertTrue(map.contains(0, 5))
    assertTrue(map.containsBackward(5))
    assertTrue(map.containsForward(0))
  }

  /** Test set already existing codomain key. */
  @Test
  @DisplayName("Test set already existing codomain key")
  fun testSetAlreadyExistingCodomainKey() {
    assertTrue(map.containsBackward(1))
    assertEquals(2, map.size)

    map[4] = 1

    assertEquals(2, map.size)
    assertTrue(map.contains(4, 1))
    assertTrue(map.containsBackward(1))
    assertTrue(map.containsForward(4))
  }

  /** Test get a key. */
  @Test
  @DisplayName("Test get a key")
  fun testGetKey() {
    assertEquals(1, map[0])
  }
}
