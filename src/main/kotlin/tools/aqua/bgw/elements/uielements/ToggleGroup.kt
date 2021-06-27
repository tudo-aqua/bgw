@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

/**
 * A ToggleGroup may be set as an attribute in ToggleButton or RadioButton.
 * @see ToggleButton
 * @see RadioButton
 *
 * All ToggleButtons that keep the same instance of a ToggleGroup belong to that ToggleGroup.
 * Only one ToggleButton may be selected in a ToggleGroup.
 * This means whenever a ToggleButton changes its selected state to true,
 * all other ToggleButtons in the same ToggleGroup get deselected.
 *
 * An exception to this rule is, whenever a new ToggleButton that is currently selected gets added to the ToggleGroup.
 */
class ToggleGroup {
	private val buttons: MutableList<ToggleButton> = mutableListOf()
	
	internal fun addButton(toggleButton: ToggleButton) {
		buttons.add(toggleButton)
	}
	
	internal fun removeButton(toggleButton: ToggleButton) {
		buttons.remove(toggleButton)
	}
	
	internal fun buttonSelectedStateChanged(toggleButton: ToggleButton) {
		if (toggleButton.isSelected)
			buttons.forEach { if (it != toggleButton) it.isSelected = false }
	}
}