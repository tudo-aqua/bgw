@file:Suppress("unused")

package tools.aqua.bgw.core

/**
 * Used to define horizontal centering behaviour.
 *
 * @param positionMultiplier internal position multiplier.
 * @param pivotMultiplier internal pivot multiplier.
 *
 * @see tools.aqua.bgw.core.BoardGameScene
 * @see tools.aqua.bgw.core.BoardGameApplication
 */
enum class HorizontalAlignment(internal val positionMultiplier: Double, internal val pivotMultiplier: Double) {
	/**
	 * Alignment on the left.
	 */
	LEFT(0.0, -1.0),
	
	/**
	 * Alignment centered horizontally.
	 */
	CENTER(0.5, 0.0),
	
	/**
	 * Alignment on the right.
	 */
	RIGHT(1.0, 1.0)
}