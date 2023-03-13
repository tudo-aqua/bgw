package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.visual.Visual

class HexagonView(
    posX: Number = 0,
    posY: Number = 0,
    size: Number = 0,
    visual: Visual
) : GameComponentView(posX, posY, size, size, visual)