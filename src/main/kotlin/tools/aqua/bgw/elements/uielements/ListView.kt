@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Font

/**
 * A list view displaying its children next to each other in the given orientation.
 *
 * @param height Height for this ListView. Default: 0.
 * @param width Width for this ListView. Default: 0.
 * @param posX Horizontal coordinate for this ListView. Default: 0.
 * @param posY Vertical coordinate for this ListView. Default: 0.
 * @param items Items displayed as children in this ListView.
 * @param orientation Orientation in which the children get displayed in this ListView.
 * @param formatFunction toString function for the children.
 */
open class ListView<T>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	items: List<T> = listOf(),
	font: Font = Font(), //TODO: Unused?
	orientation: Orientation = Orientation.VERTICAL,
	formatFunction: ((T) -> String)? = null
) : UIElementView(height = height, width = width, posX = posX, posY = posY) {
	
	/**
	 * Property for the items displayed as children in this ListView.
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items displayed as children in this ListView.
	 */
	var items: MutableList<T>
		get() = observableItemsList.list
		set(value) {
			observableItemsList.clear()
			observableItemsList.addAll(value)
		}
	
	/**
	 * Property for the orientation in which the children get displayed.
	 */
	val orientationProperty: ObjectProperty<Orientation> = ObjectProperty(orientation)
	
	/**
	 * Orientation in which the children get displayed.
	 */
	var orientation: Orientation
		get() = orientationProperty.value
		set(value) {
			orientationProperty.value = value
		}
	
	/**
	 * Property for the toString function for the children.
	 */
	var formatFunctionProperty: ObjectProperty<((T) -> String)?> = ObjectProperty(formatFunction)
	
	/**
	 * toString function for the children.
	 */
	var formatFunction: ((T) -> String)?
		get() = formatFunctionProperty.value
		set(value) {
			formatFunctionProperty.value = value
		}
	
	init {
		observableItemsList.addAll(items)
	}
}