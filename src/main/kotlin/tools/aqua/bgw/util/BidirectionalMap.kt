@file:Suppress("MemberVisibilityCanBePrivate")

package tools.aqua.bgw.util

/**
 * A bidirectional map.
 */
class BidirectionalMap<T : Any, R : Any> {
	private val map: MutableList<Pair<T, R>> = mutableListOf()
	
	/**
	 * Adds a relation A -> B if domain does not contain A and coDomain does not contain B.
	 */
	fun add(entity: T, elementView: R): Boolean {
		if (contains(entity, elementView))
			return false
		
		map.add(Pair(entity, elementView))
		return true
	}
	
	/**
	 * Forward lookup for entry.
	 *
	 * @param entity Relation key
	 *
	 * @return B for query A if relation A -> B exists
	 */
	fun forward(entity: T): R = map.first { t -> t.first == entity }.second
	
	/**
	 * Backward lookup for entry.
	 *
	 * @param value Relation key
	 *
	 * @return A for query B if relation A -> B exists
	 */
	fun backward(value: R): T = map.first { t -> t.second == value }.first
	
	/**
	 * Removes relation A -> B.
	 *
	 * @param entity A
	 * @param value B
	 */
	fun remove(entity: T, value: R): Boolean = map.removeIf { t -> t.first == entity && t.second == value }
	
	/**
	 * Removes relation A -> B.
	 *
	 * @param pair (A, B)
	 */
	fun remove(pair: Pair<T, R>): Boolean = remove(pair.first, pair.second)
	
	/**
	 * Removes by forward lookup.
	 * Removes relation A -> B.
	 *
	 * @param entity A
	 */
	fun removeForward(entity: T): Boolean = map.removeIf { t -> t.first == entity }
	
	/**
	 * Removes by backward lookup.
	 * Removes relation A -> B.
	 *
	 * @param value B
	 */
	fun removeBackward(value: R): Boolean = map.removeIf { t -> t.second == value }
	
	/**
	 * Returns whether relation A -> B exists.
	 *
	 * @param entity A
	 * @param value B
	 */
	fun contains(entity: T, value: R): Boolean = containsForward(entity) && containsBackward(value)
	
	/**
	 * Returns whether a relation A -> * exists.
	 *
	 * @param entity A
	 */
	fun containsForward(entity: T): Boolean = map.any { t -> t.first == entity }
	
	/**
	 * Returns whether a relation * -> B exists.
	 *
	 * @param value B
	 */
	fun containsBackward(value: R): Boolean = map.any { t -> t.second == value }
	
	/**
	 * Returns the domain as a set.
	 */
	fun getDomain(): Set<T> = map.map { t -> t.first }.toSet()
	
	/**
	 * Returns the coDomain as a set.
	 */
	fun getCoDomain(): Set<R> = map.map { t -> t.second }.toSet()
	
	/**
	 * Clears map.
	 */
	fun clear() {
		map.clear()
	}
}