@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView

/**
 * ElementAnimation superclass.
 *
 * @param element ElementView to animate
 * @param duration Animation duration in milliseconds. Default: 1 second
 */
sealed class ElementAnimation<T : ElementView>(val element: T, duration: Int = 1000) : Animation(duration = duration)