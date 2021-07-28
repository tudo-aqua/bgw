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

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font

/**
 * A [ListView] displaying its items next to each other in the given orientation.
 * The [formatFunction] is used to gain a [String] representation of each item.
 * If no [formatFunction] is specified the [toString] function gets used instead.
 *
 * @param height height for this [ListView]. Default: [ListView.DEFAULT_LISTVIEW_HEIGHT].
 * @param width width for this [ListView]. Default: [ListView.DEFAULT_LISTVIEW_WIDTH].
 * @param posX horizontal coordinate for this [ListView]. Default: 0.
 * @param posY vertical coordinate for this [ListView]. Default: 0.
 * @param items initial list of items for this [ListView]. Default: empty list.
 * @param font font to be used for this [ListView]. Default: default [Font] constructor.
 * @param orientation orientation for this [ListView]. Default: [Orientation.VERTICAL].
 * @param formatFunction the [formatFunction] that is used to represent the items. Default: `null`.
 */
open class ListView<T>(
	height: Number = DEFAULT_LISTVIEW_HEIGHT,
	width: Number = DEFAULT_LISTVIEW_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	items: List<T> = listOf(),
	font: Font = Font(), //TODO: Unused?
	orientation: Orientation = Orientation.VERTICAL,
	formatFunction: ((T) -> String)? = null
) : UIComponent(height = height, width = width, posX = posX, posY = posY, font) {
	
	/**
	 * [Property] for the items list for this [ListView].
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items list for this ListView.
	 * @see observableItemsList
	 */
	var items: MutableList<T>
		get() = observableItemsList.list
		set(value) {
			observableItemsList.clear()
			observableItemsList.addAll(value)
		}
	
	/**
	 * [Property] for the [Orientation] of this [ListView].
	 */
	val orientationProperty: ObjectProperty<Orientation> = ObjectProperty(orientation)
	
	/**
	 * [Orientation] of this [ListView] displayed.
	 * @see orientationProperty
	 */
	var orientation: Orientation
		get() = orientationProperty.value
		set(value) {
			orientationProperty.value = value
		}
	
	/**
	 * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each item.
	 * If the value is `null`, the [toString] function of the item is used instead.
	 */
	var formatFunctionProperty: ObjectProperty<((T) -> String)?> = ObjectProperty(formatFunction)
	
	/**
	 * The [formatFunction] that gets used to obtain a [String] representation for each item.
	 * If the value is `null`, the [toString] function of the item is used instead.
	 * @see formatFunctionProperty
	 */
	var formatFunction: ((T) -> String)?
		get() = formatFunctionProperty.value
		set(value) {
			formatFunctionProperty.value = value
		}
	
	init {
		observableItemsList.addAll(items)
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ListView].
	 */
	companion object {
		/**
		 * Suggested [ListView] [height].
		 */
		const val DEFAULT_LISTVIEW_HEIGHT: Int = 400
		
		/**
		 * Suggested [ListView] [width].
		 */
		const val DEFAULT_LISTVIEW_WIDTH: Int = 200
	}
}