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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RemoveTest : BidirectionalMapTestBase() {
  @Test
  @DisplayName("Test remove of existing relation")
  fun testRemoveExistingRelation() {
    assertTrue(map.remove(0, 1))
    assertFalse(map.contains(0, 1))
    assertEquals(1, map.size)
  }

  @Test
  @DisplayName("Test remove of relation with existing key and non-existing value")
  fun testRemoveRelationExistingKey() {
    assertFalse(map.remove(0, 5))
    assertTrue(map.contains(0, 1))
    assertEquals(2, map.size)
  }

  @Test
  @DisplayName("Test remove of relation with non-existing key and existing value")
  fun testRemoveRelationExistingValue() {
    assertFalse(map.remove(5, 1))
    assertTrue(map.contains(0, 1))
    assertEquals(2, map.size)
  }

  @Test
  @DisplayName("Test remove forward on existing key")
  fun testRemoveForwardExisting() {
    assertTrue(map.removeForward(0))
    assertFalse(map.contains(0, 1))
    assertEquals(1, map.size)
  }

  @Test
  @DisplayName("Test remove forward on non-existing key")
  fun testRemoveForwardNonExisting() {
    assertFalse(map.removeForward(5))
    assertEquals(2, map.size)
  }

  @Test
  @DisplayName("Test remove backward on existing value")
  fun testRemoveBackwardExisting() {
    assertTrue(map.removeBackward(1))
    assertFalse(map.contains(0, 1))
    assertEquals(1, map.size)
  }

  @Test
  @DisplayName("Test remove backward on non-existing value")
  fun testRemoveBackwardNonExisting() {
    assertFalse(map.removeBackward(5))
    assertEquals(2, map.size)
  }

  @Test
  @DisplayName("Test clear")
  fun testClear() {
    map.clear()
    assertEquals(0, map.size)
  }

  @Test
  @DisplayName("Test isEmpty and isNotEmpty")
  fun testIsEmpty() {
    assertFalse(map.isEmpty())
    assertTrue(map.isNotEmpty())

    map.clear()

    assertTrue(map.isEmpty())
    assertFalse(map.isNotEmpty())
  }
}
