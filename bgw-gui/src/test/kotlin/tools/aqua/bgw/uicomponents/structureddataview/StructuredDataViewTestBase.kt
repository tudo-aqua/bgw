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

package tools.aqua.bgw.uicomponents.structureddataview

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.StructuredDataView
import tools.aqua.bgw.observable.ValueObserver

/** Test base for [ListView]. */
open class StructuredDataViewTestBase(instance: StructuredDataView<String>) {
  /** The items in the [ListView]. */
  protected val items: List<String> =
      listOf(
          "Line 1",
          "Line 2",
          "Line 3",
          "Line 4",
          "Line 5",
      )

  /** The [ListView]. */
  protected val dataView: StructuredDataView<String> = instance

  /** [TestListener] catching update invocation of selectedIndices list for [dataView]. */
  private val indicesListener: TestListener<List<Int>> = TestListener()

  /** [TestListener] catching update invocation of selectedItems list for [dataView]. */
  private val itemsListener: TestListener<List<Int>> = TestListener()

  protected var invokedIndex = -1
  private var invokedSingle = 0
  private var invokedAll = 0
  private var invokedClear = 0

  init {
    dataView.items.addAll(items)
  }

  /** Sets up test listener. */
  @BeforeEach
  fun setUp() {
    dataView.selectedIndices.addListener(indicesListener)
    dataView.selectedIndices.addListener(itemsListener)
    dataView.onSelectionEvent =
        {
          invokedSingle++
          invokedIndex = it
        }
    dataView.onSelectAllEvent = { invokedAll++ }
    dataView.onSelectNoneEvent = { invokedClear++ }
  }

  /** Returns 'true' iff the listener got invoked. */
  protected fun checkNotified(count: Int = 1): Boolean =
      indicesListener.invokedCount == count && itemsListener.invokedCount == count

  /** Returns 'false' iff the listener got invoked. */
  protected fun checkNotNotified(): Boolean =
      indicesListener.invokedCount == 0 && itemsListener.invokedCount == 1

  /** Checks invocation of gui updates. */
  protected fun checkInvocation(single: Int, all: Int, clear: Int) {
    assertEquals(single, invokedSingle)
    assertEquals(all, invokedAll)
    assertEquals(clear, invokedClear)
  }

  /** Test listener registering callback invocation. */
  class TestListener<T> : ValueObserver<T> {

    /** Tracks invocation count. */
    var invokedCount: Int = 0

    override fun update(oldValue: T, newValue: T) {
      invokedCount++
    }
  }
}
