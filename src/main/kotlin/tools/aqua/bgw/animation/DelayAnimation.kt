@file:Suppress("unused")

package tools.aqua.bgw.animation

/**
 * An asynchronous delay that fires onFinished after given duration.
 *
 * @param duration Animation duration in milliseconds. Default: 1 second
 */
class DelayAnimation(duration: Int = 1000) : Animation(duration = duration)