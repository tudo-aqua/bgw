package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import java.awt.Color

/**
 * A solid color visual.
 *
 * @param color Color to use as filling
 */
class ColorVisual(color: Color) : SingleLayerVisual() {
	
	/**
	 * Property for the displayed color of this visual.
	 * The alpha channel gets multiplied with the transparency property i.e. alpha = 128 (50%)
	 * and transparency = 0.5 (50%) leads to 25% visibility / 75% transparency.
	 */
	val colorProperty = ObjectProperty(color)
	
	/**
	 * The displayed color of this visual.
	 * The alpha channel gets multiplied with the transparency property i.e. alpha = 128 (50%)
	 * and transparency = 0.5 (50%) leads to 25% visibility / 75% transparency.
	 */
	var color
		get() = colorProperty.value
		set(value) {
			colorProperty.value = value
		}
	
	/**
	 * A solid color visual.
	 *
	 * @param r Red channel
	 * @param g Green channel
	 * @param b Blue channel
	 * @param a Alpha channel. Default: 255
	 */
	constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(Color(r, g, b, a))
}