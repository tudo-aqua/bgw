package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A standard Button with text.
 */
open class Button(
	height: Number = SOPRA_BUTTON_HEIGHT,
	width: Number = SOPRA_BUTTON_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font(),
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label) {
	
	/**
	 * Defines some static constants that can be used as suggested properties of a button.
	 */
	companion object {
		/**
		 * Suggested button height for the SoPra.
		 */
		const val SOPRA_BUTTON_HEIGHT = 50
		
		/**
		 * Suggested button width for the SoPra.
		 */
		const val SOPRA_BUTTON_WIDTH = 100
	}
}