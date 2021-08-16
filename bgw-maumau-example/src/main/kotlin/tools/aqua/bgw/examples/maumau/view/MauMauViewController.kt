package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.util.BidirectionalMap

/**
 * Main view controller.
 */
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {
	
	/**
	 * The main menu scene.
	 */
	private val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
	
	/**
	 * The main game scene.
	 */
	val mauMauGameScene: MauMauGameScene = MauMauGameScene()
	
	/**
	 * Refresh view controller instance for the [LogicController] callbacks.
	 */
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	/**
	 * Logic controller instance.
	 */
	val logicController: LogicController = LogicController(refreshViewController)
	
	/**
	 * CardMap mapping entity cards onto view components.
	 */
	val cardMap: BidirectionalMap<MauMauCard, CardView> = BidirectionalMap()
	
	init {
		registerGameEvents()
		registerMenuEvents()
		
		showGameScene(mauMauGameScene)
		showMenuScene(mauMauMenuScene)
		show()
	}
	
	/**
	 * Registers events in the main game scene.
	 */
	private fun registerGameEvents() {
		//Register hint button to calculate hint for current player
		mauMauGameScene.hintButton.onMouseClicked = {
			logicController.showHint()
		}
		
		//Set onClick handler for draw stack
		mauMauGameScene.drawStack.onMouseClicked = {
			if (!logicController.game.drawStack.isEmpty())
				logicController.drawCard()
		}
		
		//Set drag drop acceptor and handler for game stack
		mauMauGameScene.gameStack.dropAcceptor = this::tryElementDropped
		mauMauGameScene.gameStack.onDragDropped = this::elementDropped
		
		//Set onClick handler for jack selection
		mauMauGameScene.buttonDiamonds.onMousePressed = { logicController.selectSuit(CardSuit.DIAMONDS) }
		mauMauGameScene.buttonHearts.onMousePressed = { logicController.selectSuit(CardSuit.HEARTS) }
		mauMauGameScene.buttonSpades.onMousePressed = { logicController.selectSuit(CardSuit.SPADES) }
		mauMauGameScene.buttonClubs.onMousePressed = { logicController.selectSuit(CardSuit.CLUBS) }
	}
	
	/**
	 * Calculates whether the dragged card may be played.
	 *
	 * @param event Drag event.
	 *
	 * @return `true` if playing the dragged card is a valid move
	 */
	private fun tryElementDropped(event: DragEvent): Boolean {
		if (event.draggedComponent !is CardView)
			return false
		
		return logicController.checkRules(cardMap.backward(event.draggedComponent as CardView))
	}
	
	/**
	 * Plays dragged card after successful drop.
	 *
	 * @param event Drag event.
	 */
	private fun elementDropped(event: DragEvent) {
		logicController.playCard(cardMap.backward(event.draggedComponent as CardView), false)
	}
	
	/**
	 * Registers events in the main menu scene.
	 */
	private fun registerMenuEvents() {
		mauMauGameScene.mainMenuButton.onMouseClicked = {
			showMenuScene(mauMauMenuScene)
		}
		
		mauMauMenuScene.continueGameButton.onMouseClicked = {
			hideMenuScene()
		}
        
        mauMauMenuScene.newGameButton.onMouseClicked = {
            logicController.newGame()
            hideMenuScene()
        }
        
        mauMauMenuScene.exitButton.onMouseClicked = {
            exit()
        }
    }
}
