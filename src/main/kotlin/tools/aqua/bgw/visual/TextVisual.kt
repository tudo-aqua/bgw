package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.StringProperty

/**
 * A visual displaying text.
 *
 * @param text text to display
 */
class TextVisual(text: String) : SingleLayerVisual() { //TODO: Add Font size here and in VisualBuilder
	/**
	 * The property for the displayed text.
	 */
	val textProperty = StringProperty(text)
	
	/**
	 * The displayed text.
	 */
	var text
		get() = textProperty.value
		set(value) {
			textProperty.value = value
		}
}