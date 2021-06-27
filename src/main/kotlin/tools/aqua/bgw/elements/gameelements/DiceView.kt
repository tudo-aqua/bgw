@file:Suppress("unused")

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.observable.IntegerProperty
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

/**
 * A DiceView may be used to visualize a dice.
 *
 * Visualization:
 * The Visual at the currentSide value is used to visualize the dice.
 *
 * @param height height for this DiceView. Default: the suggested dice height.
 * @param width width for this DiceView. Default: the suggested dice width.
 * @param posX horizontal coordinate for this DiceView. Default: 0.
 * @param posY vertical coordinate for this DiceView. Default: 0.
 * @param visuals list of visuals to represent the sides of the die.
 */
open class DiceView(
	height: Number = DICE_HEIGHT,
	width: Number = DICE_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual>
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals) {
	
	/**
	 * The property for the current Side that is displayed.
	 */
	val currentSideProperty: IntegerProperty = IntegerProperty(1)
	
	/**
	 * Current side that is displayed.
	 * @see currentSideProperty
	 */
	var currentSide: Int
		get() = currentSideProperty.value
		set(value) {
			currentSideProperty.value = value
		}
	
	/**
	 * Returns the current imageVisual for this DiceView.
	 *
	 * @see ImageVisual
	 */
	fun getCurrentImageVisual(): ImageVisual = visuals[currentVisual] as ImageVisual
	
	/**
	 * Sets the currentSide to be displayed.
	 */
	fun showSide(value: Int) {
		check(value <= visuals.size) { "Value is larger than side count" }
		currentSide = value
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a DiceView.
	 */
	companion object {
		/**
		 * Suggested DiceView height.
		 */
		const val DICE_HEIGHT: Int = 80
		
		/**
		 * Suggested DiceView width.
		 */
		const val DICE_WIDTH: Int = 80
	}
}