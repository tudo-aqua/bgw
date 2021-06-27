@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.elements.container

import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.CardView.Companion.DEFAULT_CARD_HEIGHT
import tools.aqua.bgw.elements.gameelements.CardView.Companion.DEFAULT_CARD_WIDTH
import tools.aqua.bgw.exception.EmptyStackException
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.visual.Visual

/**
 * A CardStackView may be used to visualize a card stack.
 *
 * Visualization:
 * The current Visual is used to visualize a background.
 * The current Visual of the CardView at elements[0] is used to visualize the topmost card of the card stack.
 * The positioning of contained CardViews gets ignored and the specified alignment gets used to position them instead.
 *
 * @param height height for this CardStackView. Default: the suggested card height.
 * @param width width for this CardStackView. Default: the suggested card width.
 * @param posX horizontal coordinate for this CardStackView. Default: 0.
 * @param posY vertical coordinate for this CardStackView. Default: 0.
 * @param verticalAlignment specifies how the contained CardViews should be aligned vertically. Default: CENTER.
 * @param horizontalAlignment specifies how the contained CardViews should be aligned horizontally. Default: CENTER.
 * @param visual visual for this CardStackView. Default: empty Visual.
 * @see CardView
 */
open class CardStackView<T : CardView>(
	height: Number = DEFAULT_CARD_HEIGHT,
	width: Number = DEFAULT_CARD_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	verticalAlignment: VerticalAlignment = VerticalAlignment.CENTER,
	horizontalAlignment: HorizontalAlignment = HorizontalAlignment.CENTER,
	visual: Visual = Visual.EMPTY
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY, visuals = mutableListOf(visual)) {
	
	/**
	 * Property for the verticalAlignment of CardViews in this CardStackView.
	 * @see VerticalAlignment
	 */
	val verticalAlignmentProperty: ObjectProperty<VerticalAlignment> = ObjectProperty(verticalAlignment)
	
	/**
	 * Property for the horizontalAlignment of CardViews in this CardStackView.
	 * @see HorizontalAlignment
	 */
	val horizontalAlignmentProperty: ObjectProperty<HorizontalAlignment> = ObjectProperty(horizontalAlignment)
	
	/**
	 * VerticalAlignment of CardViews in this CardStackView.
	 * @see VerticalAlignment
	 * @see verticalAlignmentProperty
	 */
	var verticalAlignment: VerticalAlignment
		get() = verticalAlignmentProperty.value
		set(value) {
			verticalAlignmentProperty.value = value
		}
	
	/**
	 * HorizontalAlignment of CardViews in this CardStackView.
	 * @see HorizontalAlignment
	 * @see verticalAlignmentProperty
	 */
	var horizontalAlignment: HorizontalAlignment
		get() = horizontalAlignmentProperty.value
		set(value) {
			horizontalAlignmentProperty.value = value
		}
	
	init {
		horizontalAlignmentProperty.internalListener = { _, _ ->
			observableElements.forEach { it.layoutX() }
		}
		verticalAlignmentProperty.internalListener = { _, _ ->
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
	 * Returns the topmost CardView, or null, if the stack is empty.
	 * Does not modify the CardStackView.
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
	
	
	override fun addElement(element: T, index: Int) {
		super.addElement(element, index)
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
		posXProperty.setInternalListenerAndInvoke(0.0) { _, _ -> layoutX() }
		posYProperty.setInternalListenerAndInvoke(0.0) { _, _ -> layoutY() }
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