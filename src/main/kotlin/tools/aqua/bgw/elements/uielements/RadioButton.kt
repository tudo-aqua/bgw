@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

/**
 * A RadioButton may be used as a [Button] that is either selected or not selected.
 * An important feature of [ToggleButton]s is the [ToggleGroup].
 * A [RadioButton] has the same functionality as a [ToggleButton] but a different visual representation.
 * @see ToggleButton
 * @see ToggleGroup
 *
 * @param height height for this [RadioButton]. Default: 0.
 * @param width width for this [RadioButton]. Default: 0.
 * @param posX horizontal coordinate for this [RadioButton]. Default: 0.
 * @param posY vertical coordinate for this [RadioButton]. Default: 0.
 * @param isSelected the initial state for this [RadioButton]. Default: false.
 * @param toggleGroup the ToggleGroup of this [RadioButton]. Default: null.
 */
open class RadioButton(
	height: Number = 0,
	width: Number = 0,
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
)