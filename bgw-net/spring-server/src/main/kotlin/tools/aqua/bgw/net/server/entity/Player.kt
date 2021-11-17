package tools.aqua.bgw.net.server.entity

import org.springframework.web.socket.WebSocketSession

class Player(val session: WebSocketSession, var game: Game?) {
	val sessionId: String
	get() = session.id

	override fun equals(other: Any?): Boolean {
		if (other is Player) {
			return sessionId == other.sessionId
		}
		return false
	}

	override fun hashCode(): Int {
		var result = session.hashCode()
		result = 31 * result + (game?.hashCode() ?: 0)
		result = 31 * result + sessionId.hashCode()
		return result
	}
}