package tools.aqua.bgw.net.server.entity

class Game(val gameID: String, val sessionID: String, val password: String, initiator: Player) {
	val players: MutableList<Player> = mutableListOf(initiator)

	override fun equals(other: Any?): Boolean {
		if (other is Game) {
			return sessionID == other.sessionID
		}
		return false
	}

	override fun hashCode(): Int {
		var result = gameID.hashCode()
		result = 31 * result + sessionID.hashCode()
		result = 31 * result + password.hashCode()
		result = 31 * result + players.hashCode()
		return result
	}
}
