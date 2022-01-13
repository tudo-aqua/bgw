package tools.aqua.bgw.net.server.service

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.player
import java.lang.UnsupportedOperationException

@Service
class MessageService(
	@Autowired private val gameService: GameService,
	@Autowired private val validationService: ValidationService,
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
		val player = wsSession.player
		val game = player.game
		val status = if (game != null) {
			try {
				if (validationService.validate(gameActionMessage.payload, game.gameID))
					GameMessageStatus.SUCCESS
				else
					GameMessageStatus.INVALID_JSON
			} catch (exception: JsonSchemaNotFoundException) {
				GameMessageStatus.SCHEMA_NOT_FOUND
			}
			GameMessageStatus.SUCCESS
		} else GameMessageStatus.NO_ASSOCIATED_GAME
		if (status == GameMessageStatus.SUCCESS) {
			player.session.sendMessage(TextMessage(GameActionResponse(status).encode()))
			game?.broadcastMessage(player, gameActionMessage.copy(sender = player.name))
		}
	}


	private fun handleCreateGameMessage(wsSession: WebSocketSession, createGameMessage: CreateGameMessage) {
		val player = wsSession.player
		val createGameResponseStatus = gameService.createGame(
			createGameMessage.gameID,
			createGameMessage.sessionID,
			player
		)
		wsSession.sendMessage(TextMessage(CreateGameResponse(createGameResponseStatus).encode()))
	}

	private fun handleJoinGameMessage(wsSession: WebSocketSession, joinGameMessage: JoinGameMessage) {
		val player = wsSession.player
		val joinGameResponseStatus = gameService.joinGame(
			player,
			joinGameMessage.sessionId
		)
		wsSession.sendMessage(TextMessage(JoinGameResponse(joinGameResponseStatus).encode()))

		if (joinGameResponseStatus == JoinGameResponseStatus.SUCCESS) {
			val notification = UserJoinedNotification(joinGameMessage.greeting, player.name)
			gameService.getBySessionID(joinGameMessage.sessionId)?.broadcastMessage(player, notification)
		}
	}

	private fun handleLeaveGameMessage(wsSession: WebSocketSession, leaveGameMessage: LeaveGameMessage) {
		val player = wsSession.player
		val game = player.game
		val leaveGameResponseStatus = gameService.leaveGame(player)
		wsSession.sendMessage(TextMessage(LeaveGameResponse(leaveGameResponseStatus).encode()))

		if (leaveGameResponseStatus == LeaveGameResponseStatus.SUCCESS) {
			val notification = UserDisconnectedNotification(leaveGameMessage.goodbyeMessage, player.name)
			val message = notification.encode()
			game?.let {
				it.players.map(Player::session).forEach { session ->
					session.sendMessage(TextMessage(message))
				}
			}
		}
	}

	fun broadcastNotification(game: Game, msg: Message) {
		game.players.map(Player::session).forEach {
			it.sendMessage(TextMessage(msg.encode()))
		}
	}

	private fun Game.broadcastMessage(sender: Player, msg: Message) {
		(players - sender).map(Player::session).forEach {
			it.sendMessage(TextMessage(msg.encode()))
		}
	}
}