package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.entity.MauMauGame
import tools.aqua.bgw.examples.maumau.main.GAME_ID
import tools.aqua.bgw.examples.maumau.net.MauMauNetworkClient
import java.net.InetAddress

/**
 * Service for handling network communication.
 */
class NetworkService(private val logicController: LogicController) {
	/**
	 * Network client.
	 */
	private lateinit var client: MauMauNetworkClient
	
	/**
	 * Connects to server and starts a new game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param sessionID Session ID to host.
	 */
	fun tryHostGame(address: String, name: String, sessionID: String) {
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
	fun tryJoinGame(address: String, name: String, sessionID: String) {
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
		
		client = MauMauNetworkClient(
			playerName = name,
			host = addr[0],
			port = addr[1].toInt(),
			logicController = logicController,
		)
		client.connect()
		
		return true //TODO: Check status after bgw-net error handling upgrade
	}
	
	/**
	 * Send initialize game message to connected opponent.
	 */
	fun sendInit(game : MauMauGame) {
		client.sendInitializeGameMessage(SerializationUtil.serializeInitMessage(game))
	}
	
	//region helper
	/**
	 * Checks [address], [name] and [gameID] for not being empty,
	 * [address] to be parsable to an ip and port and [gameID] for being a positive integer.
	 */
	fun validateInputs(address: String, name: String, gameID: String): Boolean {
		if(address.isEmpty()) {
			logicController.view.showConnectWarningDialog(
				title = "Address is empty",
				message = "Please fill in the address field."
			)
		}
		
		val split = address.split(":")
		if(split.size != 2 || !validateIP(split[0]) || !validatePort(split[1])) {
			logicController.view.showConnectWarningDialog(
				title = "Address invalid",
				message = "Address is invalid. Must be in format 127.0.0.1:8080"
			)
		}
		
		if (name.isEmpty()) {
			logicController.view.showConnectWarningDialog(
				title = "Name is empty",
				message = "Please fill in the name field."
			)
			return false
		}
		
		if (gameID.isEmpty()) {
			logicController.view.showConnectWarningDialog(
				title = "gameID is empty",
				message = "Please fill in the gameID field."
			)
			return false
		}
		
		return true
	}
	
	/**
	 * Tries parsing [ip] into a hostname.
	 *
	 * @return 'true' if InetAddress.getByName returns a valid connection.
	 */
	private fun validateIP(ip : String) : Boolean {
		val converted : InetAddress?
		try {
			converted = InetAddress.getByName(ip)
		}catch(_ : Exception){
			return false
		}
		return converted != null
	}
	
	/**
	 * Tries parsing [port] into an ip port.
	 */
	private fun validatePort(port : String) : Boolean = (port.toIntOrNull() ?: false) in 1..65534
	//endregion
}