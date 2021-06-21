package tools.aqua.bgw.core

/**
 * Used to define the horizontal centering behaviour
 * of the corresponding main BoardGameScene in current BoardGameApplication.
 *
 * @param positionMultiplier Position multiplier
 * @param pivotMultiplier Pivot multiplier
 *
 * @see tools.aqua.bgw.core.BoardGameScene
 * @see tools.aqua.bgw.core.BoardGameApplication
 */
enum class HorizontalAlignment(val positionMultiplier: Double, val pivotMultiplier: Double) {
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