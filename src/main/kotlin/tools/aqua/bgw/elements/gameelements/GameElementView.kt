@file:Suppress("unused")

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.Visual

/**
 * Abstract baseclass for game elements like [CardView]s or [TokenView]s.
 * This class is used to restrict the type argument of containers.
 *
 * @param height height for this [GameElementView].
 * @param width width for this [GameElementView].
 * @param posX horizontal coordinate for this [GameElementView].
 * @param posY vertical coordinate for this [GameElementView].
 * @param visual visual for this [GameElementView].
 *
 * @see tools.aqua.bgw.elements.container
 */
sealed class GameElementView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : DynamicView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	visual = visual
) {
	/**
	 * @throws RuntimeException if [GameElementView] does not support children.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this element has no children.")
	}
}