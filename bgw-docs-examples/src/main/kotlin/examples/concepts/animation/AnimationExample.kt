package examples.concepts.animation

import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

fun main() {
	AnimationExample()
}

class AnimationExample : BoardGameApplication("Visuals example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)
	
	private val imageFront: ImageVisual = ImageVisual(
		path = "card_deck.png",
		width = 130,
		height = 200,
		offsetX = 260,
		offsetY = 200
	)
	private val imageBack: ImageVisual = ImageVisual(
		path = "card_deck.png",
		width = 130,
		height = 200,
		offsetX = 260,
		offsetY = 800
	)
	private val dieVisuals: MutableList<Visual> = mutableListOf(
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 0, offsetY = 0),
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 200, offsetY = 0),
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 400, offsetY = 0),
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 600, offsetY = 0),
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 800, offsetY = 0),
		ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 1000, offsetY = 0),
	)
	private val randomCardFaces: MutableList<Visual> = mutableListOf(
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 0, offsetY = 0),
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 130, offsetY = 0),
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 0),
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 390, offsetY = 0),
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 520, offsetY = 0),
		ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 650, offsetY = 0),
	)
	
	private val buttonDelay: Button = Button(posX = 900, posY = 800, label = "Delay").apply {
		visual = ColorVisual.WHITE
	}
	private val buttonMovement: Button = Button(posX = 500, posY = 700, label = "Move").apply {
		visual = ColorVisual.WHITE
	}
	private val buttonRotation: Button = Button(posX = 700, posY = 700, label = "Rotate").apply {
		visual = ColorVisual.WHITE
	}
	private val buttonFlip: Button = Button(posX = 900, posY = 700, label = "Flip").apply {
		visual = ColorVisual.WHITE
	}
	private val buttonRandomize: Button = Button(posX = 1100, posY = 700, label = "Randomize").apply {
		visual = ColorVisual.WHITE
	}
	private val buttonDie: Button = Button(posX = 1300, posY = 700, label = "Roll").apply {
		visual = ColorVisual.WHITE
	}
	
	private val cardMovement: CardView = CardView(posX = 495, posY = 450, front = imageFront, back = imageBack)
	private val cardRotation: CardView = CardView(posX = 695, posY = 450, front = imageFront, back = imageBack)
	private val cardFlip: CardView = CardView(posX = 895, posY = 450, front = imageFront, back = imageBack)
	private val cardRandomize: CardView = CardView(posX = 1095, posY = 450, front = imageFront, back = imageBack)
	private val die: DiceView = DiceView(posX = 1320, posY = 500, visuals = dieVisuals)
	
	init {
		buttonDelay.onMouseClicked = {
			gameScene.lock()
			gameScene.playAnimation(DelayAnimation(duration = 2000).apply {
				onFinished = {
					println("Delay finished!")
					gameScene.unlock()
				}
			})
		}
		buttonMovement.onMouseClicked = {
			gameScene.playAnimation(
				MovementAnimation(
					componentView = cardMovement,
					byX = 0,
					byY = -50,
					duration = 1000
				)
			)
		}
		buttonRotation.onMouseClicked = {
			gameScene.playAnimation(
				RotationAnimation(
					componentView = cardRotation,
					byAngle = 180.0,
					duration = 1000
				)
			)
		}
		buttonFlip.onMouseClicked = {
			gameScene.playAnimation(
				FlipAnimation(
					componentView = cardFlip,
					fromVisual = cardFlip.backVisual,
					toVisual = cardFlip.frontVisual,
					duration = 1000
				)
			)
		}
		buttonRandomize.onMouseClicked = {
			gameScene.playAnimation(
				RandomizeAnimation(
					componentView = cardRandomize,
					visuals = randomCardFaces,
					toVisual = cardFlip.frontVisual,
					duration = 1000,
					speed = 50
				)
			)
		}
		buttonDie.onMouseClicked = {
			gameScene.playAnimation(
				DiceAnimation(
					dice = die,
					toSide = 3,
					duration = 1000,
					speed = 50
				)
			)
		}
		
		gameScene.addComponents(buttonDelay, buttonMovement, buttonRotation, buttonFlip, buttonRandomize, buttonDie)
		gameScene.addComponents(cardMovement, cardRotation, cardFlip, cardRandomize, die)
		showGameScene(gameScene)
		show()
	}
}