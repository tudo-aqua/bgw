@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView

/**
 * A movement animation.
 * Moves given [ElementView] relative to parents anchor point.
 *
 * @param elementView [ElementView] to animate
 * @param fromX initial X position. Default: Current PosX.
 * @param toX resulting X position. Default: Current PosX.
 * @param fromY initial Y position. Default: Current PosY.
 * @param toY resulting Y position. Default: Current PosY.
 * @param duration duration in milliseconds. Default: 1 second.
 */
class MovementAnimation<T : ElementView>(
	elementView: T,
	fromX: Number = elementView.posX,
	toX: Number = elementView.posX,
	fromY: Number = elementView.posY,
	toY: Number = elementView.posY,
	duration: Int = 1000
) : ElementAnimation<T>(element = elementView, duration = duration) {
	
	/**
	 * Initial X position.
	 */
	val fromX: Double = fromX.toDouble()
	
	/**
	 * Resulting X position.
	 */
	val toX: Double = toX.toDouble()
	
	/**
	 * Initial Y position.
	 */
	val fromY: Double = fromY.toDouble()
	
	/**
	 * Resulting Y position.
	 */
	val toY: Double = toY.toDouble()
	
	/**
	 * A movement animation.
	 * Moves given ElementView relative to parents anchor point.
	 *
	 * @param elementView GameElementView to animate
	 * @param byX Relative X movement.
	 * @param byY Relative Y movement.
	 * @param duration Animation duration in milliseconds. Default: 1 second
	 */
	constructor(elementView: T, byX: Number = 0.0, byY: Number = 0.0, duration: Int = 1000) : this(
		elementView = elementView,
		toX = elementView.posX + byX.toDouble(),
		toY = elementView.posY + byY.toDouble(),
		duration = duration
	)
	
	companion object {
		/**
		 * Creates a movement animation to another element's position.
		 * Moves given ElementView relative to parents anchor point.
		 *
		 * @param elementView GameElementView to animate
		 * @param toElementViewPosition Defines the destination element to move the given element to.
		 * @param scene The scene.
		 * @param duration Animation duration in milliseconds. Default: 1 second
		 */
		fun <T : ElementView> toElementView(
			elementView: T,
			toElementViewPosition: T,
			scene: Scene<*>,
			duration: Int = 1000
		): MovementAnimation<T> {
			//Find visual tree for elements and drop root node
			val pathToElement = scene.findPathToChild(elementView).dropLast(1)
			val pathToDestination = scene.findPathToChild(toElementViewPosition).dropLast(1)
			
			//Sum relative positions
			val elementAbsoluteX = pathToElement.sumOf { it.posX }
			val elementAbsoluteY = pathToElement.sumOf { it.posY }
			val destinationAbsoluteX = pathToDestination.sumOf { it.posX }
			val destinationAbsoluteY = pathToDestination.sumOf { it.posY }
			
			return MovementAnimation(
				elementView = elementView,
				byX = destinationAbsoluteX - elementAbsoluteX,
				byY = destinationAbsoluteY - elementAbsoluteY,
				duration = duration
			)
		}
	}
}