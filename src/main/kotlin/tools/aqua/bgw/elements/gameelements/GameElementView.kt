package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.Visual

/**
 * Abstract super class for game elements like cards or tokens.
 * This class is used to restrict the type argument of containers.
 * @see tools.aqua.bgw.elements.container
 */
sealed class GameElementView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual>
) : DynamicView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	visuals = visuals
) {
	/**
	 * {@inheritDoc}.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this Element has no children.")
	}
}