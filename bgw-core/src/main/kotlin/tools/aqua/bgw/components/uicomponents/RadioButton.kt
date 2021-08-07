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
import tools.aqua.bgw.visual.Visual

/**
 * [RadioButton] is a subclass of [ToggleButton] with a different visual representation.
 * A [RadioButton] may be used as a [Button] that is either selected or not selected.
 * An important feature of [ToggleButton]s is the [ToggleGroup].
 *
 * [ToggleGroup]s can be used to group [ToggleButton]s.
 *
 * All [ToggleButton]s that keep the same instance of a [ToggleGroup] belong to that [ToggleGroup].
 * Only one [ToggleButton] may be selected in a [ToggleGroup].
 * This means whenever a [ToggleButton] changes its selected state to true,
 * all other [ToggleButton]s in the same [ToggleGroup] get deselected.
 *
 * An exception to this rule is, whenever a new [ToggleButton] that is currently selected gets added to the ToggleGroup.
 *
 * @see ToggleButton
 * @see ToggleGroup
 *
 * @param posX horizontal coordinate for this [RadioButton]. Default: 0.
 * @param posY vertical coordinate for this [RadioButton]. Default: 0.
 * @param width width for this [RadioButton]. Default: [RadioButton.DEFAULT_RADIOBUTTON_WIDTH].
 * @param height height for this [RadioButton]. Default: [RadioButton.DEFAULT_RADIOBUTTON_HEIGHT].
 * @param isSelected the initial state for this [RadioButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [RadioButton]. Default: null.
 * @param visual background [Visual]. Default: [Visual.EMPTY]
 */
open class RadioButton(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_RADIOBUTTON_WIDTH,
	height: Number = DEFAULT_RADIOBUTTON_HEIGHT,
	font: Font = Font(),
	isSelected: Boolean = false,
	toggleGroup: ToggleGroup? = null,
	visual: Visual = Visual.EMPTY
) : ToggleButton(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = font,
	isSelected = isSelected,
	toggleGroup = toggleGroup,
	visual = visual) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [RadioButton].
	 */
	companion object {
		/**
		 * Suggested [RadioButton] [height].
		 */
		const val DEFAULT_RADIOBUTTON_HEIGHT: Int = 45

		/**
		 * Suggested [RadioButton] [width].
		 */
		const val DEFAULT_RADIOBUTTON_WIDTH: Int = 45
	}
}