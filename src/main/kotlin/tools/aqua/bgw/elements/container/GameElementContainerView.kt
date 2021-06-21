package tools.aqua.bgw.elements.container

import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.observable.IObservable
import tools.aqua.bgw.observable.ObservableLinkedList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * GameElementContainerView is the abstract super class for containers that can contain ElementViews or its subclasses.
 * It provides the ObservableList to store ElementViews and some useful methods to work on said list.
 *
 * @param height Height for this gameElementContainerView. Default: 0.
 * @param width Width for this gameElementContainerView. Default: 0.
 * @param posX Horizontal coordinate for this GameElementView. Default: 0.
 * @param posY Vertical coordinate for this GameElementView. Default: 0.
 */
sealed class GameElementContainerView<T : GameElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visuals: MutableList<Visual> = ArrayList()
) : DynamicView(height = height, width = width, visuals = visuals, posX = posX, posY = posY) {
	/**
	 * An ObservableList to store the ElementViews that are contained in this GameElementContainerView.
	 * If changes are made to this list, the BGW framework will re-render this GameElementContainerView.
	 */
	internal val observableElements: ObservableList<T> = ObservableLinkedList()
	
	/**
	 * ElementViews that are contained in this GameElementContainerView.
	 * If changes are made to this list, the BGW framework will re-render this GameElementContainerView.
	 */
	var elements: List<T> = observableElements.toList()
		get() = observableElements.toList()
		private set
	
	/**
	 * Adds a listener on the observableElements list.
	 */
	fun addElementsListener(listener: IObservable) {
		observableElements.addListener(listener)
	}
	
	/**
	 * Removes a listener from the observableElements list.
	 */
	fun removeElementsListener(listener: IObservable) {
		observableElements.removeListener(listener)
	}
	
	/**
	 * Removes all listeners from the observableElements list.
	 */
	fun clearElementsListener() {
		observableElements.clearListeners()
	}
	
	/**
	 * Adds an ElementView to this GameElementViewContainer.
	 *
	 * @param element Element to add.
	 */
	@Synchronized
	open fun addElement(element: T) {
		check(!observableElements.contains(element)) {
			"Element $element is already contained in this $this."
		}
		check(element.parent == null) {
			"Element $element is already contained in another container."
		}
		
		observableElements.add(element.apply { parent = this@GameElementContainerView })
	}
	
	/**
	 * Adds all ElementViews passed as varargs to this GameElementContainerView.
	 *
	 * @param elements Vararg ElementViews to add.
	 */
	open fun addAllElements(vararg elements: T) {
		addAllElements(elements.toList())
	}
	
	/**
	 * Adds all ElementViews contained in the passed collection to this GameElementContainerView.
	 *
	 * @param collection Collection containing the ElementViews to add.
	 */
	@Synchronized
	open fun addAllElements(collection: Collection<T>) {
		collection.forEach { addElement(it) }
	}
	
	/**
	 * Removes the element specified by the parameter from this GameElementContainerView.
	 *
	 * @param element The element to remove.
	 */
	@Synchronized
	open fun removeElement(element: T) {
		observableElements.remove(element.apply { parent = null })
	}
	
	/**
	 * Removes all ElementViews from this GameElementContainerView.
	 */
	@Synchronized
	open fun removeAll(): List<T> {
		val tmp = observableElements.toList()
		observableElements.forEach { it.parent = null }
		observableElements.clear()
		return tmp
	}
	
	/**
	 * Returns a copy of the elements list.
	 */
	fun getAllElements(): List<T> = observableElements.toList()
	
	/**
	 * Returns the size of this elements list.
	 */
	fun numberOfElements() = observableElements.size()
	
	/**
	 * Returns whether the elements list is empty (true) or not (false) .
	 */
	fun isEmpty() = observableElements.isEmpty()
	
	/**
	 * Returns whether the elements list is not empty (true) or not (false).
	 */
	fun isNotEmpty() = !isEmpty()
	
	/**
	 * {@inheritDoc}.
	 */
	override fun getChildPosition(child: ElementView): Coordinate? = Coordinate(child.posX, child.posY)
	
	/**
	 * {@inheritDoc}.
	 */
	@Suppress("UNCHECKED_CAST")
	override fun removeChild(child: ElementView) {
		try {
			this.removeElement(child as T)
		} catch (_: ClassCastException) {
		}
	}
}