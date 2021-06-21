package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A standard Label with text.
 */
open class Label(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font()
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label)