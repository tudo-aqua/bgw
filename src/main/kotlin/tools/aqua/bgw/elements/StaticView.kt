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
 * @param height height for this [StaticView].
 * @param width width for this [StaticView].
 * @param posX the X coordinate for this [StaticView] relative to its container.
 * @param posY the Y coordinate for this [StaticView] relative to its container.
 * @param visual visual for this [StaticView].
 *
 * @see MenuScene
 */
abstract class StaticView<T : ElementView>(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : ElementView(height = height, width = width, posX = posX, posY = posY, visual = visual)