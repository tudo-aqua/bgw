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

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Test single functions in ObservableList. */
class SingleListFunctionTests : ObservableListTestBase() {

  /** Test indices. */
  @Test
  @DisplayName("Test indices")
  fun testIndices() {
    assertEquals(0 until 5, list.indices)

    checkNotNotified()
  }

  /** Test get. */
  @Test
  @DisplayName("Test get")
  fun testGet() {
    for (i in list.indices) assertEquals(unordered[i], list[i])

    checkNotNotified()
  }

  /** Test set. */
  @Test
  @DisplayName("Test set")
  fun testSet() {
    list[0] = -1
    assertEquals(-1, list[0])

    for (i in 1 until list.size) assertEquals(unordered[i], list[i])

    checkNotified()

    assertThrows<IndexOutOfBoundsException> { list[-1] = 0 }

    assertThrows<IndexOutOfBoundsException> { list[5] = 0 }
  }

  /** Test size. */
  @Test
  @DisplayName("Test size")
  fun testSize() {
    assertEquals(5, list.size)
    list.add(-1)
    assertEquals(6, list.size)
    list.clear()
    assertEquals(0, list.size)

    checkNotNotified()
  }

  /** Test isEmpty and isNotEmpty. */
  @Test
  @DisplayName("Test isEmpty and isNotEmpty")
  fun testIsEmpty() {
    assertFalse(list.isEmpty())
    assertTrue(list.isNotEmpty())
    list.clear()
    assertTrue(list.isEmpty())
    assertFalse(list.isNotEmpty())

    checkNotNotified()
  }

  /** Test contains. */
  @Test
  @DisplayName("Test contains")
  fun testContains() {
    assertTrue(list.contains(13))
    assertFalse(list.contains(0))
    assertFalse(list.contains("13"))
    assertFalse(list.contains(13.0))

    checkNotNotified()
  }

  /** Test indexOf and lastIndexOf. */
  @Test
  @DisplayName("Test indexOf and lastIndexOf")
  fun testIndexOf() {
    // 13,25,17,13,-4
    assertEquals(1, list.indexOf(25))
    assertEquals(1, list.lastIndexOf(25))

    assertEquals(0, list.indexOf(13))
    assertEquals(3, list.lastIndexOf(13))

    assertEquals(-1, list.indexOf(-1))
    assertEquals(-1, list.lastIndexOf(-1))

    checkNotNotified()
  }

  /** Test subList. */
  @Test
  @DisplayName("Test subList")
  fun testSubList() {
    // 13,25,17,13,-4
    assertEquals(listOf(25, 17, 13), list.subList(1, 4))
    assertEquals(emptyList<Int>(), list.subList(1, 1))

    checkNotNotified()

    assertThrows<IllegalArgumentException> { list.subList(2, 1) }
  }

  /** Test sort. */
  @Test
  @DisplayName("Test sort")
  fun testSort() {
    list.sort(Comparator.comparingInt { it })
    for (i in list.indices) assertEquals(ordered[i], list[i])

    checkNotified()
  }

  /** Test spliterator. */
  @Test
  @DisplayName("Test spliterator")
  fun testSpliterator() {
    val it = list.spliterator()
    val result = mutableListOf<Int>()

    it.forEachRemaining { result.add(it) }

    checkListDeepEquals(list, result)

    checkNotNotified()
  }

  /** Test iterator. */
  @Test
  @DisplayName("Test iterator")
  fun testIterator() {
    val it = list.iterator()
    val result = mutableListOf<Int>()

    while (it.hasNext()) {
      result.add(it.next())
    }

    checkListDeepEquals(list, result)

    checkNotNotified()
  }
}
