package examples.components.container

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import javax.swing.text.ComponentView

fun main() {
	ContainerExample()
}

class ContainerExample : BoardGameApplication("Container Example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ImageVisual("bg.jpg"))

	private val cardStack: CardStack<CardView> = CardStack<CardView>(
		height = DEFAULT_CARD_HEIGHT * 1.2,
		width = DEFAULT_CARD_WIDTH * 1.2,
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
		height = DEFAULT_TOKEN_HEIGHT * 2,
		width = DEFAULT_TOKEN_WIDTH * 5,
		posX = 1010,
		posY = 50,
		visual = ColorVisual(255, 0, 0, 80)
	)

	private val linearLayout: LinearLayout<CardView> = LinearLayout<CardView>(
		height = DEFAULT_CARD_HEIGHT * 1.2,
		width = DEFAULT_CARD_WIDTH * 4,
		posX = 50,
		posY = 590,
		visual = ColorVisual(255, 0, 255, 80),
		spacing = -DEFAULT_CARD_WIDTH * 0.3,
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

	private val alignmentControls: Pane<UIComponent> = Pane<UIComponent>(posX = 50, posY = 335, width = 0, height = 0)
		.apply {
			add(Label(posY = 8, width = 220, text = "Vertical Alignment:", font = Font(20)))
			add(Label(posY = 68, width = 220, text = "Horizontal Alignment:", font = Font(20)))
			add(Label(posY = 128, width = 220, text = "LinearLayout Orientation:", font = Font(20)))
			add(Button(width = 80, posX = 220, text = "TOP").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(VerticalAlignment.TOP, cardStack.alignment.horizontalAlignment)
					linearLayout.alignment =
						Alignment.of(VerticalAlignment.TOP, linearLayout.alignment.horizontalAlignment)
				}
			})
			add(Button(width = 80, posX = 320, text = "CENTER").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(VerticalAlignment.CENTER, cardStack.alignment.horizontalAlignment)
					linearLayout.alignment =
						Alignment.of(VerticalAlignment.CENTER, linearLayout.alignment.horizontalAlignment)
				}
			})
			add(Button(width = 80, posX = 420, text = "BOTTOM").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(VerticalAlignment.BOTTOM, cardStack.alignment.horizontalAlignment)
					linearLayout.alignment =
						Alignment.of(VerticalAlignment.BOTTOM, linearLayout.alignment.horizontalAlignment)
				}
			})
			add(Button(width = 80, posX = 220, posY = 60, text = "LEFT").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.LEFT)
					linearLayout.alignment =
						Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.LEFT)
				}
			})
			add(Button(width = 80, posX = 320, posY = 60, text = "CENTER").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.CENTER)
					linearLayout.alignment =
						Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.CENTER)
				}
			})
			add(Button(width = 80, posX = 420, posY = 60, text = "RIGHT").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					cardStack.alignment =
						Alignment.of(cardStack.alignment.verticalAlignment, HorizontalAlignment.RIGHT)
					linearLayout.alignment =
						Alignment.of(linearLayout.alignment.verticalAlignment, HorizontalAlignment.RIGHT)
				}
			})
			add(Button(width = 130, posX = 220, posY = 120, text = "VERTICAL").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					linearLayout.orientation = Orientation.VERTICAL
				}
			})
			add(Button(width = 130, posX = 370, posY = 120, text = "HORIZONTAL").apply {
				visual = ColorVisual.LIGHT_GRAY
				onMouseClicked = {
					linearLayout.orientation = Orientation.HORIZONTAL
				}
			})

		}

	init {
		gameScene.addComponents(cardStack, alignmentControls, area, linearLayout, satchel,)
		showGameScene(gameScene)
		show()
	}

	private fun generateCards(row: Int, numCards: Int): Iterable<CardView> {
		require(row in 0..3)
		require(numCards in 0..13)
		val cardBackImageVisual = ImageVisual(
			path = "card_deck.png",
			height = DEFAULT_CARD_HEIGHT.toInt(),
			width = DEFAULT_CARD_WIDTH.toInt(),
			offsetX = DEFAULT_CARD_WIDTH.toInt() * 2,
			offsetY = DEFAULT_CARD_HEIGHT.toInt() * 4,
		)

		return (0 until numCards)
			.map { index ->
				ImageVisual(
					path = "card_deck.png",
					height = DEFAULT_CARD_HEIGHT.toInt(),
					width = DEFAULT_CARD_WIDTH.toInt(),
					offsetX = DEFAULT_CARD_WIDTH.toInt() * index,
					offsetY = DEFAULT_CARD_HEIGHT.toInt() * row
				)
			}
			.map { imageVisual ->
				CardView(front = imageVisual, back = cardBackImageVisual)
			}
	}
}