package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.SudokuGrid
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class SudokuGameScene : BoardGameScene(background = ColorVisual.WHITE) {
	
	val sudokuGrid: SudokuGrid = SudokuGrid(posX = 960, posY = 540, size = 900, majorSpacing = 5, minorSpacing = 2)
	val hintButton: Button = Button(
		height = 80,
		width = 80,
		posX = 1450,
		posY = 920,
		visual = ImageVisual("light-bulb.png")
	)
	
	init {
		addComponents(sudokuGrid, hintButton)
	}
}