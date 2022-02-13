package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.maumau.main.GAME_ID
import tools.aqua.bgw.examples.maumau.main.NETWORK_SECRET
import tools.aqua.bgw.examples.maumau.net.GameActionMessage
import tools.aqua.bgw.examples.maumau.net.GameOverMessage
import tools.aqua.bgw.examples.maumau.net.InitGameMessage
import tools.aqua.bgw.examples.maumau.view.Refreshable
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.common.CreateGameResponseStatus
import tools.aqua.bgw.net.common.GameMessageStatus
import tools.aqua.bgw.net.common.JoinGameResponseStatus
import tools.aqua.bgw.net.common.LeaveGameResponseStatus

/**
 * Service for handling network communication.
 */
class NetworkService(private val view: Refreshable, private val logicController: LogicController) {
	/**
	 * Network client.
	 */
	private lateinit var client: BoardGameClient<InitGameMessage, GameActionMessage, GameOverMessage>
	
	/**
	 * Connects to server and starts a new game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param sessionID Session ID to host.
	 */
	fun tryHostGame(address: String, name: String, sessionID: String): Unit {
		if (!tryConnect(address, name))
			return
		
		client.createGame(GAME_ID, sessionID)
	}
	
	/**
	 * Connects to server and joins a game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param sessionID Session ID to join to.
	 */
	fun tryJoinGame(address: String, name: String, sessionID: String): Unit {
		if (!tryConnect(address, name))
			return
		
		client.joinGame(sessionID, "greeting")
	}
	
	/**
	 * Connects to server.
	 *
	 * @param address Server address and port in format "127.0.0.1:8080"
	 * @param name Player name.
	 */
	private fun tryConnect(address: String, name: String): Boolean {
		val addr = address.split(":")
		
		client = BoardGameClient(
			playerName = name,
			secret = NETWORK_SECRET,
			initGameClass = InitGameMessage::class.java,
			gameActionClass = GameActionMessage::class.java,
			endGameClass = GameOverMessage::class.java,
			host = addr[0],
			port = addr[1].toInt()
		)
		client.init()
		client.connect()
		
		return true //TODO: Check status after bgw-net error handling upgrade
	}
	
	/**
	 * Sets listeners for network events
	 */
	private fun BoardGameClient<InitGameMessage, GameActionMessage, GameOverMessage>.init() {
		onOpen = {
			println("Connection is now open")
		}
		onClose = { code, reason, _ ->
			println("Connection closed with code: $code and reason: $reason")
		}
		onCreateGameResponse = {
			BoardGameApplication.runOnGUIThread {
				when (it.responseStatus) {
					CreateGameResponseStatus.SUCCESS -> view.onCreateGameSuccess()
					CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> view.onCreateGameError("You are already in a game.")
					CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS -> view.onCreateGameError("Session id already exists.")
					CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST -> error(it)
					CreateGameResponseStatus.SERVER_ERROR -> view.onServerError()
				}
			}
		}
		onJoinGameResponse = {
			BoardGameApplication.runOnGUIThread {
				when (it.responseStatus) {
					JoinGameResponseStatus.SUCCESS -> view.onJoinGameSuccess()
					JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME -> view.onCreateGameError("You are already in a game.")
					JoinGameResponseStatus.INVALID_SESSION_ID -> view.onCreateGameError("Session id invalid.")
					JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN -> view.onJoinGameError("Player name is already taken.")
					JoinGameResponseStatus.SERVER_ERROR -> view.onServerError()
				}
			}
		}
		onLeaveGameResponse = {
			BoardGameApplication.runOnGUIThread {
				when (it.responseStatus) {
					LeaveGameResponseStatus.SUCCESS -> {}
					LeaveGameResponseStatus.NO_ASSOCIATED_GAME -> error(it)
					LeaveGameResponseStatus.SERVER_ERROR -> view.onServerError()
				}
			}
		}
		onUserJoined = { BoardGameApplication.runOnGUIThread { view.onUserJoined(it.sender) } }
		onUserLeft = { BoardGameApplication.runOnGUIThread { view.onUserLeft(it.sender) } }
		
		onGameActionResponse = {
			BoardGameApplication.runOnGUIThread {
				when (it.status) {
					GameMessageStatus.SUCCESS -> view.onGameActionAccepted()
					GameMessageStatus.NO_ASSOCIATED_GAME -> error(it)
					GameMessageStatus.INVALID_JSON -> error(it)
					GameMessageStatus.SERVER_ERROR -> view.onServerError()
				}
			}
		}
		onGameActionReceived = { payload, _ ->
			BoardGameApplication.runOnGUIThread {
				logicController.doTurn(payload)
			}
		}
	}
	
	/**
	 * Checks name and gameId for not being empty and gameId for being a positive integer.
	 */
	fun validateInputs(name: String, gameID: String): Boolean {
		if (name.isEmpty()) {
			view.showConnectWarningDialog(
				title = "Name is empty",
				message = "Please fill in the name field."
			)
			return false
		}
		
		if (gameID.isEmpty()) {
			view.showConnectWarningDialog(
				title = "gameID is empty",
				message = "Please fill in the gameID field."
			)
			return false
		}
		
		return true
	}
}