@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.Observable

/**
 * Visual baseclass.
 */
sealed class Visual : Observable() {
	/**
	 * Copies this [Visual] to a new object.
	 */
	abstract fun copy(): Visual
	
	companion object {
		/**
		 * An empty [Visual].
		 */
		val EMPTY: Visual = CompoundVisual()
	}
}