@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font
import java.awt.Color

/**
 * A color picker that allows to chose a color.
 *
 * @param height height for this ColorPicker. Default: [ColorPicker.DEFAULT_COLOR_PICKER_HEIGHT].
 * @param width width for this ColorPicker. Default: [ColorPicker.DEFAULT_COLOR_PICKER_WIDTH].
 * @param posX horizontal coordinate for this ColorPicker. Default: 0.
 * @param posY vertical coordinate for this ColorPicker. Default: 0.
 * @param initialColor the color that is initially selected. Default: [Color.WHITE].
 */
open class ColorPicker(
	height: Number = DEFAULT_COLOR_PICKER_HEIGHT,
	width: Number = DEFAULT_COLOR_PICKER_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	val initialColor: Color = Color.WHITE
) : UIElementView(height = height, width = width, posX = posX, posY = posY, font = Font()) {
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ColorPicker].
	 */
	companion object {
		/**
		 * Suggested [ColorPicker] [height].
		 */
		const val DEFAULT_COLOR_PICKER_HEIGHT: Int = 30
		
		/**
		 * Suggested [ColorPicker] [width].
		 */
		const val DEFAULT_COLOR_PICKER_WIDTH: Int = 120
	}
}