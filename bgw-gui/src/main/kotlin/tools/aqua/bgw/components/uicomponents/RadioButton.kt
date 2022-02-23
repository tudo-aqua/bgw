/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

import tools.aqua.bgw.core.DEFAULT_RADIO_BUTTON_HEIGHT
import tools.aqua.bgw.core.DEFAULT_RADIO_BUTTON_WIDTH
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * [RadioButton] is analogous to a [ToggleButton] with a different visual representation but cannot
 * be deselected.
 *
 * A [RadioButton] may be used as a [Button] that is either selected or not selected. An important
 * feature of [RadioButton]s is the [ToggleGroup].
 *
 * [ToggleGroup]s can be used to group [RadioButton]s.
 *
 * All [RadioButton]s that keep the same instance of a [ToggleGroup] belong to that [ToggleGroup].
 * Only one [RadioButton] may be selected in a [ToggleGroup]. This means whenever a [RadioButton]
 * changes its selected state to `true`, all other [RadioButton]s in the same [ToggleGroup] get
 * deselected.
 *
 * An exception to this rule is, whenever a new [RadioButton] that is currently selected gets added
 * to the ToggleGroup.
 *
 * @constructor Creates a [RadioButton].
 *
 * @param posX Horizontal coordinate for this [RadioButton]. Default: 0.
 * @param posY Vertical coordinate for this [RadioButton]. Default: 0.
 * @param width Width for this [RadioButton]. Default: [DEFAULT_RADIO_BUTTON_WIDTH].
 * @param height Height for this [RadioButton]. Default: [DEFAULT_RADIO_BUTTON_HEIGHT].
 * @param font [Font] to be used to display text.
 * @param isSelected The initial state for this [RadioButton]. Default: `false`.
 * @param toggleGroup The ToggleGroup of this [RadioButton]. Default: empty group.
 * @param visual Background [Visual]. Default: [Visual.EMPTY]
 *
 * @see ToggleButton
 * @see ToggleGroup
 */
open class RadioButton(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_RADIO_BUTTON_WIDTH,
    height: Number = DEFAULT_RADIO_BUTTON_HEIGHT,
    font: Font = Font(),
    isSelected: Boolean = false,
    toggleGroup: ToggleGroup = ToggleGroup(),
    visual: Visual = Visual.EMPTY
) :
    BinaryStateButton(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        font = font,
        isSelected = isSelected,
        toggleGroup = toggleGroup,
        visual = visual)
