package tools.aqua.bgw.net.server.entity

import org.springframework.stereotype.Repository

@Repository
class PlayerRepository() : ObjectRepository<Player> {
	val playerSet : MutableSet<Player> = mutableSetOf()

	override fun add(obj: Player) {
		playerSet.add(obj)
	}

	override fun remove(obj: Player) {
		playerSet.remove(obj)
	}

	override fun getAll(): List<Player> {
		return playerSet.toList()
	}

	override fun getById(string: String): Player {
		return playerSet.find {
			it.session.id == string
		} ?: throw NoSuchElementException()
	}

	override fun existsId(string: String): Boolean {
		return playerSet.any { it.session.id == string }
	}
}