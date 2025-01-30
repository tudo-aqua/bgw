/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.lists.ReadonlyObservableList
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [StructuredDataView] displaying its items with given format function.
 *
 * @constructor Creates a [StructuredDataView].
 *
 * @param T Generic [StructuredDataView] content.
 * @param posX Horizontal coordinate for this [StructuredDataView].
 * @param posY Vertical coordinate for this [StructuredDataView].
 * @param width Width for this [StructuredDataView].
 * @param height Height for this [StructuredDataView].
 * @param items Initial list of items for this [StructuredDataView].
 * @param font [Font] to be used for this [StructuredDataView].
 * @param visual Background [Visual].
 * @param selectionMode Selection mode to be used for this [StructuredDataView].
 * @param selectionBackground Background for selected items in this [StructuredDataView].
 */
sealed class StructuredDataView<T>(
    posX: Number,
    posY: Number,
    width: Number,
    height: Number,
    items: List<T>,
    font: Font,
    visual: Visual,
    selectionMode: SelectionMode,
    selectionBackground: ColorVisual,
) :
    UIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        font = font,
        visual = visual,
    ) {
  /**
   * An [ObservableList] that contains the data objects. The first object in this [ObservableList]
   * will be the topmost row in the rendered [UIComponent].
   */
  val items: ObservableList<T> = ObservableArrayList(items)

  /**
   * [Property] for the [selectionMode] of this [UIComponent].
   *
   * Changing from [SelectionMode.SINGLE] to [SelectionMode.MULTIPLE] keeps selection as is.
   * Changing from [SelectionMode.MULTIPLE] to [SelectionMode.SINGLE] selects the item that was last
   * selected. Changing from or to [SelectionMode.NONE] clears selection.
   *
   * Selected items and indices may be observed by registering observers on [selectedItems] and
   * [selectedIndices].
   */
  internal val selectionModeProperty: Property<SelectionMode> = Property(selectionMode)

  /**
   * Selection mode to be used for this [UIComponent].
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
   * [Property] for the background color for selected items in this [UIComponent]. Item selection is
   * enabled via [selectionMode].
   */
  internal val selectionBackgroundProperty: Property<ColorVisual> = Property(selectionBackground)

  /**
   * Background color for selected items in this [UIComponent]. Item selection is enabled via
   * [selectionMode].
   */
  var selectionBackground: ColorVisual
    get() = selectionBackgroundProperty.value
    set(value) {
      selectionBackgroundProperty.value = value
    }

  /**
   * [Property] for the style of selected items in this [UIComponent]. Item selection is enabled via
   * [selectionMode].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * the elements in this [UIComponent]. Critical failures, bugs or other undefined behaviour could
   * occur when using this feature.
   */
  @Deprecated("The property is no longer used as of BGW 0.10.")
  internal val selectionStyleProperty: Property<String> = Property("")

  /**
   * Style of selected items in this [UIComponent]. Item selection is enabled via [selectionMode].
   *
   * This gets applied last, so it may override any changes made via other fields and functions of
   * the elements in this [UIComponent]. Critical failures, bugs or other undefined behaviour could
   * occur when using this feature.
   */
  @Deprecated("CSS Styling is no longer supported as of BGW 0.10.")
  var selectionStyle: String
    get() = selectionStyleProperty.value
    set(value) {
      selectionStyleProperty.value = value
    }

  /** Backing field for [selectedItems] for internal updating. */
  internal val selectedItemsList: ObservableList<T> = ObservableArrayList()

  /**
   * [ReadonlyObservableList] containing all currently selected items in this [UIComponent].
   * Register a listener to listen for selection changes. Item selection is enabled via
   * [selectionMode].
   *
   * @see selectedIndices
   * @see selectionMode
   */
  val selectedItems: ReadonlyObservableList<T>
    get() = selectedItemsList

  /** Backing field for [selectedIndices] for internal updating. */
  internal val selectedIndicesList: ObservableList<Int> = ObservableArrayList()

  /**
   * [ReadonlyObservableList] containing indices of all currently selected items in this
   * [UIComponent]. Register a listener to listen for selection changes. Item selection is enabled
   * via [selectionMode].
   *
   * @see selectedItems
   * @see selectionMode
   */
  val selectedIndices: ReadonlyObservableList<Int>
    get() = selectedIndicesList

  /** Internal event handler for selection. */
  internal var onSelectionEvent: ((Int) -> Unit)? = {
    if (this.items.isNotEmpty()) {
      if (selectedIndicesList.contains(it)) {
        selectedItemsList.remove(this.items[it])
        selectedIndicesList.remove(it)
      } else {
        selectedItemsList.add(this.items[it])
        selectedIndicesList.add(it)
      }
    }
  }

  /** Internal event handler for selection. */
  internal var onSelectAllEvent: (() -> Unit)? = {
    selectedItemsList.clear()
    selectedIndicesList.clear()

    this.items.indices.forEach {
      selectedItemsList.add(this.items[it])
      selectedIndicesList.add(it)
    }
  }

  /** Internal event handler for selection. */
  internal var onSelectNoneEvent: (() -> Unit)? = {
    selectedItemsList.clear()
    selectedIndicesList.clear()
  }

  /**
   * Selects the element at the specified index in this [UIComponent]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [index] is out of bounds.
   */
  fun select(index: Int) {
    checkSelectionEnabled()
    require(index in items.indices) { "Index is out of bounds." }

    if (selectedIndices.size > 0 && selectionMode == SelectionMode.SINGLE) {
      clearSelection()
    }

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
      "Cannot select element because it is not contained in this UIComponent."
    }

    select(items.indexOf(element))
  }

  /**
   * Selects the first element in this [UIComponent]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [UIComponent] is empty.
   */
  fun selectFirst() {
    select(0)
  }

  /**
   * Selects the last element in this [UIComponent]. Clears current selection.
   *
   * @throws IllegalStateException If selection mode is [SelectionMode.NONE].
   * @throws IllegalArgumentException If [UIComponent] is empty.
   */
  fun selectLast() {
    select(items.size - 1)
  }

  /**
   * Selects the next element in this [UIComponent]. Clears current selection.
   *
   * Selects first element if none is currently selected. If the last element is selected the
   * selection does not change.
   *
   * @throws IllegalStateException If selection mode is not [SelectionMode.SINGLE].
   * @throws IllegalArgumentException If [UIComponent] is empty.
   */
  fun selectNext() {
    check(selectionMode == SelectionMode.SINGLE) {
      "Cannot select next item in selection mode '$selectionMode'."
    }

    if (selectedIndices.isEmpty()) select(0)
    else if (selectedIndices[0] < items.size - 1) select(selectedIndices[0] + 1)
  }

  /**
   * Selects the previous element in this [UIComponent]. Clears current selection.
   *
   * Selects last element if none is currently selected.
   *
   * @throws IllegalStateException If selection mode is not [SelectionMode.SINGLE].
   * @throws IllegalArgumentException If [UIComponent] is empty.
   */
  fun selectPrevious() {
    check(selectionMode == SelectionMode.SINGLE) {
      "Cannot select previous item in selection mode '$selectionMode'."
    }

    if (selectedIndices.isEmpty()) select(items.size - 1)
    else if (selectedIndices[0] > 0) select(selectedIndices[0] - 1)
  }

  /**
   * Selects all items in this [UIComponent].
   *
   * @throws IllegalStateException If selection mode is not set to [SelectionMode.MULTIPLE].
   * @throws IllegalArgumentException If [UIComponent] is empty.
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
