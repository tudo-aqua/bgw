package tools.aqua.bgw.elements.container

import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.elements.uielements.Orientation
import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.ObjectProperty

/**
 * A LinearLayoutContainer may be used to visualize a zone containing GameElementViews.
 * GameElementViews inside the container get placed side by side from left to right.
 * You may Specify a spacing between Elements which also may be negative
 * e.g Elements like playing cards should overlap.
 * You can inherit from this class if you want to add additional functionality or fields.
 * Inheriting does NOT change how an areaContainerView is visualized by the BGW framework.
 *
 * Visualization:
 * The Visual at visuals[0] is used to visualize a background.
 * If all Elements are still within bounds with the user defined spacing,
 * the user defined spacing gets used to space the Elements.
 * Otherwise the biggest possible spacing is used
 * so that all Elements are still withing bounds of the LinearLayoutContainer.
 *
 * @param height Height for this LinearLayoutContainer. Default: 0.0
 * @param width Width for this LinearLayoutContainer. Default: 0.0
 * @param posX Horizontal coordinate for this LinearLayoutContainer. Default: 0.0.
 * @param posY Vertical coordinate for this LinearLayoutContainer. Default: 0.0.
 * @param spacing Spacing between Elements.
 * @param orientation Orientation for the LinearLayoutContainer. Default: HORIZONTAL.
 */
open class LinearLayoutContainer<T : GameElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	spacing: Number = 0,
	orientation: Orientation = Orientation.HORIZONTAL,
	verticalAlignment: VerticalAlignment = VerticalAlignment.TOP,
	horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY) {
	
	/**
	 * Property for the spacing of GameElementViews in this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 */
	val spacingProperty: DoubleProperty = DoubleProperty(spacing.toDouble())
	
	/**
	 * Property for the orientation of GameElementViews in this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see Orientation
	 */
	val orientationProperty: ObjectProperty<Orientation> = ObjectProperty(orientation)
	
	/**
	 * Property for the verticalAlignment of GameElementViews in this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see VerticalAlignment
	 */
	val verticalAlignmentProperty: ObjectProperty<VerticalAlignment> = ObjectProperty(verticalAlignment)
	
	/**
	 * Property for the horizontalAlignment of GameElementViews in this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see HorizontalAlignment
	 */
	val horizontalAlignmentProperty: ObjectProperty<HorizontalAlignment> = ObjectProperty(horizontalAlignment)
	
	
	/**
	 * Sets the Spacing for this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 */
	var spacing: Double
		get() = spacingProperty.value
		set(value) {
			spacingProperty.value = value
		}
	
	/**
	 * Sets the orientation for this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see Orientation
	 */
	var orientation: Orientation
		get() = orientationProperty.value
		set(value) {
			orientationProperty.value = value
		}
	
	/**
	 * Sets the verticalAlignment for this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see VerticalAlignment
	 */
	var verticalAlignment: VerticalAlignment
		get() = verticalAlignmentProperty.value
		set(value) {
			verticalAlignmentProperty.value = value
		}
	
	/**
	 * Sets the verticalAlignment for this LinearLayoutContainer.
	 * Changes are rendered directly by the framework.
	 * @see HorizontalAlignment
	 */
	var horizontalAlignment: HorizontalAlignment
		get() = horizontalAlignmentProperty.value
		set(value) {
			horizontalAlignmentProperty.value = value
		}
	
	init {
		observableElements.setInternalListenerAndInvoke {
			layout()
		}
		spacingProperty.internalListener = { layout() }
		orientationProperty.internalListener = { layout() }
		verticalAlignmentProperty.internalListener = { layout() }
		horizontalAlignmentProperty.internalListener = { layout() }
	}
	
	override fun addElement(element: T) {
		super.addElement(element)
		element.apply { addPosListeners() }
	}
	
	override fun addAllElements(collection: Collection<T>) {
		super.addAllElements(collection)
		collection.forEach { it.addPosListeners() }
	}
	
	override fun addAllElements(vararg elements: T) {
		addAllElements(elements.toList())
	}
	
	override fun removeElement(element: T) {
		super.removeElement(element.apply { removePosListeners() })
	}
	
	override fun removeAll(): List<T> {
		return super.removeAll().onEach {
			it.removePosListeners()
		}
	}
	
	private fun T.addPosListeners() {
		//TODO: maybe increase performance
		posXProperty.internalListener = { observableElements.internalListener?.invoke() }
		posYProperty.internalListener = { observableElements.internalListener?.invoke() }
	}
	
	private fun T.removePosListeners() {
		posXProperty.internalListener = null
		posYProperty.internalListener = null
	}
	
	private fun layoutHorizontal() {
		val totalContentWidth: Double = observableElements.sumOf { it.width }
		val totalContentWidthWithSpacing = totalContentWidth + (observableElements.size() - 1) * spacing
		val newSpacing: Double = if (totalContentWidthWithSpacing > width) {
			-((totalContentWidth - width) / (observableElements.size() - 1)) //ignore user defined spacing
		} else {
			spacing //use user defined spacing
		}
		val newTotalContentWidth = totalContentWidth + (observableElements.size() - 1) * newSpacing
		val initial = when (horizontalAlignment) {
			HorizontalAlignment.LEFT -> 0.0
			HorizontalAlignment.CENTER -> (width - newTotalContentWidth) / 2
			HorizontalAlignment.RIGHT -> width - newTotalContentWidth
		}
		
		observableElements.fold(initial) { acc: Double, element: T ->
			element.posYProperty.setSilent(
				when (verticalAlignment) {
					VerticalAlignment.TOP -> 0.0
					VerticalAlignment.CENTER -> (height - element.height) / 2
					VerticalAlignment.BOTTOM -> height - element.height
				}
			)
			element.posXProperty.setSilent(acc)
			acc + element.width + newSpacing
		}
	}
	
	private fun layoutVertical() {
		val totalContentHeight: Double = observableElements.sumOf { it.height }
		val totalContentHeightWithSpacing = totalContentHeight + (observableElements.size() - 1) * spacing
		val newSpacing: Double = if (totalContentHeightWithSpacing > height) {
			-((totalContentHeight - height) / (observableElements.size() - 1)) //ignore user defined spacing
		} else {
			spacing //use user defined spacing
		}
		val newTotalContentHeight = totalContentHeight + (observableElements.size() - 1) * newSpacing
		val initial = when (verticalAlignment) {
			VerticalAlignment.TOP -> 0.0
			VerticalAlignment.CENTER -> (width - newTotalContentHeight) / 2
			VerticalAlignment.BOTTOM -> width - newTotalContentHeight
		}
		observableElements.fold(initial) { acc: Double, element: T ->
			element.posYProperty.setSilent(acc)
			element.posXProperty.setSilent(
				when (horizontalAlignment) {
					HorizontalAlignment.LEFT -> 0.0
					HorizontalAlignment.CENTER -> (width - element.width) / 2
					HorizontalAlignment.RIGHT -> width - element.width
				}
			)
			acc + element.height + newSpacing
		}
	}
	
	private fun layout() {
		when (orientation) {
			Orientation.HORIZONTAL -> layoutHorizontal()
			Orientation.VERTICAL -> layoutVertical()
		}
	}
	
}