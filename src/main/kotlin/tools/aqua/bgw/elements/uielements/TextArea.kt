@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A [TextArea] is a multi line input field.
 * Whenever user input occurs the [label] field gets updated.
 *
 * @param height height for this [TextArea]. Default: [TextArea.DEFAULT_TEXTAREA_HEIGHT].
 * @param width width for this [TextArea]. Default: [TextArea.DEFAULT_TEXTAREA_WIDTH].
 * @param posX horizontal coordinate for this [TextArea]. Default: 0.
 * @param posY vertical coordinate for this [TextArea]. Default: 0.
 * @param label initial label for this [TextArea]. Default: "TextArea".
 * @param prompt Prompt for this [TextArea].
 *        This gets displayed as a prompt to the user whenever the label is an empty string.
 *        Default: empty string.
 */
open class TextArea(
	height: Number = DEFAULT_TEXTAREA_HEIGHT,
	width: Number = DEFAULT_TEXTAREA_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "TextArea",
	font: Font = Font(),
	val prompt: String = "",
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, label = label, font = font) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [TextArea].
	 */
	companion object {
		/**
		 * Suggested [TextArea] [height].
		 */
		const val DEFAULT_TEXTAREA_HEIGHT: Int = 100
		
		/**
		 * Suggested [TextArea] [width].
		 */
		const val DEFAULT_TEXTAREA_WIDTH: Int = 200
	}
}