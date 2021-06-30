@file:Suppress("unused")

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all [ElementView]s that are considered static.
 *
 * This class is used to distinguish between [ElementView]s that can be used in [MenuScene]s
 * and those that can't.
 *
 * Only StaticViews are allowed in [MenuScene]s.
 *
 * @see MenuScene
 */
abstract class StaticView<T : ElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : ElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals)