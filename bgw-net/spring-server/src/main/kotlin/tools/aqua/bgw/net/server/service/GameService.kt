package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.GameRepository
import tools.aqua.bgw.net.server.entity.Player

@Service
class GameService(@Autowired private val gameRepository: GameRepository, @Autowired private val messageService: MessageService) {
	fun createGame(gameID: String, sessionID: String, password: String, initiator: Player): CreateGameState {
		val state: CreateGameState = when {
			initiator.game != null -> CreateGameState.ALREADY_ASSOCIATED_WITH_GAME
			gameRepository.existsId(sessionID) -> CreateGameState.GAME_WITH_ID_ALREADY_EXISTS
			else -> {
				val game = Game(gameID, sessionID, password, initiator)
				initiator.game = game
				CreateGameState.SUCCESS
			}
		}
		return state
	}

	fun joinGame(sessionID: String, password: String, greeting: String, player: Player): JoinGameState {
		if (player.game != null) return JoinGameState.ALREADY_ASSOCIATED_WITH_GAME

		lateinit var game: Game
		try {
			game = gameRepository.getById(sessionID)
		} catch (e : NoSuchElementException) {
			return JoinGameState.INVALID_ID
		}

		if (game.password != password) return JoinGameState.INVALID_PASSWORD

		game.addPlayer(player)

		return JoinGameState.SUCCESS
	}

	fun leaveGame(player: Player): DisconnectFromGameState {
		val game = player.game ?: return DisconnectFromGameState.NO_ASSOCIATED_GAME
		game.removePlayer(player)
		return DisconnectFromGameState.SUCCESS
	}

	@Synchronized
	private fun Game.addPlayer(player: Player) {
		player.game = this
		players.forEach { messageService.sendNotification(it.session, UserJoinedNotification("")) }
		players.add(player)
	}

	@Synchronized
	private fun Game.removePlayer(player: Player) {
		player.game = null
		players.remove(player)
		if (players.isNotEmpty()) {
			players.forEach { messageService.sendNotification(it.session, UserDisconnectedNotification("")) }
		} else {
			gameRepository.remove(this)
		}
	}
}