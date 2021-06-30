@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.event.Event
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.exception.IllegalInheritanceException
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.IntegerProperty
import tools.aqua.bgw.observable.Observable
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * ElementView is the abstract superclass of all framework elements.
 * It defines important fields and functions that are necessary to visualize inheriting elements.
 *
 * @param height height for this ElementView. Default: 0.0.
 * @param width width for this ElementView. Default: 0.0.
 * @param posX the X coordinate for this ElementView relative to its container. Default: 0.0.
 * @param posY the Y coordinate for this ElementView relative to its container. Default: 0.0.
 * @param visuals a mutable list of possible Visuals for this ElementView. Default: an empty ArrayList.
 *
 * @throws IllegalInheritanceException inheriting from this Class is not advised,
 * because it cannot be rendered and trying to do so will result in an IllegalInheritanceException.
 *
 * @see IllegalInheritanceException
 */
abstract class ElementView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	var visuals: MutableList<Visual> = ArrayList()
) : Observable() {
	
	/**
	 * The parent of this ElementView.
	 * Null if this ElementView is not contained in a container or a scene.
	 * If the element has been added directly to a scene, parent is equal to the scene's rootNode.
	 * If the element is contained within a container, parent is equal to that container.
	 * @see Scene
	 * @see GameElementContainerView
	 */
	var parent: ElementView? = null
		internal set
	
	/**
	 * Field that indicates whether posX and posY denote the center or top left of this ElementView.
	 */
	internal var layoutFromCenter: Boolean = false
	
	/**
	 * Name field only for debugging purposes. Has no effect on rendering
	 */
	var name: String = javaClass.name + "@" + Integer.toHexString(this.hashCode())
	
	/**
	 * Property for the height of this ElementView.
	 */
	val heightProperty: DoubleProperty = DoubleProperty(height.toDouble())
	
	/**
	 * The height for this ElementView.
	 * @see heightProperty
	 */
	var height: Double
		get() = heightProperty.value
		set(value) {
			heightProperty.value = value
		}
	
	/**
	 * Property for the width of this ElementView.
	 */
	val widthProperty: DoubleProperty = DoubleProperty(width.toDouble())
	
	/**
	 * The width for this ElementView.
	 * @see widthProperty
	 */
	var width: Double
		get() = widthProperty.value
		set(value) {
			widthProperty.value = value
		}
	
	/**
	 * Property for the horizontal position of this ElementView.
	 */
	val posXProperty: DoubleProperty = DoubleProperty(posX.toDouble())
	
	/**
	 * Horizontal position of this ElementView.
	 * @see posXProperty
	 */
	var posX: Double
		get() = posXProperty.value
		set(value) {
			posXProperty.value = value
		}
	
	/**
	 * Property for the vertical position of this ElementView.
	 */
	val posYProperty: DoubleProperty = DoubleProperty(posY.toDouble())
	
	/**
	 * Vertical position of this ElementView.
	 * @see posYProperty
	 */
	var posY: Double
		get() = posYProperty.value
		set(value) {
			posYProperty.value = value
		}
	
	/**
	 * Property for the rotation of this ElementView in degrees.
	 * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
	 *
	 * example conversions:
	 * -10  -> 350
	 * -370 -> 350
	 * 370  -> 10
	 * 730  -> 10
	 */
	val rotationProperty: DoubleProperty = DoubleProperty(0.0)

	/**
	 * Rotation of this ElementView in degrees.
	 * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
	 *
	 * example conversions:
	 * -10  -> 350
	 * -370 -> 350
	 * 370  -> 10
	 * 730  -> 10
	 * @see rotationProperty
	 */
	var rotation: Double
		get() = rotationProperty.value
		set(value) {
			rotationProperty.value = value
		}
	
	/**
	 * Property for the index of the current Visual in the visuals list.
	 * @see Visual
	 */
	val currentVisualProperty: IntegerProperty = IntegerProperty()
	
	/**
	 * Index of the current Visual in the visuals list.
	 * @see Visual
	 * @see currentVisualProperty
	 */
	var currentVisual: Int
		get() = currentVisualProperty.value
		set(value) {
			currentVisualProperty.value = value
		}
	
	/**
	 * Property for the opacity of this ElementView.
	 * Should be in range 0.0 to 1.0.
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Note that invisible objects (opacity == 0.0) still remain interactive.
	 */
	val opacityProperty: DoubleProperty = DoubleProperty(1.0)
	
	/**
	 * Opacity of this ElementView.
	 * Must be in range 0.0 to 1.0.
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Note that invisible objects (opacity == 0.0) still remain interactive.
	 * @see opacityProperty
	 */
	var opacity: Double
		get() = opacityProperty.value
		set(value) {
			require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }
			
			opacityProperty.value = value
		}
	
	/**
	 * Property for the visibility of this ElementView.
	 * Invisible ElementViews are disabled.
	 * An object marked as visible may still be opaque due to opacity
	 * @see isDisabledProperty
	 * @see opacityProperty
	 */
	val isVisibleProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Visibility of this ElementView.
	 * Invisible ElementViews are disabled.
	 * An object marked as visible may still be opaque due to opacity
	 * @see isDisabledProperty
	 * @see opacityProperty
	 * @see isVisibleProperty
	 */
	var isVisible: Boolean
		get() = isVisibleProperty.value
		set(value) {
			isVisibleProperty.value = value
		}
	
	/**
	 * Property that controls if user input events cause input functions of this ElementView to get invoked.
	 * `true` means no invocation, where `false` means invocation.
	 *
	 * For a list of affected functions refer to the `See Also` section.
	 *
	 * @see onMouseEntered
	 * @see onMouseExited
	 * @see dropAcceptor
	 * @see onDragElementDropped
	 * @see onKeyPressed
	 * @see onKeyReleased
	 * @see onKeyTyped
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see onMouseClicked
	 */
	val isDisabledProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Controls if user input events cause input functions of this ElementView to get invoked.
	 * `true` means no invocation, where `false` means invocation.
	 *
	 * For a list of affected functions refer to the `See Also` section.
	 *
	 * @see onMouseEntered
	 * @see onMouseExited
	 * @see dropAcceptor
	 * @see onDragElementDropped
	 * @see onKeyPressed
	 * @see onKeyReleased
	 * @see onKeyTyped
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see onMouseClicked
	 *
	 * @see isDisabledProperty
	 */
	var isDisabled: Boolean
		get() = isDisabledProperty.value
		set(value) {
			isDisabledProperty.value = value
		}
	
	/**
	 * Property that controls whether this ElementView is focusable or not.
	 */
	val isFocusableProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Controls whether this ElementView is focusable or not.
	 * @see isFocusableProperty
	 */
	var isFocusable: Boolean
		get() = isFocusableProperty.value
		set(value) {
			isFocusableProperty.value = value
		}
	
	/**
	 * Gets invoked with an event whenever the mouse enters this ElementView.
	 * @see Event
	 * @see isDisabledProperty
	 */
	var onMouseEntered: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with an event whenever the mouse leaves this ElementView.
	 * @see Event
	 * @see isDisabledProperty
	 */
	var onMouseExited: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the mouse is pressed inside this ElementView.
	 * @see MouseEvent
	 * @see isDisabledProperty
	 */
	var onMousePressed: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the mouse is released inside this ElementView.
	 * @see MouseEvent
	 * @see isDisabledProperty
	 */
	var onMouseReleased: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the mouse is clicked inside this ElementView.
	 * Gets invoked after onMousePressed and onMouseReleased.
	 * @see MouseEvent
	 * @see onMousePressed
	 * @see onMouseReleased
	 * @see isDisabledProperty
	 */
	var onMouseClicked: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a keyEvent whenever a key is pressed while this ElementView has focus.
	 * @see KeyEvent
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyPressed: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a keyEvent whenever a key is released while this ElementView has focus.
	 * @see KeyEvent
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyReleased: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a keyEvent whenever a key is typed while this ElementView has focus.
	 * Gets invoked after onKeyPressed
	 * @see KeyEvent
	 * @see onKeyPressed
	 * @see isDisabledProperty
	 * @see isFocusableProperty
	 */
	var onKeyTyped: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Rreturns whether this ElementView is a valid drop target
	 * for the dragged element in the given DropEvent or not.
	 *
	 * Implement this function in such a way that it returns `true` if this [ElementView] accepts the drop of the given
	 * [DropEvent.draggedElement] or `false` if a drop is not valid. The [DropEvent.draggedElement] will snap back
	 * if all available drop targets return `false`.
	 *
	 * It is advised not to modify the [Scene] or its children in this function.
	 * A better suited function to modify the [Scene] or its children
	 * after a drag and drop gesture is [onDragElementDropped].
	 *
	 * Note: [onDragElementDropped] only gets invoked if the dropAcceptor returns `true` for the given [DropEvent].
	 *
	 * @see onDragElementDropped
	 * @see DropEvent
	 * @see isDisabledProperty
	 */
	var dropAcceptor: ((DropEvent) -> Boolean)? = null
	
	/**
	 * Gets invoked with a DropEvent whenever a drag and drop gesture finishes over this ElementView
	 * and the dropAcceptor returns `true` for the given DropEvent.
	 *
	 * @see dropAcceptor
	 * @see DropEvent
	 * @see isDisabledProperty
	 */
	var onDragElementDropped: ((DropEvent) -> Unit)? = null
	
	/**
	 * Returns the Visual that is currently used to render this ElementView.
	 */
	fun getCurrentVisual(): Visual = if (currentVisual < visuals.size) visuals[currentVisual] else Visual.EMPTY
	
	/**
	 * Changes the current Visual that gets used for rendering this ElementView
	 * to the Visual at the specified index in the visuals list.
	 *
	 * @param id the index for the Visual that should be rendered.
	 */
	fun showVisual(id: Int) {
		if (id <= visuals.size)
			currentVisual = id
		else
			throw IndexOutOfBoundsException("Index out of bounds for visuals.")
	}
	
	/**
	 * Clears the visuals list and adds all visuals passed as a parameter.
	 * Sets first Visual as the one to show.
	 *
	 * @param newVisuals the new Visuals for this ElementView.
	 */
	fun setVisuals(vararg newVisuals: Visual) {
		setVisuals(0, *newVisuals)
	}
	
	/**
	 * Clears the visuals list and adds all visuals passed as a parameter and shows selected one.
	 *
	 * @param newVisuals the new Visuals for this ElementView.
	 * @param visualToShow Index of the visual to show
	 */
	fun setVisuals(visualToShow: Int, vararg newVisuals: Visual) {
		if (visualToShow !in newVisuals.indices)
			throw ArrayIndexOutOfBoundsException("VisualToShow index out of bounds")
		
		visuals.clear()
		visuals.addAll(newVisuals)
		currentVisual = visualToShow
	}
	
	/**
	 * Removes this element from it's parent.
	 *
	 * @throws IllegalStateException If this element is not contained in any container. Use parent field to check.
	 * @see parent
	 */
	fun removeFromParent(): ElementView {
		val par = parent
		
		checkNotNull(par) { "This child has no parent." }
		
		return par.also { it.removeChild(this) }
	}
	
	/**
	 * Removes element from container's children if supported.
	 *
	 * @param child child to be removed.
	 *
	 * @throws RuntimeException if the elementView does not support children.
	 */
	internal abstract fun removeChild(child: ElementView)
	
	/**
	 * Method returning a contained child's coordinates within this ElementView if supported.
	 * This method has to be overridden.
	 * Returns null on all elementViews not supporting this feature.
	 *
	 * @param child child to find.
	 *
	 * @return coordinate of given child in this ElementView or null if not supported,.
	 */
	@Suppress("FunctionOnlyReturningConstant")
	internal open fun getChildPosition(child: ElementView): Coordinate? = null
}