@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Baseclass for all [UIElementView]s that have a label.
 *
 * @param height height for this [LabeledUIElementView].
 * @param width width for this [LabeledUIElementView].
 * @param posX horizontal coordinate for this [LabeledUIElementView].
 * @param posY vertical coordinate for this [LabeledUIElementView].
 * @param label label for this [LabeledUIElementView].
 * @param font font to be used for the label.
 */
sealed class LabeledUIElementView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	font: Font,
	label: String
) : UIElementView(height = height, width = width, posX = posX, posY = posY, font = font) {
	/**
	 * Property for the label of this LabeledUIElementView.
	 */
	val labelProperty: StringProperty = StringProperty(label)
	
	/**
	 * Label of this LabeledUIElementView.
	 * @see labelProperty
	 */
	var label: String
		get() = labelProperty.value
		set(value) {
			labelProperty.value = value
		}
}