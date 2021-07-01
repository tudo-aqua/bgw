@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.LimitedDoubleProperty

/**
 * Visual baseclass.
 */
sealed class Visual {
	
	/**
	 * Property for the transparency / alpha channel for this visual.
	 * Must be set between 0 (full transparent) and 1 (non-transparent / solid).
	 * Default: 1.
	 */
	val transparencyProperty: LimitedDoubleProperty = LimitedDoubleProperty(0, 1, 1)
	
	/**
	 * Transparency / alpha channel for this visual.
	 * Must be set between 0 (full transparent) and 1 (non-transparent / solid).
	 * Default: 1.
	 */
	var transparency: Double
		get() = transparencyProperty.value
		set(value) {
			transparencyProperty.value = value
		}
	
	companion object {
		/**
		 * An empty Visual.
		 */
		val EMPTY: Visual = CompoundVisual()
	}
}