package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * Represents a TextArea that can be written to.
 * Whenever user input occurs the textProperty field gets updated.
 *
 * @param height Height for this textArea. Default: 0.
 * @param width Width for this textArea. Default: 0.
 * @param posX Horizontal coordinate for this TextArea. Default: 0.
 * @param posY Vertical coordinate for this TextArea. Default: 0.
 * @param text Initial text for this textArea. Default: empty string.
 * @param prompt Prompt for this textArea.
 *        This gets displayed as a prompt to the user whenever the textProperty's value is an empty string.
 *        Default: empty string.
 */
open class TextArea(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	text: String = "",
	font: Font = Font(),
	val prompt: String = "",
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, label = text, font = font)