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

package tools.aqua.bgw.core

import javafx.scene.layout.StackPane
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.builder.DragElementObject
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.RootElement
import tools.aqua.bgw.observable.*
import tools.aqua.bgw.util.CoordinatePlain
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW scenes.
 *
 * @param width [Scene] width in virtual coordinates.
 * @param height [Scene] height in virtual coordinates.
 * @param background [Scene] [background] [Visual].
 *
 * @see BoardGameScene
 * @see MenuScene
 */
sealed class Scene<T : ElementView>(width: Number, height: Number, background: Visual) {
	/**
	 * [Property] for the currently dragged [ElementView] encapsulated in a [DragElementObject] or null if no element is
	 * currently dragged.
	 */
	internal val draggedElementObjectProperty: ObjectProperty<DragElementObject?> = ObjectProperty(null)
	
	/**
	 * Currently dragged [ElementView] encapsulated in a [DragElementObject] or null if no element is currently dragged.
	 */
	val draggedElement: DynamicView?
		get() = draggedElementObjectProperty.value?.draggedElement
	
	
	/**
	 * The root node of this [Scene].
	 * Use it to compare the parent [Property] of any [ElementView]
	 * to find out whether it was directly added to the [Scene].
	 */
	val rootNode: ElementView = RootElement(this)
	
	/**
	 * The width of this [Scene] in virtual coordinates.
	 */
	val width: Double = width.toDouble()
	
	/**
	 * The height of this [Scene] in virtual coordinates.
	 */
	val height: Double = height.toDouble()
	
	/**
	 * All [ElementView]s on the root node.
	 */
	internal val rootElements: ObservableList<T> = ObservableArrayList()
	
	/**
	 * [Property] for the [background] [Visual] of this [Scene].
	 */
	internal val backgroundProperty: ObjectProperty<Visual> = ObjectProperty(background)
	
	/**
	 * The background [Visual] of this [Scene].
	 */
	var background: Visual
		get() = backgroundProperty.value
		set(value) {
			backgroundProperty.value = value
		}
	
	/**
	 * [Property] for the [opacity] of the [background] of this [Scene].
	 */
	internal val opacityProperty = DoubleProperty(1.0)
	
	/**
	 * Opacity of the [background] of this [Scene].
	 */
	var opacity: Double
		get() = opacityProperty.value
		set(value) {
			require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }
			
			opacityProperty.value = value
		}
	
	/**
	 * [Property] for the currently displayed zoom detail of this [Scene].
	 */
	internal val zoomDetailProperty = ObjectProperty(CoordinatePlain(0, 0, width, height))
	
	/**
	 * The currently displayed zoom detail of this [Scene].
	 */
	internal var zoomDetail
		get() = zoomDetailProperty.value
		set(value) {
			zoomDetailProperty.value = value
		}
	
	/**
	 * [Map] for all [ElementView]s to their [StackPane]s.
	 */
	internal val elementsMap: MutableMap<ElementView, StackPane> = HashMap()
	
	/**
	 * All [Animation]s currently playing.
	 */
	internal val animations: ObservableList<Animation> = ObservableArrayList()
	
	/**
	 * Adds all given [ElementView]s to the root node and [rootElements] list.
	 *
	 * @param elements elements to add.
	 */
	fun addElements(vararg elements: T) {
		rootElements.addAll(elements.toList().onEach {
			check(it.parent == null) { "Element $it is already contained in another container." }
			it.parent = rootNode
		})
	}
	
	/**
	 * Removes all given [ElementView]s from the root node and [rootElements] list.
	 *
	 * @param elements elements to remove.
	 */
	fun removeElements(vararg elements: T) {
		rootElements.removeAll(elements.toList().onEach {
			it.parent = null
		})
	}
	
	/**
	 * Removes all [ElementView]s from the root node and [rootElements] list.
	 */
	fun clearElements() {
		rootElements.forEach { it.parent = null }
		rootElements.clear()
	}
	
	/**
	 * Plays given [Animation].
	 *
	 * @param animation animation to play.
	 */
	fun playAnimation(animation: Animation) {
		animations.add(animation)
	}

//	/**
//	 * Zooms [Scene] to given bounds.
//	 *
//	 * @param fromX left bound.
//	 * @param fromY top bound.
//	 * @param toX right bound.
//	 * @param toY bottom bound.
//	 */
//	fun zoomTo(fromX: Number, fromY: Number, toX: Number, toY: Number): Unit =
//		zoomTo(fromX.toDouble(), fromY.toDouble(), toX.toDouble(), toY.toDouble())

//	/**
//	 * Zooms [Scene] to given bounds.
//	 *
//	 * @param from top left [Coordinate].
//	 * @param to bottom right [Coordinate].
//	 */
//	fun zoomTo(from: Coordinate, to: Coordinate): Unit =
//		zoomTo(from.xCoord, from.yCoord, to.xCoord, to.yCoord)

//	/**
//	 * Zooms [Scene] to given bounds.
//	 *
//	 * @param to layout bounds.
//	 */
//	fun zoomTo(to: CoordinatePlain): Unit =
//		zoomTo(to.topLeft, to.bottomRight)

//	/**
//	 * Zooms scene out to max bounds.
//	 */
//	fun zoomOut() {
//		zoomDetail = CoordinatePlain(0, 0, width, height)
//	}

//	/**
//	 * Sets [zoomDetailProperty] to given bounds.
//	 * Checks for targets out of layout bounds.
//	 */
//	private fun zoomTo(fromX: Double, fromY: Double, toX: Double, toY: Double) {
//		when {
//			fromX < 0 || fromY < 0 || toX < 0 || toY < 0 ->
//				throw IllegalArgumentException("All bounds must be positive.")
//			fromX >= toX ->
//				throw IllegalArgumentException("Right X bound is not greater than left X bound.")
//			fromY >= toY ->
//				throw IllegalArgumentException("Bottom Y bound is not greater than top Y bound.")
//			toX > width ->
//				throw IllegalArgumentException("Right X bound is greater than scene width.")
//			toY > height ->
//				throw IllegalArgumentException("Bottom Y bound is greater than scene height.")
//		}
//
//		zoomDetail = CoordinatePlain(fromX, fromY, toX, toY)
//	}
	
	/**
	 * Searches [node] recursively through the visual tree and logs path where the [node] appears as first element and
	 * the [rootNode] as last.
	 *
	 * @param node child to find.
	 *
	 * @return path to child.
	 *
	 * @throws IllegalStateException if child was not contained in this [Scene].
	 */
	fun findPathToChild(node: ElementView): List<ElementView> {
		if (node is RootElement<*>) {
			check(node == rootNode) { "Child is contained in another scene" }
			return listOf(rootNode)
		}
		
		checkNotNull(node.parent) { "Encountered element $node that is not contained in a scene." }
		
		return mutableListOf(node) + findPathToChild(node.parent!!)
	}
	
	/**
	 * Removes [child] from the root.
	 */
	internal fun removeChild(child: ElementView) {
		try {
			@Suppress("UNCHECKED_CAST")
			this.removeElements(child as T)
		} catch (_: ClassCastException) {
		}
	}
	
	companion object {
		/**
		 * Default scene width.
		 */
		const val DEFAULT_SCENE_WIDTH: Double = 1920.0
		
		/**
		 * Default scene height.
		 */
		const val DEFAULT_SCENE_HEIGHT: Double = 1080.0
	}
}

