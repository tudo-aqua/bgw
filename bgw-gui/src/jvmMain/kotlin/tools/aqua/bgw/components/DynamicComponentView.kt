/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused", "memberVisibilityCanBePrivate")

package tools.aqua.bgw.components

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.ReadonlyBooleanProperty
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [ComponentView]s that can be draggable. Inheriting components can only be added
 * to [BoardGameScene]s.
 *
 * @constructor Creates a [DynamicComponentView].
 *
 * @param posX The X coordinate for this [DynamicComponentView] relative to its container.
 * @param posY The Y coordinate for this [DynamicComponentView] relative to its container.
 * @param width Width for this [DynamicComponentView].
 * @param height Height for this [DynamicComponentView].
 * @param visual Visual for this [DynamicComponentView].
 *
 * @see BoardGameScene
 */
abstract class DynamicComponentView
internal constructor(posX: Number, posY: Number, width: Number, height: Number, visual: Visual) :
    ComponentView(posX = posX, posY = posY, width = width, height = height, visual = visual) {

  /**
   * [Property] that controls whether component is draggable or not.
   *
   * @see isDraggable
   */
  internal val isDraggableProperty: BooleanProperty = BooleanProperty(false)

  /** Controls whether component is draggable or not. */
  var isDraggable: Boolean
    get() = isDraggableProperty.value
    set(value) {
      isDraggableProperty.value = value
    }

  /**
   * [Property] that reflects whether component is currently dragged or not.
   *
   * @see isDragged
   */
  internal val isDraggedProperty: ReadonlyBooleanProperty = ReadonlyBooleanProperty(false)

  /** Reflects whether component is currently dragged or not. */
  var isDragged: Boolean
    get() = isDraggedProperty.value
    internal set(value) {
      isDraggedProperty.value = value
    }

  /**
   * Gets invoked with a [DragEvent] whenever a drag gesture is started with this [ComponentView].
   *
   * @see DragEvent
   */
  var onDragGestureStarted: ((DragEvent) -> Unit)? = null

  /**
   * Gets invoked with a [DragEvent] whenever a mouse movement occurs during a drag gesture with
   * this [ComponentView].
   *
   * @see DragEvent
   */
  var onDragGestureMoved: ((DragEvent) -> Unit)? = null

  /**
   * Gets invoked with a [DragEvent] whenever a drag gesture has ended with this rendered
   * [ComponentView].
   *
   * Second parameter is `true` if the drop target accepted drop, `false` otherwise.
   *
   * @see DragEvent
   */
  var onDragGestureEnded: ((DropEvent, Boolean) -> Unit)? = null
}
