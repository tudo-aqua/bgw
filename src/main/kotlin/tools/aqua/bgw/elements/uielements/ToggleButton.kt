package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.BooleanProperty

/**
 * A ToggleButton may be used as a Button that is either selected or not selected.
 * An important feature of ToggleButtons is the ToggleGroup.
 * @see ToggleGroup
 *
 * @param height Height for this ToggleButton. Default: 0.
 * @param width Width for this ToggleButton. Default: 0.
 * @param posX Horizontal coordinate for this ToggleButton. Default: 0.
 * @param posY Vertical coordinate for this ToggleButton. Default: 0.
 * @param isSelected the initial state for this ToggleButton. Default: false.
 * @param toggleGroup the ToggleGroup of this ToggleButton. Default: null.
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
) { //TODO: maybe labeled?
	
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
	 * Property for the selected state of this ToggleButton. Changes are rendered directly by the framework.
	 */
	val selectedProperty: BooleanProperty = BooleanProperty(isSelected)
	
	/**
	 * Sets the selected state for this ToggleButton. Changes are rendered directly by the framework.
	 */
	var isSelected: Boolean
		get() = selectedProperty.value
		set(value) {
			selectedProperty.value = value
		}
	
	init {
		this.toggleGroup = toggleGroup
		selectedProperty.internalListener = { fire() }
	}
	
	private fun fire() {
		toggleGroup?.buttonSelectedStateChanged(this)
	}
}

