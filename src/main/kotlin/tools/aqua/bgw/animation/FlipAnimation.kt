@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView

/**
 * A flip animation.
 * Sets background to given [fromVisual] than contracts background in half the given duration, switches to [toVisual]
 * and extends again in half the given duration.
 *
 * @param elementView [ElementView] to animate.
 * @param fromVisual initial visual.
 * @param toVisual resulting visual.
 * @param duration duration in milliseconds. Default: 1 second.
 */
class FlipAnimation<T : ElementView>(elementView: T, var fromVisual: Int, var toVisual: Int, duration: Int = 500) :
	ElementAnimation<T>(element = elementView, duration = duration)