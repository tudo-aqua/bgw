package tools.aqua.bgw.examples.tetris.entity

import tools.aqua.bgw.visual.ImageVisual

data class Tile(val color : Color?) {
	val imageVisual : ImageVisual? = color?.imageVisual?.copy()
}

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