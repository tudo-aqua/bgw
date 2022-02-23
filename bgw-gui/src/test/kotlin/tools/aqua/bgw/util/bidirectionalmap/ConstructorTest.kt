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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.util.BidirectionalMap

class ConstructorTest : BidirectionalMapTestBase() {
  @Test
  @DisplayName("Test empty constructor")
  fun testEmptyConstructor() {
    val lMap = BidirectionalMap<Int, Int>()

    assertTrue(lMap.isEmpty())
  }

  @Test
  @DisplayName("Test constructor with elements")
  fun testConstructorWithElements() {
    val item1 = Pair(0, 1)
    val item2 = Pair(2, 3)
    val lMap = BidirectionalMap(item1, item2)

    assertEquals(2, lMap.size)
    assertTrue(lMap.contains(item1.first, item1.second))
    assertTrue(lMap.contains(item2.first, item2.second))
  }

  @Test
  @DisplayName("Test constructor with duplicate key")
  fun testConstructorWithDuplicateKey() {
    val item1 = Pair(0, 1)
    val item2 = Pair(0, 2)

    assertThrows<IllegalArgumentException> { BidirectionalMap(item1, item2) }
  }

  @Test
  @DisplayName("Test constructor with duplicate value")
  fun testConstructorWithDuplicateValue() {
    val item1 = Pair(0, 1)
    val item2 = Pair(1, 1)

    assertThrows<IllegalArgumentException> { BidirectionalMap(item1, item2) }
  }

  @Test
  @DisplayName("Test constructor with duplicate pairs")
  fun testConstructorWithDuplicatePairs() {
    val item1 = Pair(0, 1)
    val item2 = Pair(0, 1)

    val lMap = BidirectionalMap(item1, item2)

    assertEquals(1, lMap.size)
    assertTrue(lMap.contains(item1))
  }
}
