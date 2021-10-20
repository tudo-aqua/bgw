package tools.aqua.bgw.animation

/**
 * An [Animation] consisting of multiple Animations that are played in parallel,
 * when this [ParallelAnimation] is played.
 *
 * @constructor creates a new [ParallelAnimation].
 *
 * @param animations The [Animation]s that this [ParallelAnimation] should contain.
 */
data class ParallelAnimation(val animations: List<Animation>): Animation(animations.maxOf(Animation::duration)) {

	/**
	 * Creates a new [ParallelAnimation].
	 * Additional constructor that enables the use of varargs for the animations.
	 *
	 * @param animation The [Animation]s that this [ParallelAnimation] should contain.
	 */
	constructor(vararg animation: Animation) : this(animation.toList())
}