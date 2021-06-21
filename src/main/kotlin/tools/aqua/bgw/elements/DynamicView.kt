package tools.aqua.bgw.elements

import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.visual.Visual

/**
 * Superclass for all dynamic views that can be used only in BoardGameScenes.
 */
abstract class DynamicView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : ElementView(height = height, width = width, posX = posX, posY = posY, visuals = visuals) {
	
	/**
	 * Property that controls if element is draggable or not.
	 */
	val isDraggableProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Field that controls if element is draggable or not.
	 */
	var isDraggable: Boolean
		get() = isDraggableProperty.value
		set(value) {
			isDraggableProperty.value = value
		}
	
	/**
	 * Property that reflects when element is dragged.
	 */
	val isDraggedProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Field that reflects when element is dragged.
	 */
	var isDragged: Boolean
		get() = isDraggedProperty.value
		internal set(value) {
			isDraggedProperty.value = value
		}
	
	/**
	 * Gets invoked with a DragEvent whenever a drag gesture is started on this rendered ElementView.
	 * @see DragEvent
	 */
	var onDragGestureStarted: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent before onDragGestureStarted.
	 * @see DragEvent
	 */
	internal var preDragGestureStarted: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent after onDragGestureStarted.
	 * @see DragEvent
	 */
	internal var postDragGestureStarted: ((DragEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent whenever a drag gesture is moved on this rendered ElementView.
	 * @see DragEvent
	 */
	var onDragGestureMoved: ((DragEvent) -> Unit)? = null

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