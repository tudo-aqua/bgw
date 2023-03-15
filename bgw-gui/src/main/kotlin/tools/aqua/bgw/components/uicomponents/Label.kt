/*
 * Copyright 2021-2023 The BoardGameWork Authors
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
import tools.aqua.bgw.core.DEFAULT_LABEL_HEIGHT
import tools.aqua.bgw.core.DEFAULT_LABEL_WIDTH
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A basic [Label] displaying text.
 *
 * @constructor Creates a [Label].
 *
 * @param posX Horizontal coordinate for this [Label]. Default: 0.
 * @param posY Vertical coordinate for this [Label]. Default: 0.
 * @param width Width for this [Label]. Default: [DEFAULT_LABEL_WIDTH].
 * @param height Height for this [Label]. Default: [DEFAULT_LABEL_HEIGHT].
 * @param text Text for this [Label]. Default: empty String.
 * @param font [Font] to be used for the [text]. Default: default [Font] constructor.
 * @param alignment Alignment to be used for the [text] Default: [Alignment.CENTER].
 * @param isWrapText Defines if [text] should be wrapped. Default: `false`.
 * @param visual Background [Visual]. Default: [Visual.EMPTY]
 */
open class Label(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_LABEL_WIDTH,
    height: Number = DEFAULT_LABEL_HEIGHT,
    text: String = "",
    font: Font = Font(),
    alignment: Alignment = Alignment.CENTER,
    isWrapText: Boolean = false,
    visual: Visual = Visual.EMPTY
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
