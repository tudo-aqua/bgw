package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.SudokuGrid
import tools.aqua.bgw.visual.ColorVisual

class SudokuGameScene : BoardGameScene(background = ColorVisual.WHITE) {
	
	val sudokuGrid: SudokuGrid = SudokuGrid(posX = 960, posY = 540, size = 900, majorSpacing = 5, minorSpacing = 2)
	val keyCapture: Button = Button(0,0,0,0).apply { opacity = 0.0 }
	
	init {
		addComponents(sudokuGrid, keyCapture)
	}
}