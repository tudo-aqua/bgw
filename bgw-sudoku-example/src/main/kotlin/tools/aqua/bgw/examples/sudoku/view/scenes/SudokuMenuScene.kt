package tools.aqua.bgw.examples.sudoku.view.scenes


import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * ViewController for the tools.aqua.bgw.examples.sudoku.main menu scene.
 */
class SudokuMenuScene : MenuScene(width = 300, height = 600, background = ColorVisual(Color.WHITE)) {
	
	private val titleLabel : Label = Label(
		height = 100,
		width = 200,
		posX = 50,
		posY = 0,
		text = "Main menu",
		font = Font(fontWeight = Font.FontWeight.BOLD)
	)
	
	val continueGameButton: Button = MenuButton(position = 1, text = "Continue")
	val newGameEasyButton: Button = MenuButton(position = 2, text = "New Easy Game")
	val newGameMediumButton: Button = MenuButton(position = 3, text = "New Medium Game")
	val newGameHardButton: Button = MenuButton(position = 4, text = "New Hard Game")
	val exitButton: Button = MenuButton(position = 5, text = "Exit")
	
	init {
		addComponents(
			titleLabel,
			continueGameButton,
			newGameEasyButton,
			newGameMediumButton,
			newGameHardButton,
			exitButton,
		)
	}
}

