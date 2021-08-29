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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions")

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.observable.Observer
import tools.aqua.bgw.observable.ObservableLinkedList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for containers that can contain [GameComponentView]s or its subclasses.
 *
 * It provides the list to store [GameComponentView]s and some useful methods to work on said list.
 *
 * @param posX horizontal coordinate for this [GameComponentContainer].
 * @param posY vertical coordinate for this [GameComponentContainer].
 * @param width width for this [GameComponentContainer].
 * @param height height for this [GameComponentContainer].
 * @param visual visual for this [GameComponentContainer].
 */
sealed class GameComponentContainer<T : GameComponentView>(
	posX: Number,
	posY: Number,
	width: Number,
	height: Number,
	visual: Visual
) : DynamicComponentView(posX = posX, posY = posY, width = width, height = height, visual = visual), Iterable<T> {
	/**
	 * An [ObservableList] to store the [GameComponentView]s that are contained in this [GameComponentContainer].
	 *
	 * If changes are made to this list, this [GameComponentContainer] gets re-rendered.
	 */
	internal val observableComponents: ObservableList<T> = ObservableLinkedList()

	/**
	 * [onAdd] gets invoked anytime after a [GameComponentView] is added to this
	 * [GameComponentContainer] with the added [GameComponentView] as its receiver.
	 */
	var onAdd : (T.() -> Unit)? = null

	/**
	 * [onRemove] gets invoked anytime after a [GameComponentView] is removed from this
	 * [GameComponentContainer] with the removed [GameComponentView] as its receiver.
	 */
	var onRemove : (T.() -> Unit)? = null

	/**
	 * [GameComponentView]s that are contained in this [GameComponentContainer].
	 */
	var components: List<T> = observableComponents.toList()
		get() = observableComponents.toList()
		private set
	
	/**
	 * Internal onRemove handler.
	 */
	internal abstract fun T.onRemove()
	
	/**
	 * Internal onAdd handler.
	 */
	internal abstract fun T.onAdd()
	
	/**
	 * Adds a [listener] on the [observableComponents] list.
	 */
	fun addComponentsListener(listener: Observer) {
		observableComponents.addListener(listener)
	}
	
	/**
	 * Removes a [listener] from the [observableComponents] list.
	 */
	fun removeComponentsListener(listener: Observer) {
		observableComponents.removeListener(listener)
	}
	
	/**
	 * Removes all listeners from the [observableComponents] list.
	 */
	fun clearComponentsListener() {
		observableComponents.clearListeners()
	}
	
	/**
	 * Adds a [GameComponentView] to this [GameComponentContainer].
	 *
	 * @param component Component to add.
	 *
	 * @throws IllegalArgumentException If [component] is already contained.
	 * @throws IllegalArgumentException If [index] is out of bounds for [components].
	 */
	@Suppress("DuplicatedCode")
	@Synchronized
	fun add(component: T, index: Int = observableComponents.size) {
		require(!observableComponents.contains(component)) {
			"Component $component is already contained in this $this."
		}
		require(component.parent == null) {
			"Component $component is already contained in another container."
		}
		require(index in 0..observableComponents.size) {
			"Index $index is out of list range."
		}
		
		observableComponents.add(index, component)
		component.apply {
			parent = this@GameComponentContainer
			this.onAdd()
			onAdd?.invoke(this)
		}
	}
	
	/**
	 * Adds all [GameComponentView]s passed as varargs to this [GameComponentContainer].
	 *
	 * Whenever a [GameComponentView] is encountered, that is already contained, an
	 * [IllegalArgumentException] is thrown and no further [GameComponentView] is added.
	 *
	 * @param components Vararg [GameComponentView]s to add.
	 * @throws IllegalArgumentException If a [GameComponentView] is already contained.
	 */
	fun addAll(vararg components: T) {
		try {
			addAll(components.toList())
		} catch (e: IllegalArgumentException) {
			throw IllegalArgumentException(e.message)
		}
	}
	
	/**
	 * Adds all [GameComponentView]s contained in [collection] to this [GameComponentContainer].
	 *
	 * Whenever an [GameComponentView]] is encountered, that is already contained, an
	 * [IllegalArgumentException] is thrown and no further [GameComponentView] is added.
	 *
	 * @param collection Collection containing the [GameComponentView]s to add.
	 *
	 * @throws IllegalArgumentException If a [GameComponentView] is already contained.
	 */
	@Synchronized
	fun addAll(collection: Collection<T>) {
		try {
			collection.forEach { add(it) }
		} catch (e: IllegalArgumentException) {
			throw IllegalArgumentException(e.message)
		}
	}
	
	/**
	 * Removes the [GameComponentView] specified by the parameter from this [GameComponentContainer].
	 *
	 * @param component The [GameComponentView] to remove.
	 *
	 * @return `true` if the [GameComponentContainer] was altered by the call, `false` otherwise.
	 */
	@Synchronized
	fun remove(component: T): Boolean {
		if (observableComponents.remove(component)) {
			component.parent = null
			component.onRemove()
			onRemove?.invoke(component)
			return true
		}
		return false
	}
	
	/**
	 * Removes all [GameComponentView]s from this [GameComponentContainer].
	 *
	 * @return List of all removed components.
	 */
	@Synchronized
	fun clear(): List<T> {
		val tmp = observableComponents.toList()
		tmp.map { remove(it) }
		return tmp
	}
	
	/**
	 * Removes all [GameComponentView]s contained in [collection] from this [GameComponentContainer].
	 *
	 * @param collection The [GameComponentView]s to remove.
	 *
	 * @return `true` if the [GameComponentContainer] was altered by the call, `false` otherwise.
	 */
	@Synchronized
	fun removeAll(collection: Collection<T>) : Boolean =
		collection.map { remove(it) }.fold(false) { x,y -> x || y }
	
	/**
	 * Removes all [GameComponentView]s matching the [predicate] from this [GameComponentContainer].
	 *
	 * @param predicate The predicate to evaluate.
	 *
	 * @return `true` if the [GameComponentContainer] was altered by the call, `false` otherwise.
	 */
	@Synchronized
	fun removeAll(predicate: (T) -> Boolean): Boolean =
		components.map { if (predicate(it)) remove(it) else false }.fold(false) { x, y -> x || y }
	
	/**
	 * Returns the size of the [components] list.
	 * @see components
	 */
	fun numberOfComponents(): Int = observableComponents.size
	
	/**
	 * Returns whether the [components] list is empty.
	 *
	 * @return `true` if this list contains no components, `false` otherwise.
	 *
	 * @see isNotEmpty
	 * @see components
	 */
	fun isEmpty(): Boolean = observableComponents.isEmpty()
	
	/**
	 * Returns whether the [components] list is not empty.
	 *
	 * @return `true` if this list contains components, `false` otherwise.
	 *
	 * @see isEmpty
	 * @see components
	 */
	fun isNotEmpty(): Boolean = !isEmpty()
	
	/**
	 * Returning a contained child's coordinates within this container.
	 *
	 * @param child Child to find.
	 *
	 * @return Coordinate of given child in this container relative to containers anchor point.
	 */
	override fun getChildPosition(child: ComponentView): Coordinate? = Coordinate(child.posX, child.posY)
	
	/**
	 * Removes [component] from container's children.
	 *
	 * @param component Child to be removed.
	 *
	 * @throws IllegalArgumentException If the child's type is incompatible with container's type.
	 */
	override fun removeChild(component: ComponentView) {
		try {
			@Suppress("UNCHECKED_CAST")
			this.remove(component as T)
		} catch (_: ClassCastException) {
			throw IllegalArgumentException("$component type is incompatible with container's type.")
		}
	}
	
	/**
	 * Returns an iterator over the elements of this [GameComponentContainer].
	 *
	 * @return Iterator over the elements of this [GameComponentContainer].
	 */
	override fun iterator(): Iterator<T> = observableComponents.iterator()
}