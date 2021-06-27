@file:Suppress("unused")

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all ElementVies that are considered static.
 *
 * This class is used to distinguish between ElementViews that can be used in MenuScenes
 * and ElementViews that can not be used in MenuScenes.
 *
 * Only StaticViews are allowed in MenuScenes.
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