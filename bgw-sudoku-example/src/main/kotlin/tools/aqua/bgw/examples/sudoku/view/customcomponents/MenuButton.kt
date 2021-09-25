package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

class MenuButton(position : Int, text : String) : Button(
	height = 80,
	width = 200,
	posX = 50,
	posY = position * 100,
	text = text,
	font = Font(color = Color.WHITE),
	visual = ColorVisual.BLACK
)