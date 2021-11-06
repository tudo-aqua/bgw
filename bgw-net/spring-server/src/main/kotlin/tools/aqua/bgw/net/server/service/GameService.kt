package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import tools.aqua.bgw.net.server.model.Player
import tools.aqua.bgw.net.server.model.Game
import tools.aqua.bgw.net.server.model.Games
import java.util.*

@Service
class GameService(@Autowired private val games: Games) {
	fun createGame(initiator: Player) : Game =
		Game(UUID.randomUUID()).apply {
			players.add(initiator)
			initiator.game = this
		}

	fun endGame(game: Game) {
		game.players.forEach {
			it.game = null
			it.session.sendMessage(TextMessage("GameEnded")) //TODO proper message
		}
		games.remove(game)
	}
}