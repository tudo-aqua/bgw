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

import tools.aqua.bgw.core.DEFAULT_TEXT_AREA_HEIGHT
import tools.aqua.bgw.core.DEFAULT_TEXT_AREA_WIDTH
import tools.aqua.bgw.util.Font

/**
 * A [TextArea] is a multi line input field.
 *
 * Whenever user input occurs the [text] field gets updated.
 *
 * @constructor Creates a [TextArea].
 *
 * @param posX Horizontal coordinate for this [TextArea]. Default: 0.
 * @param posY Vertical coordinate for this [TextArea]. Default: 0.
 * @param width Width for this [TextArea]. Default: [DEFAULT_TEXT_AREA_WIDTH].
 * @param height Height for this [TextArea]. Default: [DEFAULT_TEXT_AREA_HEIGHT].
 * @param text Initial text for this [TextArea]. Default: empty String.
 * @param prompt Prompt for this [TextArea].
 *        This gets displayed as a prompt to the user whenever the label is an empty String.
 *        Default: empty String.
 *
 * @see TextField
 */
open class TextArea(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_TEXT_AREA_WIDTH,
	height: Number = DEFAULT_TEXT_AREA_HEIGHT,
	text: String = "",
	font: Font = Font(),
	prompt: String = "",
) : TextInputUIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	text = text,
	prompt = prompt,
	font = font)