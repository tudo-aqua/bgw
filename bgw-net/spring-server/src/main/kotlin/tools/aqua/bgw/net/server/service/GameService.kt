package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.GameRepository
import tools.aqua.bgw.net.server.entity.Player

@Service
class GameService(@Autowired private val gameRepository: GameRepository) {
	@Synchronized
	fun createGame(gameID: String, sessionID: String, owner: Player): CreateGameResponseStatus {
		val status = if (owner.game == null) {
			val game = Game(gameID, sessionID, owner)
			if (gameRepository.add(game)) {
				owner.game = game
				CreateGameResponseStatus.SUCCESS
			} else CreateGameResponseStatus.GAME_WITH_ID_ALREADY_EXISTS
		} else CreateGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME
		return status
	}

	@Synchronized
	fun leaveGame(player: Player): LeaveGameResponseStatus {
		val game = player.game
		return if (game == null) {
			LeaveGameResponseStatus.NO_ASSOCIATED_GAME
		} else {
			if (game.remove(player)) {
				player.game = null
				LeaveGameResponseStatus.SUCCESS
			} else LeaveGameResponseStatus.SERVER_ERROR
		}
	}

	@Synchronized
	fun joinGame(player: Player, sessionID: String): JoinGameResponseStatus =
		if (player.game != null) {
			JoinGameResponseStatus.ALREADY_ASSOCIATED_WITH_GAME
		} else {
			val game = gameRepository.getBySessionID(sessionID)
			when {
				game == null -> JoinGameResponseStatus.INVALID_ID
				game.players.any { it.name == player.name } -> JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN
				else -> {
					game.add(player)
					player.game = game
					JoinGameResponseStatus.SUCCESS
				}
			}
		}

	@Synchronized
	@Scheduled(fixedRate = 60000)
	fun removeOrphanedGames() {

	}

	fun getBySessionID(sessionID: String): Game? = gameRepository.getBySessionID(sessionID)

	fun getAll(): List<Game> = gameRepository.getAll()
}


