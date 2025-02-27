/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

import tools.aqua.bgw.core.DEFAULT_COMBOBOX_HEIGHT
import tools.aqua.bgw.core.DEFAULT_COMBOBOX_WIDTH
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A standard [ComboBox] that may be populated with items of specified type parameter.
 *
 * The [formatFunction] is used to gain a String representation of each item. If no [formatFunction]
 * is specified the [toString] function gets used instead.
 *
 * Whenever the user selects an item, the [selectedItem] gets updated.
 *
 * @constructor Creates a [ComboBox].
 *
 * @param T Generic [ComboBox] content.
 * @param posX Horizontal coordinate for this [ComboBox]. Default: 0.
 * @param posY Vertical coordinate for this [ComboBox]. Default: 0.
 * @param width Width for this [ComboBox]. Default: [DEFAULT_COMBOBOX_WIDTH].
 * @param height Height for this [ComboBox]. Default: [DEFAULT_COMBOBOX_HEIGHT].
 * @param font [Font] to be used for the options. Default: default [Font] constructor.
 * @param visual [Visual] that is used to represent this [ComboBox]. Default: empty [Visual].
 * @property prompt Prompt for this [ComboBox]. This gets displayed as a prompt to the user whenever
 * the [selectedItem] value is `null`. Default: empty string.
 * @param items The initial selection of items. Default: empty list.
 * @param formatFunction The formatFunction that is used to represent the items. Default: `null`.
 *
 * @see Font
 * @see Visual
 * @see UIComponent
 *
 * @since 0.1
 */
open class ComboBox<T>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_COMBOBOX_WIDTH,
    height: Number = DEFAULT_COMBOBOX_HEIGHT,
    font: Font = Font(),
    visual: Visual = Visual.EMPTY,
    /** Prompt for this [ComboBox]. */
    val prompt: String = "",
    items: List<T> = emptyList(),
    formatFunction: ((T) -> String)? = null,
) :
    UIComponent(
        posX = posX, posY = posY, width = width, height = height, font = font, visual = visual) {

  internal fun select(selectedItem: Int) {
    if (selectedItem < 0 || selectedItem >= observableItemsList.size)
        selectedItemProperty.value = null
    else selectedItemProperty.value = observableItemsList[selectedItem]

    onItemSelected?.invoke(selectedItemProperty.value)
  }

  /**
   * [Property] for the [items] [List] for this [ComboBox].
   *
   * @see items
   */
  internal val observableItemsList: ObservableList<T> = ObservableArrayList()

  /** [List] of all selectable items for this [ComboBox]. */
  var items: List<T>
    get() = observableItemsList.toList()
    set(value) {
      observableItemsList.clear()
      observableItemsList.addAll(value)
    }

  /**
   * [Property] for the selected item.
   *
   * Value may be `null` if no item is selected.
   *
   * @see selectedItem
   */
  internal val selectedItemProperty: Property<T?> = Property(null)

  /**
   * The selected item.
   *
   * May be `null` if no item is selected.
   */
  var selectedItem: T?
    get() = selectedItemProperty.value
    set(value) {
      selectedItemProperty.value = value
    }

  /**
   * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each
   * item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   *
   * @see formatFunction
   */
  internal var formatFunctionProperty: Property<((T) -> String)?> = Property(formatFunction)

  /**
   * The [formatFunction] that gets used to obtain a [String] representation for each item.
   *
   * If the value is `null`, the [toString] function of the item is used instead.
   */
  var formatFunction: ((T) -> String)?
    get() = formatFunctionProperty.value
    set(value) {
      formatFunctionProperty.value = value
    }

  init {
    observableItemsList.addAll(items)

    if (selectedItem != null) {
      require(items.contains(selectedItem)) { "Items list does not contain element to select." }

      selectedItemProperty.value = selectedItem
    }
  }

  /**
   * Get the index of the currently selected item.
   *
   * @return The index of the currently selected item.
   *
   * @see selectedItem
   *
   * @since 0.10
   */
  fun getSelectedIndex(): Int {
    return observableItemsList.indexOf(selectedItem)
  }

  /**
   * Manually trigger the [formatFunction] for a given item.
   *
   * @param item Item to format.
   *
   * @return Formatted [String] representation of the item.
   *
   * @see formatFunction
   *
   * @since 0.10
   */
  fun formatItem(item: Any?): String {
    val function = formatFunction ?: { it: Any? -> it.toString() }
    return function(item as T)
  }

  /**
   * Gets invoked whenever an item is selected.
   *
   * @see selectedItem
   *
   * @since 0.10
   */
  var onItemSelected: ((T?) -> Unit)? = null
}
