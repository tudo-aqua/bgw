package tools.aqua.bgw.elements.uielements

/**
 * A RadioButton may be used as a Button that is either selected or not selected.
 * An important feature of ToggleButtons is the ToggleGroup.
 * A RadioButton has the same functionality as a ToggleButton but a different visual representation.
 * @see ToggleButton
 * @see ToggleGroup
 *
 * @param height Height for this ToggleButton. Default: 0.
 * @param width Width for this ToggleButton. Default: 0.
 * @param posX Horizontal coordinate for this ToggleButton. Default: 0.
 * @param posY Vertical coordinate for this ToggleButton. Default: 0.
 * @param isSelected the initial state for this ToggleButton. Default: false.
 * @param toggleGroup the ToggleGroup of this ToggleButton. Default: null.
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