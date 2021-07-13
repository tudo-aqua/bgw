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