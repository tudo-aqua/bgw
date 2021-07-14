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

import tools.aqua.bgw.util.Font.FontStyle
import java.awt.Color

/**
 * This class is used to represent a font.
 * For more customization of fonts, the CSS feature can be used.
 *
 * @param size size of this Font in `pt`. May be a floating-point value. Default: 14.
 * @param color color of this font. Default: [java.awt.Color.BLACK].
 * @param family font family as a String for this Font. Default: "Arial".
 * @param fontStyle font style for this Font. Default: [FontStyle.REGULAR].
 *
 * @see FontStyle
 */
class Font(
	val size: Number = 14,
	val color: Color = Color.BLACK,
	val family: String = "Arial",
	val fontStyle: FontStyle = FontStyle.REGULAR
) {
	/**
	 * Enum class for representing all available font styles for the Font class.
	 * @see Font
	 */
	enum class FontStyle {
		/**
		 * Regular font style.
		 */
		REGULAR,
		
		/**
		 * Font style that is bolder than REGULAR but not as bold as BOLD.
		 */
		SEMI_BOLD,
		
		/**
		 * Bold font style.
		 */
		BOLD,
		
		/**
		 * Italic font style.
		 */
		ITALIC,
	}
}