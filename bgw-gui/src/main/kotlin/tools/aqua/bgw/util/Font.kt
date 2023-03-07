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

@file:Suppress("unused")

package tools.aqua.bgw.util

import java.awt.Color
import javafx.scene.text.Font
import tools.aqua.bgw.core.DEFAULT_FONT_SIZE
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.util.Font.FontWeight

/**
 * This class is used to represent a font. For more customization of fonts, the CSS feature can be
 * used.
 *
 * @constructor Creates a [Font].
 *
 * @property size Size of this Font in `px`. Maybe a floating-point value. Default: 14.
 * @property color Color of this font. Default: [java.awt.Color.BLACK].
 * @property family Font family as a String for this Font. Default: "Arial".
 * @property fontWeight Font weight for this Font. Default: [FontWeight.NORMAL].
 * @property fontStyle Font style for this Font. Default: [FontStyle.NORMAL].
 *
 * @see FontStyle
 */
data class Font(
    val size: Number = DEFAULT_FONT_SIZE,
    val color: Color = Color.BLACK,
    val family: String = "Arial",
    val fontWeight: FontWeight = FontWeight.NORMAL,
    val fontStyle: FontStyle = FontStyle.NORMAL
) {
  /**
   * Enum class for representing all available font weights for the Font class.
   * @see Font
   */
  enum class FontWeight {
    /** Light font weight. */
    LIGHT,

    /** Normal font weight. */
    NORMAL,

    /** Font style weight is bolder than [NORMAL] but not as bold as [BOLD]. */
    SEMI_BOLD,

    /** Bold font weight. */
    BOLD,
  }

  /**
   * Enum class for representing all available font styles for the Font class.
   *
   * @see Font
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
