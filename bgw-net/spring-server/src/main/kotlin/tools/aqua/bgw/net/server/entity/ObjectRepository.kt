package tools.aqua.bgw.net.server.entity

import kotlin.jvm.Throws

interface ObjectRepository<T> {
	fun add(obj: T)

	fun remove(obj: T)

	fun getAll() : List<T>

	@Throws(NoSuchElementException::class)
	fun getById(string: String) : T

	fun existsId(string: String) : Boolean
}