@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A basic label displaying text.
 *
 * @param height height for this [Label]. Default: 0.
 * @param width width for this [Label]. Default: 0.
 * @param posX horizontal coordinate for this [Label]. Default: 0.
 * @param posY vertical coordinate for this [Label]. Default: 0.
 * @param label label for this [Label]. Default: empty String.
 * @param font font to be used for the [label]. Default: default [Font] constructor.
 */
open class Label(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font()
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label)