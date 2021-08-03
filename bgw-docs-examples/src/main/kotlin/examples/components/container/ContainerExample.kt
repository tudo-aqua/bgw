package examples.components.container

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

fun main() {
    ContainerExample()
}

class ContainerExample : BoardGameApplication("Container Example") {
    private val gameScene: BoardGameScene = BoardGameScene(background = ImageVisual("bg.jpg"))
    
    private val cardStack: CardStack<CardView> = CardStack<CardView>(
        height = CardView.DEFAULT_CARD_HEIGHT * 1.2,
        width = CardView.DEFAULT_CARD_WIDTH * 1.2,
        posX = 50,
        posY = 50,
        visual = ColorVisual(0, 0, 255, 80)
    ).apply {
        addAll(generateCards(0, 5).map {
            it.apply {
                showBack()
                isDraggable = true
                onDragGestureStarted = {
                    showFront()
                }
            }
        })
        dropAcceptor = {
            it.draggedComponent is CardView
        }
        onDragDropped = {
            add((it.draggedComponent as CardView).apply { showBack() })
        }
    }
    
    private val area: Area<TokenView> = Area(
        height = TokenView.DEFAULT_TOKEN_HEIGHT * 2,
        width = TokenView.DEFAULT_TOKEN_WIDTH * 5,
        posX = 1010,
        posY = 50,
        visual = ColorVisual(255, 0, 0, 80)
    )
    
    private val linearLayout: LinearLayout<CardView> = LinearLayout<CardView>(
        height = CardView.DEFAULT_CARD_HEIGHT * 1.2,
        width = CardView.DEFAULT_CARD_WIDTH * 4,
        posX = 50,
        posY = 590,
        visual = ColorVisual(255, 0, 255, 80),
        spacing = -CardView.DEFAULT_CARD_WIDTH * 0.3,
        alignment = Alignment.CENTER
    ).apply {
        addAll(generateCards(1, 4).map {
            it.apply {
                showFront()
                isDraggable = true
                onDragGestureStarted = {
                    showFront()
                }
            }
        })
        dropAcceptor = {
            it.draggedComponent is CardView
        }
        onDragDropped = {
            add((it.draggedComponent as CardView).apply { showFront() })
        }
    }
    
    private val satchel: Satchel<TokenView> = Satchel(
        height = 200,
        width = 200,
        posX = 1010,
        posY = 590,
        visual = ColorVisual(0, 255, 0, 80)
    )
    
    private val alignmentControls: Pane<UIComponent> = Pane<UIComponent>(posX = 50, posY = 335).apply {
        add(Label(posY = 8, width = 220, label = "Vertical Alignment:", font = Font(20)))
        add(Label(posY = 68, width = 220, label = "Horizontal Alignment:", font = Font(20)))
        add(Label(posY = 128, width = 220, label = "LinearLayout Orientation:", font = Font(20)))
        add(Button(width = 80, posX = 220, label = "TOP").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(VerticalAlignment.TOP, cardStack.alignment.horizontalAlignment)
                linearLayout.alignment = Alignment.of(VerticalAlignment.TOP, linearLayout.alignment.horizontalAlignment)
            }
        })
        add(Button(width = 80, posX = 320, label = "CENTER").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(VerticalAlignment.CENTER, cardStack.alignment.horizontalAlignment)
                linearLayout.alignment = Alignment.of(VerticalAlignment.CENTER, linearLayout.alignment.horizontalAlignment)
            }
        })
        add(Button(width = 80, posX = 420, label = "BOTTOM").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(VerticalAlignment.BOTTOM, cardStack.alignment.horizontalAlignment)
                linearLayout.alignment = Alignment.of(VerticalAlignment.BOTTOM, linearLayout.alignment.horizontalAlignment)
            }
        })
        add(Button(width = 80, posX = 220, posY = 60, label = "LEFT").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.LEFT)
                linearLayout.alignment = Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.LEFT)
            }
        })
        add(Button(width = 80, posX = 320, posY = 60, label = "CENTER").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.CENTER)
                linearLayout.alignment = Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.CENTER)
            }
        })
        add(Button(width = 80, posX = 420, posY = 60, label = "RIGHT").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                cardStack.alignment = Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.RIGHT)
                linearLayout.alignment = Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.RIGHT)
            }
        })
        add(Button(width = 130, posX = 220, posY = 120, label = "VERTICAL").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                linearLayout.orientation = Orientation.VERTICAL
            }
        })
        add(Button(width = 130, posX = 370, posY = 120, label = "HORIZONTAL").apply {
            visual = ColorVisual.LIGHT_GRAY
            onMouseClicked = {
                linearLayout.orientation = Orientation.HORIZONTAL
            }
        })

    }

    init {
        gameScene.addComponents(cardStack, alignmentControls, area, linearLayout, satchel)
        showGameScene(gameScene)
        show()
    }

    private fun generateCards(row: Int, numCards: Int) : Iterable<CardView> {
        require(row in 0..3)
        require(numCards in 0..13)
        val cardBackImageVisual = ImageVisual(
            path = "card_deck.png",
            height = CardView.DEFAULT_CARD_HEIGHT,
            width = CardView.DEFAULT_CARD_WIDTH,
            offsetX = CardView.DEFAULT_CARD_WIDTH * 2,
            offsetY = CardView.DEFAULT_CARD_HEIGHT * 4,
        )

        return (0 until numCards)
            .map { index ->
                println("offsetX: ${CardView.DEFAULT_CARD_WIDTH * index}, offsetY ${CardView.DEFAULT_CARD_HEIGHT * row}")
                ImageVisual(
                    path = "card_deck.png",
                    height = CardView.DEFAULT_CARD_HEIGHT,
                    width = CardView.DEFAULT_CARD_WIDTH,
                    offsetX = CardView.DEFAULT_CARD_WIDTH * index,
                    offsetY = CardView.DEFAULT_CARD_HEIGHT * row
                )
            }
            .map { imageVisual ->
                CardView(front = imageVisual, back = cardBackImageVisual)
            }
    }
}