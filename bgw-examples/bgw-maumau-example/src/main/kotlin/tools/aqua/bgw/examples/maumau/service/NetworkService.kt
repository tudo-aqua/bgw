package tools.aqua.bgw.examples.maumau.service

import tools.aqua.bgw.examples.maumau.view.Refreshable

class NetworkService(private val view: Refreshable) {
	
	/**
	 * Connects to server and starts a new game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param gameID Game ID to host.
	 */
	fun tryHostGame(address:String, name: String, gameID: Int): Boolean {
		return true
	}
	
	/**
	 * Connects to server and joins a game session.
	 *
	 * @param address Server address and port.
	 * @param name Player name.
	 * @param gameID Game ID to join to.
	 */
	fun tryJoinGame(address:String, name: String, gameID: Int): Boolean {
		return true
	}
	
	/**
	 * Checks name and gameId for not being empty and gameId for being a positive integer.
	 */
	fun validateInputs (name: String, gameID: String) : Boolean {
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
		
		val id = gameID.toIntOrNull()
		if (id == null || id < 0) {
			view.showConnectWarningDialog(
				title = "gameID is not a number",
				message = "Please fill in the gameID field with a positive number."
			)
			return false
		}
		
		return true
	}
}