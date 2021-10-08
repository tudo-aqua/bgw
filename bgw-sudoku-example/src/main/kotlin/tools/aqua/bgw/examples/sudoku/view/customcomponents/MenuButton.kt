package tools.aqua.bgw.examples.sudoku.view.customcomponents

import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ToggleButton
import tools.aqua.bgw.components.uicomponents.UIComponent
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

class MenuToggleButton(position : Int, text : String, isSelected : Boolean = false) : Pane<UIComponent>(
	height = 80,
	width = 200,
	posX = 50,
	posY = position * 100,
) {
	
	val button: ToggleButton = ToggleButton(
		height = 80,
		width = 50,
		posX = 0,
		posY = 0,
		isSelected = isSelected
	)
	val label: Label = Label(
		height = 80,
		width = 150,
		posX = 50,
		posY = 0,
		text = text
	)
	
	init {
		addAll(button, label)
	}
}

