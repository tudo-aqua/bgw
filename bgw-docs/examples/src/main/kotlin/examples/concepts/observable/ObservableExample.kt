/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package examples.concepts.observable

import tools.aqua.bgw.components.uicomponents.ListView
import tools.aqua.bgw.components.uicomponents.SelectionMode
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.observable.ValueObserver
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.IntegerProperty
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/** Compile-checked snippets of the Observable guide. */
fun main() {
  val score = IntegerProperty(0)

  score.addListener { oldValue, newValue -> println("Score changed from $oldValue to $newValue") }

  score.value = 1 // prints "Score changed from 0 to 1"
  score.value = 1 // equal value, the listener is not invoked

  // Invoke the listener once immediately with a given initial value
  score.addListenerAndInvoke(score.value) { _, newValue -> println("Score is $newValue") }

  // A listener that is removed again
  val observer = ValueObserver<Int> { _, newValue -> println("Observed $newValue") }
  score.addListener(observer)
  score.removeListener(observer)

  // Generic and limited properties
  val player = Property("Alice")
  player.value = "Bob"

  val health =
      LimitedDoubleProperty(lowerBoundInclusive = 0, upperBoundInclusive = 100, initialValue = 100)
  health.value = 50.0
  // health.value = 150.0 would throw an IllegalArgumentException

  // Fire the listeners although the value did not change
  player.notifyUnchanged()

  // Fire exactly once, as soon as the value is true
  val ready = BooleanProperty(false)
  ready.once(initialValue = false, expectedValue = true) { _, _ -> println("Ready!") }
  ready.value = true

  // Observable lists
  val names = ObservableArrayList(listOf("Alice", "Bob"))
  names.addListener { oldList, newList -> println("List changed from $oldList to $newList") }
  names.add("Carol")
  names.removeAt(0)

  // BGW components expose observable lists
  val listView =
      ListView(
          items = mutableListOf("Alice", "Bob", "Carol"),
          visual = ColorVisual(Color(0x0F141F)),
          font = Font(20.0, Color.WHITE),
          selectionMode = SelectionMode.SINGLE,
          selectionBackground = ColorVisual(Color(0xBB6DFF)))
  listView.items.addListener { _, newItems -> println("Items are now $newItems") }
  listView.items.add("Dave")
  listView.selectedItems.addListener { _, selected -> println("Selected: $selected") }
}
