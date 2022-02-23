/*
 * Copyright 2021-2022 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")

package tools.aqua.bgw.util

/**
 * [Stack] represents a Last In First Out (LIFO) data structure. It provides useful functions to
 * manipulate the [Stack].
 *
 * @constructor Creates a [Stack] with, given initial elements.
 */
open class Stack<T>(elements: Collection<T>) {

  private val data: ArrayDeque<T> = ArrayDeque(elements)

  /** Size of this [Stack]. */
  val size: Int
    get() = data.size

  /** Creates a [Stack] with vararg initial elements. */
  constructor(vararg element: T) : this(element.toList())

  /** Creates an empty [Stack]. */
  constructor() : this(listOf())

  /**
   * Pops the topmost element in this [Stack].
   *
   * @return Topmost element in this [Stack].
   *
   * @throws NoSuchElementException If the stack is empty.
   */
  fun pop(): T {
    if (data.isEmpty()) throw NoSuchElementException("Can not pop on empty Stack $this.")

    return data.removeLast()
  }

  /**
   * Pops the topmost element in this [Stack].
   *
   * @return Topmost element in this [Stack] or null if the [Stack] is empty.
   */
  fun popOrNull(): T? = data.removeLastOrNull()

  /**
   * Pops the n topmost elements in this [Stack], where n is specified by the parameter. The topmost
   * element in the stack gets the list's head i.e. index `0`.
   *
   * @param numToPop Specifies how many elements to pop.
   *
   * @return The popped elements in a List, with the last popped element at the highest index.
   *
   * @throws IllegalArgumentException If numToPop is negative or greater than the [Stack]'s size.
   */
  fun popAll(numToPop: Int = data.size): List<T> {
    require(numToPop >= 0) { "NumToPop must not be negative." }
    require(numToPop <= data.size) { "Not enough elements to pop in this Stack $this." }
    return List(numToPop) { data.removeLast() }
  }

  /**
   * Pops all elements in this [Stack] and returns them in a [List], with the topmost element as the
   * list's head i.e. index `0`.
   *
   * @return All elements in this [Stack] popped.
   */
  fun clear(): List<T> {
    val tmp = List(data.size) { data[data.size - 1 - it] }
    data.clear()
    return tmp
  }

  /**
   * Pushes the supplied element onto the [Stack].
   *
   * @param element The element to push onto this [Stack].
   */
  fun push(element: T) {
    data.addLast(element)
  }

  /**
   * Pushes all the supplied elements onto the [Stack]. The element at index 0 of the List is pushed
   * first.
   *
   * @param elements The elements to push onto this [Stack].
   */
  fun pushAll(elements: Collection<T>) {
    elements.forEach { data.addLast(it) }
  }

  /**
   * Pushes all the supplied elements onto the [Stack]. The first parameter is pushed first.
   *
   * @param elements The elements to push onto this [Stack].
   */
  fun pushAll(vararg elements: T) {
    elements.forEach { data.addLast(it) }
  }

  /**
   * Returns the topmost element in this [Stack] but does not pop it.
   *
   * @return Topmost element in this [Stack].
   *
   * @throws NoSuchElementException If the stack is empty.
   */
  fun peek(): T {
    if (data.isEmpty()) throw NoSuchElementException("Can not peek on empty Stack $this.")

    return data.last()
  }

  /**
   * Returns the topmost element in this [Stack] but does not pop it.
   *
   * @return Topmost element in this [Stack] or null if the [Stack] is empty.
   */
  fun peekOrNull(): T? = data.lastOrNull()

  /**
   * Returns all elements in this [Stack], with the last pushed Element at the highest index.
   *
   * @return All elements in this [Stack] as [List].
   *
   * @throws IllegalArgumentException If numToPeek is negative or greater than the [Stack]'s size.
   */
  fun peekAll(numToPeek: Int = data.size): List<T> {
    require(numToPeek >= 0) { "NumToPeek must not be negative." }
    require(numToPeek <= data.size) { "Not enough elements to peek in this Stack $this." }
    return List(numToPeek) { data[data.size - it - 1] }
  }

  /**
   * Returns the index of the first occurrence of the specified element in the [Stack], or -1 if the
   * specified element is not contained in the [Stack].
   *
   * @return First index of the [element] or -1 if it is not contained in the [Stack].
   */
  fun indexOf(element: T): Int = data.indexOf(element)

  /**
   * Returns whether this [Stack] is empty or not.
   *
   * @return `true` if the [Stack] contains no elements, `false` otherwise.
   */
  fun isEmpty(): Boolean = data.isEmpty()

  /**
   * Returns whether this [Stack] is not empty.
   *
   * @return `true` if the [Stack] contains elements, `false` otherwise.
   */
  fun isNotEmpty(): Boolean = !isEmpty()

  /** Shuffles this [Stack]. */
  fun shuffle() {
    data.shuffle()
  }

  /**
   * Sorts this [Stack].
   *
   * @param comparator Comparator for sorting.
   */
  fun sort(comparator: Comparator<in T>) {
    data.sortWith(comparator)
  }
}
