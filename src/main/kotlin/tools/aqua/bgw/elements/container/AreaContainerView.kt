package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.visual.Visual

/**
 * An AreaContainerView may be used to visualize a zone containing GameElementViews.
 *
 * Visualization:
 * The current Visual is used to visualize a background.
 * The placement of the contained Elements is used to place them relative
 * to the top left corner of this AreaContainerView.
 * Elements that are out of bounds for this AreaContainerView will still get rendered.
 *
 * @param height geight for this AreaContainerView. Default: 0.
 * @param width width for this AreaContainerView. Default: 0.
 * @param posX horizontal coordinate for this AreaContainerView. Default: 0.
 * @param posY vertical coordinate for this AreaContainerView. Default: 0.
 * @param visual visual for this AreaContainerView. Default: empty Visual.
 */
open class AreaContainerView<T : GameElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual = Visual.EMPTY
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY, visuals = mutableListOf(visual))