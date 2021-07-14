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

package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.visual.Visual

/**
 * A [GameElementPoolView] may be used to visualize a pool containing [GameElementView]s.
 * A typical use case for a [GameElementPoolView] may be to visualize a pile of hidden items,
 * where the user should not know what item might be drawn next.
 *
 * Visualization:
 * The current [Visual] is used to visualize the area from where the user can start a drag and drop gesture.
 *
 * How to Use:
 * Upon adding a [GameElementView] to a [GameElementPoolView]
 * a snapshot of the initial state of the [GameElementView] gets created and stored.
 * Then the [GameElementView] is made draggable, invisible and its size gets fit to the [GameElementPoolView] size.
 *
 * The initial state consist of the following properties:
 * 	-isDraggable
 * 	-isVisible
 * 	-width
 * 	-height
 *
 * Any changes made to those properties while a [GameElementView] is contained in the [GameElementPoolView] get ignored,
 * but they override the initial state.
 *
 * As soon as an element gets removed (e.g. by initiating a drag and drop gesture) the initial state gets restored.
 * The [GameElementView] at the highest index in the elements list
 * registers the next drag and drop gesture above this [GameElementPoolView].
 *
 * @param height height for this [GameElementPoolView]. Default: 0.
 * @param height width for this [GameElementPoolView]. Default: 0.
 * @param posX horizontal coordinate for this [GameElementPoolView]. Default: 0.
 * @param posY vertical coordinate for this [GameElementPoolView]. Default: 0.
 * @param visual visual for this [GameElementPoolView]. Default: [Visual.EMPTY].
 */
open class GameElementPoolView<T : GameElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual = Visual.EMPTY,
) :
	GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	
	private val initialStates: HashMap<ElementView, InitialState> = HashMap()
	
	override fun add(element: T, index: Int) {
		super.add(element, index)
		initialStates[element] = InitialState(
			isDraggable = element.isDraggable,
			isVisible = element.isVisible,
			width = element.width,
			height = element.height
		)
		element.initializePoolElement()
		element.addInternalListeners()
		element.addPosListeners()
	}
	
	override fun addAll(collection: Collection<T>) {
		collection.forEach { add(it) }
	}
	
	override fun addAll(vararg elements: T) {
		addAll(elements.toList())
	}
	
	override fun remove(element: T) {
		super.remove(element)
		element.removeInternalListeners()
		element.restoreInitialBehaviour()
		element.removePosListeners()
		initialStates.remove(element)
	}
	
	private fun GameElementView.initializePoolElement() {
		isVisibleProperty.setSilent(false)
		widthProperty.setSilent(this@GameElementPoolView.width)
		heightProperty.setSilent(this@GameElementPoolView.height)
		isDraggableProperty.setSilent(true)
	}
	
	private fun GameElementView.restoreInitialBehaviour() {
		val initialState = initialStates[this]!!
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isDraggableProperty.setSilent(initialState.isDraggable)
		isVisibleProperty.setSilent(initialState.isVisible)
	}
	
	private fun GameElementView.addInternalListeners() {
		isDraggableProperty.internalListener = { _, nV ->
			initialStates[this]!!.isDraggable = nV
			isDraggableProperty.setSilent(true)
		}
		
		isVisibleProperty.internalListener = { _, nV ->
			initialStates[this]!!.isVisible = nV
			isVisibleProperty.setSilent(false)
		}
		
		widthProperty.internalListener = { _, nV ->
			initialStates[this]!!.width = nV
			widthProperty.setSilent(this@GameElementPoolView.width)
		}
		
		heightProperty.internalListener = { _, nV ->
			initialStates[this]!!.height = nV
			heightProperty.setSilent(this@GameElementPoolView.height)
		}
	}
	
	private fun GameElementView.removeInternalListeners() {
		isDraggableProperty.internalListener = null
		isVisibleProperty.internalListener = null
		widthProperty.internalListener = null
		heightProperty.internalListener = null
	}

	private fun GameElementView.addPosListeners() {
		this.posXProperty.addListenerAndInvoke(0.0) { _, _ ->
			posXProperty.setSilent(0.0)
		}
		this.posYProperty.addListenerAndInvoke(0.0) { _, _ ->
			posYProperty.setSilent(0.0)
		}
	}

	private fun GameElementView.removePosListeners() {
		this.posXProperty.internalListener = null
		this.posYProperty.internalListener = null
	}
	
	private class InitialState(var isDraggable: Boolean, var isVisible: Boolean, var width: Double, var height: Double)
}