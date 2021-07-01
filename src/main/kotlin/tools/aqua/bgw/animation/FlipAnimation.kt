@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.Visual

/**
 * A flip animation.
 * Sets background to given [fromVisual] than contracts background in half the given duration, switches to [toVisual]
 * and extends again in half the given duration.
 *
 * @param elementView [ElementView] to animate.
 * @param fromVisual initial [Visual].
 * @param toVisual resulting [Visual].
 * @param duration duration in milliseconds. Default: 1 second.
 */
class FlipAnimation<T : ElementView>(
	elementView: T,
	var fromVisual: Visual,
	var toVisual: Visual,
	duration: Int = 500
) : ElementAnimation<T>(element = elementView, duration = duration)