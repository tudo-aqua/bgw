package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.main.*
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
	
	val token1 = TokenView(posX = 50, posY = 50, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		
		dropAcceptor = { println("Try drop on TOKEN1"); true }
	}
	
	val token2 = TokenView(posX = 250, posY = 50, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		scale = 2.0
		
		dropAcceptor = { println("Try drop on TOKEN2"); true }
	}
	
	val token3 = TokenView(posX = 50, posY = 250, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		rotation = 45.0
		
		dropAcceptor = { println("Try drop on TOKEN3"); true }
	}
	
	val token4 = TokenView(posX = 250, posY = 250, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		rotation = 45.0
		scale = 2.0
		
		dropAcceptor = { println("Try drop on TOKEN4"); true }
	}
	
	val dragToken = TokenView(posX = 50, posY = 50, width = 50, height = 50, visual = ColorVisual.CYAN).apply {
		isDraggable = true
	}
	
	val area = Area<TokenView>(posX = 200, posY = 400, width = 500, height = 500, visual = ColorVisual.WHITE).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.WHITE }
		onDragGestureExited = { visual = ColorVisual.WHITE }
		add(token1)
		add(token2)
		add(token3)
		add(token4)
		//rotation = 45.0
		//scale = 0.5
		
		dropAcceptor = { println("Try drop on AREA"); false }
	}
	
	
	val token5 = TokenView(posX = 0, posY = 0, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		
		dropAcceptor = { println("Try drop on TOKEN5"); true }
	}
	
	val token6 = TokenView(posX = 0, posY = 0, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		scale = 2.0
		
		dropAcceptor = { println("Try drop on TOKEN6"); true }
	}
	
	val token7 = TokenView(posX = 0, posY = 0, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		rotation = 45.0
		
		dropAcceptor = { println("Try drop on TOKEN7"); true }
	}
	
	val token8 = TokenView(posX = 0, posY = 0, width = 50, height = 50, visual = ColorVisual.RED).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.RED }
		onDragGestureExited = { visual = ColorVisual.RED }
		rotation = 45.0
		scale = 2.0
		
		dropAcceptor = { println("Try drop on TOKEN8"); true }
	}
	
	val grid = GridPane<TokenView>(posX = 1000, posY = 600, columns = 2, rows = 2, spacing = 0, visual = ColorVisual.WHITE).apply {
		//onMouseEntered = { visual = ColorVisual.YELLOW }
		onDragGestureEntered = { visual = ColorVisual.GREEN }
		//onMouseExited = { visual = ColorVisual.WHITE }
		onDragGestureExited = { visual = ColorVisual.WHITE }
		set(0,0, token5)
		set(1,0, token6)
		set(0,1, token7)
		set(1,1, token8)
		//rotation = 45.0
		//scale = 0.5
		
		dropAcceptor = { println("Try drop on GRID"); false }
	}.apply {
		setCenterMode(Alignment.CENTER)
	}
	
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
			mainMenuButton,
			area, dragToken, grid
		)
	}
}