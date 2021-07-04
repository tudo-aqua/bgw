@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.observable

import java.util.*
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.function.UnaryOperator
import kotlin.streams.toList

/**
 * An observable [ArrayList].
 */
class ObservableArrayList<T>(elements: Collection<T> = listOf()) : ObservableList<T>() {
	/**
	 * List field.
	 */
	override val list: MutableList<T> = ArrayList(elements)
}

/**
 * An observable [LinkedList].
 */
class ObservableLinkedList<T>(elements: Collection<T> = listOf()) : ObservableList<T>() {
	/**
	 * List field.
	 */
	override val list: MutableList<T> = LinkedList(elements)
}

/**
 * An observable [List] implementation.
 */
abstract class ObservableList<T> : Observable(), Iterable<T> {
	
	/**
	 * Returns the number of elements in this list.
	 */
	val size: Int
		get() = list.size
	
	/**
	 * All available indices of this list as IntRange i.e. 0..size()-1.
	 */
	val indices: IntRange
		get() = 0 until size
	
	/**
	 * List field.
	 */
	abstract val list: MutableList<T>
	
	/**
	 * Returns `true` if this list contains no elements.
	 *
	 * @return `true` if this list contains no elements.
	 */
	fun isEmpty(): Boolean = list.size == 0
	
	/**
	 * Returns `true` if this list contains elements.
	 *
	 * @return `true` if this list contains elements.
	 */
	fun isNotEmpty(): Boolean = !isEmpty()
	
	/**
	 * Returns `true` if this list contains the specified element.
	 * More formally, returns `true` if and only if this list contains
	 * at least one element `e` such that
	 * `Objects.equals(o, e)`.
	 *
	 * @param o element whose presence in this list is to be tested.
	 * @return `true` if this list contains the specified element, `false` otherwise.
	 */
	operator fun contains(o: Any?): Boolean = list.contains(o)
	
	/**
	 * Returns the index of the first occurrence of the specified element in this list,
	 * or -1 if this list does not contain the element.
	 * More formally, returns the lowest index `i` such that
	 * `Objects.equals(o, get(i))`, or -1 if there is no such index.
	 *
	 * @return first index of the element, or -1 if the list does not contain the element.
	 */
	fun indexOf(o: Any?): Int = list.indexOf(o)
	
	/**
	 * Returns the index of the last occurrence of the specified element in this list,
	 * or -1 if this list does not contain the element.
	 * More formally, returns the highest index `i` such that
	 * `Objects.equals(o, get(i))`,
	 * or -1 if there is no such index.
	 *
	 * @return last index of the element, or -1 if the list does not contain the element.
	 */
	fun lastIndexOf(o: Any?): Int = list.lastIndexOf(o)
	
	/**
	 * Returns the element at the specified position in this list.
	 *
	 * @param index index of the element to return.
	 *
	 * @return the element at the specified position in this list.
	 *
	 * @throws IndexOutOfBoundsException If the index exceeds the list's bounds.
	 */
	operator fun get(index: Int): T = list[index]
	
	/**
	 * Replaces the element at the specified position in this list with
	 * the specified element.
	 *
	 * @param index index of the element to replace.
	 * @param element element to be stored at the specified position.
	 *
	 * @return the element previously at the specified position.
	 *
	 * @throws IndexOutOfBoundsException if the index exceeds the list's bounds.
	 */
	operator fun set(index: Int, element: T): T {
		val oldValue: T = list[index]
		list[index] = element
		notifyChange()
		return oldValue
	}
	
	/**
	 * Appends the specified element to the end of this list.
	 *
	 * @param element element to be appended to this list.
	 */
	fun add(element: T): Boolean {
		val result = list.add(element)
		notifyChange()
		return result
	}
	
	/**
	 * Inserts the specified element at the specified position in this list.
	 * Shifts the element currently at that position (if any) and any subsequent elements to the right
	 * (adds one to their indices).
	 *
	 * @param index index at which the specified element is to be inserted.
	 * @param element element to be inserted.
	 *
	 * @throws IndexOutOfBoundsException if the index exceeds the list's bounds.
	 */
	fun add(index: Int, element: T) {
		list.add(index, element)
		notifyChange()
	}
	
	/**
	 * Removes the first occurrence of the specified element from this list, if it is present.
	 * If the list does not contain the element, it is unchanged.
	 * More formally, removes the element with the lowest index `i` such that `Objects.equals(o, get(i))`
	 * (if such an element exists).
	 * Returns `true` if this list contained the specified element
	 * (or equivalently, if this list changed as a result of the call).
	 *
	 * @param o element to be removed from this list, if present.
	 *
	 * @return `true` if this list contained the specified element.
	 */
	fun remove(o: T): Boolean {
		val result = list.remove(o)
		
		if (result)
			notifyChange()
		
		return result
	}
	
	/**
	 * Removes an element at the specified [index] from the list.
	 *
	 * @return the element that has been removed.
	 *
	 * @throws IndexOutOfBoundsException if the index exceeds the list's bounds.
	 */
	fun removeAt(index: Int): T {
		val result = list.removeAt(index)
		notifyChange()
		return result
	}
	
	/**
	 * Removes the first element from this list and returns that removed element,
	 * or returns null if this list is empty.
	 */
	fun removeFirstOrNull(): T? {
		val result = list.removeFirstOrNull()
		
		if (result != null)
			notifyChange()
		
		return result
	}
	
	/**
	 * Removes the first element from this list and returns that removed element.
	 *
	 * @throws NoSuchElementException if the list was empty.
	 */
	fun removeFirst(): T = removeFirstOrNull() ?: throw NoSuchElementException("List is empty")
	
	/**
	 * Removes the last element from this list and returns that removed element,
	 * or returns null if this list is empty.
	 */
	fun removeLastOrNull(): T? {
		val result = list.removeLastOrNull()
		
		if (result != null)
			notifyChange()
		
		return result
	}
	
	/**
	 * Removes the last element from this list and returns that removed element.
	 *
	 * @throws NoSuchElementException if the list was empty.
	 */
	fun removeLast(): T = removeLastOrNull() ?: throw NoSuchElementException("List is empty")
	
	
	/**
	 * Removes all of the elements from this list. The list will be empty after this call returns.
	 */
	fun clear() {
		if (list.isNotEmpty()) {
			list.clear()
			notifyChange()
		}
	}
	
	/**
	 * Appends all of the elements in the specified collection to the end of this list,
	 * in the order that they are returned by the specified collection's Iterator.
	 * The behavior of this operation is undefined if the specified collection is modified
	 * while the operation is in progress. (This implies that the behavior of this call is undefined
	 * if the specified collection is this list, and this list is nonempty.)
	 *
	 * @param elements collection containing elements to be added to this list.
	 *
	 * @return `true` if this list changed as a result of the call.
	 *
	 * @throws NullPointerException if the specified collection is null.
	 */
	fun addAll(elements: Collection<T>): Boolean {
		val result = list.addAll(elements)
		notifyChange()
		return result
	}
	
	/**
	 * Inserts all of the elements in the specified collection into this list,
	 * starting at the specified position. Shifts the element currently at that position (if any)
	 * and any subsequent elements to the right (increases their indices). The new elements will appear
	 * in the list in the order that they are returned by the specified collection's iterator.
	 *
	 * @param index index at which to insert the first element from the specified collection.
	 * @param elements collection containing elements to be added to this list.
	 *
	 * @return `true` if this list changed as a result of the call.
	 *
	 * @throws IndexOutOfBoundsException if the index exceeds the list's bounds.
	 * @throws NullPointerException if the specified collection is null.
	 */
	fun addAll(index: Int, elements: Collection<T>): Boolean {
		val result = list.addAll(index, elements)
		notifyChange()
		return result
	}
	
	/**
	 * Removes from this list all of its elements that are contained in the specified collection.
	 *
	 * @param elements collection containing elements to be removed from this list.
	 *
	 * @return `true` if this list changed as a result of the call.
	 *
	 * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection.
	 * @throws NullPointerException if this list contains a null element and the specified collection
	 * does not permit null elements or if the specified collection is null.
	 *
	 * @see Collection.contains
	 */
	fun removeAll(elements: Collection<*>): Boolean {
		val result = list.removeAll(elements)
		
		if (result)
			notifyChange()
		
		return result
	}
	
	/**
	 * Retains only the elements in this list that are contained in the specified collection.
	 * In other words, removes from this list all of its elements that are not contained in the specified collection.
	 *
	 * @param elements collection containing elements to be retained in this list.
	 *
	 * @return `true` if this list changed as a result of the call.
	 *
	 * @throws ClassCastException if the class of an element of this list is incompatible with the specified collection.
	 * @throws NullPointerException if this list contains a null element and the specified collection
	 * does not permit null elements or if the specified collection is null.
	 *
	 * @see Collection.contains
	 */
	fun retainAll(elements: Collection<*>): Boolean {
		val result = list.retainAll(elements)
		notifyChange()
		return result
	}
	
	/**
	 * Returns a view of the portion of this list between the specified `fromIndex` inclusive and `toIndex` exclusive.
	 * (If `fromIndex` and `toIndex` are equal, the returned list is empty.)
	 * The returned list is backed by this list, so non-structural changes in the returned list
	 * are reflected in this list, and vice-versa.
	 * The returned list supports all of the optional list operations.
	 *
	 * This method eliminates the need for explicit range operations (of the sort that commonly exist for arrays).
	 * Any operation that expects a list can be used as a range operation
	 * by passing a subList view instead of a whole list.
	 * For example, the following idiom removes a range of elements from a list:
	 * <pre>
	 * list.subList(from, to).clear();
	 * </pre>
	 * Similar idioms may be constructed for [.indexOf] and [.lastIndexOf], and all of the algorithms in the
	 * [Collections] class can be applied to a subList.
	 *
	 * The semantics of the list returned by this method become undefined if the backing list (i.e., this list)
	 * is *structurally modified* in any way other than via the returned list.
	 * (Structural modifications are those that change the size of this list, or otherwise perturb it in such
	 * a fashion that iterations in progress may yield incorrect results.).
	 *
	 * @throws IndexOutOfBoundsException if any index exceeds the list's bounds.
	 * @throws IllegalArgumentException if the endpoint indices are out of order (fromIndex > toIndex).
	 */
	fun subList(fromIndex: Int, toIndex: Int): List<T> = list.subList(fromIndex, toIndex)
	
	/**
	 * ForEach action.
	 */
	fun forEach(action: Consumer<in T>): Unit = list.forEach(action)
	
	/**
	 * Creates *[late-binding](Spliterator.html#binding)* and *fail-fast* [Spliterator] over the elements in this list.
	 *
	 * @return a `Spliterator` over the elements in this list
	 */
	fun spliterator(): Spliterator<T> = list.spliterator()
	
	/**
	 * Removes all of the elements of this collection that satisfy the given predicate.
	 * Errors or runtime exceptions thrown during iteration or by the predicate are relayed to the caller.
	 *
	 * @param filter a predicate which returns `true` for elements to be removed.
	 *
	 * @return `true` if elements were removed.
	 * @throws NullPointerException if the specified filter is null
	 */
	fun removeIf(filter: Predicate<in T>): Boolean {
		val result = list.removeIf(filter)
		
		if (result)
			notifyChange()
		
		return result
	}
	
	/**
	 * Replaces each element of this list with the result of applying the operator to that element.
	 *
	 * @param operator the operator to apply to each element.
	 */
	fun replaceAll(operator: UnaryOperator<T>) {
		list.replaceAll(operator)
		notifyChange()
	}
	
	/**
	 * Sorts this list by given comparator.
	 *
	 * @param comparator comparator to be applied.
	 * If the elements contained in this list implement the comparable interface, pass null.
	 */
	fun sort(comparator: Comparator<in T>?) {
		val sorted = list.stream().sorted(comparator).toList()
		list.clear()
		list.addAll(sorted)
		//notifyChange() not necessary since addAll() already notified.
	}
	
	/**
	 * Returns an iterator over the elements in this list.
	 *
	 * @return iterator over the elements in this list.
	 */
	override fun iterator(): Iterator<T> = list.iterator()
}