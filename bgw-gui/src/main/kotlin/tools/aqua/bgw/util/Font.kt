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

package tools.aqua.bgw.util

import javafx.scene.text.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.util.Font.FontWeight
import java.awt.Color

/**
 * This class is used to represent a font.
 * For more customization of fonts, the CSS feature can be used.
 *
 * @constructor Creates a [Font].
 *
 * @param size Size of this Font in `px`. Maybe a floating-point value. Default: 14.
 * @param color Color of this font. Default: [java.awt.Color.BLACK].
 * @param family Font family as a String for this Font. Default: "Arial".
 * @param fontWeight Font weight for this Font. Default: [FontWeight.NORMAL].
 * @param fontStyle Font style for this Font. Default: [FontStyle.NORMAL].
 *
 * @see FontStyle
 */
class Font(
	val size: Number = 14,
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
		/**
		 * Light font weight.
		 */
		LIGHT,
		
		/**
		 * Normal font weight.
		 */
		NORMAL,
		
		/**
		 * Font style weight is bolder than [NORMAL] but not as bold as [BOLD].
		 */
		SEMI_BOLD,
		
		/**
		 * Bold font weight.
		 */
		BOLD,
	}
	
	/**
	 * Enum class for representing all available font styles for the Font class.
	 *
	 * @see Font
	 */
	enum class FontStyle {
		/**
		 * Normal font style.
		 */
		NORMAL,
		
		/**
		 * Italic font style.
		 */
		ITALIC,
		
		/**
		 * Oblique font style.
		 */
		OBLIQUE,
	}
}