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

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [UIComponent]s that have a label.
 *
 * @param posX Horizontal coordinate for this [LabeledUIComponent].
 * @param posY Vertical coordinate for this [LabeledUIComponent].
 * @param width Width for this [LabeledUIComponent].
 * @param height Height for this [LabeledUIComponent].
 * @param text Label for this [LabeledUIComponent].
 * @param font [Font] to be used for the [text].
 * @param alignment Alignment to be used for the [text].
 * @param isWrapText Defines if [text] should be wrapped, if it exceeds the label's width.
 * @param visual Background [Visual].
 */
sealed class LabeledUIComponent(
    posX: Number,
    posY: Number,
    width: Number,
    height: Number,
    text: String,
    font: Font,
    alignment: Alignment,
    isWrapText: Boolean,
    visual: Visual
) : UIComponent(
    posX = posX,
    posY = posY,
    width = width,
    height = height,
    font = font,
    visual = visual) {

    /**
     * [Property] for the [isWrapText] state of this [Label].
     *
     * @see isWrapText
     */
    val isWrapTextProperty: BooleanProperty = BooleanProperty(isWrapText)

    /**
     * Defines if text should be wrapped, if it exceeds the [Label]'s [width].
     *
     * @see isWrapTextProperty
     */
    var isWrapText: Boolean
        get() = isWrapTextProperty.value
        set(value) {
            isWrapTextProperty.value = value
        }

    /**
     * [Property] for the label of this [LabeledUIComponent].
     *
     * @see text
     */
    val textProperty: StringProperty = StringProperty(text)

    /**
     * Label of this [LabeledUIComponent].
     *
     * @see textProperty
     */
    var text: String
        get() = textProperty.value
        set(value) {
            textProperty.value = value
        }


    /**
     * [Property] for the [Alignment] of this [LabeledUIComponent].
     *
     * @see alignment
     */
    val alignmentProperty: Property<Alignment> = Property(alignment)

    /**
     * [Alignment] of this [LabeledUIComponent].
     *
     * @see alignmentProperty
     */
    var alignment: Alignment
        get() = alignmentProperty.value
        set(value) {
            alignmentProperty.value = value
        }
}