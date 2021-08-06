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

@file:Suppress("unused")

package tools.aqua.bgw.components.layoutviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.observable.IObservable
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

//TODO: Docs
open class Pane<T : ComponentView>(
    width: Number = 0,
    height: Number = 0,
    posX: Number = 0,
    posY: Number = 0,
    visual: Visual = Visual.EMPTY
) :
    LayoutView<T>(width = width, height = height, posX = posX, posY = posY, visual = visual), Iterable<T> {
    
    internal val observableComponents: ObservableArrayList<T> = ObservableArrayList()
    
    /**
     * [ComponentView]s that are contained in this [Pane].
     */
    var components: List<T> = observableComponents.toList()
        get() = observableComponents.toList()
        private set
    
    /**
     * Adds a [listener] on the [observableComponents] list.
     */
    fun addComponentsListener(listener: IObservable) {
        observableComponents.addListener(listener)
    }
    
    /**
     * Removes a [listener] from the [observableComponents] list.
     */
    fun removeComponentsListener(listener: IObservable) {
        observableComponents.removeListener(listener)
    }
    
    /**
     * Removes all listeners from the [observableComponents] list.
     */
    fun clearComponentsListener() {
        observableComponents.clearListeners()
    }
    
    /**
     * Adds an [ComponentView] to this [Pane].
     *
     * @param component component to add.
     * @throws IllegalArgumentException if [component] is already contained.
     * @throws IllegalArgumentException if [index] is out of bounds for [components].
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
        
        observableComponents.add(index, component.apply { parent = this@Pane })
    }
    
    /**
     * Adds all [ComponentView]s passed as varargs to this [Pane].
     * Whenever a [ComponentView] is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further [ComponentView] is added.
     *
     * @param components vararg [ComponentView]s to add.
     * @throws IllegalArgumentException if an [ComponentView] is already contained.
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
     * Whenever an [ComponentView] is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further [ComponentView] is added.
     *
     * @param collection collection containing the [ComponentView]s to add.
     * @throws IllegalArgumentException if an [ComponentView] is already contained.
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
     * @param component the [ComponentView] to remove.
     */
    @Synchronized
    fun remove(component: T) {
        observableComponents.remove(component.apply { parent = null })
    }
    
    /**
     * Removes all [ComponentView]s from this [Pane].
     * @return list of all removed components.
     */
    @Synchronized
    fun removeAll(): List<T> {
        val tmp = observableComponents.toList()
        observableComponents.forEach { it.parent = null }
        observableComponents.clear()
        return tmp
    }
    
    /**
     * Returns the size of the components list.
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
     * @param child child to find.
     *
     * @return coordinate of given child in this container relative to containers anchor point.
     */
    override fun getChildPosition(child: ComponentView): Coordinate = Coordinate(child.posX, child.posY)
    
    /**
     * Removes [component] from container's children.
     *
     * @param component child to be removed.
     *
     * @throws RuntimeException if the child's type is incompatible with container's type.
     */
    override fun removeChild(component: ComponentView) {
        try {
            @Suppress("UNCHECKED_CAST")
            this.remove(component as T)
        } catch (_: ClassCastException) {
            throw RuntimeException("$component type is incompatible with container's type.")
        }
    }
    
    override fun iterator(): Iterator<T> = observableComponents.iterator()
}