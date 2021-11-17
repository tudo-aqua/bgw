package tools.aqua.bgw.net.server.entity

import org.springframework.stereotype.Repository

@Repository
class GameRepository: ObjectRepository<Game> {
	val gameSet: MutableSet<Game> = mutableSetOf()

	override fun add(obj: Game) {
		gameSet.add(obj)
	}

	override fun remove(obj: Game) {
		gameSet.remove(obj)
	}

	override fun getAll(): List<Game> {
		return gameSet.toList()
	}

	override fun getById(string: String): Game {
		return gameSet.find {
			it.sessionID == string
		} ?: throw NoSuchElementException()
	}

	override fun existsId(string: String): Boolean {
		return gameSet.any { it.sessionID == string }
	}
}