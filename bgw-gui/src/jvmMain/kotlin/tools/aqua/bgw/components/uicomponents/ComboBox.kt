/*
 * Copyright 2021-2024 The BoardGameWork Authors
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
 * Whenever the user selects an item, the [selectedItemProperty] gets updated.
 *
 * @constructor Creates a [ComboBox].
 *
 * @param T Generic [ComboBox] content.
 * @param posX Horizontal coordinate for this [ComboBox]. Default: 0.
 * @param posY Vertical coordinate for this [ComboBox]. Default: 0.
 * @param width Width for this [ComboBox]. Default: [DEFAULT_COMBOBOX_WIDTH].
 * @param height Height for this [ComboBox]. Default: [DEFAULT_COMBOBOX_HEIGHT].
 * @param font [Font] to be used for the options. Default: default [Font] constructor.
 * @property prompt Prompt for this [ComboBox]. This gets displayed as a prompt to the user whenever
 * the [selectedItemProperty] value is `null`.Default: empty string.
 * @param items The initial selection of items. Default: empty list.
 * @param formatFunction The formatFunction that is used to represent the items. Default: `null`.
 */
open class ComboBox<T>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_COMBOBOX_WIDTH,
    height: Number = DEFAULT_COMBOBOX_HEIGHT,
    font: Font = Font(),
    val prompt: String = "",
    items: List<T> = emptyList(),
    formatFunction: ((T) -> String)? = null,
) :
    UIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        font = font,
        visual = Visual.EMPTY) {

    internal fun select(selectedItem: Int) {
        if(selectedItem < 0 || selectedItem >= observableItemsList.size) selectedItemProperty.value = null
        else selectedItemProperty.value = observableItemsList[selectedItem]
    }

    internal fun getSelectedIndex(): Int {
        return observableItemsList.indexOf(selectedItem)
    }

    /**
   * [Property] for the [items] [List] for this [ComboBox].
   *
   * @see items
   */
  val observableItemsList: ObservableList<T> = ObservableArrayList()

  /**
   * Items [List] for this [ComboBox].
   *
   * @see observableItemsList
   */
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
  val selectedItemProperty: Property<T?> = Property(null)

  /**
   * The selected item.
   *
   * May be `null` if no item is selected.
   *
   * @see selectedItemProperty
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
  var formatFunctionProperty: Property<((T) -> String)?> = Property(formatFunction)

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

  init {
    observableItemsList.addAll(items)

    if (selectedItem != null) {
      require(items.contains(selectedItem)) { "Items list does not contain element to select." }

      selectedItemProperty.value = selectedItem
    }
  }
}
