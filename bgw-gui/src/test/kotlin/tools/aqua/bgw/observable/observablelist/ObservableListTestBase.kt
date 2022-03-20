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

package tools.aqua.bgw.observable.observablelist

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.observable.ValueObserver
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList

/** Test base for [ObservableList]. */
open class ObservableListTestBase {

  /** The [ObservableList]. */
  protected lateinit var list: ObservableList<Int>
  protected lateinit var emptyList: ObservableList<Int>

  /** Unordered values in [list]. */
  protected val unordered: List<Int> = listOf(13, 25, 17, 13, -4)

  /** Ordered values in [list]. */
  protected val ordered: List<Int> = listOf(-4, 13, 13, 17, 25)

  /** [TestListener] catching update invocation for [list]. */
  private val listener: TestListener<List<Int>> = TestListener()

  /** [TestListener] catching update invocation for [emptyList]. */
  private val emptyListener: TestListener<List<Int>> = TestListener()

  /** Fills the list with the unordered elements and registers listener before each test. */
  @BeforeEach
  fun setUp() {
    list = ObservableArrayList(unordered)
    list.addListener(listener)

    emptyList = ObservableArrayList()
    emptyList.addListener(listener)
  }

  /** Returns 'false' iff the listener got invoked. */
  protected fun checkNotNotified(): Boolean = listener.invokedCount == 0

  /** Returns 'true' iff the listener got invoked. */
  protected fun checkNotified(count: Int = 1): Boolean = listener.invokedCount == count

  protected fun <T> checkListDeepEquals(list: ObservableList<T>, reference: List<T>) {
    for (i in reference.indices) assertEquals(reference[i], list[i])
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
