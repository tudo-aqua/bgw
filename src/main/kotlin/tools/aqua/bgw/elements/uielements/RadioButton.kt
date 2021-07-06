@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

/**
 * A [RadioButton] may be used as a [Button] that is either selected or not selected.
 * A [RadioButton] has the same functionality as a [ToggleButton] but a different visual representation.
 * An important feature of [ToggleButton]s is the [ToggleGroup].
 * @see ToggleButton
 * @see ToggleGroup
 *
 * @param height height for this [RadioButton]. Default: [RadioButton.DEFAULT_RADIOBUTTON_HEIGHT].
 * @param width width for this [RadioButton]. Default: [RadioButton.DEFAULT_RADIOBUTTON_WIDTH].
 * @param posX horizontal coordinate for this [RadioButton]. Default: 0.
 * @param posY vertical coordinate for this [RadioButton]. Default: 0.
 * @param isSelected the initial state for this [RadioButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [RadioButton]. Default: null.
 */
open class RadioButton(
	height: Number = DEFAULT_RADIOBUTTON_HEIGHT,
	width: Number = DEFAULT_RADIOBUTTON_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	isSelected: Boolean = false,
	toggleGroup: ToggleGroup? = null,
) : ToggleButton(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	isSelected = isSelected,
	toggleGroup = toggleGroup
) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [RadioButton].
	 */
	companion object {
		/**
		 * Suggested [RadioButton] [height].
		 */
		const val DEFAULT_RADIOBUTTON_HEIGHT: Int = 20
		
		/**
		 * Suggested [RadioButton] [width].
		 */
		const val DEFAULT_RADIOBUTTON_WIDTH: Int = 100
	}
}