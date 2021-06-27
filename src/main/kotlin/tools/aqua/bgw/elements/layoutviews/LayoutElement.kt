@file:Suppress("unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all layout elements.
 */
sealed class LayoutElement<T : ElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : StaticView<T>(height = height, width = width, posX = posX, posY = posY, visuals = visuals)