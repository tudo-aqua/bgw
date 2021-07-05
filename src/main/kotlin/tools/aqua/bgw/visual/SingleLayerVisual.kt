@file:Suppress("unused")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.LimitedDoubleProperty

/**
 * Baseclass for single layer visuals.
 */
sealed class SingleLayerVisual : Visual() {
    /**
     * Property for the [transparency] / alpha channel for this [Visual].
     * Must be set between 0 (full transparent) and 1 (non-transparent / solid).
     * Default: 1.
     */
    val transparencyProperty: LimitedDoubleProperty = LimitedDoubleProperty(0, 1, 1)

    /**
     * The [transparency] / alpha channel for this [Visual].
     * Must be set between 0 (full transparent) and 1 (non-transparent / solid).
     * Default: 1.
     */
    var transparency: Double
        get() = transparencyProperty.value
        set(value) {
            transparencyProperty.value = value
        }
}