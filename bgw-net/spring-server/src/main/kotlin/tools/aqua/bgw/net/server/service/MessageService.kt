package tools.aqua.bgw.net.server.service

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.entity.Player
import java.lang.UnsupportedOperationException

@Service
class MessageService(
	@Autowired private val gameService: GameService,
	@Autowired private val validationService: ValidationService
) {
	fun handleMessage(session: WebSocketSession, messageString: String) {
		when (val message: Message = Json.decodeFromString<Message>(messageString)) {
			is Response -> throw UnsupportedOperationException()
			is Notification -> throw UnsupportedOperationException()
			is EndGameMessage -> throw UnsupportedOperationException()
			is GameActionMessage -> handleGameActionMessage(session, message)
			is InitializeGameMessage -> throw UnsupportedOperationException()
			is CreateGameMessage -> handleCreateGameMessage(session, message)
			is JoinGameMessage -> handleJoinGameMessage(session, message)
			is LeaveGameMessage -> handleLeaveGameMessage(session, message)
		}
	}

	private fun handleGameActionMessage(wsSession: WebSocketSession, gameActionMessage: GameActionMessage) {
		val player = wsSession.getPlayer()
		val game = player.game
		val status = if (game != null) {
			//TODO validation
			GameMessageStatus.SUCCESS
		} else GameMessageStatus.NO_ASSOCIATED_GAME
		if (status == GameMessageStatus.SUCCESS) {
			player.session.sendMessage(TextMessage(GameActionResponse(status).encode()))
			(game!!.players - player).map(Player::session).forEach {
				it.sendMessage(
					TextMessage(
						GameActionMessage(
							gameActionMessage.payload,
							gameActionMessage.prettyPrint
						).encode()
					)
				)
			}
		}
	}


	private fun handleCreateGameMessage(wsSession: WebSocketSession, createGameMessage: CreateGameMessage) {
		val player = wsSession.getPlayer()
		val createGameResponseStatus = gameService.createGame(
			createGameMessage.gameID,
			createGameMessage.sessionID,
			player
		)
		wsSession.sendMessage(TextMessage(CreateGameResponse(createGameResponseStatus).encode()))
	}

	private fun handleJoinGameMessage(wsSession: WebSocketSession, joinGameMessage: JoinGameMessage) {
		val player = wsSession.getPlayer()
		val joinGameResponseStatus = gameService.joinGame(
			player,
			joinGameMessage.sessionId
		)
		wsSession.sendMessage(TextMessage(JoinGameResponse(joinGameResponseStatus).encode()))

		if (joinGameResponseStatus == JoinGameResponseStatus.SUCCESS) {
			val notification = UserJoinedNotification("${player.name}: ${joinGameMessage.greeting}")
			val message = notification.encode()
			gameService.getBySessionID(joinGameMessage.sessionId)?.let {
				(it.players - player).map(Player::session).forEach { session ->
					session.sendMessage(TextMessage(message))
				}
			}
		}
	}

	private fun handleLeaveGameMessage(wsSession: WebSocketSession, leaveGameMessage: LeaveGameMessage) {
		val player = wsSession.getPlayer()
		val game = player.game
		val leaveGameResponseStatus = gameService.leaveGame(player)
		wsSession.sendMessage(TextMessage(LeaveGameResponse(leaveGameResponseStatus).encode()))

		if (leaveGameResponseStatus == LeaveGameResponseStatus.SUCCESS) {
			val notification = UserDisconnectedNotification("${player.name}: ${leaveGameMessage.goodbyeMessage}")
			val message = notification.encode()
			game?.let {
				it.players.map(Player::session).forEach { session ->
					session.sendMessage(TextMessage(message))
				}
			}
		}
	}

	private fun WebSocketSession.getPlayer(): Player {
		val player = attributes["player"] ?: error("missing attribute") //TODO
		if (player is Player) return player else error("wrong type") //TODO
	}
}