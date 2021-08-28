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

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.DEFAULT_CHECKBOX_HEIGHT
import tools.aqua.bgw.core.DEFAULT_CHECKBOX_WIDTH
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A simple [CheckBox] with a [text].
 *
 * @constructor Creates a [CheckBox].
 *
 * @param posX Horizontal coordinate for this [CheckBox]. Default: 0.
 * @param posY Vertical coordinate for this [CheckBox]. Default: 0.
 * @param width Width for this [CheckBox]. Default: [DEFAULT_CHECKBOX_WIDTH].
 * @param height Height for this [CheckBox]. Default: [DEFAULT_CHECKBOX_HEIGHT].
 * @param text Text for this [CheckBox]. Default: empty String.
 * @param font [Font] to be used for the [text]. Default: default [Font] constructor.
 * @param alignment [Alignment] to be used for the [text] Default: [Alignment.CENTER].
 * @param isWrapText Defines if [text] should be wrapped. Default: `false`.
 * @param isChecked The initial checked state. Default: `false`.
 * @param allowIndeterminate The initial [allowIndeterminate] state. Default: `false`.
 * @param isIndeterminate The initial [isIndeterminate] state. Default: `false`.
 * @param visual Background [Visual]. Default: [Visual.EMPTY]
 */
open class CheckBox(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_CHECKBOX_WIDTH,
	height: Number = DEFAULT_CHECKBOX_HEIGHT,
	text: String = "",
	font: Font = Font(),
	alignment: Alignment = Alignment.CENTER,
	isWrapText: Boolean = false,
	visual: Visual = Visual.EMPTY,
	isChecked: Boolean = false,
	allowIndeterminate: Boolean = false,
	isIndeterminate: Boolean = false,
) : LabeledUIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	text = text,
	font = font,
	isWrapText = isWrapText,
	alignment = alignment,
	visual = visual) {
	/**
	 * [Property] for the checked state.
	 *
	 * @see checked
	 */
	val checkedProperty: BooleanProperty = BooleanProperty(isChecked)
	
	/**
	 * The checked state.
	 *
	 * @see checkedProperty
	 */
	var checked: Boolean
		get() = checkedProperty.value
		set(value) {
			checkedProperty.value = value
		}
	
	/**
	 * [Property] for whether this component allows an indeterminate state.
	 *
	 * @see allowIndeterminate
	 */
	val allowIndeterminateProperty: BooleanProperty = BooleanProperty(allowIndeterminate)
	
	/**
	 * [Boolean] whether this component allows an indeterminate state.
	 *
	 * @see allowIndeterminateProperty
	 */
	var allowIndeterminate: Boolean
		get() = allowIndeterminateProperty.value
		set(value) {
			allowIndeterminateProperty.value = value
		}
	
	/**
	 * [Property] for the indeterminate state.
	 *
	 * @see isIndeterminate
	 */
	val indeterminateProperty: BooleanProperty = BooleanProperty(isIndeterminate)
	
	/**
	 * [Boolean] whether this component in the indeterminate state.
	 *
	 * @see indeterminateProperty
	 */
	var isIndeterminate: Boolean
		get() = indeterminateProperty.value
		set(value) {
			indeterminateProperty.value = value
		}
}