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

@file:Suppress("unused", "memberVisibilityCanBePrivate")

package tools.aqua.bgw.components

import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [ComponentView]s that can be draggable.
 *
 * @param height height for this [DynamicComponentView].
 * @param width width for this [DynamicComponentView].
 * @param posX the X coordinate for this [DynamicComponentView] relative to its container.
 * @param posY the Y coordinate for this [DynamicComponentView] relative to its container.
 * @param visual visual for this [DynamicComponentView].
 */
abstract class DynamicComponentView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : ComponentView(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	
	/**
	 * [Property] that controls whether component is draggable or not.
	 */
	val isDraggableProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Controls whether component is draggable or not.
	 * @see isDraggableProperty
	 */
	var isDraggable: Boolean
		get() = isDraggableProperty.value
		set(value) {
			isDraggableProperty.value = value
		}
	
	/**
	 * [Property] that reflects whether component is dragged or not.
	 */
	val isDraggedProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Reflects whether component is dragged or not.
	 * @see isDisabledProperty
	 */
	var isDragged: Boolean
		get() = isDraggedProperty.value
		internal set(value) {
			isDraggedProperty.value = value
		}
	
	/**
	 * Gets invoked with a [DragEvent] whenever a drag gesture is started on this [ComponentView].
	 * @see DragEvent
	 */
	var onDragGestureStarted: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [DragEvent] whenever a mouse movement occurs during a drag gesture on this [ComponentView].
	 * @see DragEvent
	 */
	var onDragGestureMoved: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a [DragEvent] whenever a drag gesture has ended on this rendered [ComponentView].
	 * Second parameter is `true` if at least one drop target accepted drop, `false` otherwise.
	 *
	 * @see DragEvent
	 */
	var onDragGestureEnded: ((DropEvent, Boolean) -> Unit)? = null
}