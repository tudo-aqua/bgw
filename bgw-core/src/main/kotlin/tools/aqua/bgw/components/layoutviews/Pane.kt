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

@file:Suppress("unused", "TooManyFunctions")

package tools.aqua.bgw.components.layoutviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.observable.Observer
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * A [Pane] can be used to group [ComponentView]s for easier position management and layout.
 *
 * @constructor Creates a [Pane].
 *
 * @param posX Horizontal coordinate for this [Pane]. Default: 0.
 * @param posY Vertical coordinate for this [Pane]. Default: 0.
 * @param width Width for this [Pane].
 * @param height Height for this [Pane].
 * @param visual Visual for this [Pane]. Default: [Visual.EMPTY].
 */
open class Pane<T : ComponentView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number,
    height: Number,
    visual: Visual = Visual.EMPTY
) : LayoutView<T>(posX = posX, posY = posY, width = width, height = height, visual = visual), Iterable<T> {
    
    internal val observableComponents: ObservableArrayList<T> = ObservableArrayList()

    /**
     * [onAdd] gets invoked anytime after a [ComponentView] is added to this
     * [Pane] with the added [ComponentView] as its receiver.
     */
    var onAdd : (T.() -> Unit)? = null

    /**
     * [onRemove] gets invoked anytime after a [ComponentView] is removed from this
     * [Pane] with the removed [ComponentView] as its receiver.
     */
    var onRemove : (T.() -> Unit)? = null

    /**
     * [ComponentView]s that are contained in this [Pane].
     */
    var components: List<T> = observableComponents.toList()
        get() = observableComponents.toList()
        private set
    
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
     * Adds a [ComponentView] to this [Pane].
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
            parent = this@Pane
            onAdd?.invoke(this)
        }
    }
    
    /**
     * Adds all [ComponentView]s passed as varargs to this [Pane].
     *
     * Whenever a [ComponentView] is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further [ComponentView] is added.
     *
     * @param components Vararg [ComponentView]s to add.
     * @throws IllegalArgumentException If an [ComponentView] is already contained.
     */
    fun addAll(vararg components: T) {
        try {
            addAll(components.toList())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        }
    }
    
    /**
     * Adds all [ComponentView]s contained in [collection] to this [Pane].
     *
     * Whenever an [ComponentView] is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further [ComponentView] is added.
     *
     * @param collection [Collection] containing the [ComponentView]s to add.
     * @throws IllegalArgumentException If an [ComponentView] is already contained.
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
     * Removes the [ComponentView] specified by the parameter from this [Pane].
     *
     * @param component The [ComponentView] to remove.
     *
     * @return `true` if the [Pane] was altered by the call, `false` otherwise.
     */
    @Synchronized
    fun remove(component: T): Boolean {
        if (observableComponents.remove(component)) {
            component.parent = null
            onRemove?.invoke(component)
            return true
        }
        return false
    }

    /**
     * Removes all [ComponentView]s from this [Pane].
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
     * Removes all [ComponentView]s contained in [collection] from this [Pane].
     *
     * @param collection The [ComponentView]s to remove.
     *
     * @return `true` if the [Pane] was altered by the call, `false` otherwise.
     */
    @Synchronized
    fun removeAll(collection: Collection<T>) : Boolean =
        collection.map { remove(it) }.fold(false) { x,y -> x || y }

    /**
     * Removes all [ComponentView]s matching the [predicate] from this [Pane].
     *
     * @param predicate The predicate to evaluate.
     *
     * @return `true` if the [Pane] was altered by the call, `false` otherwise.
     */
    @Synchronized
    fun removeAll(predicate: (T) -> Boolean): Boolean =
        components.map { if (predicate(it)) remove(it) else false }.fold(false) { x, y -> x || y }


    /**
     * Returns the size of the components list.
     *
     * @return Number of children.
     *
     * @see components
     */
    fun numberOfComponents(): Int = observableComponents.size
    
    /**
     * Returns whether the components list is empty.
     *
     * @return `true` if this list contains no components, `false` otherwise.
     *
     * @see isNotEmpty
     * @see components
     */
    fun isEmpty(): Boolean = observableComponents.isEmpty()

    /**
     * Returns whether the components list is not empty.
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
    override fun getChildPosition(child: ComponentView): Coordinate =
        Coordinate(child.posX, child.posY)
    
    /**
     * Returning a contained child's coordinates within this container with scale.
     *
     * @param child Child to find.
     *
     * @return Coordinate of given child in this container relative to containers anchor point.
     */
    override fun getActualChildPosition(child: ComponentView): Coordinate =
        Coordinate(child.actualPosX, child.actualPosY)
    
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
     * Returns an iterator over the elements of this object.
     */
    override fun iterator(): Iterator<T> = observableComponents.iterator()
}