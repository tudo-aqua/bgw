@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.event.AnimationFinishedEvent

/**
 * [Animation] baseclass.
 *
 * @param duration duration in milliseconds. Default: 1 second.
 */
sealed class Animation(var duration: Int = 1000) {
    
    /**
     * [Boolean] indicating whether the [Animation] is currently playing.
     */
    var running: Boolean = false
    
    /**
     * Gets invoked when [Animation] has finished.
     */
    var onFinished: ((AnimationFinishedEvent) -> Unit)? = null
}

