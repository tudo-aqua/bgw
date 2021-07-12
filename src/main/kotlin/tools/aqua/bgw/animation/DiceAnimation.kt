@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.gameelements.DiceView

/**
 * A dice roll [Animation].
 * Shuffles through dice visuals for given [duration] and shows [toSide] in the end. Use the [speed] parameter
 * to define how many steps the animation should have.
 * For example:
 * An animation with [duration] = 1s and [speed] = 50 will change the visual 50 times within the [duration] of one
 * second.
 *
 * @param dice [DiceView] to animate.
 * @param toSide resulting side after roll.
 * @param duration duration in milliseconds. Default: 1 second.
 * @param speed count of steps. Default: 50 steps.
 */
class DiceAnimation<T : DiceView>(
	dice: T,
	val toSide: Int,
	duration: Int = 1000,
	val speed: Int = 50
) : ElementAnimation<T>(element = dice, duration = duration)