/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.visual

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.observable.properties.Property

/**
 * A solid color visual. Displays a rectangle filled with the given [color].
 *
 * @constructor Creates a solid [ColorVisual] filled with given [Color].
 *
 * @param color Color to use as filling.
 */
open class ColorVisual(color: Color) : SingleLayerVisual() {

  /**
   * [Property] for the displayed [Color] of this [Visual].
   *
   * The alpha channel gets multiplied with the [transparencyProperty] i.e. alpha = 128 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
   *
   * @see color
   */
  val colorProperty: Property<Color> = Property(color)

  /**
   * The displayed [Color] of this [Visual].
   *
   * The alpha channel gets multiplied with the [transparencyProperty] i.e. alpha = 128 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
   *
   * @see colorProperty
   */
  var color: Color
    get() = colorProperty.value
    set(value) {
      colorProperty.value = value
    }

  /**
   * Creates a solid [ColorVisual] filled with given RGBA values.
   *
   * The alpha channel gets multiplied with the [transparencyProperty] i.e. alpha = 128 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency. All values must be in
   * range 0 until 255 which corresponds to 00..FF in hexadecimal.
   *
   * @param r Red channel.
   * @param g Green channel.
   * @param b Blue channel.
   * @param a Alpha channel. Default: 255.
   */
  constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(Color(r, g, b, a))

  /** Copies this [ColorVisual] to a new object. */
  override fun copy(): ColorVisual =
      ColorVisual(Color(color.red, color.green, color.blue, color.alpha)).apply {
        transparency = this@ColorVisual.transparency
        style = this@ColorVisual.style
        filters = this@ColorVisual.filters
      }

  companion object {
    /** [ColorVisual] filled [WHITE] but completely opaque. */
    val TRANSPARENT: ColorVisual = ColorVisual(Color.WHITE).apply { transparency = 0.0 }

    /** [ColorVisual] filled [WHITE]. */
    val WHITE: ColorVisual = ColorVisual(Color.WHITE)

    /** [ColorVisual] filled [LIGHT_GRAY]. */
    val LIGHT_GRAY: ColorVisual = ColorVisual(Color.LIGHT_GRAY)

    /** [ColorVisual] filled [GRAY]. */
    val GRAY: ColorVisual = ColorVisual(Color.GRAY)

    /** [ColorVisual] filled [DARK_GRAY]. */
    val DARK_GRAY: ColorVisual = ColorVisual(Color.DARK_GRAY)

    /** [ColorVisual] filled [BLACK]. */
    val BLACK: ColorVisual = ColorVisual(Color.BLACK)

    /** [ColorVisual] filled [RED]. */
    val RED: ColorVisual = ColorVisual(Color.RED)

    /** [ColorVisual] filled [PINK]. */
    val PINK: ColorVisual = ColorVisual(Color.PINK)

    /** [ColorVisual] filled [ORANGE]. */
    val ORANGE: ColorVisual = ColorVisual(Color.ORANGE)

    /** [ColorVisual] filled [YELLOW]. */
    val YELLOW: ColorVisual = ColorVisual(Color.YELLOW)

    /** [ColorVisual] filled [GREEN]. */
    val GREEN: ColorVisual = ColorVisual(Color.GREEN)

    /** [ColorVisual] filled [MAGENTA]. */
    val MAGENTA: ColorVisual = ColorVisual(Color.MAGENTA)

    /** [ColorVisual] filled [CYAN]. */
    val CYAN: ColorVisual = ColorVisual(Color.CYAN)

    /** [ColorVisual] filled [BLUE]. */
    val BLUE: ColorVisual = ColorVisual(Color.BLUE)
  }
}
