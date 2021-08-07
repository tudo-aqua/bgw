package examples.maumau.view

import examples.maumau.entity.CardSuit
import examples.maumau.entity.MauMauCard
import examples.maumau.service.LogicController
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.util.BidirectionalMap

class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {
	
	private val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	val mauMauGameScene: MauMauGameScene = MauMauGameScene()
	val logicController: LogicController = LogicController(refreshViewController)
	
	val cardMap: BidirectionalMap<MauMauCard, CardView> = BidirectionalMap()
	
	init {
		registerGameEvents()
		registerMenuEvents()
		
		showGameScene(mauMauGameScene)
		showMenuScene(mauMauMenuScene)
		show()
	}
	
	private fun registerGameEvents() {
		mauMauGameScene.hintButton.onMouseClicked = {
			logicController.showHint()
		}
		
		mauMauGameScene.gameStack.dropAcceptor = this::tryElementDropped
		mauMauGameScene.gameStack.onDragDropped = this::elementDropped
		
		mauMauGameScene.drawStack.onMouseClicked = {
			if (!logicController.game.drawStack.isEmpty())
				logicController.drawCard()
		}
		
		mauMauGameScene.buttonDiamonds.onMousePressed = { logicController.selectSuit(CardSuit.DIAMONDS) }
		mauMauGameScene.buttonHearts.onMousePressed = { logicController.selectSuit(CardSuit.HEARTS) }
		mauMauGameScene.buttonSpades.onMousePressed = { logicController.selectSuit(CardSuit.SPADES) }
		mauMauGameScene.buttonClubs.onMousePressed = { logicController.selectSuit(CardSuit.CLUBS) }
	}
	
	private fun tryElementDropped(event: DragEvent): Boolean {
		if (event.draggedComponent !is CardView)
			return false
		
		return logicController.checkRules(cardMap.backward(event.draggedComponent as CardView))
	}
	
	private fun elementDropped(event: DragEvent) {
		logicController.playCard(cardMap.backward(event.draggedComponent as CardView), false)
	}
	
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
