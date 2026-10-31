/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.observable

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.properties.IntegerProperty

/** Regression tests for observable notification and list-boundary behavior. */
class ObservableRegressionTest {

  @Test
  fun `getOrNull returns null at and outside list boundaries`() {
    val list = ObservableArrayList(listOf("a", "b"))

    assertNull(list.getOrNull(-1))
    assertNull(list.getOrNull(list.size))
    assertNull(list.getOrNull(list.size + 1))
  }

  @Test
  fun `one-shot listener does not prevent later listeners from running`() {
    val property = IntegerProperty(0)
    val notifications = mutableListOf<String>()
    property.once(0, 1) { _, _ -> notifications.add("once") }
    property.addListener(ValueObserver { _, _ -> notifications.add("persistent") })

    property.value = 1
    property.value = 2

    assertEquals(listOf("once", "persistent", "persistent"), notifications)
  }

  @Test
  fun `internal listener remains readable and invokable`() {
    val list = ObservableArrayList<Int>()
    var invocations = 0
    list.internalListener = { _, _ -> invocations++ }

    list.internalListener?.invoke(emptyList(), emptyList())

    assertEquals(1, invocations)
  }

  @Test
  fun `no-op bulk operations neither report nor notify a change`() {
    val list = ObservableArrayList(listOf(1, 2))
    var notifications = 0
    list.addListener(ValueObserver { _, _ -> notifications++ })

    assertFalse(list.addAll(emptyList()))
    assertFalse(list.retainAll(listOf(1, 2)))
    list.sort(compareBy { it })

    assertEquals(0, notifications)
  }

  @Test
  fun `clearing through setAll reports a change`() {
    val list = ObservableArrayList(listOf(1))

    assertTrue(list.setAll(emptyList()))
    assertTrue(list.isEmpty())
  }

  @Test
  fun `removing a nullable element still mutates and notifies`() {
    val list = ObservableArrayList<String?>(listOf(null, "remaining"))
    var notifications = 0
    list.addListener(ValueObserver { _, _ -> notifications++ })

    assertNull(list.removeFirst())

    assertEquals(listOf("remaining"), list.toList())
    assertEquals(1, notifications)
  }
}
