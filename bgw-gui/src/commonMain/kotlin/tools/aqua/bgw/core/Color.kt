/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.core

/**
 * Represents a color with red, green, blue and alpha values.
 *
 * @property red Red value. Must be in range 0 until 255.
 * @property green Green value. Must be in range 0 until 255.
 * @property blue Blue value. Must be in range 0 until 255.
 * @property alpha Alpha value. Must be in range 0.0 until 1.0.
 * @constructor Creates a [Color] with given red, green, blue and alpha values.
 * @since 0.10
 */
data class Color(val red: Int, val green: Int, val blue: Int, val alpha: Double) {

  /**
   * Creates a [Color] with given red, green and blue values.
   *
   * @param red Red value. Must be in range 0 until 255.
   * @param green Green value. Must be in range 0 until 255.
   * @param blue Blue value. Must be in range 0 until 255.
   */
  constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 1.0)

  /**
   * Creates a [Color] with given red, green, blue and alpha values.
   *
   * @param red Red value. Must be in range 0 until 255.
   * @param green Green value. Must be in range 0 until 255.
   * @param blue Blue value. Must be in range 0 until 255.
   * @param alpha Alpha value. Must be in range 0 until 255.
   */
  constructor(
      red: Int,
      green: Int,
      blue: Int,
      alpha: Int
  ) : this(red, green, blue, alpha.toDouble() / 255.0)

  /**
   * Creates a [Color] with given string hex value.
   *
   * @param hex Hexadecimal string representation of the color.
   */
  constructor(
      hex: String
  ) : this(
      hex.substring(1, 3).toInt(16), hex.substring(3, 5).toInt(16), hex.substring(5, 7).toInt(16))

  /**
   * Creates a [Color] with given numeric hex value.
   *
   * @param hex Hexadecimal numerical representation of the color.
   */
  constructor(hex: Int) : this((hex shr 16) and 0xFF, (hex shr 8) and 0xFF, hex and 0xFF)

  /**
   * Returns the hex string representation of the color.
   *
   * @return Hex string representation of the color.
   */
  internal fun toHex(): String {
    return "#${red.toString(16).padStart(2, '0')}${green.toString(16).padStart(2, '0')}${
            blue.toString(16).padStart(2, '0')
        }"
  }

  companion object {
    /** Transparent [Color] with values (0, 0, 0, 0.0) */
    val TRANSPARENT: Color = Color(0, 0, 0, alpha = 0.0)

    /** White [Color] with values (255, 255, 255, 1.0) */
    val WHITE: Color = Color(255, 255, 255)

    /** Light gray [Color] with values (192, 192, 192, 1.0) */
    val LIGHT_GRAY: Color = Color(192, 192, 192)

    /** Gray [Color] with values (128, 128, 128, 1.0) */
    val GRAY: Color = Color(128, 128, 128)

    /** Dark gray [Color] with values (64, 64, 64, 1.0) */
    val DARK_GRAY: Color = Color(64, 64, 64)

    /** Black [Color] with values (0, 0, 0, 1.0) */
    val BLACK: Color = Color(0, 0, 0)

    /** Red [Color] with values (255, 0, 0, 1.0) */
    val RED: Color = Color(255, 0, 0)

    /** Pink [Color] with values (255, 175, 175, 1.0) */
    val PINK: Color = Color(255, 175, 175)

    /** Orange [Color] with values (255, 200, 0, 1.0) */
    val ORANGE: Color = Color(255, 200, 0)

    /** Yellow [Color] with values (255, 255, 0, 1.0) */
    val YELLOW: Color = Color(255, 255, 0)

    /** Green [Color] with values (0, 255, 0, 1.0) */
    val GREEN: Color = Color(0, 255, 0)

    /** Lime [Color] with values (200, 255, 0) */
    val LIME: Color = Color(200, 255, 0)

    /** Magenta [Color] with values (255, 0, 255, 1.0) */
    val MAGENTA: Color = Color(255, 0, 255)

    /** Cyan [Color] with values (0, 255, 255, 1.0) */
    val CYAN: Color = Color(0, 255, 255)

    /** Blue [Color] with values (0, 0, 255, 1.0) */
    val BLUE: Color = Color(0, 0, 255)

    /** Purple [Color] with values (200, 0, 255, 1.0) */
    val PURPLE: Color = Color(200, 0, 255)

    /** Brown [Color] with values (139, 69, 19, 1.0) */
    val BROWN: Color = Color(139, 69, 19)
  }
}
