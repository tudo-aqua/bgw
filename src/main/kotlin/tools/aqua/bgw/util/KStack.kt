@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.util

/**
 * KStack represents a Last In First Out (LIFO) data structure.
 * It provides useful functions to manipulate the KStack.
 */
class KStack<T> {
	private val data = mutableListOf<T>()
	
	/**
	 * Pops the topmost element in this KStack.
	 *
	 * @return topmost element in this KSTack.
	 */
	fun pop(): T {
		if (data.size < 1) throw RuntimeException("Can not pop on empty KStack $this.")
		return data.removeAt(0)
	}
	
	/**
	 * Pops the n topmost elements in this KStack, where n is specified by the parameter.
	 *
	 * @param numToPop specifies how many elements to pop.
	 * @return the popped elements in a List, with the last popped element at the highest index.
	 * @throws IllegalArgumentException if numToPop is negative or greater than the KStack's size.
	 */
	fun pop(numToPop: Int): List<T> {
		when {
			numToPop < 0 ->
				throw IllegalArgumentException("NumToPop must not be negative.")
			numToPop !in 0..data.size ->
				throw IllegalArgumentException("Not enough elements to pop in this KStack $this.")
			else ->
				return List(numToPop) { pop() }
		}
	}
	
	/**
	 * Pops all elements in this KStack and returns them in a List,
	 * with the last pushed element at the highest index.
	 */
	fun popAll(): List<T> {
		val tmp = data.asReversed()
		clear()
		return tmp
	}
	
	/**
	 * Pushes the supplied element onto the KStack.
	 *
	 * @param element the element to push onto this KStack.
	 */
	fun push(element: T) {
		data.add(0, element)
	}
	
	/**
	 * Pushes all the supplied elements onto the KStack.
	 * The element at index 0 of the List is pushed first.
	 *
	 * @param elements the elements to push onto this KStack.
	 */
	fun push(elements: List<T>) {
		elements.forEach { push(it) }
	}
	
	/**
	 * Returns the topmost element in this KStack but does not pop it.
	 */
	fun peek(): T {
		if (data.size < 1) throw RuntimeException("Can not peek on empty KStack $this.")
		return data[0]
	}
	
	/**
	 * Returns all elements in this KStack,
	 * with the last pushed Element at the highest index.
	 */
	fun peekAll(): List<T> = data.asReversed()
	
	/**
	 * Returns the index of the first occurrence of the specified element in the KStack,
	 * or -1 if the specified element is not contained in the KStack.
	 */
	fun indexOf(element: T): Int = data.indexOf(element)
	
	/**
	 * Returns whether this KStack is empty or not.
	 */
	fun isEmpty(): Boolean = data.isEmpty()
	
	/**
	 * Returns the size of this KStack.
	 */
	fun size(): Int = data.size
	
	/**
	 * Clears this KStack.
	 */
	fun clear(): Unit = data.clear()
	
	/**
	 * Shuffles this KStack.
	 */
	fun shuffle(): Unit = data.shuffle()
}