@file:Suppress("unused")

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.visual.Visual

/**
 * A TokenView may be used to visualize any kind of token.
 *
 * Visualization:
 * The current Visual is used to visualize the token.
 *
 * @param height height for this TokenView.
 * @param width width for this TokenView.
 * @param posX horizontal coordinate for this TokenView. Default: 0.
 * @param posY vertical coordinate for this TokenView. Default: 0.
 * @param visual visual for this TokenView.
 */
open class TokenView(
	height: Number,
	width: Number,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visuals = mutableListOf(visual))