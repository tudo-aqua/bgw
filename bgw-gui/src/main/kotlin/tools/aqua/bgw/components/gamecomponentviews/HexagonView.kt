package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.core.DEFAULT_HEXAGON_SIZE
import tools.aqua.bgw.visual.Visual

/**
 * A [HexagonView] represents a hexagonal shaped game component view.
 *
 * @constructor Creates a [HexagonView] with a given [Visual].
 *
 * @param posX Horizontal coordinate for this [HexagonView]. Default: 0.
 * @param posY Vertical coordinate for this [HexagonView]. Default: 0.
 * @param size Represents the diameter of a circle all six points of the [HexagonView] lie on. Default: [DEFAULT_HEXAGON_SIZE].
 * @param visual Visual for this [HexagonView].
 */
class HexagonView(
    posX: Number = 0,
    posY: Number = 0,
    val size: Number = DEFAULT_HEXAGON_SIZE,
    visual: Visual
) : GameComponentView(posX, posY, size, size, visual)