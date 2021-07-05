@file:Suppress("unused")

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.visual.Visual

/**
 * A [DiceView] may be used to visualize a dice.
 *
 * Visualization:
 * The Visual at the [currentSide] value is used to visualize the dice.
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
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visual = Visual.EMPTY) {
	
	/**
	 * [Visual]s for this [DiceView].
	 */
	internal val visuals: ObservableArrayList<Visual> = ObservableArrayList(visuals.onEach { it.copy() })
	
	/**
	 * Current side that is displayed, 0-based.
	 * If index is greater than the amount of visuals stored in [visuals] or negative,
	 * [Visual.EMPTY] will be displayed.
	 */
	var currentSide: Int = 0
		set(value) {
			if (field != value) {
				field = value
				
				visualProperty.value = if (value in visuals.indices)
					visuals[value]
				else
					Visual.EMPTY
			}
		}
	
	init {
		visualProperty.value = if (visuals.isNotEmpty())
			visuals.first()
		else
			Visual.EMPTY
	}
	
	/**
	 * Sets all visuals for this DiceView. Clears old visuals.
	 * All [visuals] get copied before being added.
	 * If [currentSide] is out of range, a [Visual.EMPTY] will be shown.
	 */
	fun setVisuals(visuals: List<Visual>) {
		this.visuals.setSilent(visuals.onEach { it.copy() })
		this.visuals.notifyChange()
		
		visualProperty.value = if (currentSide in visuals.indices)
			visuals[currentSide]
		else
			Visual.EMPTY
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [DiceView].
	 */
	companion object {
		/**
		 * Suggested [DiceView] [height].
		 */
		const val DICE_HEIGHT: Int = 80
		
		/**
		 * Suggested [DiceView] [width].
		 */
		const val DICE_WIDTH: Int = 80
	}
}