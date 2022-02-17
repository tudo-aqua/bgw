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
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.core.DEFAULT_SATCHEL_HEIGHT
import tools.aqua.bgw.core.DEFAULT_SATCHEL_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * A [Satchel] may be used to visualize a pool containing [GameComponentView]s.
 *
 * A typical use case for a [Satchel] may be to visualize a pile of hidden items,
 * where the user should not know what item might be drawn next.
 *
 * Visualization:
 *
 * The current [Visual] is used to visualize the area from where the user can start a drag and drop gesture.
 *
 *
 * How to Use:
 *
 * Upon adding a [GameComponentView] to a [Satchel] a snapshot of the initial state of the [GameComponentView] gets
 * created and stored. Then the [GameComponentView] is made draggable, invisible and its size gets fit to the [Satchel]
 * size.
 *
 * The initial state consist of the following properties:
 *
 * 	-[isDraggable]
 *
 * 	-[isVisible]
 *
 * 	-[width]
 *
 * 	-[height]
 *
 *
 * Any changes made to those properties while a [GameComponentView] is contained in the [Satchel] get ignored,
 * but they override the initial state.
 *
 * As soon as a component gets removed (e.g. by initiating a drag and drop gesture) the initial state gets restored.
 * The [GameComponentView] at the highest index in the components list registers the next drag and drop gesture above
 * this [Satchel].
 *
 * @constructor Creates a [Satchel].
 *
 * @param posX horizontal coordinate for this [Satchel]. Default: 0.
 * @param posY vertical coordinate for this [Satchel]. Default: 0.
 * @param width width for this [Satchel]. Default: [DEFAULT_SATCHEL_WIDTH].
 * @param height height for this [Satchel]. Default: [DEFAULT_SATCHEL_HEIGHT].
 * @param visual visual for this [Satchel]. Default: [Visual.EMPTY].
 */
open class Satchel<T : GameComponentView>(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_SATCHEL_WIDTH,
	height: Number = DEFAULT_SATCHEL_HEIGHT,
	visual: Visual = Visual.EMPTY,
) :
	GameComponentContainer<T>(posX = posX, posY = posY, width = width, height = height, visual = visual) {
	
	private val initialStates: HashMap<ComponentView, InitialState> = HashMap()

	override fun T.onAdd() {
		val initialState = InitialState(
			isDraggable = this.isDraggable,
			opacity = this.opacity,
			width = this.width,
			height = this.height
		)
		
		//initialize satchel component
		opacityProperty.setSilent(0.0)
		widthProperty.setSilent(this@Satchel.width)
		heightProperty.setSilent(this@Satchel.height)
		isDraggableProperty.setSilent(true)
		
		//add internal listeners
		isDraggableProperty.internalListener = { _, nV ->
			initialState.isDraggable = nV
			isDraggableProperty.setSilent(true)
		}
		
		opacityProperty.internalListener = { _, nV ->
			initialState.opacity = nV
			opacityProperty.setSilent(0.0)
		}
		
		widthProperty.internalListener = { _, nV ->
			initialState.width = nV
			widthProperty.setSilent(this@Satchel.width)
		}
		
		heightProperty.internalListener = { _, nV ->
			initialState.height = nV
			heightProperty.setSilent(this@Satchel.height)
		}
		
		//add pos listeners
		this.posXProperty.addListenerAndInvoke(0.0) { _, _ ->
			posXProperty.setSilent(0.0)
		}
		this.posYProperty.addListenerAndInvoke(0.0) { _, _ ->
			posYProperty.setSilent(0.0)
		}
		
		initialStates[this] = initialState
	}

	override fun T.onRemove() {
		//remove internal listeners
		isDraggableProperty.internalListener = null
		opacityProperty.internalListener = null
		widthProperty.internalListener = null
		heightProperty.internalListener = null
		
		val initialState = initialStates[this] ?: return
		
		//restore initial behaviour
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isDraggableProperty.setSilent(initialState.isDraggable)
		opacityProperty.setSilent(initialState.opacity)
		
		//remove pos listeners
		posXProperty.internalListener = null
		posYProperty.internalListener = null
		
		initialStates.remove(this)
	}
	
	private class InitialState(var isDraggable: Boolean, var opacity: Double, var width: Double, var height: Double)
}