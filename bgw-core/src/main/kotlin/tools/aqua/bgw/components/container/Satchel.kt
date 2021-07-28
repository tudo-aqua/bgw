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

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponents.GameComponent
import tools.aqua.bgw.visual.Visual

/**
 * A [Satchel] may be used to visualize a pool containing [GameComponent]s.
 * A typical use case for a [Satchel] may be to visualize a pile of hidden items,
 * where the user should not know what item might be drawn next.
 *
 * Visualization:
 * The current [Visual] is used to visualize the area from where the user can start a drag and drop gesture.
 *
 * How to Use:
 * Upon adding a [GameComponent] to a [Satchel]
 * a snapshot of the initial state of the [GameComponent] gets created and stored.
 * Then the [GameComponent] is made draggable, invisible and its size gets fit to the [Satchel] size.
 *
 * The initial state consist of the following properties:
 * 	-isDraggable
 * 	-isVisible
 * 	-width
 * 	-height
 *
 * Any changes made to those properties while a [GameComponent] is contained in the [Satchel] get ignored,
 * but they override the initial state.
 *
 * As soon as a component gets removed (e.g. by initiating a drag and drop gesture) the initial state gets restored.
 * The [GameComponent] at the highest index in the components list
 * registers the next drag and drop gesture above this [Satchel].
 *
 * @param height height for this [Satchel]. Default: 0.
 * @param height width for this [Satchel]. Default: 0.
 * @param posX horizontal coordinate for this [Satchel]. Default: 0.
 * @param posY vertical coordinate for this [Satchel]. Default: 0.
 * @param visual visual for this [Satchel]. Default: [Visual.EMPTY].
 */
open class Satchel<T : GameComponent>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual = Visual.EMPTY,
) :
	GameComponentContainer<T>(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	
	private val initialStates: HashMap<ComponentView, InitialState> = HashMap()
	
	override fun add(component: T, index: Int) {
		super.add(component, index)
		initialStates[component] = InitialState(
			isDraggable = component.isDraggable,
			isVisible = component.isVisible,
			width = component.width,
			height = component.height
		)
		component.initializeSatchelComponent()
		component.addInternalListeners()
		component.addPosListeners()
	}
	
	override fun remove(component: T): Boolean = when (super.remove(component)) {
		true -> {
			component.removeInternalListeners()
			component.restoreInitialBehaviour()
			component.removePosListeners()
			initialStates.remove(component)
			true
		}
		false -> false
	}
	
	private fun GameComponent.initializeSatchelComponent() {
		isVisibleProperty.setSilent(false)
		widthProperty.setSilent(this@Satchel.width)
		heightProperty.setSilent(this@Satchel.height)
		isDraggableProperty.setSilent(true)
	}
	
	private fun GameComponent.restoreInitialBehaviour() {
		val initialState = initialStates[this]!!
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isDraggableProperty.setSilent(initialState.isDraggable)
		isVisibleProperty.setSilent(initialState.isVisible)
	}
	
	private fun GameComponent.addInternalListeners() {
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
			widthProperty.setSilent(this@Satchel.width)
		}
		
		heightProperty.internalListener = { _, nV ->
			initialStates[this]!!.height = nV
			heightProperty.setSilent(this@Satchel.height)
		}
	}
	
	private fun GameComponent.removeInternalListeners() {
		isDraggableProperty.internalListener = null
		isVisibleProperty.internalListener = null
		widthProperty.internalListener = null
		heightProperty.internalListener = null
	}
	
	private fun GameComponent.addPosListeners() {
		this.posXProperty.addListenerAndInvoke(0.0) { _, _ ->
			posXProperty.setSilent(0.0)
		}
		this.posYProperty.addListenerAndInvoke(0.0) { _, _ ->
			posYProperty.setSilent(0.0)
		}
	}
	
	private fun GameComponent.removePosListeners() {
		this.posXProperty.internalListener = null
		this.posYProperty.internalListener = null
	}
	
	private class InitialState(var isDraggable: Boolean, var isVisible: Boolean, var width: Double, var height: Double)
}