package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.GameRepository
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.PlayerRepository

/**
 * This service exposes all active games and players to the frontend.
 * //TODO maybe make Games and Players read only for frontend?
 */
@Service
class FrontendService(
	private val playerRepository: PlayerRepository,
	private val gameRepository: GameRepository
) {
	val activePlayers: List<Player>
		get() = playerRepository.getAll()

	val activeGames: List<Game>
		get() = gameRepository.getAll()
}