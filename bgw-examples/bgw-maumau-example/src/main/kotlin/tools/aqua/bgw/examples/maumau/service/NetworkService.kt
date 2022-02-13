package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.entity.GameAction
import tools.aqua.bgw.examples.maumau.main.GAME_ID
import tools.aqua.bgw.examples.maumau.main.NETWORK_SECRET
import tools.aqua.bgw.examples.maumau.net.GameActionMessage
import tools.aqua.bgw.examples.maumau.net.GameOverMessage
import tools.aqua.bgw.examples.maumau.net.InitGameMessage
import tools.aqua.bgw.examples.maumau.view.Refreshable
import tools.aqua.bgw.net.client.BoardGameClient

/**
 * Service for handling network communication.
 */
class NetworkService(private val view: Refreshable) {
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
	fun tryHostGame(address: String, name: String, sessionID: String): Boolean {
		if (!tryConnect(address, name))
			return false
		
		client.createGame(GAME_ID, sessionID)
		client.sendGameActionMessage(GameActionMessage(GameAction.PLAY, CardSuit.HEARTS, CardValue.SEVEN))
		return true
	}
	
	/**
	 * Connects to server and joins a game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param sessionID Session ID to join to.
	 */
	fun tryJoinGame(address: String, name: String, sessionID: String): Boolean {
		if (!tryConnect(address, name))
			return false
		
		client.joinGame(sessionID, "greeting")
		client.sendGameActionMessage(GameActionMessage(GameAction.DRAW, CardSuit.CLUBS, CardValue.FIVE))
		return true
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
			println("$it")
		}
		onJoinGameResponse = {
			println("$it")
		}
		onLeaveGameResponse = {
			println("$it")
		}
		onUserJoined = {
			println("$it")
		}
		onUserLeft = {
			println("$it")
		}
		onGameActionResponse = {
			println("$it")
		}
		onGameActionReceived = { payload, sender ->
			println("$sender sent $payload")
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