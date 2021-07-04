@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.visual.Visual

/**
 * A randomization [Animation] that shuffles between different visuals.
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