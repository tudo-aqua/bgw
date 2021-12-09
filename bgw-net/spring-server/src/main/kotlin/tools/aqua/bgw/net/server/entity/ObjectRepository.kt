package tools.aqua.bgw.net.server.entity

import kotlin.jvm.Throws

interface ObjectRepository<T> {
	fun add(obj: T) : Boolean

	fun remove(obj: T) : Boolean

	fun getAll() : List<T>
}