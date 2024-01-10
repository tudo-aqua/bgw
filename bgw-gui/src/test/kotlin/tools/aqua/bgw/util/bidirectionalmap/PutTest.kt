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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.util.BidirectionalMap

/** Test put and putAll function in BidirectionalMap. */
class PutTest : BidirectionalMapTestBase() {

  /** Test put a new pair. */
  @Test
  @DisplayName("Test put new pair")
  fun testPutNew() {
    assertEquals(2, map.size)
    map.put(4, 5)
    assertEquals(3, map.size)
    assertTrue(map.contains(4, 5))
    assertTrue(map.containsBackward(5))
    assertTrue(map.containsForward(4))
  }

  /** Test put already existing domain key. */
  @Test
  @DisplayName("Test put already existing domain key")
  fun testPutAlreadyExistingDomainKey() {
    assertTrue(map.containsForward(0))
    assertEquals(2, map.size)

    map.put(0, 5)

    assertEquals(2, map.size)
    assertTrue(map.contains(0, 5))
    assertTrue(map.containsBackward(5))
    assertTrue(map.containsForward(0))
  }

  /** Test put already existing codomain key. */
  @Test
  @DisplayName("Test put already existing codomain key")
  fun testPutAlreadyExistingCodomainKey() {
    assertTrue(map.containsBackward(1))
    assertEquals(2, map.size)

    map.put(4, 1)

    assertEquals(2, map.size)
    assertTrue(map.contains(4, 1))
    assertTrue(map.containsBackward(1))
    assertTrue(map.containsForward(4))
  }

  /** Test putAll with varargs of new pairs. */
  @Test
  @DisplayName("Test putAll with varargs of new pairs")
  fun testPutAllListOfPairs() {
    assertEquals(2, map.size)
    val listOfPairs = listOf(Pair(4, 5), Pair(6, 7))

    assertFalse(map.contains(4, 5))
    assertFalse(map.contains(6, 7))
    assertFalse(map.containsBackward(5))
    assertFalse(map.containsBackward(7))
    assertFalse(map.containsForward(4))
    assertFalse(map.containsForward(6))

    map.putAll(*listOfPairs.toTypedArray())

    assertEquals(4, map.size)

    assertTrue(map.contains(4, 5))
    assertTrue(map.contains(6, 7))
  }

  /** Test putAll with varargs of new and old pairs. */
  @Test
  @DisplayName("Test putAll with varargs of new and old pairs")
  fun testPutAllListOfPairsWithOld() {
    assertEquals(2, map.size)
    val listOfPairs = listOf(Pair(0, 5), Pair(6, 7), Pair(8, 3))

    assertTrue(map.contains(0, 1))
    assertFalse(map.contains(6, 7))
    assertFalse(map.contains(8, 3))
    assertFalse(map.containsBackward(5))
    assertFalse(map.containsBackward(7))
    assertFalse(map.containsForward(6))
    assertFalse(map.containsForward(8))
    assertTrue(map.containsBackward(3))

    map.putAll(*listOfPairs.toTypedArray())
    assertEquals(3, map.size)

    assertTrue(map.contains(0, 5))
    assertTrue(map.contains(6, 7))
    assertTrue(map.contains(8, 3))
  }

  /** Test putAll with [BidirectionalMap] of new pairs. */
  @Test
  @DisplayName("Test putAll with map of new pairs")
  fun testPutAllMapOfPairs() {
    assertEquals(2, map.size)
    val mapOfPairs = BidirectionalMap(Pair(4, 5), Pair(6, 7))

    assertFalse(map.contains(4, 5))
    assertFalse(map.contains(6, 7))
    assertFalse(map.containsBackward(5))
    assertFalse(map.containsBackward(7))
    assertFalse(map.containsForward(4))
    assertFalse(map.containsForward(6))

    map.putAll(mapOfPairs)

    assertEquals(4, map.size)

    assertTrue(map.contains(4, 5))
    assertTrue(map.contains(6, 7))
  }

  /** Test putAll with [BidirectionalMap] of new and old pairs. */
  @Test
  @DisplayName("Test putAll with map of new and old pairs")
  fun testPutAllMapOfPairsWithOld() {
    assertEquals(2, map.size)
    val mapOfPairs = BidirectionalMap(Pair(0, 5), Pair(6, 7), Pair(8, 3))

    assertTrue(map.contains(0, 1))
    assertFalse(map.contains(6, 7))
    assertFalse(map.contains(8, 3))
    assertFalse(map.containsBackward(5))
    assertFalse(map.containsBackward(7))
    assertFalse(map.containsForward(6))
    assertFalse(map.containsForward(8))
    assertTrue(map.containsBackward(3))

    map.putAll(mapOfPairs)
    assertEquals(3, map.size)

    assertTrue(map.contains(0, 5))
    assertTrue(map.contains(6, 7))
    assertTrue(map.contains(8, 3))
  }
}
