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

@file:Suppress("unused", "LongParameterList")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [UIComponent]s that have a text input field.
 *
 * @param posX Horizontal coordinate for this [TextInputUIComponent].
 * @param posY Vertical coordinate for this [TextInputUIComponent].
 * @param width Width for this [TextInputUIComponent].
 * @param height Height for this [TextInputUIComponent].
 * @param text Text for this [TextInputUIComponent].
 * @param prompt Prompt for this [TextInputUIComponent].
 * @param font Font to be used for the [text].
 */
sealed class TextInputUIComponent(
	posX: Number,
	posY: Number,
	width: Number,
	height: Number,
	text: String,
	prompt: String,
	font: Font,
) : UIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = font,
	visual = Visual.EMPTY) {
	
	/**
	 * [Property] for the text of this [TextInputUIComponent].
	 *
	 * @see text
	 */
	val textProperty: StringProperty = StringProperty(text)
	
	/**
	 * Text of this [TextInputUIComponent].
	 *
	 * @see textProperty
	 */
	var text: String
		get() = textProperty.value
		set(value) {
			textProperty.value = value
		}
	
	/**
	 * [Property] for the prompt of this [TextInputUIComponent].
	 *
	 * @see prompt
	 */
	val promptProperty: StringProperty = StringProperty(prompt)
	
	/**
	 * Prompt of this [TextInputUIComponent].
	 *
	 * @see promptProperty
	 */
	var prompt: String
		get() = promptProperty.value
		set(value) {
			promptProperty.value = value
		}
}
