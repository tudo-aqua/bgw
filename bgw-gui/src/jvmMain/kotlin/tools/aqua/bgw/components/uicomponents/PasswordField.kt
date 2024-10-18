/*
 * Copyright 2021-2024 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.DEFAULT_TEXT_FIELD_HEIGHT
import tools.aqua.bgw.core.DEFAULT_TEXT_FIELD_WIDTH
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [PasswordField] is a single line input field that shows stars instead of typed text.
 *
 * Whenever user input occurs the [text] field gets updated.
 *
 * @constructor Creates a [PasswordField].
 *
 * @param posX Horizontal coordinate for this [PasswordField]. Default: 0.
 * @param posY Vertical coordinate for this [PasswordField]. Default: 0.
 * @param width Width for this [PasswordField]. Default: [DEFAULT_TEXT_FIELD_WIDTH].
 * @param height Height for this [PasswordField]. Default: [DEFAULT_TEXT_FIELD_HEIGHT].
 * @param text Initial text for this [PasswordField]. Default: empty String.
 * @param prompt Prompt for this [PasswordField]. This gets displayed as a prompt to the user
 * whenever the label is an empty string. Default: empty string.
 * @param font [Font] to be used to display [text].
 * @param visual [Visual] to be used as a background. Defaults to a Light-gray [ColorVisual].
 *
 * @see TextField
 * @see TextArea
 */
open class PasswordField(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_TEXT_FIELD_WIDTH,
    height: Number = DEFAULT_TEXT_FIELD_HEIGHT,
    text: String = "",
    prompt: String = "",
    font: Font = Font(),
    visual: Visual = ColorVisual(Color(240, 240, 240))
) :
    TextInputUIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        text = text,
        prompt = prompt,
        font = font,
        visual = visual)
