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

package tools.aqua.bgw.observable.observablelist

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Test add function in ObservableList. */
class AddTest : ObservableListTestBase() {

  /** Test add. */
  @Test
  @DisplayName("Test add")
  fun testAdd() {
    assertEquals(5, list.size)

    list.add(42)

    assertEquals(6, list.size)

    checkListDeepEquals(list, unordered.toMutableList().apply { add(42) })

    assertEquals(42, list[5])

    checkNotified()
  }

  /** Test add empty list. */
  @Test
  @DisplayName("Test add empty list")
  fun testAddEmptyList() {
    assertEquals(0, emptyList.size)

    emptyList.add(42)

    assertEquals(1, emptyList.size)
    assertEquals(42, emptyList[0])

    checkNotified()
  }

  /** Test add at index 0. */
  @Test
  @DisplayName("Test add at index 0")
  fun testAddIndexAtStart() {
    assertEquals(5, list.size)

    list.add(0, 42)

    assertEquals(6, list.size)

    for (i in 0 until list.size - 1) assertEquals(unordered[i], list[i + 1])
    assertEquals(42, list[0])

    checkNotified()
  }

  /** Test add at last index. */
  @Test
  @DisplayName("Test add at last index")
  fun testAddIndexAtEnd() {
    assertEquals(5, list.size)

    list.add(5, 42)

    assertEquals(6, list.size)

    checkListDeepEquals(list, unordered.toMutableList().apply { add(42) })

    checkNotified()
  }

  /** Test add at index out of bounds. */
  @Test
  @DisplayName("Test add at index out of bounds")
  fun testAddIndexOutOfBounds() {
    assertThrows<IndexOutOfBoundsException> { list.add(-1, 42) }

    assertThrows<IndexOutOfBoundsException> { list.add(6, 42) }
  }

  /** Test add all. */
  @Test
  @DisplayName("Test add all")
  fun testAddAll() {
    // 13,25,17,13,-4
    val resultList = listOf(13, 25, 17, 13, -4, -1, -2)

    assertEquals(5, list.size)

    list.addAll(listOf(-1, -2))

    assertEquals(7, list.size)

    checkListDeepEquals(list, resultList)

    checkNotified()
  }

  /** Test add all at index. */
  @Test
  @DisplayName("Test add all at index")
  fun testAddAllAtIndex() {
    // 13,25,17,13,-4
    val resultList = listOf(13, 25, -1, -2, 17, 13, -4)

    assertEquals(5, list.size)

    list.addAll(2, listOf(-1, -2))

    assertEquals(7, list.size)

    checkListDeepEquals(list, resultList)

    checkNotified()
  }

  /** Test add all at index out of bounds. */
  @Test
  @DisplayName("Test add all at index out of bounds")
  fun testAddAllAtIndexOutOfBounds() {
    assertThrows<IndexOutOfBoundsException> { list.addAll(-1, listOf(-1, -2)) }

    assertThrows<IndexOutOfBoundsException> { list.addAll(6, listOf(-1, -2)) }
  }

  /** Test set all. */
  @Test
  @DisplayName("Test set all")
  fun testSetAll() {
    // 13,25,17,13,-4
    val resultList = listOf(-1, -2)

    assertEquals(5, list.size)

    list.setAll(listOf(-1, -2))

    assertEquals(2, list.size)

    checkListDeepEquals(list, resultList)

    checkNotified()
  }

  /** Test set all with same size but different elements. */
  @Test
  @DisplayName("Test set all with same size but different elements")
  fun testSetAllSameSizeDifferentElements() {
    // 13,25,17,13,-4
    val newList = listOf(13, 25, 17, 13, -3)

    assertEquals(5, list.size)

    list.setAll(newList)

    assertEquals(5, list.size)

    checkListDeepEquals(list, newList)

    checkNotified()
  }

  /** Test set all current list empty. */
  @Test
  @DisplayName("Test set all current list empty")
  fun testSetAllCurrentListEmpty() {
    // 13,25,17,13,-4
    val resultList = listOf(-1, -2)

    assertEquals(0, emptyList.size)

    emptyList.setAll(listOf(-1, -2))

    assertEquals(2, emptyList.size)

    checkListDeepEquals(emptyList, resultList)

    checkNotified()
  }

  /** Test set all new list empty. */
  @Test
  @DisplayName("Test set all new list empty")
  fun testSetAllNewListEmpty() {
    assertEquals(5, list.size)

    list.setAll(emptyList())

    assertEquals(0, list.size)

    checkNotified()
  }

  /** Test set all same elements. */
  @Test
  @DisplayName("Test set all same elements")
  fun testSetAllSameElements() {
    val snapshot = list.toList()

    assertEquals(5, list.size)

    list.setAll(list.toList())

    assertEquals(5, list.size)

    checkListDeepEquals(list, snapshot)

    checkNotNotified()
  }

  /** Test set silent. */
  @Test
  @DisplayName("Test set silent")
  fun testSetSilent() {
    val resultList = listOf(-1, -2)

    list.setSilent(resultList)

    assertEquals(2, list.size)

    checkListDeepEquals(list, resultList)

    checkNotNotified()
  }
}
