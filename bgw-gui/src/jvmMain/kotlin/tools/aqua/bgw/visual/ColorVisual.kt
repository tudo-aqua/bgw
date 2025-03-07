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

package tools.aqua.bgw.visual

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.observable.properties.Property

/**
 * A solid color visual. Displays a rectangle filled with the given [color].
 *
 * @constructor Creates a solid [ColorVisual] filled with given [Color].
 *
 * @param color Color to use as filling.
 *
 * @since 0.1
 */
open class ColorVisual(color: Color) : SingleLayerVisual() {
  /**
   * [Property] for the displayed [Color] of this [Visual].
   *
   * The alpha channel gets multiplied with the [transparency] i.e. alpha = 128 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
   *
   * @see color
   */
  internal val colorProperty: Property<Color> = Property(color)

  /**
   * The displayed [Color] of this [Visual].
   *
   * The alpha channel gets multiplied with the [transparency] i.e. alpha = 128 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
   */
  var color: Color
    get() = colorProperty.value
    set(value) {
      colorProperty.value = value
    }

  /**
   * Creates a solid [ColorVisual] filled with given RGBA values.
   *
   * The alpha channel gets multiplied with the [transparency] i.e. alpha = 0.5 (50%) and
   * [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
   *
   * @param r Red channel between 0 and 255.
   * @param g Green channel between 0 and 255.
   * @param b Blue channel between 0 and 255.
   * @param alpha Alpha channel (0 - 255 for Int, 0.0 - 1.0 for Double). Value is 1.0 by default and
   * if parameter is out of a valid range.
   *
   * @see SingleLayerVisual
   * @see CompoundVisual
   * @see TextVisual
   * @see ImageVisual
   *
   * @since 0.1
   */
  constructor(
      r: Int,
      g: Int,
      b: Int,
      alpha: Number = 1.0
  ) : this(
      Color(
          r,
          g,
          b,
          when (alpha) {
            is Int -> if (alpha in 0..255) alpha / 255.0 else 1.0
            is Double -> if (alpha in 0.0..1.0) alpha.toDouble() else 1.0
            else -> 1.0
          }))

  /**
   * Creates a solid [ColorVisual] filled with given [java.awt.Color]. This is only a fallback for
   * legacy BGW versions and will be removed in future versions.
   *
   * @param awtColor Color to use as filling.
   *
   * @see Color
   *
   * @since 0.10
   */
  @Deprecated("Using java.awt.Color is no longer recommended as of BGW 0.10.", ReplaceWith("Color"))
  constructor(
      awtColor: java.awt.Color
  ) : this(Color(awtColor.red, awtColor.green, awtColor.blue, awtColor.alpha))

  /** Copies this [ColorVisual] to a new object. */
  override fun copy(): ColorVisual =
      ColorVisual(Color(color.red, color.green, color.blue, color.alpha)).apply {
        transparency = this@ColorVisual.transparency
        style.applyDeclarations(this@ColorVisual.style)
        filters.applyDeclarations(this@ColorVisual.filters)
        flipped = this@ColorVisual.flipped
      }

  companion object {
    /** [ColorVisual] filled with [Color.WHITE] but completely opaque. */
    val TRANSPARENT: ColorVisual
      get() = ColorVisual(Color.WHITE).apply { transparency = 0.0 }

    /** [ColorVisual] filled with [Color.WHITE]. */
    val WHITE: ColorVisual
      get() = ColorVisual(Color.WHITE)

    /** [ColorVisual] filled with [Color.LIGHT_GRAY]. */
    val LIGHT_GRAY: ColorVisual
      get() = ColorVisual(Color.LIGHT_GRAY)

    /** [ColorVisual] filled with [Color.GRAY]. */
    val GRAY: ColorVisual
      get() = ColorVisual(Color.GRAY)

    /** [ColorVisual] filled with [Color.DARK_GRAY]. */
    val DARK_GRAY: ColorVisual
      get() = ColorVisual(Color.DARK_GRAY)

    /** [ColorVisual] filled with [Color.BLACK]. */
    val BLACK: ColorVisual
      get() = ColorVisual(Color.BLACK)

    /** [ColorVisual] filled with [Color.RED]. */
    val RED: ColorVisual
      get() = ColorVisual(Color.RED)

    /** [ColorVisual] filled with [Color.PINK]. */
    val PINK: ColorVisual
      get() = ColorVisual(Color.PINK)

    /** [ColorVisual] filled with [Color.ORANGE]. */
    val ORANGE: ColorVisual
      get() = ColorVisual(Color.ORANGE)

    /** [ColorVisual] filled with [Color.YELLOW]. */
    val YELLOW: ColorVisual
      get() = ColorVisual(Color.YELLOW)

    /** [ColorVisual] filled with [Color.GREEN]. */
    val GREEN: ColorVisual
      get() = ColorVisual(Color.GREEN)

    /** [ColorVisual] filled with [Color.LIME]. */
    val LIME: ColorVisual
      get() = ColorVisual(Color.LIME)

    /** [ColorVisual] filled with [Color.MAGENTA]. */
    val MAGENTA: ColorVisual
      get() = ColorVisual(Color.MAGENTA)

    /** [ColorVisual] filled with [Color.CYAN]. */
    val CYAN: ColorVisual
      get() = ColorVisual(Color.CYAN)

    /** [ColorVisual] filled with [Color.BLUE]. */
    val BLUE: ColorVisual
      get() = ColorVisual(Color.BLUE)

    /** [ColorVisual] filled with [Color.PURPLE]. */
    val PURPLE: ColorVisual
      get() = ColorVisual(Color.PURPLE)

    /** [ColorVisual] filled with [Color.BROWN]. */
    val BROWN: ColorVisual
      get() = ColorVisual(Color.BROWN)
  }
}
