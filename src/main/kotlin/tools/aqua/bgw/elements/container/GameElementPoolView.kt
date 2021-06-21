package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.event.DragEvent

/**
 * A GameElementPoolView may bu used to visualize a pool containing GameElementViews.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how an GameElementPoolView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at visuals[0] is used to visualize a background.
 * The placement of the contained Elements is used to place them relative
 * to the top left corner of this GameElementPoolView.
 * Elements that are out of bounds for this GameElementPoolView will still get rendered.
 *
 * @param height Height for this GameElementPoolView. Default: 0.
 * @param height Width for this GameElementPoolView. Default: 0.
 * @param posX Horizontal coordinate for this GameElementPoolView. Default: 0.
 * @param posY Vertical coordinate for this GameElementPoolView. Default: 0.
 */
open class GameElementPoolView<T : GameElementView>(
	height: Number = 200,
	width: Number = 130,
	posX: Number = 0,
	posY: Number = 0
) :
	GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY) {
	
	private val initialStates: HashMap<ElementView, InitialState> = HashMap()
	
	override fun addElement(element: T) {
		super.addElement(element)
		initialStates[element] = InitialState(
			isDraggable = element.isDraggable,
			isVisible = element.isVisible,
			width = element.width,
			height = element.height
		)
		element.initializePoolElement()
		element.addInternalListeners()
	}
	
	override fun addAllElements(collection: Collection<T>) {
		collection.forEach { addElement(it) }
	}
	
	override fun addAllElements(vararg elements: T) {
		addAllElements(elements.toList())
	}
	
	override fun removeElement(element: T) {
		super.removeElement(element)
		element.removeInternalListeners()
		element.restoreInitialBehaviour()
		initialStates.remove(element)
	}
	
	private fun ElementView.initializePoolElement() {
		isVisibleProperty.setSilent(false)
		widthProperty.setSilent(this@GameElementPoolView.width)
		heightProperty.setSilent(this@GameElementPoolView.height)
		isDraggableProperty.setSilent(true)
		preDragGestureStarted = preDragGestureStarted()
		preDragGestureEnded = preDragGestureEnded()
	}
	
	private fun ElementView.restoreInitialBehaviour() {
		val initialState = initialStates[this]!!
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isDraggableProperty.setSilent(initialState.isDraggable)
		isVisibleProperty.setSilent(initialState.isVisible)
		preDragGestureStarted = null
		preDragGestureEnded = null
	}
	
	private fun ElementView.addInternalListeners() {
		isDraggableProperty.internalListener = {
			initialStates[this]!!.isDraggable = it
			isDraggableProperty.setSilent(true)
		}
		
		isVisibleProperty.internalListener = {
			initialStates[this]!!.isVisible = it
			isVisibleProperty.setSilent(false)
		}
		
		widthProperty.internalListener = {
			initialStates[this]!!.width = it
			widthProperty.setSilent(this@GameElementPoolView.width)
		}
		
		heightProperty.internalListener = {
			initialStates[this]!!.height = it
			heightProperty.setSilent(this@GameElementPoolView.height)
		}
	}
	
	private fun ElementView.removeInternalListeners() {
		isDraggableProperty.internalListener = null
		isVisibleProperty.internalListener = null
		widthProperty.internalListener = null
		heightProperty.internalListener = null
	}
	
	private fun ElementView.preDragGestureStarted(): ((DragEvent) -> Unit) = {
		val initialState = initialStates[this]!!
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isVisibleProperty.setSilent(initialState.isVisible)
	}
	
	private fun ElementView.preDragGestureEnded(): ((DragEvent, Boolean) -> Unit) = { _, success ->
		if (!success) {
			isVisibleProperty.setSilent(false)
			widthProperty.setSilent(this@GameElementPoolView.width)
			heightProperty.setSilent(this@GameElementPoolView.height)
		} else {
			preDragGestureStarted = null
		}
	}
	
	private class InitialState(var isDraggable: Boolean, var isVisible: Boolean, var width: Double, var height: Double)
}