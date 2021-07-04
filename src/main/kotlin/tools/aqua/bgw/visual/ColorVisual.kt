@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import java.awt.Color

/**
 * A solid color visual.
 * Displays a rectangle filled with the given [color].
 *
 * @param color color to use as filling.
 */
open class ColorVisual(color: Color) : SingleLayerVisual() {
	
	/**
	 * [Property] for the displayed [Color] of this [Visual].
	 * The alpha channel gets multiplied with the [transparencyProperty] i.e. alpha = 128 (50%)
	 * and [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
	 */
	val colorProperty: ObjectProperty<Color> = ObjectProperty(color)
	
	/**
	 * The displayed [Color] of this [Visual].
	 * The alpha channel gets multiplied with the [transparencyProperty] i.e. alpha = 128 (50%)
	 * and [transparency] = 0.5 (50%) leads to 25% visibility / 75% transparency.
	 */
	var color: Color
		get() = colorProperty.value
		set(value) {
			colorProperty.value = value
		}
	
	/**
	 * A solid color visual.
	 * Displays a rectangle filled with the given [color].
	 * All values must be in range 0 until 255 which corresponds to 00 until FF in hexadecimal.
	 *
	 * @param r red channel.
	 * @param g green channel.
	 * @param b blue channel.
	 * @param a alpha channel. Default: 255.
	 */
	constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(Color(r, g, b, a))
	
	companion object {
		/**
		 * [ColorVisual] filled [WHITE] but completely opaque.
		 */
		val TRANSPARENT: ColorVisual = ColorVisual(Color.WHITE).apply { transparency = 0.0 }
		
		/**
		 * [ColorVisual] filled [WHITE].
		 */
		val WHITE: ColorVisual = ColorVisual(Color.WHITE)
		
		/**
		 * [ColorVisual] filled [LIGHT_GRAY].
		 */
		val LIGHT_GRAY: ColorVisual = ColorVisual(Color.LIGHT_GRAY)
		
		/**
		 * [ColorVisual] filled [GRAY].
		 */
		val GRAY: ColorVisual = ColorVisual(Color.GRAY)
		
		/**
		 * [ColorVisual] filled [DARK_GRAY].
		 */
		val DARK_GRAY: ColorVisual = ColorVisual(Color.DARK_GRAY)
		
		/**
		 * [ColorVisual] filled [BLACK].
		 */
		val BLACK: ColorVisual = ColorVisual(Color.BLACK)
		
		/**
		 * [ColorVisual] filled [RED].
		 */
		val RED: ColorVisual = ColorVisual(Color.RED)
		
		/**
		 * [ColorVisual] filled [PINK].
		 */
		val PINK: ColorVisual = ColorVisual(Color.PINK)
		
		/**
		 * [ColorVisual] filled [ORANGE].
		 */
		val ORANGE: ColorVisual = ColorVisual(Color.ORANGE)
		
		/**
		 * [ColorVisual] filled [YELLOW].
		 */
		val YELLOW: ColorVisual = ColorVisual(Color.YELLOW)
		
		/**
		 * [ColorVisual] filled [GREEN].
		 */
		val GREEN: ColorVisual = ColorVisual(Color.GREEN)
		
		/**
		 * [ColorVisual] filled [MAGENTA].
		 */
		val MAGENTA: ColorVisual = ColorVisual(Color.MAGENTA)
		
		/**
		 * [ColorVisual] filled [CYAN].
		 */
		val CYAN: ColorVisual = ColorVisual(Color.CYAN)
		
		/**
		 * [ColorVisual] filled [BLUE].
		 */
		val BLUE: ColorVisual = ColorVisual(Color.BLUE)
	}
}