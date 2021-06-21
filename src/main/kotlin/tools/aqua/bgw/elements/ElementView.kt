@file:Suppress("MemberVisibilityCanBePrivate", "Unused")

package tools.aqua.bgw.elements

import tools.aqua.bgw.event.*
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.IntegerProperty
import tools.aqua.bgw.observable.Observable
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.Visual

/**
 * ElementView is the abstract super class of all framework elements.
 * It defines important fields and methods that are needed to visualize inheriting elements.
 *
 * Do not inherit from this class.
 *
 * @param height Height for this ElementView. Default: 0.0.
 * @param width Width for this ElementView. Default: 0.0.
 * @param posX The X coordinate for this ElementView relative to its container. Default: 0.0.
 * @param posY The Y coordinate for this ElementView relative to its container. Default: 0.0.
 * @param visuals A mutable list of possible Visuals for this ElementView. Default: an empty ArrayList.
 */
abstract class ElementView(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	var visuals: MutableList<Visual> = ArrayList()
) : Observable() {
	
	/**
	 * The parent view of this ElementView.
	 * If and only if it has not been added to a scene or has been removed this field is null.
	 * If the element has been added directly to a scene, parent is equal to the scene's rootNode.
	 * If the element is contained within a container, this field contains this container.
	 */
	var parent: ElementView? = null
		internal set
	
	/**
	 * Field that indicated whether posX/Y is anchored in the center of the view component or top left.
	 */
	internal var layoutFromCenter: Boolean = false
	
	/**
	 * Name field only for debugging purposes. Has no effect on rendering
	 */
	var name = javaClass.name + "@" + Integer.toHexString(this.hashCode())
	
	/**
	 * Property for the height of this ElementView. Changes are rendered directly by the framework.
	 */
	val heightProperty: DoubleProperty = DoubleProperty(height.toDouble())
	
	/**
	 * Sets the Height for this ElementView. Changes are rendered directly by the framework.
	 */
	var height: Double
		get() = heightProperty.value
		set(value) {
			heightProperty.value = value
		}
	
	/**
	 * Property for the width of this ElementView. Changes are rendered directly by the framework.
	 */
	val widthProperty: DoubleProperty = DoubleProperty(width.toDouble())
	
	/**
	 * Sets the Width for this ElementView. Changes are rendered directly by the framework.
	 */
	var width: Double
		get() = widthProperty.value
		set(value) {
			widthProperty.value = value
		}
	
	/**
	 * Property for the horizontal position of the object.
	 */
	val posXProperty: DoubleProperty = DoubleProperty(posX.toDouble())
	
	/**
	 * Horizontal position of the object.
	 */
	var posX: Double
		get() = posXProperty.value
		set(value) {
			posXProperty.value = value
		}
	
	/**
	 * Property for the vertical position of the object.
	 */
	val posYProperty: DoubleProperty = DoubleProperty(posY.toDouble())
	
	/**
	 * Vertical position of the object.
	 */
	var posY: Double
		get() = posYProperty.value
		set(value) {
			posYProperty.value = value
		}
	
	/**
	 * Property for the rotation of the object in degrees e.g. 0 to 360 from the center.
	 * Negative rotations and degrees above 360 get mapped onto domain [0,360).
	 */
	val rotationProperty: DoubleProperty = DoubleProperty(0.0)
	
	/**
	 * Rotation of the object in degrees e.g. 0 to 360 from the center.
	 * Negative rotations and degrees above 360 get mapped onto domain [0,360).
	 */
	var rotation: Double
		get() = rotationProperty.value
		set(value) {
			rotationProperty.value = value
		}
	
	/**
	 * Property for the index of the Current Visual in the visuals list.
	 * Changes of this property are rendered directly by the framework.
	 */
	val currentVisualProperty: IntegerProperty = IntegerProperty()
	
	/**
	 * Index of the Current Visual in the visuals list.
	 * Changes of this property are rendered directly by the framework.
	 */
	var currentVisual: Int
		get() = currentVisualProperty.value
		set(value) {
			currentVisualProperty.value = value
		}
	
	/**
	 * Property for the opacity of a rendered Element.
	 * Should be in range 0.0 to 1.0
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Changes of this property are rendered directly by the framework.
	 * Note that invisible objects (e.g. opacity = 0) still remain interactive.
	 */
	val opacityProperty: DoubleProperty = DoubleProperty(1.0)
	
	/**
	 * Opacity of a rendered Element.
	 * Must be in range 0.0 to 1.0
	 * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
	 * Changes of this property are rendered directly by the framework.
	 * Note that invisible objects (e.g. opacity = 0) still remain interactive.
	 */
	var opacity: Double
		get() = opacityProperty.value
		set(value) {
			require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }
			
			opacityProperty.value = value
		}
	
	/**
	 * Property for the visibility of a rendered Element.
	 * Changes of this property are rendered directly by the framework.
	 * Note that invisible are non-interactive.
	 * An object marked as visible may still be opaque due to opacity
	 *
	 * @see opacityProperty
	 */
	val isVisibleProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Visibility of a rendered Element.
	 * Changes of this property are rendered directly by the framework.
	 * Note that invisible are non-interactive.
	 * An object marked as visible may still be opaque due to opacity
	 *
	 * @see opacity
	 */
	var isVisible: Boolean
		get() = isVisibleProperty.value
		set(value) {
			isVisibleProperty.value = value
		}
	
	/**
	 * Property that controls if GUI events are passed to the eventHandlers of this ElementView.
	 * true means no events are passed, where false means events are passed.
	 */
	val isDisabledProperty: BooleanProperty = BooleanProperty(false)
	
	/**
	 * Field that controls if GUI events are passed to the eventHandlers of this ElementView.
	 * true means no events are passed, where false means events are passed.
	 */
	var isDisabled: Boolean
		get() = isDisabledProperty.value
		set(value) {
			isDisabledProperty.value = value
		}
	
	/**
	 * Property that controls if element is focusable or not.
	 */
	val isFocusableProperty: BooleanProperty = BooleanProperty(true)
	
	/**
	 * Field that controls if element is focusable or not.
	 */
	var isFocusable: Boolean
		get() = isFocusableProperty.value
		set(value) {
			isFocusableProperty.value = value
		}
	
	/**
	 * Gets invoked with an event whenever the Mouse enters this rendered ElementView.
	 * @see Event
	 */
	var onMouseEntered: ((Event) -> Unit)? = null
	
	/**
	 * Gets invoked with an event whenever the Mouse leaves this rendered ElementView.
	 * @see Event
	 */
	var onMouseExited: ((Event) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the Mouse is pressed inside this rendered ElementView.
	 * @see MouseEvent
	 */
	var onMousePressed: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the Mouse is released inside this rendered ElementView.
	 * @see MouseEvent
	 */
	var onMouseReleased: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a mouseEvent whenever the Mouse is clicked inside this rendered ElementView.
	 * Gets invoked after onMousePressed and onMouseReleased
	 * @see MouseEvent
	 * @see onMousePressed
	 * @see onMouseReleased
	 */
	var onMouseClicked: ((MouseEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a keyEvent whenever a key is pressed while rendered ElementView has focus.
	 * @see KeyEvent
	 */
	var onKeyPressed: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a keyEvent whenever a key is released while rendered ElementView has focus.
	 * @see KeyEvent
	 */
	var onKeyReleased: ((KeyEvent) -> Unit)? = null
	
	//TODO: key typed is composition of keys
	/**
	 * Gets invoked with a keyEvent whenever a key is typed while rendered ElementView has focus.
	 * Gets invoked after onKeyPressed
	 * @see KeyEvent
	 * @see onKeyPressed
	 */
	var onKeyTyped: ((KeyEvent) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent whenever a drag gesture has ended on this rendered ElementView.
	 * @see DragEvent
	 */
	var onDragGestureEnded: ((DragEvent, Boolean) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent before onDragGestureEnded.
	 * @see DragEvent
	 */
	internal var preDragGestureEnded: ((DragEvent, Boolean) -> Unit)? = null
	
	/**
	 * Gets invoked with a DragEvent after onDragGestureEnded.
	 * @see DragEvent
	 */
	internal var postDragGestureEnded: ((DragEvent, Boolean) -> Unit)? = null
	
	/**
	 * Should return true if this ElementView is a valid drop target for the dragged element in the given DropEvent.
	 * Note: Do not alter the given dragged element by now. Use the onDragElementDropped handler.
	 *
	 * @see DropEvent
	 */
	var dropAcceptor: ((DropEvent) -> Boolean)? = null
	
	/**
	 * Gets invoked with a DropEvent whenever a drag gesture has ended above this rendered ElementView.
	 *
	 * @see DropEvent
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
	 * Removes this element out of it's container.
	 *
	 * @throws IllegalStateException If this element is not contained in any container. Use parent field to check.
	 */
	fun removeFromParent(): ElementView {
		val par = parent
		
		checkNotNull(par) { "This child has no parent." }
		
		return par.also { it.removeChild(this) }
	}
	
	internal abstract fun removeChild(child: ElementView)
	
	/**
	 * Method returning a contained child's coordinates within this ElementView if supported.
	 * This method has to be overridden.
	 * Returns null on all elementViews not supporting this feature.
	 *
	 * @param child Child to find
	 *
	 * @return Coordinate of given child in this ElementView or null if not supported
	 */
	@Suppress("FunctionOnlyReturningConstant")
	internal open fun getChildPosition(child: ElementView): Coordinate? = null
}