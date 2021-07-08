@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A basic [Label] displaying text.
 *
 * @param height height for this [Label]. Default: [Label.DEFAULT_LABEL_HEIGHT].
 * @param width width for this [Label]. Default: [Label.DEFAULT_LABEL_WIDTH].
 * @param posX horizontal coordinate for this [Label]. Default: 0.
 * @param posY vertical coordinate for this [Label]. Default: 0.
 * @param label label for this [Label]. Default: empty String.
 * @param font font to be used for the [label]. Default: default [Font] constructor.
 */
open class Label(
	height: Number = DEFAULT_LABEL_HEIGHT,
	width: Number = DEFAULT_LABEL_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font()
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, font = font, label = label) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [Label].
	 */
	companion object {
		/**
		 * Suggested [Label] [height].
		 */
		const val DEFAULT_LABEL_HEIGHT: Int = 30
		
		/**
		 * Suggested [Label] [width].
		 */
		const val DEFAULT_LABEL_WIDTH: Int = 120
	}
}