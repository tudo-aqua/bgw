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
 * @param height height for this cardView. Default: the suggested card height.
 * @param width width for this cardView. Default: the suggested card width.
 * @param posX horizontal coordinate for this CardView. Default: 0.
 * @param posY vertical coordinate for this CardView. Default: 0.
 * @param front visual to represent the front side of the card.
 * @param back visual to represent the back side of the card. Default: same Visual as front.
 */
open class CardView(
	height: Number = DEFAULT_CARD_HEIGHT,
	width: Number = DEFAULT_CARD_WIDTH,
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
	 * The property for the current side that is displayed.
	 *
	 * @see showFront
	 * @see showBack
	 */
	val currentSideProperty: ObjectProperty<CardSide> = ObjectProperty(CardSide.BACK)
	
	/**
	 * The current side that is displayed.
	 *
	 * @see showFront
	 * @see showBack
	 * @see currentSideProperty
	 */
	var currentSide
		get() = currentSideProperty.value
		set(value) {
			currentSideProperty.value = value
		}
	
	/**
	 * Front visual for this CardView.
	 */
	val front : Visual
		get() = visuals[0]
	
	/**
	 * Back visual for this CardView.
	 */
	val back : Visual
		get() = visuals[0]
	
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
		 * Suggested card height.
		 */
		const val DEFAULT_CARD_HEIGHT = 200
		
		/**
		 * Suggested card width.
		 */
		const val DEFAULT_CARD_WIDTH = 130
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