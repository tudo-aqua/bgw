package tools.aqua.bgw.net.protocol.client.service

import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus
import tools.aqua.bgw.net.protocol.client.view.ProtocolClientView

/**
 * [BoardGameClient] implementation for network communication.
 *
 * @param host Host address.
 * @param secret Network secret.
 */
class ProtocolBoardGameClient(
	host: String,
	secret: String,
	val view: ProtocolClientView,
	val service: NetworkService
) : BoardGameClient(
	playerName = "Spectator${System.currentTimeMillis()}",
	host = host,
	secret = secret,
	networkLoggingBehavior = NetworkLogging.VERBOSE
) {

	override fun onOpen() {
		view.onConnectionEstablished()
	}

	override fun onCreateGameResponse(response: CreateGameResponse) {
		if(response.status == CreateGameResponseStatus.SUCCESS)
			view.onGameCreated(requireNotNull(response.sessionID))
	}

	override fun onJoinGameResponse(response: JoinGameResponse) {
		if(response.status == JoinGameResponseStatus.SUCCESS)
			view.onGameJoined(requireNotNull(response.sessionID))
	}

	override fun onPlayerJoined(notification: PlayerJoinedNotification) {
		view.onPlayerJoined(notification.sender)
	}

	override fun onPlayerLeft(notification: PlayerLeftNotification) {
		view.onPlayerLeft(notification.sender)
	}

	override fun onGameActionReceived(message: GameAction, sender: String) {

		view.onGameActionReceived(
			timestamp = service.getTimestamp(),
			player=sender,
			playerNameColor = service.getPlayerColor(player = sender),
			messageType = service.parseMessageType(message=message),
			message=service.parseMessage(message=message)
		)
	}
}