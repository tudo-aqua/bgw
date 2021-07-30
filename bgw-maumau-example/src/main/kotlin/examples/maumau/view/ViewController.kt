package examples.maumau.view

import examples.maumau.entity.CardSuit
import examples.maumau.entity.MauMauCard
import examples.maumau.service.LogicController
import tools.aqua.bgw.components.gamecomponents.CardView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.util.BidirectionalMap

class ViewController : BoardGameApplication("MauMau") {
	
	val gameScene: GameScene = GameScene()
	val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
	
	val refreshViewController: RefreshViewController = RefreshViewController(this)
	val logicController: LogicController = LogicController(refreshViewController)
	
	val cardMap: BidirectionalMap<MauMauCard, CardView> = BidirectionalMap()
	
	init {
		registerGameEvents()
		registerMenuEvents()
		
		showGameScene(gameScene)
		showMenuScene(mauMauMenuScene)
		show()
	}
	
	private fun registerGameEvents() {
		gameScene.hintButton.onMouseClicked = {
			logicController.showHint()
		}
		
		gameScene.gameStack.dropAcceptor = this::tryElementDropped
		gameScene.gameStack.onDragDropped = this::elementDropped
		
		gameScene.drawStack.onMouseClicked = {
			if (!logicController.game.drawStack.isEmpty())
				logicController.drawCard()
		}
		
		gameScene.buttonDiamonds.onMousePressed = { logicController.selectSuit(CardSuit.DIAMONDS) }
		gameScene.buttonHearts.onMousePressed = { logicController.selectSuit(CardSuit.HEARTS) }
		gameScene.buttonSpades.onMousePressed = { logicController.selectSuit(CardSuit.SPADES) }
		gameScene.buttonClubs.onMousePressed = { logicController.selectSuit(CardSuit.CLUBS) }
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
		gameScene.mainMenuButton.onMouseClicked = {
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
