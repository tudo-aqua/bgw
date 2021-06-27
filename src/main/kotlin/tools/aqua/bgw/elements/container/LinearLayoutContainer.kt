@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.elements.container

import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.elements.uielements.Orientation
import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.ObjectProperty

/**
 * A LinearLayoutContainer may be used to visualize a zone containing GameElementViews.
 * GameElementViews inside the container get placed according to the specified orientation and alignment.
 * A spacing between Elements may be specified which may also be negative
 * e.g. Elements like playing cards should overlap.
 *
 * Visualization:
 * The current Visual is used to visualize a background.
 * If all Elements are still within bounds with the user defined spacing,
 * the user defined spacing gets used to space the Elements.
 * Otherwise the biggest possible spacing is used
 * so that all Elements are still withing bounds of the LinearLayoutContainer.
 *
 * @param height height for this LinearLayoutContainer. Default: 0.
 * @param width width for this LinearLayoutContainer. Default: 0.
 * @param posX horizontal coordinate for this LinearLayoutContainer. Default: 0.
 * @param posY vertical coordinate for this LinearLayoutContainer. Default: 0.
 * @param spacing spacing between contained GameElementViews. Default: 0.
 * @param orientation orientation for this LinearLayoutContainer. Default: HORIZONTAL.
 * @param verticalAlignment specifies how the contained Elements should be aligned vertically. Default: TOP.
 * @param horizontalAlignment specifies how the contained Elements should be aligned horizontally. Default: LEFT.
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
     */
    val spacingProperty: DoubleProperty = DoubleProperty(spacing.toDouble())
    
    /**
     * Property for the orientation of GameElementViews in this LinearLayoutContainer.
     * @see Orientation
     */
    val orientationProperty: ObjectProperty<Orientation> = ObjectProperty(orientation)
    
    /**
     * Property for the verticalAlignment of GameElementViews in this LinearLayoutContainer.
     * @see VerticalAlignment
     */
    val verticalAlignmentProperty: ObjectProperty<VerticalAlignment> = ObjectProperty(verticalAlignment)
    
    /**
     * Property for the horizontalAlignment of GameElementViews in this LinearLayoutContainer.
     * @see HorizontalAlignment
     */
    val horizontalAlignmentProperty: ObjectProperty<HorizontalAlignment> = ObjectProperty(horizontalAlignment)
    
    
    /**
     * Spacing for this LinearLayoutContainer.
     */
    var spacing: Double
        get() = spacingProperty.value
        set(value) {
            spacingProperty.value = value
        }
    
    /**
     * Orientation for this LinearLayoutContainer.
     * @see Orientation
     * @see orientationProperty
     */
    var orientation: Orientation
        get() = orientationProperty.value
        set(value) {
            orientationProperty.value = value
        }
    
    /**
     * VerticalAlignment for this LinearLayoutContainer.
     * @see VerticalAlignment
     * @see verticalAlignmentProperty
     */
    var verticalAlignment: VerticalAlignment
        get() = verticalAlignmentProperty.value
        set(value) {
            verticalAlignmentProperty.value = value
        }
    
    /**
     * VerticalAlignment for this LinearLayoutContainer.
     * @see HorizontalAlignment
     * @see horizontalAlignmentProperty
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
        spacingProperty.internalListener = { _, _ -> layout() }
        orientationProperty.internalListener = { _, _ -> layout() }
        verticalAlignmentProperty.internalListener = { _, _ -> layout() }
        horizontalAlignmentProperty.internalListener = { _, _ -> layout() }
    }
    
    override fun addElement(element: T, index: Int) {
        super.addElement(element, index)
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
        posXProperty.internalListener = { _, _ -> observableElements.internalListener?.invoke() }
        posYProperty.internalListener = { _, _ -> observableElements.internalListener?.invoke() }
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