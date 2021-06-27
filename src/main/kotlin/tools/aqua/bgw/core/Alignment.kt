@file:Suppress("unused")

package tools.aqua.bgw.core

/**
 * Used to define a centering behaviour.
 * Encapsulates [VerticalAlignment] and [HorizontalAlignment].
 *
 * @param verticalAlignment vertical alignment component.
 * @param horizontalAlignment horizontal alignment component.
 *
 * @see tools.aqua.bgw.core.BoardGameScene
 * @see tools.aqua.bgw.core.BoardGameApplication
 * @see tools.aqua.bgw.core.VerticalAlignment
 * @see tools.aqua.bgw.core.HorizontalAlignment
 */
enum class Alignment(val verticalAlignment: VerticalAlignment, val horizontalAlignment: HorizontalAlignment) {
	/**
	 * Alignment in the top left corner.
	 */
	TOP_LEFT(VerticalAlignment.TOP, HorizontalAlignment.LEFT),
	
	/**
	 * Alignment in the top right corner.
	 */
	TOP_RIGHT(VerticalAlignment.TOP, HorizontalAlignment.RIGHT),
	
	/**
	 * Alignment in the bottom left corner.
	 */
	BOTTOM_LEFT(VerticalAlignment.BOTTOM, HorizontalAlignment.LEFT),
	
	/**
	 * Alignment in the bottom right corner.
	 */
	BOTTOM_RIGHT(VerticalAlignment.BOTTOM, HorizontalAlignment.RIGHT),
	
	/**
	 * Alignment on the top centered horizontally.
	 */
	TOP_CENTER(VerticalAlignment.TOP, HorizontalAlignment.CENTER),
	
	/**
	 * Alignment on the bottom centered horizontally.
	 */
	BOTTOM_CENTER(VerticalAlignment.BOTTOM, HorizontalAlignment.CENTER),
	
	/**
	 * Alignment on the left centered vertically.
	 */
	LEFT_CENTER(VerticalAlignment.CENTER, HorizontalAlignment.LEFT),
	
	/**
	 * Alignment on the right centered vertically.
	 */
	RIGHT_CENTER(VerticalAlignment.CENTER, HorizontalAlignment.RIGHT),
	
	/**
	 * Alignment centered horizontally and vertically.
	 */
	CENTER(VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
}