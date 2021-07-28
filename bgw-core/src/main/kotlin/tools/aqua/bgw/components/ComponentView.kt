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

package tools.aqua.bgw.components

import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.*
import tools.aqua.bgw.exception.IllegalInheritanceException
import tools.aqua.bgw.observable.*
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * [ComponentView] is the abstract baseclass of all framework components.
 * It defines important fields and functions that are necessary to visualize inheriting components.
 *
 * @param height height for this [ComponentView].
 * @param width width for this [ComponentView].
 * @param posX the X coordinate for this [ComponentView] relative to its container.
 * @param posY the Y coordinate for this [ComponentView] relative to its container.
 * @param visual visual for this [ComponentView].
 *
 * @throws IllegalInheritanceException inheriting from this [Class] is not advised,
 * because it cannot be rendered and trying to do so will result in an [IllegalInheritanceException].
 *
 * @see IllegalInheritanceException
 */
abstract class ComponentView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : Observable() {
	
	/**
	 * The parent of this [ComponentView].
	 * `null` if this [ComponentView] is not contained in a [GameComponentContainer], [LayoutView], or a [Scene].
	 * If the component has been added directly to a [Scene], [parent] is equal to the scene's [RootComponent].
	 * If the component is contained within a container, parent is equal to that container.
	 * @see Scene
	 * @see GameComponentContainer
	 */
	var parent: ComponentView? = null
		internal set
	
	/**
	 * Field that indicates whether posX and posY denote the center or top left of this [ComponentView].
	 */
	internal var layoutFromCenter: Boolean = false
	
	/**
	 * Name field only for debugging purposes. Has no effect on rendering.
	 */
	var name: String = javaClass.name + "@" + Integer.toHexString(this.hashCode())
	
	/**
	 * [Property] for the [height] of this [ComponentView].
	 */
	val heightProperty: DoubleProperty = DoubleProperty(height.toDouble())
	
	/**
	 * The [height] for this [ComponentView].
	 * @see heightProperty
	 */
	var height: Double
		get() = heightProperty.value
		set(value) {
			heightProperty.value = value
		}
	
	/**
	 * [Property] for the [width] of this [ComponentView].
	 */
	val widthProperty: DoubleProperty = DoubleProperty(width.toDouble())
	
	/**
	 * The [width] for this [ComponentView].
	 * @see widthProperty
	 */
	var width: Double
		get() = widthProperty.value
		set(value) {
			widthProperty.value = value
		}
	
	/**
	 * [Property] for the horizontal position of this [ComponentView].
	 */
	val posXProperty: DoubleProperty = DoubleProperty(posX.toDouble())
	
	/**
	 * Horizontal position of this [ComponentView].
	 * @see posXProperty
	 */
	var posX: Double
		get() = posXProperty.value
		set(value) {
			posXProperty.value = value
		}
	
	/**
	 * [Property] for the vertical position of this [ComponentView].
	 */
	val posYProperty: DoubleProperty = DoubleProperty(posY.toDouble())
	
	/**
	 * Vertical position of this [ComponentView].
	 * @see posYProperty
	 */
	var posY: Double
		get() = posYProperty.value
		set(value) {
			posYProperty.value = value
		}
	
	/**
	 * [Property] for the horizontal scale of this [ComponentView].
	 */
	val scaleXProperty: DoubleProperty = DoubleProperty(1.0)
	
	/**
	 * Horizontal scale of this [ComponentView].
	 * @see scaleYProperty
	 */
	var scaleX: Double
		get() = scaleXProperty.value
		set(value) {
			scaleXProperty.value = value
		}
	
	/**
	 * [Property] for the vertical scale of this [ComponentView].
	 */
	val scaleYProperty: DoubleProperty = DoubleProperty(1.0)
	
	/**
	 * Vertical scale of this [ComponentView].
	 * @see scaleYProperty
	 */
	var scaleY: Double
		get() = scaleYProperty.value
		set(value) {
			scaleYProperty.value = value
		}
	
	/**
	 * Scale of this [ComponentView].
	 *
	 * @throws IllegalStateException When invoking getter if scaleX and scaleY differ.
	 * Use scaleX and scaleY getters instead. Safe to use if scale is equal in both directions.
	 *
	 * @see scaleX
	 * @see scaleY
	 */
	var scale: Double
		get() {
			require(scaleX == scaleY) {
				"Cannot return single scale because scaleX ($scaleX) and scaleY ($scaleY) are different."
			}
			return scaleX
		}
		set(value) {
			scaleX = value
			scaleY = value
		}
	
	/**
	 * [Property] for the rotation of this [ComponentView] in degrees.
	 * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
	 *
	 * example conversions:
	 * -10  -> 350
	 * -370 -> 350
	 * 370  -> 10
	 * 730  -> 10
	 */
	val rotationProperty: DoubleProperty = DoubleProperty(0.0)
	
	/**
	 * Rotation of this [ComponentView] in degrees.
	 * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
	 *
	 * example conversions:
	 * -10  -> 350
	 * -370 -> 350
	 * 370  -> 10
	 * 730  -> 10
	 * @see rotationProperty
	 */
	var rotation: Double
		get() = rotationProperty.value
		set(value) {
			rotationProperty.value = value
		}
	
	/**
	 * [Property] for the current [Visual] of this [ComponentView].
	 *
	 * @see visual
	 */
	internal val visualProperty: ObjectProperty<Visual> = ObjectProperty(visual.copy())
	
	/**
	 * Index of the current [Visual] in the visuals list.
	 *
	 * @see visualProperty
	 */
	var visual: Visual
		get() = visualProperty.value
		/**
		 * Sets a copy of the given [Visual] [value] to this field and refreshes GUI.
		 */
		set(value) {
			visualProperty.value = value.copy()
		}
	
	/**
	 * [Property] for the [opacity] of this [ComponentView].
	 * Should be in range 0.0 to 1.0.
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Note that invisible objects (opacity == 0.0) still remain interactive.
	 */
	val opacityProperty: DoubleProperty = DoubleProperty(1.0)
	
	/**
	 * Opacity of this [ComponentView].
	 * Must be in range 0.0 to 1.0.
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Note that invisible objects (opacity == 0.0) still remain interactive.
	 * @see opacityProperty
	 */
	var opacity: Double
		get() = opacityProperty.value
		set(value) {
			require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }
			
			opacityProperty.value = value
		}
	
	/**
	 * [Property] for the visibility of this [ComponentView].
	 * Invisible [ComponentView]s are disabled.
	 * An object marked as visible may still be opaque due to opacity
	 * @see isDisabledProperty
	 * @see opacityProperty
	 */
	val isVisibleProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Visibility of this [ComponentView].
	 * Invisible [ComponentView]s are disabled.
	 * An object marked as visible may still be opaque due to opacity
	 * @see isDisabledProperty
	 * @see opacityProperty
	 * @see isVisibleProperty
	 */
	var isVisible: Boolean
		get() = isVisibleProperty.value
		set(value) {
			isVisibleProperty.value = value
		}
	
	/**
	 * [Property] that controls if user input events cause input functions of this [ComponentView] to get invoked.
	 * `true` means no invocation, where `false` means invocation.
	 *
	 * For a list of affected functions refer to the `See Also` section.
	 *
	 * @see onMouseEntered
	 * @see onMouseExited
	 * @see dropAcceptor
	 * @see onDragDropped
	 * @see onKeyPressed
	 * @see onKeyReleased
	 * @see onKeyTyped
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see onMouseClicked
	 */
	val isDisabledProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Controls if user input events cause input functions of this [ComponentView] to get invoked.
	 * `true` means no invocation, where `false` means invocation.
	 *
	 * For a list of affected functions refer to the `See Also` section.
	 *
	 * @see onMouseEntered
	 * @see onMouseExited
	 * @see dropAcceptor
	 * @see onDragDropped
	 * @see onKeyPressed
	 * @see onKeyReleased
	 * @see onKeyTyped
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see onMouseClicked
	 *
	 * @see isDisabledProperty
	 */
	var isDisabled: Boolean
		get() = isDisabledProperty.value
		set(value) {
			isDisabledProperty.value = value
		}
	
	/**
	 * [Property] that controls whether this [ComponentView] is focusable or not.
	 */
	val isFocusableProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Controls whether this [ComponentView] is focusable or not.
	 * @see isFocusableProperty
	 */
	var isFocusable: Boolean
		get() = isFocusableProperty.value
		set(value) {
			isFocusableProperty.value = value
		}
	
	/**
	 * Gets invoked with an event whenever the mouse enters this [ComponentView].
	 * @see Event
	 * @see isDisabledProperty
	 */
	var onMouseEntered: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with an event whenever the mouse leaves this [ComponentView].
	 * @see Event
	 * @see isDisabledProperty
	 */
	var onMouseExited: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [MouseEvent] whenever the mouse is pressed inside this [ComponentView].
	 * @see MouseEvent
	 * @see isDisabledProperty
	 */
	var onMousePressed: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [MouseEvent] whenever the mouse is released inside this [ComponentView].
	 * @see MouseEvent
	 * @see isDisabledProperty
	 */
	var onMouseReleased: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [MouseEvent] whenever the mouse is clicked inside this [ComponentView].
	 * Gets invoked after [onMousePressed] and [onMouseReleased].
	 * @see MouseEvent
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see isDisabledProperty
	 */
	var onMouseClicked: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [KeyEvent] whenever a key is pressed while this [ComponentView] has focus.
	 * @see KeyEvent
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyPressed: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [KeyEvent] whenever a key is released while this [ComponentView] has focus.
	 * @see KeyEvent
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyReleased: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [KeyEvent] whenever a key is typed while this [ComponentView] has focus.
	 * Gets invoked after [onKeyPressed]
	 * @see KeyEvent
	 * @see onKeyPressed
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyTyped: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Returns whether this [ComponentView] is a valid drop target
	 * for the dragged component in the given [DragEvent] or not.
	 *
	 * Implement this function in such a way that it returns `true` if this [ComponentView] accepts the drop of the given
	 * [DropEvent.draggedComponent] or `false` if a drop is not valid. The [DropEvent.draggedComponent] will snap back
	 * if all available drop targets return `false`.
	 *
	 * It is advised not to modify the [Scene] or its children in this function.
	 * A better suited function to modify the [Scene] or its children
	 * after a drag and drop gesture is [onDragDropped].
	 *
	 * Note: [onDragDropped] only gets invoked if the dropAcceptor returns `true` for the given [DropEvent].
	 *
	 * @see onDragDropped
	 * @see DropEvent
	 * @see isDisabledProperty
	 */
	var dropAcceptor: ((DragEvent) -> Boolean)? = null
	
	/**
	 * Gets invoked with a [DragEvent] whenever the mouse enters this [ComponentView] while performing a drag gesture.
	 *
	 * @see DragEvent
	 */
	var onDragGestureEntered: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [DragEvent] whenever the mouse leaves this [ComponentView] while performing a drag gesture.
	 * @see DragEvent
	 */
	var onDragGestureExited: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [DragEvent] whenever a drag and drop gesture finishes over this [ComponentView]
	 * and the [dropAcceptor] returns `true` for the given [DragEvent].
	 *
	 * @see dropAcceptor
	 * @see DropEvent
	 * @see isDisabledProperty
	 */
	var onDragDropped: ((DragEvent) -> Unit)? = null
	
	/**
	 * Removes this component from it's parent.
	 *
	 * @throws IllegalStateException if this component is not contained in any container. Use parent field to check.
	 * @see parent
	 */
	fun removeFromParent(): ComponentView {
		val par = parent
		
		checkNotNull(par) { "This child has no parent." }
		
		return par.also { it.removeChild(this) }
	}
	
	/**
	 * Removes component from container's children if supported.
	 *
	 * @param component child to be removed.
	 *
	 * @throws RuntimeException if the componentView does not support children.
	 */
	internal abstract fun removeChild(component: ComponentView)
	
	/**
	 * Function returning a contained child's coordinates within this [ComponentView] if supported.
	 * This method has to be overridden.
	 * Returns `null` on all [ComponentView]s not supporting this feature.
	 *
	 * @param child child to find.
	 *
	 * @return coordinate of given child in this [ComponentView] or `null` if not supported,.
	 */
	@Suppress("FunctionOnlyReturningConstant")
	internal open fun getChildPosition(child: ComponentView): Coordinate? = null
}