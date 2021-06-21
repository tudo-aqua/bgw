package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList
import tools.aqua.bgw.util.Font

/**
 * A standard ComboBox with Items of given generic type.
 * toString() method will be used to display items.
 */
open class ComboBox<T>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	@Suppress("UNUSED_PARAMETER") prompt: String = "",
	font: Font = Font(),
	items: List<T> = listOf(),
	selectedItem: T? = null
) : UIElementView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	font = font
) { //TODO: Selected Item not working
	
	/**
	 * Property for the items in the ComboBox.
	 */
	val observableItemsList: ObservableList<T> = ObservableArrayList()
	
	/**
	 * Items in the ComboBox.
	 */
	var items: MutableList<T>
		get() = observableItemsList.list
		set(value) {
			observableItemsList.clear()
			observableItemsList.addAll(value)
		}
	
	/**
	 * Property for the selected item.
	 * Value may be null if no item is selected.
	 */
	val selectedItemProperty: ObjectProperty<T?> = ObjectProperty(null)
	
	/**
	 * The currently selected item.
	 * May be null if no item is selected.
	 */
	var selectedItem: T?
		get() = selectedItemProperty.value
		set(value) {
			require(items.contains(value)) { "Items list does not contain element to select: $value" }
			
			selectedItemProperty.value = value
		}
	
	init {
		observableItemsList.addAll(items)
		
		if (selectedItem != null) {
			require(items.contains(selectedItem)) { "Items list does not contain element to select." }
			
			selectedItemProperty.value = selectedItem
		}
	}
}