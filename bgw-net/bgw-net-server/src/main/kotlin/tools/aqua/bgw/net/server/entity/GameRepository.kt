package tools.aqua.bgw.net.server.entity

import org.springframework.stereotype.Repository

/**
 * Holds all currently active Games.
 */
@Repository
class GameRepository: ObjectRepository<Game> {
	private val gameSet: MutableSet<Game> = mutableSetOf()

	override fun add(obj: Game): Boolean = gameSet.add(obj)


	override fun remove(obj: Game): Boolean = gameSet.remove(obj)


	override fun getAll(): List<Game> {
		return gameSet.toList()
	}

	fun getBySessionID(sessionID: String): Game? = gameSet.find { it.sessionID == sessionID }
}