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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.DEFAULT_CHECKBOX_HEIGHT
import tools.aqua.bgw.core.DEFAULT_CHECKBOX_WIDTH
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A simple [CheckBox] with a [text].
 *
 * @constructor Creates a [CheckBox].
 *
 * @param posX Horizontal coordinate for this [CheckBox]. Default: 0.
 * @param posY Vertical coordinate for this [CheckBox]. Default: 0.
 * @param width Width for this [CheckBox]. Default: [DEFAULT_CHECKBOX_WIDTH].
 * @param height Height for this [CheckBox]. Default: [DEFAULT_CHECKBOX_HEIGHT].
 * @param text Text for this [CheckBox]. Default: empty String.
 * @param font [Font] to be used for the [text]. Default: default [Font] constructor.
 * @param alignment [Alignment] to be used for the [text] Default: [Alignment.CENTER].
 * @param isWrapText Defines if [text] should be wrapped. Default: `false`.
 * @param visual Background [Visual]. Default: [Visual.EMPTY]
 * @param isChecked The initial checked state. Default: `false`.
 * @param allowIndeterminate The initial [isIndeterminateAllowed] state. Default: `false`.
 * @param isIndeterminate The initial [isIndeterminate] state. Default: `false`.
 *
 * @see Font
 * @see Alignment
 * @see Visual
 * @see LabeledUIComponent
 *
 * @since 0.1
 */
open class CheckBox(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_CHECKBOX_WIDTH,
    height: Number = DEFAULT_CHECKBOX_HEIGHT,
    text: String = "",
    font: Font = Font(),
    alignment: Alignment = Alignment.CENTER,
    isWrapText: Boolean = false,
    visual: Visual = Visual.EMPTY,
    isChecked: Boolean = false,
    allowIndeterminate: Boolean = false,
    isIndeterminate: Boolean = false,
) :
    LabeledUIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        text = text,
        font = font,
        isWrapText = isWrapText,
        alignment = alignment,
        visual = visual) {
  /**
   * [Property] for the checked state.
   *
   * @see isChecked
   */
  internal val isCheckedProperty: BooleanProperty = BooleanProperty(isChecked)

  /** [Boolean] whether this component is checked. */
  var isChecked: Boolean
    get() = isCheckedProperty.value
    set(value) {
      isCheckedProperty.value = value
    }

  /**
   * [Property] for whether this component allows an indeterminate state.
   *
   * @see isIndeterminateAllowed
   */
  internal val isIndeterminateAllowedProperty: BooleanProperty = BooleanProperty(allowIndeterminate)

  /** [Boolean] whether this component allows an indeterminate state. */
  var isIndeterminateAllowed: Boolean
    get() = isIndeterminateAllowedProperty.value
    set(value) {
      isIndeterminateAllowedProperty.value = value
    }

  /**
   * [Property] for the indeterminate state.
   *
   * @see isIndeterminate
   */
  internal val isIndeterminateProperty: BooleanProperty = BooleanProperty(isIndeterminate)

  /** [Boolean] whether this component is in the indeterminate state. */
  var isIndeterminate: Boolean
    get() = isIndeterminateProperty.value
    set(value) {
      isIndeterminateProperty.value = value
    }

  init {
    isCheckedProperty.internalListener = { _, new -> onCheckedChanged?.invoke(new) }

    isIndeterminateProperty.internalListener = { _, new -> onIndeterminateChanged?.invoke(new) }
  }

  /**
   * Gets invoked whenever this [CheckBox] changes the checked state.
   *
   * @see isChecked
   *
   * @since 0.10
   */
  var onCheckedChanged: ((Boolean) -> Unit)? = null

  /**
   * Gets invoked whenever this [CheckBox] changes the indeterminate state.
   *
   * @see isIndeterminate
   *
   * @since 0.10
   */
  var onIndeterminateChanged: ((Boolean) -> Unit)? = null
}
