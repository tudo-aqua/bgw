package tools.aqua.bgw.net.server.entity

import org.springframework.stereotype.Repository

@Repository
class GameRepository: ObjectRepository<Game> {
	private val gameSet: MutableSet<Game> = mutableSetOf()

	override fun add(obj: Game) = gameSet.add(obj)


	override fun remove(obj: Game) = gameSet.remove(obj)


	override fun getAll(): List<Game> {
		return gameSet.toList()
	}

	fun getBySessionID(sessionID: String): Game? = gameSet.find { it.sessionID == sessionID }
}