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

import IDGenerator

/**
 * A [ToggleGroup] may be set as an attribute in [ToggleButton] or [RadioButton].
 *
 * All Buttons that keep the same instance of a [ToggleGroup] belong to that [ToggleGroup]. Only one
 * Button may be selected in a [ToggleGroup]. This means whenever a Button changes its selected
 * state to `true`, all other Buttons in the same [ToggleGroup] get deselected.
 *
 * An exception to this rule is, whenever a new [ToggleButton] or [RadioButton] that is currently
 * selected gets added to the [ToggleGroup].
 *
 * @constructor Creates a [ToggleGroup].
 *
 * @see ToggleButton
 * @see RadioButton
 * @see BinaryStateButton
 *
 * @since 0.1
 */
open class ToggleGroup {
  internal val id = IDGenerator.generateToggleGroupID()

  internal val buttons: MutableList<BinaryStateButton> = mutableListOf()

  internal fun addButton(button: BinaryStateButton) {
    buttons.add(button)
  }

  internal fun removeButton(button: BinaryStateButton) {
    buttons.remove(button)
  }

  internal fun buttonSelectedStateChanged(button: BinaryStateButton) {
    if (button.isSelected)
        buttons.forEach {
          if (it != button) {
            it.isSelected = false
            onDeselected?.invoke(it)
          } else {
            onSelected?.invoke(it)
          }
        }
  }

  /**
   * Gets called when a [ToggleButton] or [RadioButton] gets selected.
   *
   * @see onDeselected
   *
   * @since 0.10
   */
  var onSelected: ((BinaryStateButton) -> Unit)? = null

  /**
   * Gets called when a [ToggleButton] or [RadioButton] gets deselected.
   *
   * @see onSelected
   *
   * @since 0.10
   */
  var onDeselected: ((BinaryStateButton) -> Unit)? = null
}
