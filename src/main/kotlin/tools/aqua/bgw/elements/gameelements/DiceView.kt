package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.observable.IntegerProperty
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

/**
 * A DiceView may be used to visualize a dice of any sides.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how a DiceView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at the currentSide value is used to visualize the dice.
 *
 * @param height Height for this DiceView. Default: the suggested dice height for the SoPra.
 * @param width Width for this DiceView. Default: the suggested dice width for the SoPra.
 */
open class DiceView(
	height: Number = SOPRA_DICE_HEIGHT,
	width: Number = SOPRA_DICE_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual>
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals) {
	
	/**
	 * The property for the current Side that is displayed.
	 */
	val currentSideProperty: IntegerProperty = IntegerProperty(1)
	
	/**
	 * The current side that is displayed.
	 */
	var currentSide
		get() = currentSideProperty.value
		set(value) {
			currentSideProperty.value = value
		}
	
	/**
	 * Returns the current imageVisual for this cardView.
	 *
	 * @see ImageVisual
	 */
	fun getCurrentImageVisual() = visuals[currentVisual] as ImageVisual
	
	/**
	 * Sets the currentSide to be displayed.
	 */
	fun showSide(value: Int) {
		check(value <= visuals.size) { "Value is larger than side count" }
		
		currentSide = value
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a card.
	 */
	companion object {
		/**
		 * Suggested card height for the SoPra.
		 */
		const val SOPRA_DICE_HEIGHT = 80
		
		/**
		 * Suggested card width for the SoPra.
		 */
		const val SOPRA_DICE_WIDTH = 80
	}
}