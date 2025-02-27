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

package tools.aqua.bgw.util

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.DEFAULT_FONT_SIZE
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.util.Font.FontWeight

/**
 * This class is used to represent a font. For more customization of fonts, the CSS feature can be
 * used.
 *
 * @constructor Creates a [Font].
 *
 * @property size Size of this Font in `px`. Default: 14.
 * @property color Color of this font. Default: [Color.BLACK].
 * @property family Font family as a String for this Font. Default: "Arial".
 * @property fontWeight Font weight for this Font. Default: [FontWeight.NORMAL].
 * @property fontStyle Font style for this Font. Default: [FontStyle.NORMAL].
 *
 * @see FontWeight
 * @see FontStyle
 *
 * @since 0.1
 */
data class Font(
    val size: Number = DEFAULT_FONT_SIZE,
    val color: Color = Color.BLACK,
    val family: String = "Arial",
    val fontWeight: FontWeight = FontWeight.NORMAL,
    val fontStyle: FontStyle = FontStyle.NORMAL
) {
  constructor(
      size: Number = DEFAULT_FONT_SIZE,
      awtColor: java.awt.Color,
      family: String = "Arial",
      fontWeight: FontWeight = FontWeight.NORMAL,
      fontStyle: FontStyle = FontStyle.NORMAL
  ) : this(
      size,
      Color(awtColor.red, awtColor.green, awtColor.blue, awtColor.alpha),
      family,
      fontWeight,
      fontStyle)

  /**
   * Enum class for representing all available font weights for the Font class.
   *
   * @see Font
   *
   * @since 0.1
   */
  enum class FontWeight {
    /**
     * Thin font weight.
     *
     * @since 0.10
     */
    THIN,
    /**
     * Extra light font weight.
     *
     * @since 0.10
     */
    EXTRA_LIGHT,
    /** Light font weight. */
    LIGHT,
    /** Normal font weight. */
    NORMAL,
    /**
     * Medium font weight.
     *
     * @since 0.10
     */
    MEDIUM,
    /** Semi-bold font weight. */
    SEMI_BOLD,
    /** Bold font weight. */
    BOLD,
    /**
     * Extra bold font weight.
     *
     * @since 0.10
     */
    EXTRA_BOLD,
    /**
     * Black font weight.
     *
     * @since 0.10
     */
    BLACK;

    /**
     * Returns the corresponding CSS font weight value for this [FontWeight].
     *
     * @return Corresponding CSS font weight value.
     */
    internal fun toInt(): Int =
        when (this) {
          THIN -> 100
          EXTRA_LIGHT -> 200
          LIGHT -> 300
          NORMAL -> 400
          MEDIUM -> 500
          SEMI_BOLD -> 600
          BOLD -> 700
          EXTRA_BOLD -> 800
          BLACK -> 900
        }
  }

  /**
   * Enum class for representing all available font styles for the Font class.
   *
   * @see Font
   *
   * @since 0.1
   */
  enum class FontStyle {
    /** Normal font style. */
    NORMAL,

    /** Italic font style. */
    ITALIC,

    /** Oblique font style. */
    OBLIQUE,
  }
}
