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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.util.Font

/**
 * A [TextField] is a single line input field.
 * Whenever user input occurs the [text] field gets updated.
 *
 * @param posX horizontal coordinate for this [TextField]. Default: 0.
 * @param posY vertical coordinate for this [TextField]. Default: 0.
 * @param width width for this [TextField]. Default: [TextField.DEFAULT_TEXT_FIELD_WIDTH].
 * @param height height for this [TextField]. Default: [TextField.DEFAULT_TEXT_FIELD_HEIGHT].
 * @param text initial text for this [TextField]. Default: empty String.
 * @param prompt Prompt for this [TextField]. This gets displayed as a prompt to the user whenever the label is an
 * empty string. Default: empty string.
 */
open class TextField(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_TEXT_FIELD_WIDTH,
	height: Number = DEFAULT_TEXT_FIELD_HEIGHT,
	text: String = "",
	font: Font = Font(),
	val prompt: String = "",
) : TextInputUIComponent(posX = posX, posY = posY, width = width, height = height, text = text, font = font) {
    /**
     * Defines some static constants that can be used as suggested properties of a [TextField].
     */
    companion object {
	    /**
	     * Suggested [TextField] [height].
	     */
	    const val DEFAULT_TEXT_FIELD_HEIGHT: Int = 30
	
	    /**
	     * Suggested [TextField] [width].
	     */
	    const val DEFAULT_TEXT_FIELD_WIDTH: Int = 140
    }
}