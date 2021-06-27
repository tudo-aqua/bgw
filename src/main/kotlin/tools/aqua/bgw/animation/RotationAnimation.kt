@file:Suppress("unused", "unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.elements.ElementView

/**
 * A rotation animation.
 * Rotates given ElementView to given angle.
 *
 * @param elementView GameElementView to animate
 * @param fromAngle Initial angle. Default: current angle
 * @param toAngle Resulting angle. Default: current angle
 * @param duration Animation duration in milliseconds. Default: 1 second
 */
class RotationAnimation<T : ElementView>(
	elementView: T,
	var fromAngle: Double = elementView.rotation,
	var toAngle: Double = elementView.rotation,
	duration: Int = 1000
) : ElementAnimation<T>(element = elementView, duration = duration) {
	
	/**
	 * A rotation animation.
	 * Rotates given ElementView to given angle.
	 *
	 * @param elementView GameElementView to animate
	 * @param byAngle Relative angle. Default: 0
	 * @param duration Animation duration in milliseconds. Default: 1 second
	 */
	constructor(elementView: T, byAngle: Double = 0.0, duration: Int = 1000) : this(
		elementView = elementView,
		toAngle = elementView.rotation + byAngle,
		duration = duration
	)
}