package tools.aqua.bgw.examples.maumau.net

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.maumau.main.NETWORK_SECRET
import tools.aqua.bgw.examples.maumau.view.Refreshable
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.*

class MauMauNetworkClient(
	playerName: String,
	host: String,
	port: Int,
	val view : Refreshable
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
		
		onUserJoined = { BoardGameApplication.runOnGUIThread { view.onUserJoined(it.sender) } }
		onUserLeft = { BoardGameApplication.runOnGUIThread { view.onUserLeft(it.sender) } }
		
		onCreateGameResponse = this::onCreateGameResponse
		onJoinGameResponse = this::onJoinGameResponse
		onLeaveGameResponse = this::onLeaveGameResponse
		
		onGameActionResponse = this::onGameActionResponse
		onGameActionReceived = this::onGameActionReceived
	}
	
	private fun onCreateGameResponse(response : CreateGameResponse) {
		BoardGameApplication.runOnGUIThread {
			when (response.responseStatus) {
				CreateGameResponseStatus.SUCCESS -> view.onCreateGameSuccess()
				CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> view.onCreateGameError("You are already in a game.")
				CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS -> view.onCreateGameError("Session id already exists.")
				CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> error(response)
				CreateGameResponseStatus.SERVER_ERROR -> view.onServerError()
			}
		}
	}
	
	private fun onJoinGameResponse(response: JoinGameResponse) {
		BoardGameApplication.runOnGUIThread {
			when (response.responseStatus) {
				JoinGameResponseStatus.SUCCESS -> view.onJoinGameSuccess()
				JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> view.onCreateGameError("You are already in a game.")
				JoinGameResponseStatus.INVALID_SESSION_ID -> view.onCreateGameError("Session id invalid.")
				JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> view.onJoinGameError("Player name is already taken.")
				JoinGameResponseStatus.SERVER_ERROR -> view.onServerError()
			}
		}
	}
	
	private fun onLeaveGameResponse(response: LeaveGameResponse) {
		BoardGameApplication.runOnGUIThread {
			when (response.responseStatus) {
				LeaveGameResponseStatus.SUCCESS -> {}
				LeaveGameResponseStatus.NO_ASSOCIATED_GAME -> error(response)
				LeaveGameResponseStatus.SERVER_ERROR -> view.onServerError()
			}
		}
	}
	
	private fun onGameActionResponse(response: GameActionResponse)  {
		BoardGameApplication.runOnGUIThread {
			when (response.status) {
				GameMessageStatus.SUCCESS -> view.onGameActionAccepted()
				GameMessageStatus.NO_ASSOCIATED_GAME -> error(response)
				GameMessageStatus.INVALID_JSON -> error(response)
				GameMessageStatus.SERVER_ERROR -> view.onServerError()
			}
		}
	}
	
	private fun onGameActionReceived(message : GameActionMessage, sender : String) {
		BoardGameApplication.runOnGUIThread {
			//logicController.doTurn(payload)
		}
	}
}