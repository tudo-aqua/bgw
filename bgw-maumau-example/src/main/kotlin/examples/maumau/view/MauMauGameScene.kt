package examples.maumau.view

import examples.main.*
import examples.maumau.entity.CardSuit
import examples.maumau.entity.CardValue
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

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
	
	var otherPlayerHand: LinearLayout<CardView> = LinearLayout<CardView>(
		height = 220,
		width = 800,
		posX = 560,
		posY = 50,
		spacing = -50,
		alignment = Alignment.CENTER,
		visual = ColorVisual(255, 255, 255, 50)
	).apply {
		rotation = 180.0
	}
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
			buttonDiamonds,
			buttonHearts,
			buttonSpades,
			buttonClubs,
			hintButton,
			mainMenuButton
		)
	}
}