package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.gameelements.DiceView

/**
 * A dice roll animation.
 *
 * @param dice DiceView to animate
 * @param toSide Resulting side after roll
 * @param duration Animation duration in milliseconds. Default: 1 second
 * @param speed Count of steps. Default: 50 steps
 */
class DiceAnimation<T : DiceView>(
	dice: T,
	val toSide: Int,
	duration: Int = 1000,
	val speed: Int = 50
) : ElementAnimation<T>(element = dice, duration = duration)