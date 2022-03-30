/*
 * Copyright 2022 The BoardGameWork Authors
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

package tools.aqua.bgw.uicomponents.structureddataview.listview

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.SelectionMode

/** Tests for [ListView] with [SelectionMode.NONE]. */
class ListViewTestSelectionModeNone : ListViewTestBase(SelectionMode.NONE) {

  /** Test select by index on valid parameter. */
  @Test
  @DisplayName("Test select by index on valid parameter")
  fun testSelectByIndex() {
    assertThrows<IllegalStateException> { dataView.select(0) }
    checkNotNotified()
  }

  /** Test select by item on valid parameter. */
  @Test
  @DisplayName("Test select by item on valid parameter")
  fun testSelectByItem() {
    assertThrows<IllegalStateException> { dataView.select(items[0]) }
    checkNotNotified()
  }

  /** Test select first. */
  @Test
  @DisplayName("Test select first")
  fun testSelectFirst() {
    assertThrows<IllegalStateException> { dataView.selectFirst() }
    checkNotNotified()
  }

  /** Test select first. */
  @Test
  @DisplayName("Test select first")
  fun testSelectLast() {
    assertThrows<IllegalStateException> { dataView.selectLast() }
    checkNotNotified()
  }

  /** Test select next. */
  @Test
  @DisplayName("Test select next")
  fun testSelectNext() {
    assertThrows<IllegalStateException> { dataView.selectNext() }
    checkNotNotified()
  }

  /** Test select previous. */
  @Test
  @DisplayName("Test select previous")
  fun testSelectPrevious() {
    assertThrows<IllegalStateException> { dataView.selectPrevious() }
    checkNotNotified()
  }

  /** Test select all. */
  @Test
  @DisplayName("Test select all")
  fun testSelectAll() {
    assertThrows<IllegalStateException> { dataView.selectAll() }
    checkNotNotified()
  }

  /** Test clear selection. */
  @Test
  @DisplayName("Test clear selection")
  fun testClearSelection() {
    assertThrows<IllegalStateException> { dataView.clearSelection() }
    checkNotNotified()
  }
}
