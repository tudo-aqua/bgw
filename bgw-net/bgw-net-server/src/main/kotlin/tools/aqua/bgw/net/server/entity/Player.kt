package tools.aqua.bgw.net.server.entity

import org.springframework.web.socket.WebSocketSession

class Player(val name: String, var game: Game?, val session: WebSocketSession) {

	override fun equals(other: Any?): Boolean = if (other is Player) session == other.session else false
	
	override fun hashCode(): Int {
		return session.hashCode()
	}
}