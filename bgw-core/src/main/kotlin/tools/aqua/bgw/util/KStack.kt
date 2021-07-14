/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.util

/**
 * [KStack] represents a Last In First Out (LIFO) data structure.
 * It provides useful functions to manipulate the [KStack].
 */
open class KStack<T> {
	private val data = mutableListOf<T>()
	
	/**
	 * Size of this [KStack].
	 */
	val size: Int = data.size
	
	/**
	 * Pops the topmost element in this [KStack].
	 *
	 * @return topmost element in this [KStack].
	 */
	fun pop(): T {
		if (data.size < 1) throw RuntimeException("Can not pop on empty KStack $this.")
		return data.removeAt(0)
	}
	
	/**
	 * Pops the n topmost elements in this [KStack], where n is specified by the parameter.
	 * The topmost element in the stack gets the list's head i.e. index `0`.
	 *
	 * @param numToPop specifies how many elements to pop.
	 *
	 * @return the popped elements in a List, with the last popped element at the highest index.
	 *
	 * @throws IllegalArgumentException if numToPop is negative or greater than the [KStack]'s size.
	 */
	fun pop(numToPop: Int): List<T> {
		when {
			numToPop < 0 ->
				throw IllegalArgumentException("NumToPop must not be negative.")
			numToPop > data.size ->
				throw IllegalArgumentException("Not enough elements to pop in this KStack $this.")
			else ->
				return List(numToPop) { pop() }
		}
	}
	
	/**
	 * Pops all elements in this [KStack] and returns them in a [List],
	 * with the topmost element as the list's head i.e. index `0`.
	 *
	 * @return all elements in this [KStack] popped.
	 */
	fun popAll(): List<T> {
		val tmp = data.toList()
		clear()
		return tmp
	}
	
	/**
	 * Pushes the supplied element onto the [KStack].
	 *
	 * @param element the element to push onto this [KStack].
	 */
	fun push(element: T) {
		data.add(0, element)
	}
	
	/**
	 * Pushes all the supplied elements onto the [KStack].
	 * The element at index 0 of the List is pushed first.
	 *
	 * @param elements the elements to push onto this [KStack].
	 */
	fun push(elements: List<T>) {
		elements.forEach { push(it) }
	}
	
	/**
	 * Returns the topmost element in this [KStack] but does not pop it.
	 *
	 * @return topmost element in this [KStack].
	 */
	fun peek(): T {
		if (data.size < 1) throw RuntimeException("Can not peek on empty KStack $this.")
		return data[0]
	}
	
	/**
	 * Returns all elements in this [KStack],
	 * with the last pushed Element at the highest index.
	 *
	 * @return  all elements in this [KStack] as [List].
	 */
	fun peekAll(): List<T> = data.asReversed()
	
	/**
	 * Returns the index of the first occurrence of the specified element in the [KStack],
	 * or -1 if the specified element is not contained in the [KStack].
	 *
	 * @return first index of the [element] or -1 if it is not contained in the [KStack].
	 */
	fun indexOf(element: T): Int = data.indexOf(element)
	
	/**
	 * Returns whether this [KStack] is empty or not.
	 *
	 * @return `true` if the [KStack] contains no elements, `false` otherwise.
	 */
	fun isEmpty(): Boolean = data.isEmpty()
	
	/**
	 * Returns whether this [KStack] is not empty.
	 *
	 * @return `true` if the [KStack] contains elements, `false` otherwise.
	 */
	fun isNotEmpty(): Boolean = data.isNotEmpty()
	
	
	/**
	 * Clears this [KStack].
	 */
	fun clear(): Unit = data.clear()
	
	/**
	 * Shuffles this [KStack].
	 */
	fun shuffle(): Unit = data.shuffle()
}