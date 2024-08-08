package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
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

    // This LinearLayout is used to display the player's hand
    private val playerHand = LinearLayout<CardView>(
        posX = 0,
        posY = 100,
        width = 1920,
        height = 200,
        alignment = Alignment.TOP_LEFT,
        spacing = 50
    ).apply {
        onMouseClicked = {
            orientation = if (orientation == Orientation.HORIZONTAL) {
                Orientation.VERTICAL
            } else {
                Orientation.HORIZONTAL
            }
            spacing *= -1
        }
    }

    init {
        addComponents(playerHand)

        for (i in 0 until 5) {
            playerHand.add(CardView(
                width = 200,
                height = 200,
                front = ColorVisual(Color(i * 20, i * 20, i * 20)),
            ))
        }
    }
}