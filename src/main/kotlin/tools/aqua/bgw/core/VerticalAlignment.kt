@file:Suppress("unused")

package tools.aqua.bgw.core

/**
 * Used to define vertical centering behaviour.
 *
 * @param positionMultiplier internal position multiplier.
 * @param pivotMultiplier internal pivot multiplier.
 *
 * @see tools.aqua.bgw.core.BoardGameScene
 * @see tools.aqua.bgw.core.BoardGameApplication
 */
enum class VerticalAlignment(internal val positionMultiplier: Double, internal val pivotMultiplier: Double) {
	/**
	 * [Alignment] on the top.
	 */
	TOP(0.0, -1.0),
	
	/**
	 * [Alignment] centered vertically.
	 */
	CENTER(0.5, 0.0),
	
	/**
	 * [Alignment] on the bottom.
	 */
	BOTTOM(1.0, 1.0)
}

