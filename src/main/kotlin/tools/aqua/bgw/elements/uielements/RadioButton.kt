@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

/**
 * [RadioButton] is a subclass of [ToggleButton] with a different visual representation.
 * A [RadioButton] may be used as a [Button] that is either selected or not selected.
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