@file:Suppress("unused")

package tools.aqua.bgw.core

/**
 * Used to define the scaling behaviour of the corresponding main BoardGameScene in current BoardGameApplication.
 *
 * @see tools.aqua.bgw.core.BoardGameScene
 * @see tools.aqua.bgw.core.BoardGameApplication
 */
enum class ScaleMode {
	/**
	 * Fully automatic scaling to window size.
	 */
	FULL,
	
	/**
	 * Scaling only shrinks scene when window gets to small.
	 */
	ONLY_SHRINK,
	
	/**
	 * Disables automatic rescaling.
	 */
	NO_SCALE
}