package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.visual.Visual

/**
 * A TokenView may be used to visualize an kind of token.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how a TokenView is visualized by the BGW framework.
 *
 * @param height Height for this TokenView.
 * @param width Width for this TokenView.
 */
open class TokenView(
	height: Number,
	width: Number,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual
) : GameElementView(height = height, width = width, posX = posX, posY = posY, visuals = mutableListOf(visual))