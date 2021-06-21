package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObservableArrayList

/**
 * A compound visual containing stacked single layer visuals.
 * Each single layer visual should have opacity in order to work properly.
 *
 * @param children Children
 */
class CompoundVisual(vararg children: SingleLayerVisual) : Visual() {
	/**
	 * The property for the children of this stack.
	 */
	val childrenProperty = ObservableArrayList(children.toList())
	
	/**
	 * The children of this stack.
	 */
	var children: List<SingleLayerVisual>
		get() = childrenProperty.toList()
		set(value) {
			childrenProperty.clear()
			childrenProperty.addAll(value)
		}
}