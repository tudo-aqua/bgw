/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font

/**
 * A standard [ComboBox] that may be populated with items of specified type parameter.
 * The [formatFunction] is used to gain a String representation of each item.
 * If no [formatFunction] is specified the [toString] function gets used instead.
 *
 * Whenever the user selects an item, the [selectedItemProperty] gets updated.
 *
 * @param height height for this [ComboBox]. Default: [ComboBox.DEFAULT_COMBOBOX_HEIGHT].
 * @param width width for this [ComboBox]. Default: [ComboBox.DEFAULT_COMBOBOX_WIDTH].
 * @param posX horizontal coordinate for this [ComboBox]. Default: 0.
 * @param posY vertical coordinate for this [ComboBox]. Default: 0.
 * @param prompt prompt for this [ComboBox]. This gets displayed as a prompt to the user whenever the
 * [selectedItemProperty] value is `null`.Default: empty string.
 * @param font font to be used for the [prompt]. Default: default [Font] constructor.
 * @param items the initial selection of items. Default: empty list.
 * @param formatFunction the formatFunction that is used to represent the items. Default: `null`.
 */
open class ComboBox<T>(
	height: Number = DEFAULT_COMBOBOX_HEIGHT,
	width: Number = DEFAULT_COMBOBOX_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	val prompt: String = "",
	font: Font = Font(),
	items: List<T> = listOf(),
	formatFunction: ((T) -> String)? = null,
) : UIComponent(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	font = font
) {
	
	/**
	 * [Property] for the items list for this [ComboBox].
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items list for this [ComboBox].
	 *
	 * @see observableItemsList
	 */
	var items: MutableList<T>
		get() = observableItemsList.list
		set(value) {
			observableItemsList.clear()
			observableItemsList.addAll(value)
		}
	
	/**
	 * [Property] for the selected item.
	 * Value may be `null` if no item is selected.
	 */
	val selectedItemProperty: Property<T?> = Property(null)
	
	/**
	 * The selected item.
	 * May be `null` if no item is selected.
	 *
	 * @see selectedItemProperty
	 */
	var selectedItem: T?
		get() = selectedItemProperty.value
		set(value) {
			require(items.contains(value)) { "Items list does not contain element to select: $value" }
			
			selectedItemProperty.value = value
		}
	
	/**
	 * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each item.
	 * If the value is `null`, the [toString] function of the item is used instead.
	 */
	var formatFunctionProperty: Property<((T) -> String)?> = Property(formatFunction)
	
	/**
	 * The [formatFunction] that gets used to obtain a [String] representation for each item.
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
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ComboBox].
	 */
	companion object {
		/**
		 * Suggested [ComboBox] [height].
		 */
		const val DEFAULT_COMBOBOX_HEIGHT: Int = 30
		
		/**
		 * Suggested [ComboBox] [width].
		 */
		const val DEFAULT_COMBOBOX_WIDTH: Int = 120
	}
}