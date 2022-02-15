package tools.aqua.bgw.net.server.service

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.common.*
import tools.aqua.bgw.net.server.ORPHANED_GAME_CHECK_RATE
import tools.aqua.bgw.net.server.TIME_UNTIL_ORPHANED
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.GameRepository
import tools.aqua.bgw.net.server.entity.Player

/**
 * This service handles all interactions associated with [Game].
 */
@Service
class GameService(private val gameRepository: GameRepository) {

	private val logger = LoggerFactory.getLogger(javaClass)

	@Synchronized
	fun createGame(gameID: String, sessionID: String, initializer: Player): CreateGameResponseStatus {
		val status = if (initializer.game == null) {
			val game = Game(gameID, sessionID, initializer)
			if (gameRepository.add(game)) {
				initializer.game = game
				CreateGameResponseStatus.SUCCESS
			} else CreateGameResponseStatus.SESSION_WITH_ID_ALREADY_EXISTS
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
				game == null -> JoinGameResponseStatus.INVALID_SESSION_ID
				game.players.any { it.name == player.name } -> JoinGameResponseStatus.PLAYER_NAME_ALREADY_TAKEN
				else -> {
					game.add(player)
					player.game = game
					JoinGameResponseStatus.SUCCESS
				}
			}
		}

	/**
	 * Looks for orphaned Games and removes them.
	 *
	 * A Game is considered orphaned when
	 * [Game.orphanCandidateSince] - [System.currentTimeMillis] > [TIME_UNTIL_ORPHANED]
	 *
	 * This function is scheduled to run on a fixed rate every [ORPHANED_GAME_CHECK_RATE] milliseconds.
	 */
	@Synchronized
	@Scheduled(fixedRate = ORPHANED_GAME_CHECK_RATE)
	fun removeOrphanedGames() {
		logger.info("Started looking for orphaned games")
		var numRemoved = 0
		gameRepository.getAll().forEach { game ->
			game.orphanCandidateSince?.let {
				if (it + TIME_UNTIL_ORPHANED < System.currentTimeMillis()) {
					gameRepository.remove(game)
					numRemoved++
					logger.info("Removed game with id ${game.sessionID} because it was orphaned")
				}
			}
		}
		logger.info("Stopped looking for orphaned games. $numRemoved games have been removed")
	}

	fun getBySessionID(sessionID: String): Game? = gameRepository.getBySessionID(sessionID)

	fun getAll(): List<Game> = gameRepository.getAll()
}


