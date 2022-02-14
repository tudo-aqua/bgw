package tools.aqua.bgw.examples.maumau.view

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.GameState
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.examples.maumau.service.NetworkService
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
	 * The host game menu scene.
	 */
	private val mauMauHostGameMenuScene: MauMauHostGameMenuScene = MauMauHostGameMenuScene()
	
	/**
	 * The join game menu scene.
	 */
	private val mauMauJoinGameMenuScene: MauMauJoinGameMenuScene = MauMauJoinGameMenuScene()
	
	/**
	 * The waiting for opponent menu scene.
	 */
	val mauMauWaitForOpponentMenuScene: MauMauWaitForOpponentMenuScene = MauMauWaitForOpponentMenuScene()
	
	/**
	 * The player won menu scene.
	 */
	val mauMauPlayerWonMenuScene: MauMauPlayerWonScene = MauMauPlayerWonScene()
	
	/**
	 * The main game scene.
	 */
	val mauMauGameScene: MauMauGameScene = MauMauGameScene()
	
	/**
	 * Refresh view controller instance for the [LogicController] callbacks.
	 */
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	/**
	 * Network service instance.
	 */
	private val networkService: NetworkService = NetworkService(refreshViewController)
	
	/**
	 * Logic controller instance.
	 */
	val logicController: LogicController = LogicController(refreshViewController, networkService)
	
	/**
	 * CardMap mapping entity cards onto view components.
	 */
	val cardMap: BidirectionalMap<MauMauCard, CardView> = BidirectionalMap()
	
	init {
		registerGameEvents()
		registerMainMenuEvents()
		registerPlayerWonMenuEvents()
		registerHostMenuEvents()
		registerJoinMenuEvents()
		showGameScene(mauMauGameScene)
		showMenuScene(mauMauMenuScene)
		show()
	}
	
	/**
	 * Registers events in the main game scene.
	 */
	private fun registerGameEvents() {
		mauMauGameScene.mainMenuButton.onMouseClicked = {
			showMenuScene(mauMauMenuScene)
			logicController.gameState = GameState.MAIN_MENU
		}
		
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
	private fun registerMainMenuEvents() {
		mauMauMenuScene.continueGameButton.onMouseClicked = { /*hideMenuScene()*/ } //TODO
		
		mauMauMenuScene.newLocalGameButton.onMouseClicked = {
			logicController.newGame()
			hideMenuScene()
			logicController.gameState = GameState.PLAYER_ONE_TURN
		}
		
		mauMauMenuScene.hostGameButton.onMouseClicked = {
			showMenuScene(mauMauHostGameMenuScene)
			logicController.gameState = GameState.HOST_GAME_MENU
		}
		
		mauMauMenuScene.joinGameButton.onMouseClicked = {
			showMenuScene(mauMauJoinGameMenuScene)
			logicController.gameState = GameState.JOIN_GAME_MENU
		}
		
		mauMauMenuScene.exitButton.onMouseClicked = { exit() }
	}
	
	/**
	 * Registers events in the player won menu scene.
	 */
	private fun registerPlayerWonMenuEvents() {
		mauMauPlayerWonMenuScene.newGameButton.onMouseClicked = {
			logicController.newGame()
			hideMenuScene()
			logicController.gameState = GameState.PLAYER_ONE_TURN
		}
		
		mauMauPlayerWonMenuScene.exitButton.onMouseClicked = { exit() }
	}
	
	/**
	 * Registers events in the host game menu scene.
	 */
	private fun registerHostMenuEvents() {
		mauMauHostGameMenuScene.joinGameButton.onMouseClicked = {
			val address = mauMauHostGameMenuScene.addressText.text.trim()
			val name = mauMauHostGameMenuScene.nameText.text.trim()
			val sessionID = mauMauHostGameMenuScene.sessionIDText.text.trim()
			
			if (networkService.validateInputs(address, name, sessionID))
				networkService.tryHostGame(address, name, sessionID)
		}
		
		mauMauHostGameMenuScene.backButton.onMouseClicked = { showMenuScene(mauMauMenuScene) }
	}
	
	/**
	 * Registers events in the join game menu scene.
	 */
	private fun registerJoinMenuEvents() {
		mauMauJoinGameMenuScene.joinGameButton.onMouseClicked = {
			val address = mauMauJoinGameMenuScene.addressText.text.trim()
			val name = mauMauJoinGameMenuScene.nameText.text.trim()
			val gameID = mauMauJoinGameMenuScene.sessionIDText.text.trim()
			
			if (networkService.validateInputs(address, name, gameID))
				networkService.tryJoinGame(address, name, gameID)
		}
		
		mauMauJoinGameMenuScene.backButton.onMouseClicked = { showMenuScene(mauMauMenuScene) }
	}
}
