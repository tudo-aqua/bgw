@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.event.AnimationFinishedEvent
import tools.aqua.bgw.event.EventHandler

/**
 * Animation superclass.
 *
 * @param duration duration in milliseconds. Default: 1 second.
 */
sealed class Animation(var duration: Int = 1000) {
    
    /**
     * Boolean indicating whether the animation is currently playing.
     */
    var running: Boolean = false
    
    /**
     * [EventHandler] that gets invoked when animation has finished.
     */
    var onFinished: EventHandler<AnimationFinishedEvent>? = null
}

