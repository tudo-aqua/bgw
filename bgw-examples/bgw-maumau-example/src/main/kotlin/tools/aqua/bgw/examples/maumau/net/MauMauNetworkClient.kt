package tools.aqua.bgw.examples.maumau.net

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.maumau.entity.GameAction
import tools.aqua.bgw.examples.maumau.main.NETWORK_SECRET
import tools.aqua.bgw.examples.maumau.service.LogicController
import tools.aqua.bgw.examples.maumau.service.SerializationUtil
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.CreateGameResponse
import tools.aqua.bgw.net.common.CreateGameResponseStatus
import tools.aqua.bgw.net.common.JoinGameResponse
import tools.aqua.bgw.net.common.JoinGameResponseStatus

class MauMauNetworkClient(
	playerName: String,
	host: String,
	port: Int,
	val logicController : LogicController,
) : BoardGameClient<InitGameMessage, GameActionMessage, GameOverMessage>(
	playerName = playerName,
	secret = NETWORK_SECRET,
	initGameClass = InitGameMessage::class.java,
	gameActionClass = GameActionMessage::class.java,
	endGameClass = GameOverMessage::class.java,
	host = host,
	port = port
) {
	
	/**
	 * Sets listeners for network events
	 */
	init {
		onOpen = { println("Connection is now open") }
		onClose = { code, reason, _ -> println("Connection closed with code: $code and reason: $reason") }
		
		onUserJoined = { BoardGameApplication.runOnGUIThread { logicController.view.onUserJoined(it.sender) } }
		onUserLeft = { BoardGameApplication.runOnGUIThread { logicController.view.onUserLeft(it.sender) } }
		
		onCreateGameResponse = this::onCreateGameResponse
		onJoinGameResponse = this::onJoinGameResponse
		
		onInitializeGameReceived = this::onInitializeGameReceived
		onGameActionReceived = this::onGameActionReceived
		onEndGameReceived = this::onEndGameReceived
		
		onInitializeGameResponse = { println(it) }
		onGameActionResponse = { println(it) }
		onEndGameResponse = { println(it) }
	}
	
	private fun onCreateGameResponse(response : CreateGameResponse) {
		BoardGameApplication.runOnGUIThread {
			when (response.responseStatus) {
				CreateGameResponseStatus.SUCCESS -> logicController.view.onCreateGameSuccess()
				CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> logicController.view.onCreateGameError("You are already in a game.")
				CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS -> logicController.view.onCreateGameError("Session id already exists.")
				CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> error(response)
				CreateGameResponseStatus.SERVER_ERROR -> logicController.view.onServerError()
			}
		}
	}
	
	private fun onJoinGameResponse(response: JoinGameResponse) {
		BoardGameApplication.runOnGUIThread {
			when (response.responseStatus) {
				JoinGameResponseStatus.SUCCESS -> logicController.view.onJoinGameSuccess()
				JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> logicController.view.onCreateGameError("You are already in a game.")
				JoinGameResponseStatus.INVALID_SESSION_ID -> logicController.view.onCreateGameError("Session id invalid.")
				JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> logicController.view.onJoinGameError("Player name is already taken.")
				JoinGameResponseStatus.SERVER_ERROR -> logicController.view.onServerError()
			}
		}
	}
	
	private fun onInitializeGameReceived(message : InitGameMessage, sender : String) {
		println("Received init message: $message")
		BoardGameApplication.runOnGUIThread {
			logicController.initGame(message)
			logicController.view.onInitializeGameReceived()
		}
	}
	
	private fun onGameActionReceived(message : GameActionMessage, sender : String) {
		println("Received game message: $message")
		
		BoardGameApplication.runOnGUIThread {
			when(GameAction.valueOf(message.action)) {
				//Enemy has played a card
				GameAction.PLAY -> {
					val card = SerializationUtil.deserializeMauMauCard(message.card)
					
					logicController.game.apply {
						players[1].hand.removeCard(card)
						gameStack.playCard(card)
						nextSuit = card.cardSuit
					}
					logicController.view.refreshCardPlayed(card = card, animated = true, isCurrentPlayer = false)
				}
				
				//Enemy has drawn
				GameAction.DRAW -> {
					val card = logicController.game.drawStack.drawCard()
					logicController.game.players[1].hand.addCard(card)
					logicController.view.refreshCardDrawn(card, false)
				}
				
				//Enemy has played a seven effect and requests to draw two
				GameAction.REQUEST_DRAW_TWO -> {
					val cards = logicController.game.drawStack.drawTwo()
					logicController.game.players[0].hand.addCards(cards)
					logicController.view.refreshCardsDrawn(cards, true)
				}
				
				//Enemy has played a jack and request suit
				GameAction.REQUEST_SUIT -> {
					val suit = SerializationUtil.deserializeMauMauCard(message.card).cardSuit
					
					logicController.game.nextSuit = suit
					logicController.view.refreshSuitSelected()
				}
				
				//Enemy has ended his turn
				GameAction.END_TURN -> {
					logicController.view.refreshEndTurn()
				}
			}
		}
	}
	
	private fun onEndGameReceived(message : GameOverMessage, sender : String) {
		println("Received end message: $message")
	}
}