@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView

/**
 * [ElementAnimation] baseclass.
 *
 * @param element [ElementView] to animate.
 * @param duration duration in milliseconds.
 */
sealed class ElementAnimation<T : ElementView>(val element: T, duration: Int) : Animation(duration = duration)