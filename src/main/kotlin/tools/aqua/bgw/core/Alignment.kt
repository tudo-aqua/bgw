@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.core.HorizontalAlignment.LEFT
import tools.aqua.bgw.core.HorizontalAlignment.RIGHT
import tools.aqua.bgw.core.VerticalAlignment.BOTTOM
import tools.aqua.bgw.core.VerticalAlignment.TOP

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
	 * [Alignment] in the top left corner.
	 */
	TOP_LEFT(TOP, LEFT),
	
	/**
	 * [Alignment] in the top right corner.
	 */
	TOP_RIGHT(TOP, RIGHT),
	
	/**
	 * [Alignment] on the top centered horizontally.
	 */
	TOP_CENTER(TOP, HorizontalAlignment.CENTER),
	
	/**
	 * [Alignment] in the bottom left corner.
	 */
	BOTTOM_LEFT(BOTTOM, LEFT),
	
	/**
	 * [Alignment] in the bottom right corner.
	 */
	BOTTOM_RIGHT(BOTTOM, RIGHT),
	
	/**
	 * [Alignment] on the bottom centered horizontally.
	 */
	BOTTOM_CENTER(BOTTOM, HorizontalAlignment.CENTER),
	
	/**
	 * [Alignment] on the left centered vertically.
	 */
	CENTER_LEFT(VerticalAlignment.CENTER, LEFT),
	
	/**
	 * [Alignment] on the right centered vertically.
	 */
	CENTER_RIGHT(VerticalAlignment.CENTER, RIGHT),
	
	/**
	 * [Alignment] centered horizontally and vertically.
	 */
	CENTER(VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
	
	companion object {
		fun of(v: VerticalAlignment, h: HorizontalAlignment): Alignment = when {
			v == TOP && h == LEFT -> TOP_LEFT
			v == TOP && h == RIGHT -> TOP_RIGHT
			v == TOP && h == HorizontalAlignment.CENTER -> TOP_CENTER
			
			v == BOTTOM && h == LEFT -> BOTTOM_LEFT
			v == BOTTOM && h == RIGHT -> BOTTOM_RIGHT
			v == BOTTOM && h == HorizontalAlignment.CENTER -> BOTTOM_CENTER
			
			v == VerticalAlignment.CENTER && h == LEFT -> CENTER_LEFT
			v == VerticalAlignment.CENTER && h == RIGHT -> CENTER_RIGHT
			v == VerticalAlignment.CENTER && h == HorizontalAlignment.CENTER -> CENTER
			
			else -> throw UnsupportedOperationException()
		}
	}
}