package tools.aqua.bgw.core

/**
 * Class representing an aspect ratio between the window sides.
 *
 * @constructor Creates an aspect ratio out of width and height.
 * May be for example 1920 : 1080 as well as 16 : 9.
 *
 * @param width Width of ratio. Default: [DEFAULT_WINDOW_WIDTH].
 * @param height Height of ratio. Default: [DEFAULT_WINDOW_HEIGHT].
 */
class AspectRatio(width: Number = DEFAULT_WINDOW_WIDTH, height: Number = DEFAULT_WINDOW_HEIGHT) {
	/**
	 * Width of ratio.
	 */
	internal val width: Double = width.toDouble()
	
	/**
	 * Height of ratio.
	 */
	internal val height: Double = height.toDouble()
	
	/**
	 * Ratio as fraction.
	 */
	internal val ratio: Double = width.toDouble() / height.toDouble()
}