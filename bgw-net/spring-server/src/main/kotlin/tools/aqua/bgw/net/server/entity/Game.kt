package tools.aqua.bgw.net.server.entity

class Game(val gameID: String, val sessionID: String, val owner: Player) {
	private val mutablePlayers: MutableList<Player> = mutableListOf(owner)

	val players: List<Player>
	get() = mutablePlayers.toList()

	fun remove(player: Player) = mutablePlayers.remove(player)

	fun add(player: Player) = mutablePlayers.add(player)

	override fun equals(other: Any?): Boolean = if (other is Game) sessionID == other.sessionID else false

	override fun hashCode(): Int {
		return sessionID.hashCode()
	}
}
