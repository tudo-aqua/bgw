/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.components.uicomponents.structureddataview.listview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.SelectionMode

/** Tests for [ListView] with [SelectionMode.SINGLE]. */
class ListViewTestSelectionModeSingle : ListViewTestBase(SelectionMode.SINGLE) {

  /** Test select by index on valid parameter. */
  @Test
  @DisplayName("Test select by index on valid parameter")
  fun testSelectByIndex() {
    dataView.select(2)

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(2, invokedIndex)
  }

  /** Test select by index on first and last valid index. */
  @Test
  @DisplayName("Test select by index on first and last valid index")
  fun testSelectByIndexEdge() {
    dataView.select(0)

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(0, invokedIndex)

    // Second invocation
    dataView.select(items.size - 1)

    checkNotified()
    checkInvocation(2, 0, 0) // Invoked twice
    assertEquals(items.size - 1, invokedIndex)
  }

  /** Test select by index out of range. */
  @Test
  @DisplayName("Test select by index out of range")
  fun testSelectByIndexOutOfBounds() {
    assertThrows<IllegalArgumentException> { dataView.select(-1) }

    checkNotNotified()
    checkInvocation(0, 0, 0)

    assertThrows<IllegalArgumentException> { dataView.select(42) }

    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test select by index on already selected item. */
  @Test
  @DisplayName("Test select by index on already selected item")
  fun testSelectByIndexAlreadySelected() {
    dataView.select(2)

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(2, invokedIndex)

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(2)

    dataView.select(2)

    checkNotified(1) // Not notified again
    checkInvocation(1, 0, 0)
  }

  /** Test select by item on valid parameter. */
  @Test
  @DisplayName("Test select by item on valid parameter")
  fun testSelectByItem() {
    dataView.select(items[2])

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(2, invokedIndex)
  }

  /** Test select by item on first and last valid index. */
  @Test
  @DisplayName("Test select by item on first and last valid index")
  fun testSelectByItemEdge() {
    dataView.select(items.first())

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(0, invokedIndex)

    // Second invocation
    dataView.select(items.last())

    checkNotified(2)
    checkInvocation(2, 0, 0) // Invoked twice
    assertEquals(items.size - 1, invokedIndex)
  }

  /** Test select by item on non-existent parameter. */
  @Test
  @DisplayName("Test select by item on non-existent parameter")
  fun testSelectByItemNotInExisting() {
    assertThrows<IllegalArgumentException> { dataView.select("42") }

    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test select first. */
  @Test
  @DisplayName("Test select first")
  fun testSelectFirst() {
    dataView.selectFirst()

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(0, invokedIndex)
  }

  /** Test select first on empty items list. */
  @Test
  @DisplayName("Test select first on empty items list")
  fun testSelectFirstEmptyList() {
    dataView.items.clear()

    assertThrows<IllegalArgumentException> { dataView.selectFirst() }
    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test select first. */
  @Test
  @DisplayName("Test select first")
  fun testSelectLast() {
    dataView.selectLast()

    checkNotified()
    checkInvocation(1, 0, 0)

    assertEquals(items.size - 1, invokedIndex)
  }

  /** Test select first on empty items list. */
  @Test
  @DisplayName("Test select first on empty items list")
  fun testSelectLastEmptyList() {
    dataView.items.clear()

    assertThrows<IllegalArgumentException> { dataView.selectLast() }

    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test select next. */
  @Test
  @DisplayName("Test select next")
  fun testSelectNext() {
    dataView.selectNext()

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(0, invokedIndex) // None was selected, so index 0 should be selected now

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(0)

    // Second invocation
    dataView.selectNext()

    checkNotified(2)
    checkInvocation(2, 0, 0)
    assertEquals(1, invokedIndex) // Next index, i.e. 1 should be selected now
  }

  /** Test select next if last is selected. */
  @Test
  @DisplayName("Test select next if last is selected")
  fun testSelectNextLastSelected() {
    dataView.selectLast()
    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(items.size - 1, invokedIndex) // Last should be selected

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(items.size - 1)

    // Second invocation
    dataView.selectNext()

    checkNotified(1) // Should not notify
    checkInvocation(1, 0, 0) // Should not invoke
  }

  /** Test select previous. */
  @Test
  @DisplayName("Test select previous")
  fun testSelectPrevious() {
    dataView.selectPrevious()

    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(
        items.size - 1, invokedIndex) // None was selected, so last index should be selected now

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(items.size - 1)

    // Second invocation
    dataView.selectPrevious()

    checkNotified(2)
    checkInvocation(2, 0, 0)
    assertEquals(
        items.size - 2,
        invokedIndex) // Previous index, i.e. (items.size - 1) -1 should be selected now
  }

  /** Test select previous if last is selected. */
  @Test
  @DisplayName("Test select previous if last is selected")
  fun testSelectPreviousFirstSelected() {
    dataView.selectFirst()
    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(0, invokedIndex) // First should be selected

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(0)

    // Second invocation
    dataView.selectPrevious()

    checkNotified(1) // Should not notify
    checkInvocation(1, 0, 0) // Should not invoke
  }

  /** Test select all. */
  @Test
  @DisplayName("Test select all")
  fun testSelectAll() {
    assertThrows<IllegalStateException> { dataView.selectAll() }
    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test select all when items are empty. */
  @Test
  @DisplayName("Test select all when items are empty")
  fun testSelectAllEmptyList() {
    dataView.items.clear()
    assertThrows<IllegalStateException> { dataView.selectAll() }
    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test clear selection. */
  @Test
  @DisplayName("Test clear selection")
  fun testClearSelection() {
    dataView.select(2)
    checkNotified()
    checkInvocation(1, 0, 0)
    assertEquals(2, invokedIndex)

    // Inject selected index to selection model
    dataView.selectedIndicesList.add(2)

    dataView.clearSelection()

    checkNotified(2)
    checkInvocation(1, 0, 1) // Clear should have been invoked
  }

  /** Test clear selection if none was selected. */
  @Test
  @DisplayName("Test clear selection if none was selected")
  fun testClearSelectionNoneSelected() {
    dataView.clearSelection()

    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test clear selection if items are empty. */
  @Test
  @DisplayName("Test clear selection if items are empty")
  fun testClearSelectionEmptyList() {
    dataView.items.clear()

    dataView.clearSelection()

    checkNotNotified()
    checkInvocation(0, 0, 0)
  }
}
