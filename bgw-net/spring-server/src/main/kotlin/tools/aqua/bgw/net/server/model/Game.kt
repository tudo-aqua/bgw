package tools.aqua.bgw.net.server.model

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.*

class Game(val uuid: UUID) {
	val players: MutableList<Player> = mutableListOf()
}

@Component
class Games() {
	private val games = mutableListOf<Game>()

	fun add(game: Game) {
		games.add(game)
	}

	fun remove(game: Game) {
		games.remove(game)
	}

	fun getByUUID(uuid: UUID) {

	}
}