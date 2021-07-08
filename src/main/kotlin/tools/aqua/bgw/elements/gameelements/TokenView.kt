@file:Suppress("unused")

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.elements.gameelements.TokenView.Companion.DEFAULT_TOKEN_HEIGHT
import tools.aqua.bgw.elements.gameelements.TokenView.Companion.DEFAULT_TOKEN_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * A [TokenView] may be used to visualize any kind of token.
 *
 * Visualization:
 * The current [Visual] is used to visualize the token.
 *
 * @param height height for this TokenView. Default: [DEFAULT_TOKEN_HEIGHT].
 * @param width width for this TokenView. Default: [DEFAULT_TOKEN_WIDTH].
 * @param posX horizontal coordinate for this TokenView. Default: 0.
 * @param posY vertical coordinate for this TokenView. Default: 0.
 * @param visual visual for this TokenView.
 */
open class TokenView(
	height: Number = DEFAULT_TOKEN_HEIGHT,
	width: Number = DEFAULT_TOKEN_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [TokenView].
	 */
	companion object {
		/**
		 * Suggested [TokenView] [height].
		 */
		const val DEFAULT_TOKEN_HEIGHT: Int = 50
		
		/**
		 * Suggested [TokenView] [width].
		 */
		const val DEFAULT_TOKEN_WIDTH: Int = 50
	}
}