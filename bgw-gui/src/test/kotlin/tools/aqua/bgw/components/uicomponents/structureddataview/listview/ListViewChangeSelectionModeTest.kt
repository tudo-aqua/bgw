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
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.SelectionMode

/** Test changing [SelectionMode] in [ListView]. */
class ListViewChangeSelectionModeTest : ListViewTestBase(SelectionMode.SINGLE) {

  /** Test change to [SelectionMode.NONE]. */
  @Test
  @DisplayName("Test change to SelectionMode.NONE")
  fun testChangeToNone() {
    dataView.selectionMode = SelectionMode.NONE
    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test change to [SelectionMode.MULTIPLE]. */
  @Test
  @DisplayName("Test change to SelectionMode.MULTIPLE")
  fun testChangeToMultiple() {
    dataView.selectionMode = SelectionMode.MULTIPLE
    checkNotNotified()
    checkInvocation(0, 0, 0)
  }

  /** Test change to [SelectionMode.NONE] after selecting. */
  @Test
  @DisplayName("Test change to SelectionMode.NONE after selecting")
  fun testChangeToNoneAfterSelecting() {
    dataView.selectFirst()
    checkNotified()
    checkInvocation(1, 0, 0)

    dataView.selectionMode = SelectionMode.NONE
    checkNotified(2)
    checkInvocation(1, 0, 0)

    assertEquals(0, dataView.selectedIndices.size)
    assertEquals(0, dataView.selectedItems.size)
  }

  /** Test change to [SelectionMode.MULTIPLE] after selecting. */
  @Test
  @DisplayName("Test change to SelectionMode.MULTIPLE after selecting")
  fun testChangeToMultipleAfterSelecting() {
    dataView.selectFirst()
    checkNotified()
    checkInvocation(1, 0, 0)

    dataView.selectionMode = SelectionMode.MULTIPLE
    checkNotified(1)
    checkInvocation(1, 0, 0)
  }

  /** Test change [SelectionMode.MULTIPLE] to [SelectionMode.SINGLE]. */
  @Test
  @DisplayName("Test change SelectionMode.MULTIPLE to SelectionMode.SINGLE")
  fun testChangeMultipleToSingle() {
    dataView.selectionMode = SelectionMode.MULTIPLE
    checkNotNotified()
    checkInvocation(0, 0, 0)

    dataView.selectAll()
    checkNotified()
    checkInvocation(0, 1, 0)

    dataView.selectionMode = SelectionMode.SINGLE
    checkNotified(2)
    checkInvocation(0, 1, 0)

    assertEquals(0, dataView.selectedIndices.size)
    assertEquals(0, dataView.selectedItems.size)
  }
}
