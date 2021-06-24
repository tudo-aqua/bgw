package tools.aqua.bgw.elements

import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all ElementViews that can be draggable.
 */
abstract class DynamicView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : ElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals) {
	
	/**
	 * Property that controls whether element is draggable or not.
	 */
	val isDraggableProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Controls whether element is draggable or not.
	 * @see isDraggableProperty
	 */
	var isDraggable: Boolean
		get() = isDraggableProperty.value
		set(value) {
			isDraggableProperty.value = value
		}
	
	/**
	 * Property that reflects whether element is dragged or not.
	 */
	val isDraggedProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Reflects whether element is dragged or not.
	 * @see isDisabledProperty
	 */
	var isDragged: Boolean
		get() = isDraggedProperty.value
		internal set(value) {
			isDraggedProperty.value = value
		}
	
	/**
	 * Gets invoked with a DragEvent whenever a drag gesture is started on this ElementView.
	 * @see DragEvent
	 */
	var onDragGestureStarted: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent whenever a mouse movement occurs during a drag gesture on this ElementView.
	 * @see DragEvent
	 */
	var onDragGestureMoved: ((DragEvent) -> Unit)? = null

	/**
	 * Gets invoked with a DragEvent whenever a drag gesture has ended on this rendered ElementView.
	 * @see DragEvent
	 */
	var onDragGestureEnded: ((DragEvent, Boolean) -> Unit)? = null

//	/**
//	 * Gets passed a DragEvent whenever the Mouse enters this rendered ElementView while performing a drag gesture.
//	 * @see DragEvent
//	 */
//	var onDragGestureEntered: EventHandler<DragEvent>? = null
//
//	/**
//	 * Gets passed a DragEvent whenever the Mouse leaves this rendered ElementView while performing a drag gesture.
//	 * @see DragEvent
//	 */
//	var onDragGestureExited: EventHandler<DragEvent>? = null
}