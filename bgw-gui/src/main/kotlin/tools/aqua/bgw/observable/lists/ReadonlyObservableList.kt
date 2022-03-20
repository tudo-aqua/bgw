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

package tools.aqua.bgw.observable.lists

import tools.aqua.bgw.observable.ValueObservable
import java.util.*
import java.util.function.Consumer

/**
 * An observable [List] implementation.
 *
 * @constructor Creates an [ReadonlyObservableList].
 */
abstract class ReadonlyObservableList<T> : ValueObservable<List<T>>(), Iterable<T> {

  /** Returns the number of elements in this list. */
  val size: Int
    get() = list.size

  /** All available indices of this list as IntRange i.e. 0..size()-1. */
  val indices: IntRange
    get() = 0 until size

  /** List field. */
  protected abstract val list: MutableList<T>

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
   *
   * More formally, returns `true` if and only if this list contains at least one element `e` such
   * that `Objects.equals(o, e)`.
   *
   * @param o Element whose presence in this list is to be tested.
   *
   * @return `true` if this list contains the specified element, `false` otherwise.
   */
  operator fun contains(o: Any?): Boolean = list.contains(o)

  /**
   * Returns the index of the first occurrence of the specified element in this list, or -1 if this
   * list does not contain the element.
   *
   * More formally, returns the lowest index `i` such that `Objects.equals(o, get(i))`, or -1 if
   * there is no such index.
   *
   * @return First index of the element, or -1 if the list does not contain the element.
   */
  fun indexOf(o: Any?): Int = list.indexOf(o)

  /**
   * Returns the index of the last occurrence of the specified element in this list, or -1 if this
   * list does not contain the element.
   *
   * More formally, returns the highest index `i` such that `Objects.equals(o, get(i))`, or -1 if
   * there is no such index.
   *
   * @return Last index of the element, or -1 if the list does not contain the element.
   */
  fun lastIndexOf(o: Any?): Int = list.lastIndexOf(o)

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index Index of the element to return.
   *
   * @return The element at the specified position in this list.
   *
   * @throws IndexOutOfBoundsException If the index exceeds the list's bounds.
   */
  operator fun get(index: Int): T = list[index]

  /** ForEach action. */
  fun forEach(action: Consumer<in T>): Unit = list.forEach(action)

  /**
   * Creates a *fail-fast* [Spliterator] over the elements in this list.
   *
   * @return A [Spliterator] over the elements in this list
   */
  fun spliterator(): Spliterator<T> = list.spliterator()

  /**
   * Returns an [Iterator] over the elements in this [ReadonlyObservableList].
   *
   * @return [Iterator] over the elements in this list.
   */
  override fun iterator(): Iterator<T> = list.iterator()
}
