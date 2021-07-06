@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.util.Font

/**
 * A [ToggleButton] may be used as a [Button] that is either selected or not selected.
 * An important feature of [ToggleButton]s is the [ToggleGroup].
 * @see ToggleGroup
 *
 * @param height height for this [ToggleButton]. Default: [ToggleButton.DEFAULT_TOGGLE_BUTTON_HEIGHT].
 * @param width width for this [ToggleButton]. Default: [ToggleButton.DEFAULT_TOGGLE_BUTTON_WIDTH].
 * @param posX horizontal coordinate for this [ToggleButton]. Default: 0.
 * @param posY vertical coordinate for this [ToggleButton]. Default: 0.
 * @param font font to be used for this [ToggleButton]. Default: default [Font] constructor.
 * @param isSelected the initial state for this [ToggleButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [ToggleButton]. Default: null.
 */
open class ToggleButton(
	height: Number = DEFAULT_TOGGLE_BUTTON_HEIGHT,
	width: Number = DEFAULT_TOGGLE_BUTTON_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	font: Font = Font(),
	isSelected: Boolean = false,
	toggleGroup: ToggleGroup? = null
) : UIElementView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	font = font
) {
	
	/**
	 * The ToggleGroup of this ToggleButton.
	 * @see ToggleGroup
	 */
	var toggleGroup: ToggleGroup? = toggleGroup
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
		selectedProperty.internalListener = { _, _ -> toggleGroup?.buttonSelectedStateChanged(this) }
	}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ToggleButton].
	 */
	companion object {
		/**
		 * Suggested [ToggleButton] [height].
		 */
		const val DEFAULT_TOGGLE_BUTTON_HEIGHT: Int = 25
		
		/**
		 * Suggested [ToggleButton] [width].
		 */
		const val DEFAULT_TOGGLE_BUTTON_WIDTH: Int = 100
	}
}

