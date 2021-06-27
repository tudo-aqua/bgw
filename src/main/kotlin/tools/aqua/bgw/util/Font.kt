@file:Suppress("unused")

package tools.aqua.bgw.util

import java.awt.Color

//TODO: Missing Docs
class Font(
	val size: Number = 14,
	val color: Color = Color.BLACK,
	val family: String = "Arial",
	val fontStyle: FontStyle = FontStyle.NORMAL
)

//TODO: Missing Docs
enum class FontStyle {
	BOLD,
	
	NORMAL,
	
	SEMI_BOLD,
	
	Italic,
}