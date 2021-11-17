package tools.aqua.bgw.net.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.entity.PlayerRepository

@Service
class PlayerService(@Autowired val playerRepository: PlayerRepository) {
	fun createPlayer(session: WebSocketSession) {
		playerRepository.add(Player(session, null))
	}

	fun deletePlayer(session: WebSocketSession) {
		playerRepository.remove(playerRepository.getById(session.id))
	}

	fun getPlayerByID(id: String) : Player {
		return playerRepository.getById(id)
	}
}