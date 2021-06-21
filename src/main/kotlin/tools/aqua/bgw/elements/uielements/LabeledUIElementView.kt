package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

sealed class LabeledUIElementView(
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
	 */
	var label: String
		get() = labelProperty.value
		set(value) {
			labelProperty.value = value
		}
	
	/**
	 * {@inheritDoc}.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this Element has no children.")
	}
}