package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.visual.Visual

/**
 * A CardView may be used to visualize a card.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how a cardView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at the currentSide value is used to visualize the card.
 *
 * @param height Height for this cardView. Default: the suggested card height for the SoPra.
 * @param width Width for this cardView. Default: the suggested card width for the SoPra.
 * @param front Visual to represent the front side of the card.
 * @param back Visual to represent the back side of the card. Default: same Visual as front.
 */
open class CardView(
	height: Number = SOPRA_CARD_HEIGHT,
	width: Number = SOPRA_CARD_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	front: Visual,
	back: Visual = front
) : GameElementView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	visuals = mutableListOf(front, back)
) {
	
	/**
	 * The property for the current Side that is displayed.
	 *
	 * @see showFront
	 * @see showBack
	 */
	val currentSideProperty: ObjectProperty<CardSide> = ObjectProperty(CardSide.BACK)
	
	/**
	 * The current Side that is displayed.
	 *
	 * @see showFront
	 * @see showBack
	 */
	var currentSide
		get() = currentSideProperty.value
		set(value) {
			currentSideProperty.value = value
		}
	
	/**
	 * Returns the front visual for this cardView.
	 */
	fun getFront() = visuals[0]
	
	/**
	 * Returns the back visual for this cardView.
	 */
	fun getBack() = visuals[1]
	
	/**
	 * Sets the currentSide to be displayed to front.
	 */
	fun showFront() {
		currentSide = CardSide.FRONT
	}
	
	/**
	 * Sets the currentSide to be displayed to back.
	 */
	fun showBack() {
		currentSide = CardSide.BACK
	}
	
	/**
	 * Sets the currentSide to the parameter value.
	 */
	fun showCardSide(side: CardSide) {
		currentSide = side
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a card.
	 */
	companion object {
		/**
		 * Suggested card height for the SoPra.
		 */
		const val SOPRA_CARD_HEIGHT = 200
		
		/**
		 * Suggested card width for the SoPra.
		 */
		const val SOPRA_CARD_WIDTH = 130
	}
	
	/**
	 * Enum for the card sides FRONT and BACK with their visual indices.
	 *
	 * @param visual Corresponding visual index of this card side
	 */
	enum class CardSide(val visual: Int) {
		/**
		 * The FRONT side.
		 */
		FRONT(0),
		
		/**
		 * The BACK side.
		 */
		BACK(1)
	}
}