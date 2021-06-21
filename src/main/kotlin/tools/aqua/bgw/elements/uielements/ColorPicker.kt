package tools.aqua.bgw.elements.uielements

import java.awt.Color

class ColorPicker(
	height: Number = SOPRA_COLOR_PICKER_HEIGHT,
	width: Number = SOPRA_COLOR_PICKER_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	val initialColor: Color = Color.WHITE
) : UIElementView(height = height, width = width, posX = posX, posY = posY) {
	/**
	 * Defines some static constants that can be used as suggested properties of a color picker.
	 */
	companion object {
		/**
		 * Suggested color picker height for the SoPra.
		 */
		const val SOPRA_COLOR_PICKER_HEIGHT = 40
		
		/**
		 * Suggested color picker width for the SoPra.
		 */
		const val SOPRA_COLOR_PICKER_WIDTH = 100
	}
}