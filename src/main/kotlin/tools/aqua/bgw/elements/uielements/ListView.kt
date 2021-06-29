@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Font

/**
 * A list view displaying its items next to each other in the given orientation.
 * The formatFunction is used to gain a String representation of each item.
 * If no formatFunction is specified the toString function gets used instead.
 *
 * @param height height for this ListView. Default: 0.
 * @param width width for this ListView. Default: 0.
 * @param posX horizontal coordinate for this ListView. Default: 0.
 * @param posY vertical coordinate for this ListView. Default: 0.
 * @param items initial list of items for this ListView. Default: empty list.
 * @param font font to be used for this ListView. Default: default Font constructor.
 * @param orientation orientation for this ListView. Default: VERTICAL.
 * @param formatFunction the formatFunction that is used to represent the items. Default: null.
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
	 * Property for the items list for this ListView.
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items list for this ListView.
	 * @see observableItemsList
	 */
	var items: MutableList<T>
		get() = observableItemsList.list
		set(value) {
			observableItemsList.clear()
			observableItemsList.addAll(value)
		}
	
	/**
	 * Property for the orientation of this ListView.
	 */
	val orientationProperty: ObjectProperty<Orientation> = ObjectProperty(orientation)
	
	/**
	 * Orientation of this ListView displayed.
	 * @see orientationProperty
	 */
	var orientation: Orientation
		get() = orientationProperty.value
		set(value) {
			orientationProperty.value = value
		}

	/**
	 * Property for the formatFunction that gets used to obtain a String representation for each item.
	 * If the value is null, the toString function of the item is used instead.
	 */
	var formatFunctionProperty: ObjectProperty<((T) -> String)?> = ObjectProperty(formatFunction)

	/**
	 * The formatFunction that gets used to obtain a String representation for each item.
	 * If the value is null, the toString function of the item is used instead.
	 * @see formatFunctionProperty
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