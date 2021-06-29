@file:Suppress("unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all layout elements.
 *
 * @param height height for this LayoutElement. Default: the suggested card height.
 * @param width width for this LayoutElement. Default: the suggested card width.
 * @param posX horizontal coordinate for this LayoutElement. Default: 0.
 * @param posY vertical coordinate for this LayoutElement. Default: 0.
 * @param visuals initial list of visuals for this LayoutElement. Default: empty list.
 */
sealed class LayoutElement<T : ElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = mutableListOf()
) : StaticView<T>(height = height, width = width, posX = posX, posY = posY, visuals = visuals)