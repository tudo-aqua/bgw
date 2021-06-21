package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.LimitedDoubleProperty

/**
 * Visual superclass.
 */
sealed class Visual {
	
	internal val transparencyProperty = LimitedDoubleProperty(0, 1, 1)
	
	/**
	 * Transparency / alpha channel for this property.
	 * Must be set between 0 (full transparent) and 1 (non-transparent).
	 * Default: 1
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