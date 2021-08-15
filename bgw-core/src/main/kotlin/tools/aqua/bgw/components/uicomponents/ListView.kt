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

import tools.aqua.bgw.core.DEFAULT_LISTVIEW_HEIGHT
import tools.aqua.bgw.core.DEFAULT_LISTVIEW_WIDTH
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A [ListView] displaying its items next to each other in the given orientation.
 *
 * The [formatFunction] is used to gain a [String] representation of each item.
 * If no [formatFunction] is specified the [toString] function gets used instead.
 *
 * @param posX Horizontal coordinate for this [ListView]. Default: 0.
 * @param posY Vertical coordinate for this [ListView]. Default: 0.
 * @param width Width for this [ListView]. Default: [DEFAULT_LISTVIEW_WIDTH].
 * @param height Height for this [ListView]. Default: [DEFAULT_LISTVIEW_HEIGHT].
 * @param items Initial list of items for this [ListView]. Default: empty list.
 * @param font [Font] to be used for this [ListView]. Default: default [Font] constructor.
 * @param orientation Orientation for this [ListView]. Default: [Orientation.VERTICAL].
 * @param formatFunction The [formatFunction] that is used to represent the items. Default: `null`.
 */
open class ListView<T>(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_LISTVIEW_WIDTH,
	height: Number = DEFAULT_LISTVIEW_HEIGHT,
	items: List<T> = listOf(),
	font: Font = Font(),
	orientation: Orientation = Orientation.VERTICAL,
	formatFunction: ((T) -> String)? = null
) : UIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = font,
	visual = Visual.EMPTY) {
	/**
	 * [Property] for the items [List] for this [ListView].
	 *
	 * @see items
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items list for [List] [ListView].
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
	 * [Property] for the [formatFunction] that gets used to obtain a [String] representation for each item.
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
	}
}