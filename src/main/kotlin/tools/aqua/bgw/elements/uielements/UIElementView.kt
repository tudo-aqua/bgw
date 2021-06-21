package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font

/**
 * Superclass for all UI elements.
 *
 * @param height Height for this View. Default: 0.
 * @param width Width for this View. Default: 0.
 * @param posX Horizontal coordinate for this View. Default: 0.
 * @param posY Vertical coordinate for this View. Default: 0.
 */
sealed class UIElementView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	font: Font = Font(),
) : StaticView<UIElementView>(height = height, width = width, posX = posX, posY = posY) {
	
	/**
	 * Property for the Font of this LabeledUIElementView.
	 * @see Font
	 */
	val fontProperty: ObjectProperty<Font> = ObjectProperty(font)
	
	/**
	 * Font of this LabeledUIElementView.
	 * @see Font
	 */
	var font: Font
		get() = fontProperty.value
		set(value) {
			fontProperty.value = value
		}
	
	/**
	 * Property for the css style of this UIElementView's background.
	 * Changes are rendered directly by the framework overriding other styles.
	 */
	val backgroundStyleProperty: StringProperty = StringProperty("")
	
	/**
	 * Css style to be applied to this UIElementView's background.
	 * Be aware that changes may override the framework's behaviour leading to critical failures or undefined behaviour.
	 */
	var backgroundStyle: String
		get() = backgroundStyleProperty.value
		set(value) {
			backgroundStyleProperty.value = value
		}
	
	/**
	 * Property for the css style of this UIElementView.
	 * Changes are rendered directly by the framework overriding other styles.
	 */
	val componentStyleProperty: StringProperty = StringProperty("")
	
	/**
	 * Css style to be applied to this UIElementView.
	 * Be aware that changes may override the framework's behaviour leading to critical failures or undefined behaviour.
	 */
	var componentStyle: String
		get() = componentStyleProperty.value
		set(value) {
			componentStyleProperty.value = value
		}
	
	/**
	 * {@inheritDoc}.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this Element has no children.")
	}
}