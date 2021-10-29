package tools.aqua.bgw.examples.sudoku.view.scenes

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuButton
import tools.aqua.bgw.examples.sudoku.view.customcomponents.MenuToggleButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * SettingsMenu scene.
 */
class SudokuSettingsScene : MenuScene(width = 300, height = 600, background = ColorVisual(Color.WHITE)) {
	
	/**
	 * [Label] for the title.
	 */
	private val titleLabel: Label = Label(
		height = 100,
		width = 200,
		posX = 50,
		posY = 0,
		text = "Main menu",
		font = Font(fontWeight = Font.FontWeight.BOLD)
	)
	
	/**
	 * [MenuToggleButton] "Show Timer".
	 */
	val showTimerToggleButton : MenuToggleButton = MenuToggleButton(position = 1, text = "Show Timer")
	
	/**
	 * [MenuToggleButton] "Instant Check".
	 */
	val instantCheckToggleButton : MenuToggleButton = MenuToggleButton(position = 2, text = "Instant Check")
	
	/**
	 * [Button] "Continue".
	 */
	val continueGameButton: Button = MenuButton(position = 3, text = "Continue")
	
	init {
		addComponents(
			titleLabel,
			showTimerToggleButton,
			instantCheckToggleButton,
			continueGameButton
		)
	}
}