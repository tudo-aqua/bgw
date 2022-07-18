package tools.aqua.bgw.net.protocol.client.service

import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.protocol.client.view.ProtocolClientView
import java.awt.Color
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.LinkedList
import java.util.Queue

class NetworkService(private val view: ProtocolClientView) {

	private var client : ProtocolBoardGameClient? = null
	private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
	private val playerColors: MutableList<Color> = mutableListOf(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW)
	private val colorMap: MutableMap<String, Color> = mutableMapOf()

	// region Connection
	/**
	 * Connects to server and starts a new game session.
	 *
	 * @param address Server address and port.
	 * @param secret Server secret.
	 * @param gameID Game id.
	 * @param sessionID Session ID to host.
	 */
	fun hostGame(address: String, secret: String, gameID: String, sessionID: String) {
		if (!connect(address, secret)) return

		if (sessionID.isEmpty()) client?.createGame(gameID, "Welcome!")
		else client?.createGame(gameID, sessionID, "")
	}

	/**
	 * Connects to server and joins a game session.
	 *
	 * @param address Server address and port.
	 * @param secret Server secret.
	 * @param sessionID Session ID to join to.
	 */
	fun joinGame(address: String, secret: String, sessionID: String) {
		if (!connect(address, secret)) return

		client?.joinGame(sessionID, "")
	}

	/**
	 * Connects to server.
	 *
	 * @param address Server address and port in format "127.0.0.1:8080"
	 * @param secret Network secret.
	 */
	private fun connect(address: String, secret: String): Boolean {
		if (address.isEmpty() || secret.isEmpty())
			return false

		val newClient = ProtocolBoardGameClient(host = address, secret = secret, view = view, service = this)

		return if (newClient.connect()) {
			this.client = newClient
			true
		} else {
			false
		}
	}

	fun getTimestamp(): String =
		LocalTime.now().format(timeFormatter)

	fun getPlayerColor(player: String): Color =
		colorMap.getOrPut(player) {
			playerColors.removeFirst().also { playerColors.add(it) }
		}

	fun parseMessageType(message: GameAction): String =
		message.javaClass.simpleName

	fun parseMessage(message: GameAction): List<String> {
		return message.javaClass.declaredFields.map { it.toString() }
	}
	// endregion
}