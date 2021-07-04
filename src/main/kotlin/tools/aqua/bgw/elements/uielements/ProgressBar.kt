@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import java.awt.Color

/**
 * A ProgressBar.
 *
 * @param height height for this [ProgressBar]. Default: 0.
 * @param width width for this [ProgressBar]. Default: 0.
 * @param posX horizontal coordinate for this [ProgressBar]. Default: 0.
 * @param posY vertical coordinate for this [ProgressBar]. Default: 0.
 * @param progress the initial progress of this [ProgressBar]. Default 0.
 * @param barColor the initial bar color of this [ProgressBar]. Default [Color.CYAN].
 */
open class ProgressBar(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	progress: Double = 0.0,
	barColor: Color = Color.CYAN
) : UIElementView(height = height, width = width, posX = posX, posY = posY) {
	
	/**
	 * [Property] for the progress state of this [ProgressBar].
	 * Should be in range of 0 to 1.
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress.
	 * Any value less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 */
	val progressProperty: DoubleProperty = DoubleProperty(progress)
	
	/**
	 * Progress state of this [ProgressBar].
	 * Should be in range of 0 to 1.
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress.
	 * Any value less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 * @see progressProperty
	 */
	var progress: Double
		get() = progressProperty.value
		set(value) {
			progressProperty.value = value
		}
	
	/**
	 * [Property] for the bar color of this [ProgressBar].
	 */
	val barColorProperty: ObjectProperty<Color> = ObjectProperty(barColor)
	
	/**
	 * Bar color of this [ProgressBar].
	 * @see barColorProperty
	 */
	var barColor: Color
		get() = barColorProperty.value
		set(value) {
			barColorProperty.value = value
		}
}