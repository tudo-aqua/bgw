@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.BooleanProperty

/**
 * A [ToggleButton] may be used as a [Button] that is either selected or not selected.
 * An important feature of [ToggleButton]s is the [ToggleGroup].
 *
 * [ToggleGroup]s can be used to to group [ToggleButton]s.
 *
 * All [ToggleButton]s that keep the same instance of a [ToggleGroup] belong to that [ToggleGroup].
 * Only one [ToggleButton] may be selected in a [ToggleGroup].
 * This means whenever a [ToggleButton] changes its selected state to true,
 * all other [ToggleButton]s in the same [ToggleGroup] get deselected.
 *
 * An exception to this rule is, whenever a new [ToggleButton] that is currently selected gets added to the ToggleGroup.
 *
 * @see ToggleGroup
 *
 * @param height height for this [ToggleButton]. Default: 0.
 * @param width width for this [ToggleButton]. Default: 0.
 * @param posX horizontal coordinate for this [ToggleButton]. Default: 0.
 * @param posY vertical coordinate for this [ToggleButton]. Default: 0.
 * @param isSelected the initial state for this [ToggleButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [ToggleButton]. Default: null.
 */
open class ToggleButton(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	isSelected: Boolean = false,
	toggleGroup: ToggleGroup? = null,
) : UIElementView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
) {
	
	/**
	 * The ToggleGroup of this ToggleButton.
	 * @see ToggleGroup
	 */
	var toggleGroup: ToggleGroup? = null
		set(value) {
			toggleGroup?.removeButton(this)
			value?.addButton(this)
			field = value
		}
	
	/**
	 * Property for the selected state of this ToggleButton.
	 */
	val selectedProperty: BooleanProperty = BooleanProperty(isSelected)
	
	/**
	 * Selected state for this ToggleButton.
	 * @see selectedProperty
	 */
	var isSelected: Boolean
		get() = selectedProperty.value
		set(value) {
			selectedProperty.value = value
		}
	
	init {
		this.toggleGroup = toggleGroup
		selectedProperty.internalListener = { _, _ -> fire() }
	}
	
	private fun fire() {
		toggleGroup?.buttonSelectedStateChanged(this)
	}
}

