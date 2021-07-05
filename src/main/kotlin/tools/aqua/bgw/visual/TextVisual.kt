@file:Suppress("unused")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * A visual displaying text.
 *
 * @param text text to display.
 * @param font font to be used for the [text]
 */
open class TextVisual(text: String, font: Font = Font()) : SingleLayerVisual() {
	/**
	 * [Property] for the displayed [text].
	 */
	val textProperty: StringProperty = StringProperty(text)
	
	/**
	 * The displayed [text].
	 */
	var text: String
		get() = textProperty.value
		set(value) {
			textProperty.value = value
		}
	
	/**
	 * [Property] for the displayed [text] [Font].
	 */
	val fontProperty: ObjectProperty<Font> = ObjectProperty(font)
	
	/**
	 * The displayed [text] [Font].
	 */
	var font: Font
		get() = fontProperty.value
		set(value) {
			fontProperty.value = value
		}
	
	/**
	 * Copies this [TextVisual] to a new object.
	 */
	override fun copy(): TextVisual = TextVisual(text, font).apply { transparency = this@TextVisual.transparency }
}