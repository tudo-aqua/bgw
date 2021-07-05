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
open class CompoundVisual(children: List<SingleLayerVisual>) : Visual() {
	
	/**
	 * [ObservableList] for the [children] of this stack.
	 * The first [SingleLayerVisual] gets displayed at the bottom of the stack.
	 */
	val childrenProperty: ObservableArrayList<SingleLayerVisual> = ObservableArrayList(children)
	
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
	
	/**
	 * [CompoundVisual] constructor with vararg parameter.
	 *
	 * @param children children [SingleLayerVisual]s in the order they should be displayed, where the first [SingleLayerVisual]
	 * gets displayed at the bottom of the stack.
	 */
	constructor(vararg children: SingleLayerVisual) : this(children.toList())
	
	init {
		childrenProperty.internalListener = { notifyGUIListener() }
	}
	
	/**
	 * Copies this [CompoundVisual] to a new object recursively including children.
	 */
	override fun copy(): CompoundVisual = CompoundVisual(children.map { it.copy() as SingleLayerVisual }.toList()).apply {
		transparency = this@CompoundVisual.transparency
	}
}