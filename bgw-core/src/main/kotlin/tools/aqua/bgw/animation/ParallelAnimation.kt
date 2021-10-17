package tools.aqua.bgw.animation

/**
 * An Animation consisting of multiple Animations that are played in parallel, when this [Animation] is played.
 *
 * @param animations the [Animation]s that this [ParallelAnimation] should contain.
 */
data class ParallelAnimation(val animations: List<Animation>): Animation(animations.maxOf(Animation::duration))