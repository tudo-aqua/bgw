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

@file:Suppress("unused", "LongParameterList")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for [ToggleButton]s and [RadioButton]s.
 *
 * @param posX Horizontal coordinate for this [BinaryStateButton].
 * @param posY Vertical coordinate for this [BinaryStateButton].
 * @param width Width for this [BinaryStateButton].
 * @param height Height for this [BinaryStateButton].
 * @param text Text to be displayed for this [BinaryStateButton].
 * @param font Font to be used for this [BinaryStateButton].
 * @param alignment Alignment to be used for the [text].
 * @param isWrapText Defines if [text] should be wrapped, if it exceeds the label's width.
 * @param isSelected The initial state for this [BinaryStateButton].
 * @param toggleGroup The ToggleGroup of this [BinaryStateButton].
 * @param visual Background [Visual].
 *
 * @see ToggleGroup
 */
sealed class BinaryStateButton(
    posX: Number,
    posY: Number,
    width: Number,
    height: Number,
    text: String,
    font: Font,
    alignment: Alignment,
    isWrapText: Boolean,
    isSelected: Boolean,
    toggleGroup: ToggleGroup,
    visual: Visual
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
        visual = visual
    ) {
    /**
     * The ToggleGroup of this ToggleButton.
     *
     * @see ToggleGroup
     */
    var toggleGroup: ToggleGroup = toggleGroup
        set(value) {
            toggleGroup.removeButton(this)
            value.addButton(this)
            field = value
        }

    /**
     * [Property] for the selected state of this [ToggleButton].
     *
     * @see isSelected
     */
    internal val selectedProperty: BooleanProperty = BooleanProperty(isSelected)

    /**
     * Selected state for this [ToggleButton].
     *
     * @see selectedProperty
     */
    var isSelected: Boolean
        get() = selectedProperty.value
        set(value) {
            selectedProperty.value = value
        }

    init {
        this.toggleGroup = toggleGroup

        selectedProperty.internalListener = { _, _ ->
            toggleGroup.buttonSelectedStateChanged(this)
            if(selectedProperty.value) {
                onSelected?.invoke()
            } else {
                onDeselected?.invoke()
            }
        }
    }

    /**
     * Gets called when this [BinaryStateButton] is selected.
     *
     * @see onDeselected
     */
    var onSelected: (() -> Unit)? = null

    /**
     * Gets called when this [BinaryStateButton] is deselected.
     *
     * @see onSelected
     */
    var onDeselected: (() -> Unit)? = null
}
