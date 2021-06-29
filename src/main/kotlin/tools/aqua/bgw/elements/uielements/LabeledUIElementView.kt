@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Superclass for all UIElementViews that have a label.
 *
 * @param height height for this LabeledUIElementView. Default: 0.
 * @param width width for this LabeledUIElementView. Default: 0.
 * @param posX horizontal coordinate for this LabeledUIElementView. Default: 0.
 * @param posY vertical coordinate for this LabeledUIElementView. Default: 0.
 * @param label label for this LabeledUIElementView. Default: empty String.
 * @param font font to be used for the label. Default: default Font constructor.
 */
abstract class LabeledUIElementView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	font: Font,
	label: String = ""
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

	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this Element has no children.")
	}
}