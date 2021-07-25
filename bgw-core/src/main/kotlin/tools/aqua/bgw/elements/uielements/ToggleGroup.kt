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

package tools.aqua.bgw.elements.uielements

/**
 * A [ToggleGroup] may be set as an attribute in [ToggleButton] or [RadioButton].
 * @see ToggleButton
 * @see RadioButton
 *
 * All [ToggleButton]s that keep the same instance of a [ToggleGroup] belong to that [ToggleGroup].
 * Only one [ToggleButton] may be selected in a [ToggleGroup].
 * This means whenever a [ToggleButton] changes its selected state to true,
 * all other [ToggleButton]s in the same [ToggleGroup] get deselected.
 *
 * An exception to this rule is, whenever a new [ToggleButton] that is currently selected gets added to the ToggleGroup.
 */
open class ToggleGroup {
	private val buttons: MutableList<ToggleButton> = mutableListOf()
	
	internal fun addButton(toggleButton: ToggleButton) {
		buttons.add(toggleButton)
	}
	
	internal fun removeButton(toggleButton: ToggleButton) {
		buttons.remove(toggleButton)
	}
	
	internal fun buttonSelectedStateChanged(toggleButton: ToggleButton) {
		if (toggleButton.isSelected)
			buttons.forEach { if (it != toggleButton) it.isSelected = false }
	}
}