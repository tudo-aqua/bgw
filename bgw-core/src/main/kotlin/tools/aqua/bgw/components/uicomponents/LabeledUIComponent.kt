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
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Baseclass for all [UIComponent]s that have a label.
 *
 * @param height height for this [LabeledUIComponent].
 * @param width width for this [LabeledUIComponent].
 * @param posX horizontal coordinate for this [LabeledUIComponent].
 * @param posY vertical coordinate for this [LabeledUIComponent].
 * @param label label for this [LabeledUIComponent].
 * @param font font to be used for the [label].
 * @param alignment alignment to be used for the [label].
 */
sealed class LabeledUIComponent(
    height: Number,
    width: Number,
    posX: Number,
    posY: Number,
    label: String,
    font: Font,
    alignment: Alignment,
) : UIComponent(height = height, width = width, posX = posX, posY = posY, font = font) {
    /**
     * Property for the label of this [LabeledUIComponent].
     */
    val labelProperty: StringProperty = StringProperty(label)

    /**
     * Label of this [LabeledUIComponent].
     * @see labelProperty
     */
    var label: String
        get() = labelProperty.value
        set(value) {
            labelProperty.value = value
        }


    /**
     * Property for the alignment of this [LabeledUIComponent].
     */
    val alignmentProperty: Property<Alignment> = Property(alignment)

    /**
     * Alignment of this [LabeledUIComponent].
     * @see alignmentProperty
     */
    var alignment: Alignment
        get() = alignmentProperty.value
        set(value) {
            alignmentProperty.value = value
        }
}