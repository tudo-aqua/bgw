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

import tools.aqua.bgw.util.Font

/**
 * A [TextArea] is a multi line input field.
 * Whenever user input occurs the [text] field gets updated.
 *
 * @param height height for this [TextArea]. Default: [TextArea.DEFAULT_TEXTAREA_HEIGHT].
 * @param width width for this [TextArea]. Default: [TextArea.DEFAULT_TEXTAREA_WIDTH].
 * @param posX horizontal coordinate for this [TextArea]. Default: 0.
 * @param posY vertical coordinate for this [TextArea]. Default: 0.
 * @param text initial label for this [TextArea]. Default: empty String.
 * @param prompt Prompt for this [TextArea].
 *        This gets displayed as a prompt to the user whenever the label is an empty string.
 *        Default: empty string.
 */
open class TextArea(
	height: Number = DEFAULT_TEXTAREA_HEIGHT,
	width: Number = DEFAULT_TEXTAREA_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	text: String = "",
	font: Font = Font(),
	val prompt: String = "",
) : TextInputUIComponent(height = height, width = width, posX = posX, posY = posY, text = text, font = font) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [TextArea].
	 */
	companion object {
		/**
		 * Suggested [TextArea] [height].
		 */
		const val DEFAULT_TEXTAREA_HEIGHT: Int = 100
		
		/**
		 * Suggested [TextArea] [width].
		 */
		const val DEFAULT_TEXTAREA_WIDTH: Int = 200
	}
}