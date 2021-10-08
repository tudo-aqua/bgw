package tools.aqua.bgw.examples.sudoku.view.scenes

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.SudokuGrid
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual
import java.awt.Color

/**
 * The game scene.
 */
class SudokuGameScene : BoardGameScene(background = ColorVisual.WHITE) {
	
	/**
	 * Sudoku grid.
	 */
	val sudokuGrid: SudokuGrid = SudokuGrid(posX = 960, posY = 540, size = 900, majorSpacing = 5, minorSpacing = 2)
	
	/**
	 * Menu button.
	 */
	val menuButton : Button = Button(
		height = 50,
		width = 120,
		posX = 40,
		posY = 40,
		visual = ColorVisual.BLACK,
		text = "Menu",
		font = Font(color = Color.WHITE)
	)
	
	/**
	 * Settings button.
	 */
	val settingsButton : Button = Button(
		height = menuButton.height,
		width = menuButton.width,
		posX = menuButton.posX,
		posY = menuButton.posY + menuButton.height + 40,
		visual = ColorVisual.BLACK,
		text = "Settings",
		font = Font(color = Color.WHITE)
	)
	
	/**
	 * Label with timer.
	 */
	val timer : Label = Label(
		height = 50,
		width = 200,
		posX = 1650,
		posY = 70,
		text = "0:00:00",
		font = Font(size = 20),
		alignment = Alignment.TOP_RIGHT,
	).apply { isVisible = false }
	
	/**
	 * Button for error hints.
	 */
	val hintButton: Button = Button(
		height = 80,
		width = 80,
		posX = 1450,
		posY = 920,
		visual = ImageVisual("light-bulb.png")
	)
	
	/**
	 * Button to clear error hints.
	 */
	val clearHintsButton: Button = Button(
		height = 80,
		width = 80,
		posX = 1530,
		posY = 920,
		visual = TextVisual("clear")
	)
	
	init {
		addComponents(sudokuGrid, menuButton, settingsButton, hintButton, clearHintsButton, timer)
	}
}