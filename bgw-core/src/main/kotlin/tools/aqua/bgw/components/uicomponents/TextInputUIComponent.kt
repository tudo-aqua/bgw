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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Baseclass for all [UIComponent]s that have a text.
 *
 * @param height height for this [TextInputUIComponent].
 * @param width width for this [TextInputUIComponent].
 * @param posX horizontal coordinate for this [TextInputUIComponent].
 * @param posY vertical coordinate for this [TextInputUIComponent].
 * @param text text for this [TextInputUIComponent].
 * @param font font to be used for the [text].
 */
sealed class TextInputUIComponent(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	text: String,
	font: Font,
) : UIComponent(height = height, width = width, posX = posX, posY = posY, font = font) {
	/**
	 * Property for the text of this [TextInputUIComponent].
	 */
	val textProperty: StringProperty = StringProperty(text)
	
	/**
	 * Text of this [TextInputUIComponent].
	 * @see textProperty
	 */
	var text: String
		get() = textProperty.value
		set(value) {
			textProperty.value = value
		}
}