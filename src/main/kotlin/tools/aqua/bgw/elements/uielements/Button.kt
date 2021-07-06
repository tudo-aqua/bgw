@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A simple button with a label.
 *
 * @param height height for this Button. Default: [Button.DEFAULT_BUTTON_HEIGHT].
 * @param width width for this Button. Default: [Button.DEFAULT_BUTTON_WIDTH].
 * @param posX horizontal coordinate for this Button. Default: 0.
 * @param posY vertical coordinate for this Button. Default: 0.
 * @param label label for this Button. Default: empty String.
 * @param font font to be used for the label. Default: default [Font] constructor.
 */
open class Button(
	height: Number = DEFAULT_BUTTON_HEIGHT,
	width: Number = DEFAULT_BUTTON_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font(),
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [Button].
	 */
	companion object {
		/**
		 * Suggested [Button] [height].
		 */
		const val DEFAULT_BUTTON_HEIGHT: Int = 45
		
		/**
		 * Suggested [Button] [width].
		 */
		const val DEFAULT_BUTTON_WIDTH: Int = 120
	}
}