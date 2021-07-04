@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList

/**
 * A compound visual containing stacked [SingleLayerVisual]s.
 * Hint: Each [SingleLayerVisual] besides the bottom should have opacity in order to display all layers properly.
 *
 * @param children children [SingleLayerVisual]s in the order they should be displayed, where the first [SingleLayerVisual]
 * gets displayed at the bottom of the stack.
 */
class CompoundVisual(vararg children: SingleLayerVisual) : Visual() {
	
	/**
	 * [ObservableList] for the [children] of this stack.
	 * The first [SingleLayerVisual] gets displayed at the bottom of the stack.
	 */
	val childrenProperty: ObservableArrayList<SingleLayerVisual> = ObservableArrayList(children.toList())
	
	/**
	 * The [children] of this stack.
	 * The first [SingleLayerVisual] gets displayed at the bottom of the stack.
	 */
	var children: List<SingleLayerVisual>
		get() = childrenProperty.toList()
		set(value) {
			childrenProperty.clear()
			childrenProperty.addAll(value)
		}

	init {
	    childrenProperty.internalListener = { notifyGUIListener() }
	}
}