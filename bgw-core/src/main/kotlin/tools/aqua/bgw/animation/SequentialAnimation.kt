package tools.aqua.bgw.animation

/**
 * An [Animation] consisting of multiple [Animation]s that is played in sequence, when this [Animation] is played.
 *
 * @param animations The [Animation]s that this [SequentialAnimation] should contain.
 * Lowest index is played first, highest index is played last.
 */
data class SequentialAnimation(val animations: List<Animation>): Animation(animations.sumOf(Animation::duration))