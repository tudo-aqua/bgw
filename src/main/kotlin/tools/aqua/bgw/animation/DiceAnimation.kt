@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.gameelements.DiceView

/**
 * A dice roll [Animation].
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