package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.PlayerRepository

@Service
class PlayerService(@Autowired val playerRepository: PlayerRepository) {
	fun createPlayer(session: WebSocketSession) {
		val playerName = session.attributes["playerName"] ?: error("playerName attribute missing") //TODO
		with(Player(playerName as String, null, session)) {
			playerRepository.add(this)
			session.attributes["player"] = this
		}
	}

	fun deletePlayer(session: WebSocketSession) {
		val player = session.attributes["player"] ?: error("player attribute missing") //TODO
		//TODO remove from its game
		playerRepository.remove(player as Player)
	}

	fun getAll() : List<Player> = playerRepository.getAll()

}