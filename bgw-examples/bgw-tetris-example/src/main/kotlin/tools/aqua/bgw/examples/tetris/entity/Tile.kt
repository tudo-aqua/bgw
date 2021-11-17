package tools.aqua.bgw.examples.tetris.entity

import tools.aqua.bgw.visual.ImageVisual

/**
 * Tile representation.
 *
 * @param color The Tile's color.
 */
data class Tile(val color : Color?) {
	/**
	 * Associated [ImageVisual].
	 */
	val imageVisual : ImageVisual? = color?.imageVisual?.copy()
}

/**
 * Enum for available colors.
 *
 * @param imageVisual Associated [ImageVisual].
 */
enum class Color(val imageVisual: ImageVisual) {
	GRAY(ImageVisual("gray.png")),
	BLUE(ImageVisual("blue.png")),
	CYAN(ImageVisual("cyan.png")),
	GREEN(ImageVisual("green.png")),
	ORANGE(ImageVisual("orange.png")),
	PURPLE(ImageVisual("purple.png")),
	RED(ImageVisual("red.png")),
	YELLOW(ImageVisual("yellow.png")),
}