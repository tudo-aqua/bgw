@file:JsModule("@dnd-kit/core")
@file:JsNonModule

package tools.aqua.bgw

import ID
import data.event.DragEventAction
import data.event.EventData
import data.event.internal.DragDroppedEventData
import data.event.internal.DragGestureStartedEventData
import org.w3c.dom.pointerevents.PointerEvent
import react.*
import react.dom.aria.AriaAttributes
import react.dom.events.DragEvent
import react.dom.events.KeyboardEvent
import react.dom.events.SyntheticEvent
import web.dom.Element
import web.events.Event
import web.html.HTMLButtonElement
import web.html.HTMLElement

@JsName("DndContext")
external val DndContext: ComponentClass<DndContextProps>

external interface DndContextProps : PropsWithChildren {
    var onDragEnd: (DragEndEvent) -> Unit
    var onDragStart: (DragStartEvent) -> Unit
    var onDragMove: (DragMultiEvent) -> Unit
    var onDragOver: (DragMultiEvent) -> Unit

    var sensors: Array<dynamic>
}

@JsName("useDraggable")
external fun useDraggable(options: DraggableOptions): DraggableResult

@JsName("useDroppable")
external fun useDroppable(options: DroppableOptions): DroppableResult

external interface DraggableOptions {
    var id: String
    var disabled: Boolean
}

external interface DraggableResult {
    val attributes : AriaAttributes
    val listeners: DnDListeners
    val setNodeRef: (Element) -> Unit
    val transform: DraggableResultTransform?
}

external interface DnDListeners {
    val onKeyDown: (KeyboardEvent<*>, id: String) -> Unit
    val onPointerDown: (react.dom.events.PointerEvent<*>, id: String) -> Unit
}

external interface DraggableResultTransform {
    val x: Double
    val y: Double
    val scaleX: Double
    val scaleY: Double
}

external interface DraggableTransform {
    val x: Double
    val y: Double
}

external interface DragStartEvent {
    val active: DragEventActive?
}

external interface DragMultiEvent {
    val delta : DraggableTransform
    val over: DragEndEventOver?
    val active: DragEventActive?
}

external interface DragEndEvent {
    val delta : DraggableTransform
    val over: DragEndEventOver?
    val active: DragEventActive?
}

external interface DragEventActive {
    var id: String
}

external interface DragEndEventOver {
    var disabled : Boolean
    var id: String
}

external interface DroppableOptions {
    var id: String
    var disabled: Boolean
}

external interface DroppableResult {
    val isOver: Boolean
    val setNodeRef: (dynamic) -> Unit
}




external interface SensorOptions {
    var activationConstraint: dynamic
}

external interface PointerSensorOptions : SensorOptions

@JsName("PointerSensor")
external val PointerSensor: dynamic

@JsName("useSensor")
external fun useSensor(sensor: dynamic, options: PointerSensorOptions): dynamic

@JsName("useSensors")
external fun useSensors(vararg sensors: dynamic): Array<dynamic>