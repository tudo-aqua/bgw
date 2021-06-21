package tools.aqua.bgw.util

import java.awt.Color

class Font(
	val size: Number = 14,
	val color: Color = Color.BLACK,
	val family: String = "Arial",
	val fontStyle: FontStyle = FontStyle.NORMAL
)

enum class FontStyle {
	BOLD,
	
	NORMAL,
	
	SEMI_BOLD,
	
	Italic,
}