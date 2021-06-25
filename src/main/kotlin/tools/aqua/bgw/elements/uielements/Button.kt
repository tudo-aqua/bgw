package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A simple button with a label.
 *
 * @param height height for this Button. Default: 0.
 * @param width width for this Button. Default: 0.
 * @param posX horizontal coordinate for this Button. Default: 0.
 * @param posY vertical coordinate for this Button. Default: 0.
 * @param label label for this Button. Default: empty String.
 * @param font font to be used for the label. Default: default Font constructor.
 */
open class Button(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font(),
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label)