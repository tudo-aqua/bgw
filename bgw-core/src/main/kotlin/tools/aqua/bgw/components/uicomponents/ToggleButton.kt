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

import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A [ToggleButton] may be used as a [Button] that is either selected or not selected.
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
 * @see ToggleGroup
 *
 * @param posX horizontal coordinate for this [ToggleButton]. Default: 0.
 * @param posY vertical coordinate for this [ToggleButton]. Default: 0.
 * @param width width for this [ToggleButton]. Default: [ToggleButton.DEFAULT_TOGGLE_BUTTON_WIDTH].
 * @param height height for this [ToggleButton]. Default: [ToggleButton.DEFAULT_TOGGLE_BUTTON_HEIGHT].
 * @param font font to be used for this [ToggleButton]. Default: default [Font] constructor.
 * @param isSelected the initial state for this [ToggleButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [ToggleButton]. Default: null.
 * @param visual background [Visual]. Default: [Visual.EMPTY]
 */
open class ToggleButton(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_TOGGLE_BUTTON_WIDTH,
	height: Number = DEFAULT_TOGGLE_BUTTON_HEIGHT,
	font: Font = Font(),
	isSelected: Boolean = false,
	toggleGroup: ToggleGroup? = null,
	visual: Visual = Visual.EMPTY
) : UIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = font,
	visual = visual) {
	/**
	 * The ToggleGroup of this ToggleButton.
	 * @see ToggleGroup
	 */
	var toggleGroup: ToggleGroup? = toggleGroup
		set(value) {
			toggleGroup?.removeButton(this)
			value?.addButton(this)
			field = value
		}
	
	/**
	 * Property for the selected state of this ToggleButton.
	 */
	val selectedProperty: BooleanProperty = BooleanProperty(isSelected)
	
	/**
	 * Selected state for this ToggleButton.
	 * @see selectedProperty
	 */
	var isSelected: Boolean
		get() = selectedProperty.value
		set(value) {
			selectedProperty.value = value
		}
	
	init {
		this.toggleGroup = toggleGroup
		selectedProperty.internalListener = { _, _ -> toggleGroup?.buttonSelectedStateChanged(this) }
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ToggleButton].
	 */
	companion object {
		/**
		 * Suggested [ToggleButton] [height].
		 */
		const val DEFAULT_TOGGLE_BUTTON_HEIGHT: Int = 45
		
		/**
		 * Suggested [ToggleButton] [width].
		 */
		const val DEFAULT_TOGGLE_BUTTON_WIDTH: Int = 120
	}
}

