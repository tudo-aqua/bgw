@file:Suppress("unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.visual.Visual

/**
 * baseclass for all layout elements.
 *
 * @param height height for this [LayoutElement].
 * @param width width for this [LayoutElement].
 * @param posX horizontal coordinate for this [LayoutElement].
 * @param posY vertical coordinate for this [LayoutElement].
 * @param visual initial visual for this [LayoutElement].
 */
sealed class LayoutElement<T : ElementView>(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : StaticView<T>(height = height, width = width, posX = posX, posY = posY, visual = visual)