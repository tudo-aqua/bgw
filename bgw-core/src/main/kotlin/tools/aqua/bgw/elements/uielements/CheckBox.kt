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

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font

/**
 * A simple [CheckBox] with a [label].
 *
 * @param height height for this [CheckBox]. Default: [CheckBox.DEFAULT_CHECKBOX_HEIGHT].
 * @param width width for this [CheckBox]. Default: [CheckBox.DEFAULT_CHECKBOX_WIDTH].
 * @param posX horizontal coordinate for this [CheckBox]. Default: 0.
 * @param posY vertical coordinate for this [CheckBox]. Default: 0.
 * @param label label for this [CheckBox]. Default: empty String.
 * @param font font to be used for the [label]. Default: default [Font] constructor.
 * @param isChecked the initial checked state. Default: `false`.
 * @param allowIndeterminate the initial [allowIndeterminate] state. Default: `false`.
 * @param isIndeterminate the initial [isIndeterminate] state. Default: `false`.
 */
open class CheckBox(
	height: Number = DEFAULT_CHECKBOX_HEIGHT,
	width: Number = DEFAULT_CHECKBOX_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font(),
	isChecked: Boolean = false,
	allowIndeterminate: Boolean = false,
	isIndeterminate: Boolean = false
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label) {
	
	/**
	 * [Property] for the checked state.
	 */
	val checkedProperty: BooleanProperty = BooleanProperty(isChecked)
	
	/**
	 * The checked state.
	 * @see checkedProperty
	 */
	var checked: Boolean
		get() = checkedProperty.value
		set(value) {
			checkedProperty.value = value
		}
	
	/**
	 * [Property] for whether this element allows an indeterminate state.
	 */
	val allowIndeterminateProperty: BooleanProperty = BooleanProperty(allowIndeterminate)
	
	/**
	 * [Boolean] whether this element allows an indeterminate state.
	 * @see allowIndeterminateProperty
	 */
	var allowIndeterminate: Boolean
		get() = allowIndeterminateProperty.value
		set(value) {
			allowIndeterminateProperty.value = value
		}
	
	/**
	 * [Property] for the indeterminate state.
	 */
	val indeterminateProperty: BooleanProperty = BooleanProperty(isIndeterminate)
	
	/**
	 * [Boolean] whether this element in in the indeterminate state.
	 * @see indeterminateProperty
	 */
	var isIndeterminate: Boolean
		get() = indeterminateProperty.value
		set(value) {
			indeterminateProperty.value = value
		}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [CheckBox].
	 */
	companion object {
		/**
		 * Suggested [CheckBox] [height].
		 */
		const val DEFAULT_CHECKBOX_HEIGHT: Int = 30
		
		/**
		 * Suggested [CheckBox] [width].
		 */
		const val DEFAULT_CHECKBOX_WIDTH: Int = 120
	}
	
}