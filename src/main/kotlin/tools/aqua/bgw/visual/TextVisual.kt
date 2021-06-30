@file:Suppress("unused")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * A visual displaying text.
 *
 * @param text text to display.
 * @param font font to be used for the [text]
 */
class TextVisual(text: String, font: Font = Font()) : SingleLayerVisual() {
	/**
	 * The property for the displayed text.
	 */
	val textProperty: StringProperty = StringProperty(text)
	
	/**
	 * The displayed text.
	 */
	var text: String
		get() = textProperty.value
		set(value) {
			textProperty.value = value
		}
	
	/**
	 * The property for the displayed text font.
	 */
	val fontProperty: ObjectProperty<Font> = ObjectProperty(font)
	
	/**
	 * The displayed text font.
	 */
	var font: Font
		get() = fontProperty.value
		set(value) {
			fontProperty.value = value
		}
}