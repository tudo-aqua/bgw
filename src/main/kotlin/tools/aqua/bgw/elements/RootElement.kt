@file:Suppress("unused")

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.Scene

/**
 * The root element in the view hierarchy of a scene.
 *
 * @param scene scene of this root element.
 */
internal class RootElement<T : ElementView>(val scene: Scene<T>) : ElementView() {
	
	/**
	 * Removes element from the [scene].
	 *
	 * @param child child to be removed.
	 *
	 * @throws RuntimeException if the child's type is incompatible with scene's type.
	 */
	override fun removeChild(child: ElementView) {
		try {
			@Suppress("UNCHECKED_CAST")
			this.scene.removeElements(child as T)
		} catch (_: ClassCastException) {
			throw RuntimeException("$child type is incompatible with scene's type.")
		}
	}
}