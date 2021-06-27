@file:Suppress("unused")

package tools.aqua.bgw.core

import javafx.scene.layout.StackPane
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.builder.DragElementObject
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.RootElement
import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.CoordinatePlain
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * Superclass for BGW scenes.
 *
 * @param width Scene width in virtual coordinates
 * @param height Scene height in virtual coordinates
 */
sealed class Scene<T : ElementView>(width: Number, height: Number) {
	
	//TODO: Docs
	internal val draggedElementObjectProperty: ObjectProperty<DragElementObject?> = ObjectProperty(null)
	
	//TODO: Docs
	val draggedElement: DynamicView?
		get() = draggedElementObjectProperty.value?.draggedElement
	
	
	/**
	 * The root node of this Scene.
	 * Use it to compare the parent property of any ElementView to find out whether it wa directly added to the scene.
	 */
	val rootNode: ElementView = RootElement(this)
	
	/**
	 * The width of this scene in virtual coordinates.
	 */
	val width: Double = width.toDouble()
	
	/**
	 * The height of this scene in virtual coordinates.
	 */
	val height: Double = height.toDouble()
	
	/**
	 * All elements on the root node.
	 */
	internal val rootElements: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Property for the background visual of this scene.
	 */
	internal val backgroundProperty: ObjectProperty<Visual> =
		ObjectProperty(ColorVisual(Color(255, 255, 255))) //TODO: Can be private = BUG?
	
	/**
	 * The background visual of this scene.
	 */
	var background: Visual
		get() = backgroundProperty.value
		set(value) {
			backgroundProperty.value = value
		}
	
	/**
	 * Property for the opacity of the background of this scene.
	 */
	internal val opacityProperty = DoubleProperty(1.0)
	
	/**
	 * Opacity of the background of this scene.
	 */
	var opacity: Double
		get() = opacityProperty.value
		set(value) {
			require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }
			
			opacityProperty.value = value
		}
	
	/**
	 * Property for the currently displayed zoom detail of this scene.
	 */
	internal val zoomDetailProperty = ObjectProperty(CoordinatePlain(0, 0, width, height))
	
	/**
	 * The currently displayed zoom detail of this scene.
	 */
	internal var zoomDetail
		get() = zoomDetailProperty.value
		set(value) {
			zoomDetailProperty.value = value
		}
	
	/**
	 * Map for all elements to their stackPanes.
	 */
	internal val elementsMap: MutableMap<ElementView, StackPane> = HashMap()
	
	/**
	 * All animations currently playing.
	 */
	internal val animations: ObservableList<Animation> = ObservableArrayList()
	
	/**
	 * Adds all given elements to the root node and rootElements list.
	 *
	 * @param elements Elements to add
	 */
	fun addElements(vararg elements: T) {
		rootElements.addAll(elements.toList().onEach {
			check(it.parent == null) { "Element $it is already contained in another container." }
			it.parent = rootNode
		})
	}
	
	/**
	 * Removes all given elements from the root node and rootElements list.
	 *
	 * @param elements Elements to remove
	 */
	fun removeElements(vararg elements: T) {
		rootElements.removeAll(elements.toList().onEach {
			it.parent = null
		})
	}
	
	/**
	 * Removes all elements from the root node and rootElements list.
	 */
	fun clearElements() {
		rootElements.forEach { it.parent = null }
		rootElements.clear()
	}
	
	/**
	 * Plays given animation.
	 *
	 * @param animation Animation to play
	 */
	fun playAnimation(animation: Animation) {
		animations.add(animation)
	}
	
	/**
	 * Zooms scene to given bounds.
	 *
	 * @param fromX Left bound
	 * @param fromY Top bound
	 * @param toX Right bound
	 * @param toY Bottom bound
	 */
	fun zoomTo(fromX: Number, fromY: Number, toX: Number, toY: Number): Unit =
		zoomTo(fromX.toDouble(), fromY.toDouble(), toX.toDouble(), toY.toDouble())
	
	/**
	 * Zooms scene to given bounds.
	 *
	 * @param from Top left coordinate
	 * @param to Bottom right coordinate
	 */
	fun zoomTo(from: Coordinate, to: Coordinate): Unit =
		zoomTo(from.xCoord, from.yCoord, to.xCoord, to.yCoord)
	
	/**
	 * Zooms scene to given bounds.
	 *
	 * @param to Layout bounds
	 */
	fun zoomTo(to: CoordinatePlain): Unit =
		zoomTo(to.topLeft, to.bottomRight)
	
	/**
	 * Zooms scene out to max bounds.
	 */
	fun zoomOut() {
		zoomDetail = CoordinatePlain(0, 0, width, height)
	}
	
	/**
	 * Sets zoom detail property to given bounds.
	 * Checks for targets out of layout bounds.
	 */
	private fun zoomTo(fromX: Double, fromY: Double, toX: Double, toY: Double) {
		when {
			fromX < 0 || fromY < 0 || toX < 0 || toY < 0 ->
				throw IllegalArgumentException("All bounds must be positive.")
			fromX >= toX ->
				throw IllegalArgumentException("Right X bound is not greater than left X bound.")
			fromY >= toY ->
				throw IllegalArgumentException("Bottom Y bound is not greater than top Y bound.")
			toX > width ->
				throw IllegalArgumentException("Right X bound is greater than scene width.")
			toY > height ->
				throw IllegalArgumentException("Bottom Y bound is greater than scene height.")
		}
		
		zoomDetail = CoordinatePlain(fromX, fromY, toX, toY)
	}
	
	/**
	 * Searches node recursively through the visual tree and logs path
	 * where the node appears as first element and the root node as last.
	 *
	 * @param elementView Child to find.
	 *
	 * @return Path to child,.
	 *
	 * @throws IllegalStateException If child was not in contained in this scene.
	 */
	//TODO: How to prevent circles i.e. StackOverflow in recursion
	fun findPathToChild(elementView: ElementView): List<ElementView> {
		if (elementView is RootElement<*>) {
			check(elementView == rootNode) { "Child is contained in another scene" }
			return listOf(rootNode)
		}
		
		checkNotNull(elementView.parent) { "Encountered element $elementView that is not contained in a scene." }
		
		return mutableListOf(elementView) + findPathToChild(elementView.parent!!)
	}
	
	@Suppress("UNCHECKED_CAST")
	internal fun removeChild(child: ElementView) {
		try {
			this.removeElements(child as T)
		} catch (_: ClassCastException) {
		}
	}
	
	/*
	rootElements.flatMap { it.findPathToChildRecursively(elementView) }
		.ifEmpty { throw NoSuchElementException("Child not found in visual tree.") }

/**
 * Searches node recursively through the visual tree and logs path.
 *
 * @param elementView Child to find
 *
 * @return Path to child
 */
private fun ElementView.findPathToChildRecursively(elementView: ElementView): List<ElementView> {
	if (this == elementView)
		return mutableListOf(elementView)
	
	val path = when (this) {
		is GameElementContainerView<*> ->
			observableElements.flatMap { it.findPathToChildRecursively(elementView) }.toMutableList()
		
		is GridLayoutView<*> ->
			grid.mapNotNull { it.third }.flatMap { it.findPathToChildRecursively(elementView) }.toMutableList()
		
		else ->
			mutableListOf()
	}.ifEmpty { return listOf() }
	
	return path.apply { add(this@findPathToChildRecursively) }
}*/
}


