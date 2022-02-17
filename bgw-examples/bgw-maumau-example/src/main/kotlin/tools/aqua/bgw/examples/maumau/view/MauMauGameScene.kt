package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.main.*
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MauMauGameScene : BoardGameScene(background = ImageVisual(BG_FILE)) {
	//region Player hands
	var currentPlayerHand: LinearLayout<CardView> = LinearLayout(
		height = 220,
		width = 800,
		posX = 560,
		posY = 750,
		spacing = -50,
		alignment = Alignment.CENTER,
		visual = ColorVisual(255, 255, 255, 50)
	)
	
	var otherPlayerHand: LinearLayout<CardView> = LinearLayout(
		height = 220,
		width = 800,
		posX = 560,
		posY = 50,
		spacing = -50,
		alignment = Alignment.CENTER,
		visual = ColorVisual(255, 255, 255, 50)
	)/*.apply {
		rotation = 180.0 //TODO: Fix MovementAnimation.toComponentView Bug and re-enable rotation
	}*/
	//endregion
	
	//region Stacks
	val drawStack: CardStack<CardView> = CardStack(
		height = 200,
		width = 130,
		posX = 750,
		posY = 360,
		visual = ColorVisual(255, 255, 255, 50)
	)
	val gameStack: CardStack<CardView> = CardStack(
		height = 200,
		width = 130,
		posX = 1040,
		posY = 360,
		visual = ColorVisual(255, 255, 255, 50)
	)
	val drawStackInfo: Label = Label(height = 40, width = 130, posX = 750, posY = 320)
	val gameStackInfo: Label = Label(height = 40, width = 130, posX = 1040, posY = 320)
	
	private val waitForOpponentLabel: Label =	Label(
		height = 50,
		width = (gameStack.posX + gameStack.width) - drawStack.posX,
		posX = drawStack.posX,
		posY = 620,
		font = Font(size = 26, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD)
	).apply { isVisible = false }
	//endregion stacks
	
	//region Jack selection
	val buttonClubs: Button = Button(
		height = 200,
		width = 130,
		posX = 820,
		posY = 250,
		visual = ImageVisual(
			CARDS_FILE,
			IMG_WIDTH,
			IMG_HEIGHT,
			CardValue.ACE.ordinal * IMG_WIDTH,
			CardSuit.CLUBS.ordinal * IMG_HEIGHT
		)
	)
	
	val buttonSpades: Button = Button(
		height = 200,
		width = 130,
		posX = 970,
		posY = 250,
		visual = ImageVisual(
			CARDS_FILE,
			IMG_WIDTH,
			IMG_HEIGHT,
			CardValue.ACE.ordinal * IMG_WIDTH,
			CardSuit.SPADES.ordinal * IMG_HEIGHT
		)
	)
	
	val buttonHearts: Button = Button(
		height = 200,
		width = 130,
		posX = 820,
		posY = 470,
		visual = ImageVisual(
			CARDS_FILE,
			IMG_WIDTH,
			IMG_HEIGHT,
			CardValue.ACE.ordinal * IMG_WIDTH,
			CardSuit.HEARTS.ordinal * IMG_HEIGHT
		)
	)
	
	val buttonDiamonds: Button = Button(
		height = 200,
		width = 130,
		posX = 970,
		posY = 470,
		visual = ImageVisual(
			CARDS_FILE,
			IMG_WIDTH,
			IMG_HEIGHT,
			CardValue.ACE.ordinal * IMG_WIDTH,
			CardSuit.DIAMONDS.ordinal * IMG_HEIGHT
		)
	)
	//endregion
	
	val hintButton: Button = Button(
		height = 80,
		width = 80,
		posX = 1430,
		posY = 820,
		font = Font(20.0, fontStyle = FontStyle.ITALIC, color = Color.WHITE),
		visual = ImageVisual(LIGHT_BULB_FILE)
	)
	
	val mainMenuButton: Button = Button(
		height = 100,
		width = 200,
		posX = 20,
		posY = 20,
		text = "Main menu",
		font = Font(20.0, fontStyle = FontStyle.ITALIC, color = Color.WHITE),
		visual = ImageVisual(BUTTON_BG_FILE)
	)
	
	private var dots = 1
	private val timer = Timer()
	
	init {
		buttonClubs.isVisible = false
		buttonSpades.isVisible = false
		buttonHearts.isVisible = false
		buttonDiamonds.isVisible = false
		
		addComponents(
			drawStack,
			gameStack,
			currentPlayerHand,
			otherPlayerHand,
			drawStackInfo,
			gameStackInfo,
			waitForOpponentLabel,
			buttonDiamonds,
			buttonHearts,
			buttonSpades,
			buttonClubs,
			hintButton,
			mainMenuButton
		)
		
		lockedProperty.addListener{ _, nV -> waitForOpponentLabel.isVisible = nV}
	}
	
	/**
	 * Starts the dot animation.
	 */
	fun startAnimation() {
		timer.scheduleAtFixedRate(
			delay = 0,
			period = 500
		) {
			BoardGameApplication.runOnGUIThread {
				waitForOpponentLabel.text = "Waiting for opponents turn" + ".".repeat(dots)
				dots %= 3
				dots++
			}
		}
	}
}