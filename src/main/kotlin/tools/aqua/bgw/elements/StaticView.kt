package tools.aqua.bgw.elements

import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all static views that can be used in both MenuScenes and BoardGameScenes.
 */
abstract class StaticView<T : ElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : ElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals)