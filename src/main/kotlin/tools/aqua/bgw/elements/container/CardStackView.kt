package tools.aqua.bgw.elements.container

import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.CardView.Companion.SOPRA_CARD_HEIGHT
import tools.aqua.bgw.elements.gameelements.CardView.Companion.SOPRA_CARD_WIDTH
import tools.aqua.bgw.exception.EmptyStackException
import tools.aqua.bgw.observable.ObjectProperty

/**
 * A CardStackView may be used to visualize a card stack.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how a CardStackView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at visuals[0] is used to visualize a background.
 * The current Visual of the cardView at elements[0] is used to visualize the topmost card of the card stack.
 *
 * @param height Height for this cardStackView. Default: the suggested card height for the SoPra.
 * @param width Width for this cardStackView. Default: the suggested card width for the SoPra.
 * @param posX Horizontal coordinate for this cardStackView. Default: 0.
 * @param posY Vertical coordinate for this cardStackView. Default: 0.
 *
 * @see CardView
 */
open class CardStackView<T : CardView>(
	height: Number = SOPRA_CARD_HEIGHT,
	width: Number = SOPRA_CARD_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,
	horizontalAlignment: HorizontalAlignment = HorizontalAlignment.CENTER
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY) {
	
	/**
	 * Property for the verticalAlignment of GameElementViews in this CardStackView.
	 * Changes are rendered directly by the framework.
	 * @see VerticalAlignment
	 */
	val verticalAlignmentProperty: ObjectProperty<VerticalAlignment> = ObjectProperty(verticalAlignment)
	
	/**
	 * Property for the horizontalAlignment of GameElementViews in this CardStackView.
	 * Changes are rendered directly by the framework.
	 * @see HorizontalAlignment
	 */
	val horizontalAlignmentProperty: ObjectProperty<HorizontalAlignment> = ObjectProperty(horizontalAlignment)
	
	/**
	 * Sets the verticalAlignment for this CardStackView.
	 * Changes are rendered directly by the framework.
	 * @see VerticalAlignment
	 */
	var verticalAlignment: VerticalAlignment
		get() = verticalAlignmentProperty.value
		set(value) {
			verticalAlignmentProperty.value = value
		}
	
	/**
	 * Sets the verticalAlignment for this CardStackView.
	 * Changes are rendered directly by the framework.
	 * @see HorizontalAlignment
	 */
	var horizontalAlignment: HorizontalAlignment
		get() = horizontalAlignmentProperty.value
		set(value) {
			horizontalAlignmentProperty.value = value
		}
	
	init {
		horizontalAlignmentProperty.internalListener = {
			observableElements.forEach { it.layoutX() }
		}
		verticalAlignmentProperty.internalListener = {
			observableElements.forEach { it.layoutY() }
		}
	}
	
	
	/**
	 * Pops the topmost CardView from this CardStackView and returns it,
	 * or null, if the stack is empty.
	 */
	fun popOrNull(): T? = observableElements.removeLastOrNull()?.apply { removePosListeners(); parent = null }
	
	/**
	 * Pops the topmost CardView from this CardStackView and returns it.
	 *
	 * @throws EmptyStackException If stack was empty.
	 */
	fun pop(): T = popOrNull() ?: throw EmptyStackException()
	
	/**
	 * Returns the topmost CardView. Does not modify the CardStackView,
	 * or null, if the stack is empty.
	 */
	fun peekOrNull(): T? = observableElements.lastOrNull()
	
	/**
	 * Returns the topmost CardView. Does not modify the CardStackView.
	 *
	 * @throws EmptyStackException If stack was empty.
	 */
	fun peek(): T = peekOrNull() ?: throw EmptyStackException()
	
	/**
	 * Adds a CardView on top of this CardStackView.
	 */
	fun push(cardView: T) {
		observableElements.add(cardView)
		cardView.parent = this
		cardView.addPosListeners()
	}
	
	override fun addElement(element: T) {
		super.addElement(element)
		element.addPosListeners()
	}
	
	override fun addAllElements(collection: Collection<T>) {
		super.addAllElements(collection)
		collection.forEach { it.addPosListeners() }
	}
	
	override fun addAllElements(vararg elements: T) {
		addAllElements(elements.toList())
	}
	
	override fun removeElement(element: T) {
		super.removeElement(element)
		element.removePosListeners()
	}
	
	override fun removeAll(): List<T> {
		return super.removeAll().onEach {
			it.removePosListeners()
		}
	}
	
	private fun T.addPosListeners() {
		posXProperty.setInternalListenerAndInvoke(0.0) { layoutX() }
		posYProperty.setInternalListenerAndInvoke(0.0) { layoutY() }
	}
	
	private fun T.removePosListeners() {
		posXProperty.internalListener = null
		posYProperty.internalListener = null
	}
	
	private fun T.layoutX() {
		posXProperty.setSilent(
			when (horizontalAlignment) {
				HorizontalAlignment.LEFT -> 0.0
				HorizontalAlignment.CENTER -> (this@CardStackView.width - this.width) / 2
				HorizontalAlignment.RIGHT -> this@CardStackView.width - this.width
			}
		)
	}
	
	private fun T.layoutY() {
		posYProperty.setSilent(
			when (verticalAlignment) {
				VerticalAlignment.TOP -> 0.0
				VerticalAlignment.CENTER -> (this@CardStackView.height - this.height) / 2
				VerticalAlignment.BOTTOM -> this@CardStackView.height - this.height
			}
		)
	}
}