package tools.aqua.bgw.net.server.entity

import org.springframework.stereotype.Repository

/**
 * Holds all currently connected players.
 */
@Repository
class PlayerRepository : ObjectRepository<Player> {
	private val playerSet : MutableSet<Player> = mutableSetOf()

	override fun add(obj: Player): Boolean = playerSet.add(obj)

	override fun remove(obj: Player): Boolean = playerSet.remove(obj)

	override fun getAll(): List<Player> {
		return playerSet.toList()
	}
}