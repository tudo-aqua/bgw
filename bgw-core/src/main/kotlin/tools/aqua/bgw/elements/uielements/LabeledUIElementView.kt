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

@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Baseclass for all [UIElementView]s that have a label.
 *
 * @param height height for this [LabeledUIElementView].
 * @param width width for this [LabeledUIElementView].
 * @param posX horizontal coordinate for this [LabeledUIElementView].
 * @param posY vertical coordinate for this [LabeledUIElementView].
 * @param label label for this [LabeledUIElementView].
 * @param font font to be used for the [label].
 */
sealed class LabeledUIElementView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	font: Font,
	label: String
) : UIElementView(height = height, width = width, posX = posX, posY = posY, font = font) {
	/**
	 * Property for the label of this LabeledUIElementView.
	 */
	val labelProperty: StringProperty = StringProperty(label)
	
	/**
	 * Label of this LabeledUIElementView.
	 * @see labelProperty
	 */
	var label: String
		get() = labelProperty.value
		set(value) {
			labelProperty.value = value
		}
}