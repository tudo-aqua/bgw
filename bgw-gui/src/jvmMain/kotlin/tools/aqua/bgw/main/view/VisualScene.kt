package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class VisualScene : BoardGameScene() {
    val CARD_WIDTH = 130
    val CARD_HEIGHT = 200

    val card = CardView(
        posX = 100,
        posY = 100,
        width = CARD_WIDTH,
        height = CARD_HEIGHT,
        front = ImageVisual(
            path = "card_deck.png",
        ),
        back = ColorVisual.RED
    ).apply {
        onMouseClicked = {
            this.flip()
        }
    }

    val cardStack = CardStack<CardView>(
        posX = 500,
        posY = 100,
        width = CARD_WIDTH,
        height = CARD_HEIGHT,
        visual = ColorVisual.LIGHT_GRAY,
        alignment = Alignment.TOP_LEFT
    ).apply {
        onMouseClicked = {
            this.pop()
        }
    }

    init {
        addComponents(cardStack, card)

        for (i in 0..5) {
            cardStack.add(CardView(
                posX = 0,
                posY = 0,
                width = CARD_WIDTH - i * 10,
                height = CARD_WIDTH - i * 10,
                front = when(i) {
                    0 -> ColorVisual.RED
                    1 -> ColorVisual.ORANGE
                    2 -> ColorVisual.YELLOW
                    3 -> ColorVisual.GREEN
                    4 -> ColorVisual.BLUE
                    5 -> ColorVisual.MAGENTA
                    else -> ColorVisual.BLACK
                },
                back = when(i) {
                    0 -> ColorVisual.RED
                    1 -> ColorVisual.ORANGE
                    2 -> ColorVisual.YELLOW
                    3 -> ColorVisual.GREEN
                    4 -> ColorVisual.BLUE
                    5 -> ColorVisual.MAGENTA
                    else -> ColorVisual.BLACK
                }
            ))
        }
    }
}