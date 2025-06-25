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

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.DEFAULT_COLOR_PICKER_HEIGHT
import tools.aqua.bgw.core.DEFAULT_COLOR_PICKER_WIDTH
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A [ColorPicker] that allows to choose a [Color].
 *
 * @param posX Horizontal coordinate for this [ColorPicker]. Default: 0.
 * @param posY Vertical coordinate for this [ColorPicker]. Default: 0.
 * @param width Width for this [ColorPicker]. Default: [DEFAULT_COLOR_PICKER_WIDTH].
 * @param height Height for this [ColorPicker]. Default: [DEFAULT_COLOR_PICKER_HEIGHT].
 * @param initialColor The [Color] that is initially selected. Default: [Color.WHITE].
 * @param visual [Visual] for this [ColorPicker]. Default: [Visual.EMPTY].
 * @constructor Creates a [ColorPicker].
 * @see Color
 * @see Visual
 * @see UIComponent
 * @since 0.1
 */
open class ColorPicker(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_COLOR_PICKER_WIDTH,
    height: Number = DEFAULT_COLOR_PICKER_HEIGHT,
    initialColor: Color = Color.WHITE,
    visual: Visual = Visual.EMPTY
) :
    UIComponent(
        posX = posX, posY = posY, width = width, height = height, font = Font(), visual = visual) {
  /**
   * [Property] for the currently selected [Color].
   *
   * @see selectedColor
   */
  internal val selectedColorProperty: Property<Color> = Property(initialColor)

  /** The currently selected [Color]. */
  var selectedColor: Color
    get() = selectedColorProperty.value
    set(value) {
      selectedColorProperty.value = value
    }

  init {
    selectedColorProperty.internalListener = { _, newColor -> onColorSelected?.invoke(newColor) }
  }

  /**
   * Gets invoked whenever the selected [Color] changes.
   *
   * @see selectedColor
   * @since 0.10
   */
  var onColorSelected: ((Color) -> Unit)? = null
}
