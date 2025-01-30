/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.DEFAULT_BUTTON_HEIGHT
import tools.aqua.bgw.core.DEFAULT_BUTTON_WIDTH
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A simple [Button] with a [text].
 *
 * @constructor Creates a [Button].
 *
 * @param posX Horizontal coordinate for this [Button]. Default: 0.
 * @param posY Vertical coordinate for this [Button]. Default: 0.
 * @param width Width for this [Button]. Default: [DEFAULT_BUTTON_WIDTH].
 * @param height Height for this [Button]. Default: [DEFAULT_BUTTON_HEIGHT].
 * @param text Text for this [Button]. Default: empty String.
 * @param font Font to be used for the [text]. Default: default [Font] constructor.
 * @param alignment Alignment to be used for the [text] Default: [Alignment.CENTER].
 * @param isWrapText Defines if [text] should be wrapped. Default: `false`.
 * @param visual Visual for this button. Default: [ColorVisual.WHITE]
 *
 * @since 0.1
 *
 * @sample tools.aqua.bgw.main.examples.ExampleUIScene.button
 * @sample tools.aqua.bgw.main.examples.ExampleUIScene.button2
 */
open class Button(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_BUTTON_WIDTH,
    height: Number = DEFAULT_BUTTON_HEIGHT,
    text: String = "",
    font: Font = Font(),
    alignment: Alignment = Alignment.CENTER,
    isWrapText: Boolean = false,
    visual: Visual = ColorVisual.WHITE
) :
    LabeledUIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        text = text,
        font = font,
        alignment = alignment,
        isWrapText = isWrapText,
        visual = visual)
