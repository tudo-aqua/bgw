package tools.aqua.bgw.animation

/**
 * An [Animation] consisting of multiple [Animation]s that is played in sequence,
 * when this [SequentialAnimation] is played.
 *
 * @constructor creates a new [SequentialAnimation].
 *
 * @param animations The [Animation]s that this [SequentialAnimation] should contain.
 * Lowest index is played first, highest index is played last.
 */
data class SequentialAnimation(val animations: List<Animation>) : Animation(animations.sumOf(Animation::duration)) {

	/**
	 * creates a new [SequentialAnimation]
	 * Additional constructor that enables the use of varargs for the animations.
	 *
	 * @param animation The [Animation]s that this [ParallelAnimation] should contain.
	 */
	constructor(vararg animation: Animation) : this(animation.toList())
}