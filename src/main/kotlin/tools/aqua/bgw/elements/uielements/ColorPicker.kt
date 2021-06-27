@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import java.awt.Color

/**
 * A color picker.
 *
 * @param height height for this ColorPicker. Default: suggested color picker height.
 * @param width width for this CheckBox. Default: suggested color picker width.
 * @param posX horizontal coordinate for this CheckBox. Default: 0.
 * @param posY vertical coordinate for this CheckBox. Default: 0.
 * @param initialColor the color that is initially selected. Default: WHITE.
 */
open class ColorPicker(
	height: Number = COLOR_PICKER_HEIGHT,
	width: Number = COLOR_PICKER_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	val initialColor: Color = Color.WHITE
) : UIElementView(height = height, width = width, posX = posX, posY = posY) {
	/**
	 * Defines some static constants that can be used as suggested properties of a color picker.
	 */
	companion object {
		/**
		 * Suggested color picker height.
		 */
		const val COLOR_PICKER_HEIGHT: Int = 40
		
		/**
		 * Suggested color picker width.
		 */
		const val COLOR_PICKER_WIDTH: Int = 100
	}
}