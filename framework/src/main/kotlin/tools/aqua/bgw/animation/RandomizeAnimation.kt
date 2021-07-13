@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.visual.Visual

/**
 * A randomization [Animation] that shuffles between different visuals.
 * Shuffles through visuals in the given [visuals] [List] for given [duration] and shows [toVisual] in the end.
 * Use the [speed] parameter to define how many steps the animation should have.
 * For example:
 * An animation with [duration] = 1s and [speed] = 50 will change the visual 50 times within the [duration] of one second.
 *
 * @param element [GameElementView] to animate.
 * @param visuals [List] of [Visual]s to shuffle through.
 * @param toVisual resulting [Visual] after shuffle.
 * @param duration duration in milliseconds. Default: 1 second.
 * @param speed count of steps. Default: 50 steps.
 */
class RandomizeAnimation<T : GameElementView>(
	element: T,
	val visuals: List<Visual>,
	val toVisual: Visual,
	duration: Int = 1000,
	val speed: Int = 50
) : ElementAnimation<T>(element = element, duration = duration)