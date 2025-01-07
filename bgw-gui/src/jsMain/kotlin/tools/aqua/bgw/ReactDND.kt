/*
 * Copyright 2025 The BoardGameWork Authors
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

@file:JsModule("@dnd-kit/core")
@file:JsNonModule

package tools.aqua.bgw

import org.w3c.dom.pointerevents.PointerEvent
import react.*
import react.dom.aria.AriaAttributes
import react.dom.events.KeyboardEvent
import web.dom.Element
import web.html.HTMLElement

@JsName("DndContext") internal external val DndContext: ComponentClass<DndContextProps>

internal external interface DndContextProps : PropsWithChildren {
  var onDragEnd: (DragEndEvent) -> Unit
  var onDragStart: (DragStartEvent) -> Unit
  var onDragMove: (DragMultiEvent) -> Unit
  var onDragOver: (DragMultiEvent) -> Unit
  var measuring: MeasuringConfiguration

  var sensors: Array<dynamic>
}

internal external interface Measuring {
  var measure: (element: HTMLElement) -> LayoutRect
}

internal external interface DraggableMeasuring : Measuring

internal external interface DragOverlayMeasuring : Measuring

internal external interface DroppableMeasuring : Measuring {
  var strategy: MeasuringStrategy
  var frequency: dynamic /* MeasuringFrequency | Number */
}

internal external interface MeasuringStrategy {
  companion object {
    val Always: MeasuringStrategy
    val BeforeDragging: MeasuringStrategy
    val WhileDragging: MeasuringStrategy
  }
}

internal external interface MeasuringFrequency {
  companion object {
    val Optimized: MeasuringFrequency
  }
}

internal external interface MeasuringConfiguration {
  var draggable: DraggableMeasuring?
  var droppable: DroppableMeasuring?
  var dragOverlay: DragOverlayMeasuring?
}

@JsName("getClientRect")
internal external fun getClientRect(node: HTMLElement, options: GetClientRectOptions): LayoutRect

internal external interface GetClientRectOptions {
  var ignoreTransform: Boolean
}

@JsName("useDraggable")
internal external fun useDraggable(options: DraggableOptions): DraggableResult

@JsName("useDroppable")
internal external fun useDroppable(options: DroppableOptions): DroppableResult

@JsName("DragOverlay") internal external val DragOverlay: ComponentClass<DragOverlayProps>

internal external interface DragOverlayProps :
    PropsWithChildren, PropsWithClassName, PropsWithStyle {
  var dropAnimation: Boolean
}

internal external interface DraggableOptions {
  var id: String
  var disabled: Boolean
}

internal external interface DraggableResult {
  val attributes: AriaAttributes
  val listeners: DnDListeners
  val setNodeRef: (Element) -> Unit
  val transform: DraggableResultTransform?
}

internal external interface DnDListeners {
  val onKeyDown: (KeyboardEvent<*>, id: String) -> Unit
  val onPointerDown: (react.dom.events.PointerEvent<*>, id: String) -> Unit
}

internal external interface DraggableResultTransform {
  val x: Double
  val y: Double
  val scaleX: Double
  val scaleY: Double
}

internal external interface DraggableTransform {
  val x: Double
  val y: Double
}

internal external interface DragStartEvent {
  val active: DragEventActive?
}

internal external interface DragMultiEvent {
  val delta: DraggableTransform
  val over: DragEndEventOver?
  val active: DragEventActive?
}

internal external interface DragEndEvent {
  val delta: DraggableTransform
  val over: DragEndEventOver?
  val active: DragEventActive?
}

internal external interface DragEventActive {
  var id: String
}

internal external interface DragEndEventOver {
  var disabled: Boolean
  var rect: LayoutRect
  var id: String
}

internal external interface DroppableOptions {
  var id: String
  var disabled: Boolean
}

internal external interface DroppableResult {
  val rect: LayoutRect
  val isOver: Boolean
  val setNodeRef: (dynamic) -> Unit
}

internal external interface LayoutRect {
  var height: Double
  var width: Double
  var top: Double
  var left: Double
  var right: Double
  var bottom: Double
}

internal external interface SensorOptions {
  var activationConstraint: dynamic
}

internal external interface PointerSensorOptions : SensorOptions

@JsName("PointerSensor") internal external val PointerSensor: dynamic

@JsName("useSensor")
internal external fun useSensor(sensor: dynamic, options: PointerSensorOptions): dynamic

@JsName("useSensors") internal external fun useSensors(vararg sensors: dynamic): Array<dynamic>
