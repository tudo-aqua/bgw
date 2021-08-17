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
 * [Stack] represents a Last In First Out (LIFO) data structure.
 * It provides useful functions to manipulate the [Stack].
 *
 * @constructor Creates an empty [Stack].
 */
open class Stack<T> {
    private val data = mutableListOf<T>()

    /**
     * Size of this [Stack].
     */
    val size: Int
        get() = data.size

    /**
     * Pops the topmost element in this [Stack].
     *
     * @return Topmost element in this [Stack].
     *
     * @throws NoSuchElementException If the stack is empty.
     */
    fun pop(): T {
        if (data.size < 1)
            throw NoSuchElementException("Can not pop on empty KStack $this.")
        
        return data.removeAt(0)
    }

    /**
     * Pops the n topmost elements in this [Stack], where n is specified by the parameter.
     * The topmost element in the stack gets the list's head i.e. index `0`.
     *
     * @param numToPop Specifies how many elements to pop.
     *
     * @return The popped elements in a List, with the last popped element at the highest index.
     *
     * @throws IllegalArgumentException If numToPop is negative or greater than the [Stack]'s size.
     */
    fun popAll(numToPop: Int): List<T> {
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
     * Pops all elements in this [Stack] and returns them in a [List],
     * with the topmost element as the list's head i.e. index `0`.
     *
     * @return All elements in this [Stack] popped.
     */
    fun clear(): List<T> {
        val tmp = data.toList()
        data.clear()
        return tmp
    }

    /**
     * Pushes the supplied element onto the [Stack].
     *
     * @param element The element to push onto this [Stack].
     */
    fun push(element: T) {
        data.add(0, element)
    }

    /**
     * Pushes all the supplied elements onto the [Stack].
     * The element at index 0 of the List is pushed first.
     *
     * @param elements The elements to push onto this [Stack].
     */
    fun pushAll(elements: Collection<T>) {
        elements.forEach { push(it) }
    }

    /**
     * Pushes all the supplied elements onto the [Stack].
     * The first parameter is pushed first.
     *
     * @param elements The elements to push onto this [Stack].
     */
    fun pushAll(vararg elements : T) {
        elements.forEach { push(it) }
    }

    /**
     * Returns the topmost element in this [Stack] but does not pop it.
     *
     * @return Topmost element in this [Stack].
     *
     * @throws NoSuchElementException If the stack is empty.
     */
    fun peek(): T {
        if (data.size < 1)
            throw NoSuchElementException("Can not peek on empty KStack $this.")
        
        return data[0]
    }

    /**
     * Returns all elements in this [Stack],
     * with the last pushed Element at the highest index.
     *
     * @return All elements in this [Stack] as [List].
     */
    fun peekAll(): List<T> = data.asReversed()

    /**
     * Returns the index of the first occurrence of the specified element in the [Stack],
     * or -1 if the specified element is not contained in the [Stack].
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
    fun isNotEmpty(): Boolean = data.isNotEmpty()

    /**
     * Shuffles this [Stack].
     */
    fun shuffle(): Unit = data.shuffle()
}