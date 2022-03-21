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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.DEFAULT_LISTVIEW_HEIGHT
import tools.aqua.bgw.core.DEFAULT_LISTVIEW_WIDTH
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.lists.ReadonlyObservableList
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [ListView] displaying its items next to each other in the given orientation.
 *
 * The [formatFunction] is used to gain a [String] representation of each item. If no
 * [formatFunction] is specified the [toString] function gets used instead.
 *
 * @constructor Creates a [ListView].
 *
 * @param T Generic [ListView] content.
 * @param posX Horizontal coordinate for this [ListView]. Default: 0.
 * @param posY Vertical coordinate for this [ListView]. Default: 0.
 * @param width Width for this [ListView]. Default: [DEFAULT_LISTVIEW_WIDTH].
 * @param height Height for this [ListView]. Default: [DEFAULT_LISTVIEW_HEIGHT].
 * @param items Initial list of items for this [ListView]. Default: empty list.
 * @param font [Font] to be used for this [ListView]. Default: default [Font] constructor.
 * @param orientation Orientation for this [ListView]. Default: [Orientation.VERTICAL].
 * @param selectionMode Selection mode to be used for this [ListView]. Default:
 * [SelectionMode.SINGLE].
 * @param selectionBackground Background for selected items in this [ListView]. Default:
 * [ColorVisual.BLUE].
 * @param formatFunction The [formatFunction] that is used to represent the items. Default: `null`.
 */
open class ListView<T>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_LISTVIEW_WIDTH,
    height: Number = DEFAULT_LISTVIEW_HEIGHT,
    items: List<T> = emptyList(),
    font: Font = Font(),
    orientation: Orientation = Orientation.VERTICAL,
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    selectionBackground: ColorVisual = ColorVisual.BLUE,
    formatFunction: ((T) -> String)? = null
) :
    UIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        font = font,
        visual = Visual.EMPTY) {
  /** Items list. */
  val items: ObservableList<T> = ObservableArrayList(items)

  /**
   * [Property] for the [Orientation] of this [ListView].
   *
   * @see orientation
   */
  val orientationProperty: Property<Orientation> = Property(orientation)

  /**
   * [Orientation] of this [ListView] displayed.
   *
   * @see orientationProperty
   */
  var orientation: Orientation
    get() = orientationProperty.value
    set(value) {
      orientationProperty.value = value
    }

  /**
   * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each
   * item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   *
   * @see formatFunction
   */
  val formatFunctionProperty: Property<((T) -> String)?> = Property(formatFunction)

  /**
   * The [formatFunction] that gets used to obtain a [String] representation for each item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   *
   * @see formatFunctionProperty
   */
  var formatFunction: ((T) -> String)?
    get() = formatFunctionProperty.value
    set(value) {
      formatFunctionProperty.value = value
    }

  // TODO: Docs
  /**
   * [Property] for the [selectionMode] of this [ListView].
   *
   * Changing from [SelectionMode.SINGLE] to [SelectionMode.MULTIPLE] keeps selection as is.
   * Changing from [SelectionMode.MULTIPLE] to [SelectionMode.SINGLE] selects the item that was last
   * selected. Changing from or to [SelectionMode.NONE] clears selection.
   *
   * Selected items and indices may be observed by registering observers on [selectedItems] and
   * [selectedIndices].
   */
  val selectionModeProperty: Property<SelectionMode> = Property(selectionMode)

  /**
   * Selection mode to be used for this [ListView].
   *
   * Changing from [SelectionMode.SINGLE] to [SelectionMode.MULTIPLE] keeps selection as is.
   * Changing from [SelectionMode.MULTIPLE] to [SelectionMode.SINGLE] selects the item that was last
   * selected. Changing from or to [SelectionMode.NONE] clears selection.
   *
   * Selected items and indices may be observed by registering observers on [selectedItems] and
   * [selectedIndices].
   */
  var selectionMode: SelectionMode
    get() = selectionModeProperty.value
    set(value) {
      selectionModeProperty.value = value
    }

  /**
   * [Property] for the background color for selected items in this [ListView]. Item selection is
   * enabled via [selectionMode].
   */
  val selectionBackgroundProperty: Property<ColorVisual> = Property(selectionBackground)

  /**
   * Background color for selected items in this [ListView]. Item selection is enabled via
   * [selectionMode].
   */
  var selectionBackground: ColorVisual
    get() = selectionBackgroundProperty.value
    set(value) {
      selectionBackgroundProperty.value = value
    }

  /**
   * [Property] for the style of selected items in this [ListView]. Item selection is enabled via
   * [selectionMode].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * the elements in this [ListView]. Critical failures, bugs or other undefined behaviour could
   * occur when using this feature.
   */
  val selectionStyleProperty: Property<String> = Property("")

  /**
   * Style of selected items in this [ListView]. Item selection is enabled via [selectionMode].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * the elements in this [ListView]. Critical failures, bugs or other undefined behaviour could
   * occur when using this feature.
   */
  var selectionStyle: String
    get() = selectionStyleProperty.value
    set(value) {
      selectionStyleProperty.value = value
    }

  /** Backing field for [selectedItems] for internal updating. */
  internal val selectedItemsList: ObservableList<T> = ObservableArrayList()

  /**
   * [ReadonlyObservableList] containing all currently selected items in this [ListView]. Register a
   * listener to listen for selection changes. Item selection is enabled via [selectionMode].
   *
   * @see selectedIndices
   * @see selectionMode
   */
  val selectedItems: ReadonlyObservableList<T>
    get() = selectedItemsList

  /** Backing field for [selectedIndices] for internal updating. */
  internal val selectedIndicesList: ObservableList<Int> = ObservableArrayList()

  /**
   * [ReadonlyObservableList] containing indices of all currently selected items in this [ListView].
   * Register a listener to listen for selection changes. Item selection is enabled via
   * [selectionMode].
   *
   * @see selectedItems
   * @see selectionMode
   */
  val selectedIndices: ReadonlyObservableList<Int>
    get() = selectedIndicesList

  /** Internal event handler for selection. */
  internal var onSelectionEvent: ((Int) -> Unit)? = null

  /** Internal event handler for selection. */
  internal var onSelectAllEvent: (() -> Unit)? = null

  /** Internal event handler for selection. */
  internal var onSelectNoneEvent: (() -> Unit)? = null

  /**
   * Selects the element at the specified index in this [ListView]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [index] is out of bounds.
   */
  fun select(index: Int) {
    checkSelectionEnabled()
    require(index in items.indices) { "Index is out of bounds for this ListView." }

    if (selectedIndices.size == 1 && selectedIndices[0] == index) return

    onSelectionEvent?.invoke(index)
  }

  /**
   * Selects the specified element. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [element] is not contained in [items].
   */
  fun select(element: T) {
    require(items.contains(element)) {
      "Cannot select element because it is not contained in this ListView."
    }

    select(items.indexOf(element))
  }

  /**
   * Selects the first element in this [ListView]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [ListView] is empty.
   */
  fun selectFirst() {
    select(0)
  }

  /**
   * Selects the last element in this [ListView]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [ListView] is empty.
   */
  fun selectLast() {
    select(items.size - 1)
  }

  /**
   * Selects the next element in this [ListView]. Clears current selection.
   *
   * Selects first element if none is currently selected. If the last element is selected the
   * selection does not change.
   *
   * @throws IllegalStateException If selection mode is not [SelectionMode.SINGLE].
   * @throws IllegalArgumentException If [ListView] is empty.
   */
  fun selectNext() {
    check(selectionMode == SelectionMode.SINGLE) {
      "Cannot select next item in selection mode '$selectionMode'."
    }

    if (selectedIndices.isEmpty()) select(0)
    else if (selectedIndices[0] < items.size - 1) select(selectedIndices[0] + 1)
  }

  /**
   * Selects the previous element in this [ListView]. Clears current selection.
   *
   * Selects last element if none is currently selected.
   *
   * @throws IllegalStateException If selection mode is not [SelectionMode.SINGLE].
   * @throws IllegalArgumentException If [ListView] is empty.
   */
  fun selectPrevious() {
    check(selectionMode == SelectionMode.SINGLE) {
      "Cannot select previous item in selection mode '$selectionMode'."
    }

    if (selectedIndices.isEmpty()) select(items.size - 1)
    else if (selectedIndices[0] > 0) select(selectedIndices[0] - 1)
  }

  /**
   * Selects all items in this [ListView].
   *
   * @throws IllegalStateException If selection mode is not set to [SelectionMode.MULTIPLE].
   * @throws IllegalArgumentException If [ListView] is empty.
   *
   * @see clearSelection
   */
  fun selectAll() {
    check(selectionMode == SelectionMode.MULTIPLE) {
      "Cannot select all items in selection mode '$selectionMode'."
    }

    require(items.isNotEmpty()) { "Cannot select all items because items list is empty." }

    onSelectAllEvent?.invoke()
  }

  /**
   * Clears selection of items. Instantly returns if no item is currently selected.
   *
   * @see selectAll
   */
  fun clearSelection() {
    checkSelectionEnabled()

    if (selectedIndices.size > 0) onSelectNoneEvent?.invoke()
  }

  /** Checks selection mode not to be [SelectionMode.NONE]. */
  private fun checkSelectionEnabled(): Unit =
      check(selectionMode != SelectionMode.NONE) { "Cannot select items in selection mode 'NONE'." }
}
