package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.gameelements.GameElementView

/**
 * An AreaContainerView may be used to visualize a zone containing GameElementViews.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how an areaContainerView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at visuals[0] is used to visualize a background.
 * The placement of the contained Elements is used to place them relative
 * to the top left corner of this areaContainerView.
 * Elements that are out of bounds for this areaContainerView will still get rendered.
 *
 * @param height Height for this AreaContainerView. Default: 0.
 * @param width Width for this AreaContainerView. Default: 0.
 * @param posX Horizontal coordinate for this AreaContainerView. Default: 0.
 * @param posY Vertical coordinate for this AreaContainerView. Default: 0.
 */
open class AreaContainerView<T : GameElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY)