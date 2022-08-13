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
 * A bidirectional map. Each key-value-pair gets mapped in both directions. Keys and values must be
 * unique in the sense that there must not be a duplicate key in the domain, nor a duplicate value
 * in the coDomain, but the same element may appear once as key and once as value.
 *
 * Example:
 *
 * A -> B
 *
 * A -> C
 *
 * is invalid because A is contained twice in the Domain.<br>
 *
 * A -> B
 *
 * C -> B
 *
 * is invalid because B is contained twice in the coDomain.<br>
 *
 * A -> B
 *
 * C -> A
 *
 * is valid because A is only contained once in the domain and in the coDomain.
 *
 * @constructor Creates a map with the given set of elements mapping pair first -> pair.second.
 *
 * @param T Type of domain elements.
 * @param R Type of co-domain elements.
 * @param elements Elements to be initialized in the map.
 */
open class BidirectionalMap<T : Any, R : Any>(vararg elements: Pair<T, R>) {
  private val map: MutableList<Pair<T, R>> = mutableListOf()

  init {
    require(elements.all { add(it) })
  }

  /** The size of this map. */
  val size: Int
    /** @return The amount of pairs. */
    get() = map.size

  /**
   * Adds a relation A -> B if domain does not contain A and coDomain does not contain B. Returns
   * `false` if the relation could not be added.
   *
   * @return `true` if the element was added to the map, `false` otherwise.
   *
   * @see addAll
   */
  fun add(entity: T, value: R): Boolean {
    if (contains(entity, value)) return true

    if (containsForward(entity) || containsBackward(value)) return false

    return map.add(Pair(entity, value))
  }

  /**
   * Adds a relation A -> B if domain does not contain A and coDomain does not contain B. Returns
   * `false` if the relation could not be added.
   *
   * @return `true` if the element was added to the map or already existed, `false` otherwise.
   *
   * @see addAll
   */
  fun add(element: Pair<T, R>): Boolean = add(element.first, element.second)

  /**
   * Adds all relations A -> B. If any of the given items already exist, it gets ignored. If any
   * item contains a key or value that already exists, the map remains unchanged.
   *
   * Example: Map: [(A->B), (C->D)]
   *
   * addAll[(E->F),(G->H)] results in [(A->B), (C->D), (E->F),(G->H)] : true
   *
   * addAll[(A->B),(E->F)] results in [(A->B), (C->D), (E->F),] : true
   *
   * addAll[(A->C),(E->F)] results in [(A->B), (C->D)] : false
   *
   * @return `true` if all elements were added to the map or were already contained, `false`
   * otherwise.
   *
   * @see add
   */
  fun addAll(vararg items: Pair<T, R>): Boolean {
    val nonDuplicates = items.filter { !contains(it) }.toList()
    val keys = nonDuplicates.map { it.first }.distinct()
    val values = nonDuplicates.map { it.second }.distinct()

    if (keys.size != nonDuplicates.size ||
        values.size != nonDuplicates.size ||
        keys.any { containsForward(it) } ||
        values.any { containsBackward(it) })
        return false

    nonDuplicates.forEach { map.add(it) }

    return true
  }

  /**
   * Forward lookup for entry.
   *
   * @param entity Relation key.
   *
   * @return B for query A if relation A -> B exists.
   *
   * @throws NoSuchElementException If no such element is found.
   */
  fun forward(entity: T): R = map.first { t -> t.first == entity }.second

  /**
   * Forward lookup for entry.
   *
   * @param entity Relation key.
   *
   * @return B for query A if relation A -> B exists. `null` otherwise.
   */
  fun forwardOrNull(entity: T): R? = map.firstOrNull { t -> t.first == entity }?.second

  /**
   * Backward lookup for entry.
   *
   * @param value Relation value.
   *
   * @return A for query B if relation A -> B exists.
   *
   * @throws NoSuchElementException Ff no such element is found.
   */
  fun backward(value: R): T = map.first { t -> t.second == value }.first

  /**
   * Backward lookup for entry.
   *
   * @param value Relation value.
   *
   * @return A for query B if relation A -> B exists. `null` otherwise.
   */
  fun backwardOrNull(value: R): T? = map.firstOrNull { t -> t.second == value }?.first

  /**
   * Removes relation A -> B if it exists.
   *
   * @param entity Relation key A.
   * @param value Relation value B.
   *
   * @return `true` if the element was removed, `false` if the element was not found.
   *
   * @see removeForward
   * @see removeBackward
   */
  fun remove(entity: T, value: R): Boolean =
      map.removeIf { t -> t.first == entity && t.second == value }

  /**
   * Removes relation A -> B if it exists.
   *
   * @param element Pair (Relation key A, Relation value B)
   *
   * @return `true` if the element was removed, `false` if the element was not found.
   *
   * @see removeForward
   * @see removeBackward
   */
  fun remove(element: Pair<T, R>): Boolean = remove(element.first, element.second)

  /**
   * Removes by forward lookup. Removes relation A -> * if it exists.
   *
   * @param entity Relation key A.
   *
   * @return `true` if the element was removed, `false` if the element was not found.
   *
   * @see remove
   * @see removeBackward
   */
  fun removeForward(entity: T): Boolean = map.removeIf { t -> t.first == entity }

  /**
   * Removes by backward lookup. Removes relation * -> B.
   *
   * @param value Relation value B.
   *
   * @return `true` if the element was removed, `false` if the element was not found.
   *
   * @see remove
   * @see removeForward
   */
  fun removeBackward(value: R): Boolean = map.removeIf { t -> t.second == value }

  /**
   * Returns whether relation A -> B exists in this map.
   *
   * @param entity Relation key A.
   * @param value Relation value B.
   *
   * @return `true` if the relation exists in this map, `false` otherwise.
   *
   * @see containsForward
   * @see containsBackward
   */
  fun contains(entity: T, value: R): Boolean = containsForward(entity) && containsBackward(value)

  /**
   * Returns whether relation A -> B exists in this map.
   *
   * @param pair Relation pair A -> B.
   *
   * @return `true` if the relation exists in this map, `false` otherwise.
   *
   * @see containsForward
   * @see containsBackward
   */
  fun contains(pair: Pair<T, R>): Boolean = contains(pair.first, pair.second)

  /**
   * Returns whether a relation A -> * exists.
   *
   * @param entity Relation key A.
   *
   * @return `true` if the relation exists in this map, `false` otherwise.
   *
   * @see contains
   * @see containsBackward
   */
  fun containsForward(entity: T): Boolean = map.any { t -> t.first == entity }

  /**
   * Returns whether a relation * -> B exists.
   *
   * @param value Relation value B.
   *
   * @return `true` if the relation exists in this map, `false` otherwise.
   *
   * @see contains
   * @see containsForward
   */
  fun containsBackward(value: R): Boolean = map.any { t -> t.second == value }

  /**
   * Returns the domain of this map as a set.
   *
   * @see getCoDomain
   */
  fun getDomain(): Set<T> = map.map { t -> t.first }.toSet()

  /**
   * Returns the coDomain of this map as a set.
   *
   * @see getDomain
   */
  fun getCoDomain(): Set<R> = map.map { t -> t.second }.toSet()

  /** Clears the map. */
  fun clear() {
    map.clear()
  }

  /**
   * Returns whether this map contains no elements.
   *
   * @return `true` if this map contains no elements, false otherwise.
   */
  fun isEmpty(): Boolean = map.isEmpty()

  /**
   * Returns whether this map contains elements.
   *
   * @return `true` if this map contains elements, false otherwise.
   */
  fun isNotEmpty(): Boolean = map.isNotEmpty()
}
