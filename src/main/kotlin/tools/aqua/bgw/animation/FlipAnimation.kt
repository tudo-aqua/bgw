@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView

/**
 * A flip animation.
 * Sets background to given fromVisual than Contracts background in half the given duration.
 * Then switches to new visual and extends again in half the given duration.
 *
 * @param elementView ElementView to animate
 * @param fromVisual Initial visual
 * @param toVisual Resulting visual
 * @param duration Animation duration in milliseconds. Default: 1 second
 */
class FlipAnimation<T : ElementView>(elementView: T, var fromVisual: Int, var toVisual: Int, duration: Int = 500) :
	ElementAnimation<T>(element = elementView, duration = duration)