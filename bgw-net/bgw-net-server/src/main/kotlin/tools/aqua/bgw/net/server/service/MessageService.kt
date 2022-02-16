package tools.aqua.bgw.net.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.SchemasByGameRepository
import tools.aqua.bgw.net.server.player

/**
 * This service handles the text messages received by the web socket server.
 */
@Service
class MessageService(
	private val gameService: GameService,
	private val validationService: ValidationService,
	private val schemasByGameRepository: SchemasByGameRepository,
) {
	private val mapper = ObjectMapper().registerModule(kotlinModule())

	private fun WebSocketSession.sendMessage(message: Message) =
		sendMessage(TextMessage(mapper.writeValueAsString(message)))

	private fun Game.broadcastMessage(sender: Player, msg: Message) {
		for (session in (players - sender).map(Player::session)) {
			session.sendMessage(msg)
		}
	}

	fun handleMessage(session: WebSocketSession, messageString: String) {
		val message: Message = mapper.readValue(messageString, Message::class.java)
		println(message)
		when (message) {
			is Response -> throw UnsupportedOperationException()
			is Notification -> throw UnsupportedOperationException()
			is GameMessage -> handleGameMessage(session, message)
			is CreateGameMessage -> handleCreateGameMessage(session, message)
			is JoinGameMessage -> handleJoinGameMessage(session, message)
			is LeaveGameMessage -> handleLeaveGameMessage(session, message)
		}
	}

	private fun handleGameMessage(wsSession: WebSocketSession, gameMessage: GameMessage) {
		val player = wsSession.player
		val game = player.game
		var errors: List<String>? = null
		val status = if (game != null) try {
			errors = validationService.validate(gameMessage, game.gameID)
			if (errors == null)
				GameMessageStatus.SUCCESS
			else
				GameMessageStatus.INVALID_JSON
		} catch (exception: JsonSchemaNotFoundException) {
			GameMessageStatus.SERVER_ERROR
		} else GameMessageStatus.NO_ASSOCIATED_GAME
		
		player.session.sendMessage(when (gameMessage) {
			is InitializeGameMessage -> InitializeGameResponse(status, errors)
			is GameActionMessage ->GameActionResponse(status, errors)
			is EndGameMessage -> EndGameResponse(status, errors)
		})
		
		if (status == GameMessageStatus.SUCCESS) {
			game?.broadcastMessage(
				player,
				when (gameMessage) {
					is EndGameMessage -> gameMessage.copy(sender = player.name)
					is GameActionMessage -> gameMessage.copy(sender = player.name)
					is InitializeGameMessage -> gameMessage.copy(sender = player.name)
				}
			)
		}
	}

	private fun handleCreateGameMessage(wsSession: WebSocketSession, createGameMessage: CreateGameMessage) {
		val player = wsSession.player
		val createGameResponseStatus =
			if (!schemasByGameRepository.existsById(createGameMessage.gameID))
				CreateGameResponseStatus.GAME_ID_DOES_NOT_EXIST
			else gameService.createGame(
				createGameMessage.gameID,
				createGameMessage.sessionID,
				player
			)
		wsSession.sendMessage(CreateGameResponse(createGameResponseStatus))
	}

	private fun handleJoinGameMessage(wsSession: WebSocketSession, joinGameMessage: JoinGameMessage) {
		val player = wsSession.player
		val joinGameResponseStatus = gameService.joinGame(
			player,
			joinGameMessage.sessionId
		)
		wsSession.sendMessage(JoinGameResponse(joinGameResponseStatus))
		if (joinGameResponseStatus == JoinGameResponseStatus.SUCCESS) {
			val notification = UserJoinedNotification(joinGameMessage.greeting, player.name)
			gameService.getBySessionID(joinGameMessage.sessionId)?.broadcastMessage(player, notification)
		}
	}

	private fun handleLeaveGameMessage(wsSession: WebSocketSession, leaveGameMessage: LeaveGameMessage) {
		val player = wsSession.player
		val game = player.game
		val leaveGameResponseStatus = gameService.leaveGame(player)
		wsSession.sendMessage(LeaveGameResponse(leaveGameResponseStatus))
		if (leaveGameResponseStatus == LeaveGameResponseStatus.SUCCESS) {
			val notification = UserDisconnectedNotification(leaveGameMessage.goodbyeMessage, player.name)
			val message = mapper.writeValueAsString(notification)
			game?.let {
				it.players.map(Player::session).forEach { session ->
					session.sendMessage(TextMessage(message))
				}
			}
		}
	}

	fun broadcastNotification(game: Game, msg: Notification) {
		for (session in game.players.map(Player::session)) {
			session.sendMessage(msg)
		}
	}
}