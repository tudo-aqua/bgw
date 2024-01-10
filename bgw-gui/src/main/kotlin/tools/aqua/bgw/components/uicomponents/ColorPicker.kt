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

@file:Suppress("unused")

package tools.aqua.bgw.components.uicomponents

import java.awt.Color
import tools.aqua.bgw.core.DEFAULT_COLOR_PICKER_HEIGHT
import tools.aqua.bgw.core.DEFAULT_COLOR_PICKER_WIDTH
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A [ColorPicker] that allows to choose a [Color].
 *
 * @constructor Creates a [ColorPicker].
 *
 * @param posX Horizontal coordinate for this [ColorPicker]. Default: 0.
 * @param posY Vertical coordinate for this [ColorPicker]. Default: 0.
 * @param width Width for this [ColorPicker]. Default: [DEFAULT_COLOR_PICKER_WIDTH].
 * @param height Height for this [ColorPicker]. Default: [DEFAULT_COLOR_PICKER_HEIGHT].
 * @param initialColor The [Color] that is initially selected. Default: [Color.WHITE].
 */
open class ColorPicker(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_COLOR_PICKER_WIDTH,
    height: Number = DEFAULT_COLOR_PICKER_HEIGHT,
    initialColor: Color = Color.WHITE
) :
    UIComponent(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        font = Font(),
        visual = Visual.EMPTY) {
  /**
   * [Property] for the currently selected [Color].
   *
   * @see selectedColor
   */
  val selectedColorProperty: Property<Color> = Property(initialColor)

  /**
   * The currently selected [Color].
   *
   * @see selectedColorProperty
   */
  var selectedColor: Color
    get() = selectedColorProperty.value
    set(value) {
      selectedColorProperty.value = value
    }
}
