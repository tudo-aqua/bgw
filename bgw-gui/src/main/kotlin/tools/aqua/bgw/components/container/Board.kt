package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.core.DEFAULT_BOARD_HEIGHT
import tools.aqua.bgw.core.DEFAULT_BOARD_WIDTH
import tools.aqua.bgw.visual.Visual

open class Board<T : DynamicComponentView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_BOARD_WIDTH,
    height: Number = DEFAULT_BOARD_HEIGHT,
    visual: Visual = Visual.EMPTY,
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual) {
    /** Internal onRemove handler. */
    override fun T.onRemove() = Unit

    /** Internal onAdd handler. */
    override fun T.onAdd() = Unit
}