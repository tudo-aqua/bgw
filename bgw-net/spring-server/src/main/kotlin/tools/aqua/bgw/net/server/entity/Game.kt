package tools.aqua.bgw.net.server.entity

class Game(val gameID: String, val sessionID: String, initializer: Player) {
	private val mutablePlayers: MutableList<Player> = mutableListOf(initializer)
	var orphanCandidateSince: Long? = null
		private set

	val players: List<Player>
		get() = mutablePlayers.toList()

	fun remove(player: Player): Boolean {
		val result = mutablePlayers.remove(player)
		if (mutablePlayers.isEmpty()) {
			orphanCandidateSince = System.currentTimeMillis()
		}
		return result
	}

	fun add(player: Player): Boolean {
		val result = mutablePlayers.add(player)
		if (mutablePlayers.isNotEmpty()) {
			orphanCandidateSince = null
		}
		return result
	}

	override fun equals(other: Any?): Boolean = if (other is Game) sessionID == other.sessionID else false

	override fun hashCode(): Int {
		return sessionID.hashCode()
	}
}

