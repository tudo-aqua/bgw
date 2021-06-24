package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.visual.Visual

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
	posY: Number = 0,
	visual: Visual,
) :
	GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY, visuals = mutableListOf(visual)) {
	
	private val initialStates: HashMap<ElementView, InitialState> = HashMap()

	
	override fun addElement(element: T, index: Int) {
		super.addElement(element, index)
		initialStates[element] = InitialState(
			isDraggable = element.isDraggable,
			isVisible = element.isVisible,
			width = element.width,
			height = element.height
		)
		element.initializePoolElement()
		element.addInternalListeners()
		element.addPosListeners()
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
		element.removePosListeners()
		initialStates.remove(element)
	}
	
	private fun GameElementView.initializePoolElement() {
		isVisibleProperty.setSilent(false)
		widthProperty.setSilent(this@GameElementPoolView.width)
		heightProperty.setSilent(this@GameElementPoolView.height)
		isDraggableProperty.setSilent(true)
	}
	
	private fun GameElementView.restoreInitialBehaviour() {
		val initialState = initialStates[this]!!
		widthProperty.setSilent(initialState.width)
		heightProperty.setSilent(initialState.height)
		isDraggableProperty.setSilent(initialState.isDraggable)
		isVisibleProperty.setSilent(initialState.isVisible)
	}
	
	private fun GameElementView.addInternalListeners() {
		isDraggableProperty.internalListener = { _, nV ->
			initialStates[this]!!.isDraggable = nV
			isDraggableProperty.setSilent(true)
		}
		
		isVisibleProperty.internalListener = { _, nV ->
			initialStates[this]!!.isVisible = nV
			isVisibleProperty.setSilent(false)
		}
		
		widthProperty.internalListener = { _, nV ->
			initialStates[this]!!.width = nV
			widthProperty.setSilent(this@GameElementPoolView.width)
		}
		
		heightProperty.internalListener = { _, nV ->
			initialStates[this]!!.height = nV
			heightProperty.setSilent(this@GameElementPoolView.height)
		}
	}
	
	private fun GameElementView.removeInternalListeners() {
		isDraggableProperty.internalListener = null
		isVisibleProperty.internalListener = null
		widthProperty.internalListener = null
		heightProperty.internalListener = null
	}

	private fun GameElementView.addPosListeners() {
		this.posXProperty.addListenerAndInvoke(0.0) { _, _ ->
			posXProperty.setSilent(0.0)
		}
		this.posYProperty.addListenerAndInvoke(0.0) { _, _ ->
			posYProperty.setSilent(0.0)
		}
	}

	private fun GameElementView.removePosListeners() {
		this.posXProperty.internalListener = null
		this.posYProperty.internalListener = null
	}
	
	private class InitialState(var isDraggable: Boolean, var isVisible: Boolean, var width: Double, var height: Double)
}