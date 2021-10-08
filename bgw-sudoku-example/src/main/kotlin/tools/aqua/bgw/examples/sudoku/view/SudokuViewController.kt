package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.service.LogicController
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuGameScene
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuMenuScene
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuSettingsScene

/**
 * Main ViewController.
 */
class SudokuViewController : BoardGameApplication(windowTitle = "Sudoku") {
	
	/**
	 * The menuScene.
	 */
	private val sudokuMenuScene: SudokuMenuScene = SudokuMenuScene()
	
	/**
	 * The settingsScene.
	 */
	private val sudokuSettingsScene: SudokuSettingsScene = SudokuSettingsScene()
	
	/**
	 * The gameScene.
	 */
	val sudokuGameScene: SudokuGameScene = SudokuGameScene()
	
	/**
	 * RefreshController instance for the [LogicController] callbacks.
	 */
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	/**
	 * Logic controller instance.
	 */
	private val logicController: LogicController = LogicController(refreshViewController)
	
	init {
		sudokuMenuScene.registerEvents()
		sudokuSettingsScene.registerEvents()
		sudokuGameScene.registerEvents()
		
		showGameScene(sudokuGameScene)
		showMenuScene(sudokuMenuScene)
		show()
	}
	
	/**
	 * Registers events for [sudokuMenuScene].
	 */
	private fun SudokuMenuScene.registerEvents() {
		continueGameButton.onMouseClicked = {
			hideMenuScene()
		}
		
		newGameEasyButton.onMouseClicked = {
			logicController.newGame(Difficulty.EASY)
			hideMenuScene()
		}
		
		newGameMediumButton.onMouseClicked = {
			logicController.newGame(Difficulty.MEDIUM)
			hideMenuScene()
		}
		
		newGameHardButton.onMouseClicked = {
			logicController.newGame(Difficulty.HARD)
			hideMenuScene()
		}
		
		exitButton.onMouseClicked = {
			exit()
		}
	}
	
	/**
	 * Registers events for [sudokuSettingsScene].
	 */
	private fun SudokuSettingsScene.registerEvents() {
		showTimerToggleButton.button.selectedProperty.addListener { _, nV ->
			logicController.settings.showTimer.value = nV
		}
		
		instantCheckToggleButton.button.selectedProperty.addListener { _, nV ->
			logicController.settings.instantCheck.value = nV
			
			if (nV)
				logicController.showErrors()
			else
				sudokuGameScene.sudokuGrid.clearHints()
		}
		
		continueGameButton.onMouseClicked = {
			hideMenuScene()
		}
	}
	
	/**
	 * Registers events for [sudokuGameScene].
	 */
	private fun SudokuGameScene.registerEvents() {
		onKeyPressed = {
			when {
				it.keyCode.isArrow() -> onCursorMoved(it.keyCode)
				it.keyCode.isDigit() -> onDigitEntered(it.keyCode)
				it.keyCode == KeyCode.DELETE -> onValueDeleted()
			}
		}
		
		logicController.settings.showTimer.addListener { _, nV ->
			timer.isVisible = nV
		}
		
		menuButton.onMouseClicked = {
			showMenuScene(sudokuMenuScene)
		}
		settingsButton.onMouseClicked = {
			showMenuScene(sudokuSettingsScene)
		}
		
		hintButton.onMouseClicked = {
			logicController.requestHint()
		}
		
		clearHintsButton.onMouseClicked = {
			sudokuGrid.clearHints()
		}
	}
	
	/**
	 * Enters a digit into the grid.
	 *
	 * @param digit Pressed digit.
	 */
	private fun onDigitEntered(digit: KeyCode) {
		val cell = sudokuGameScene.sudokuGrid.selectedCell ?: return
		
		if (cell.isFixed || digit == KeyCode.NUMPAD0)
			return
		
		logicController.setValue(cell.boxIndex, cell.rowIndex, cell.colIndex, digit.string.toInt())
	}
	
	/**
	 * Deletes a digit from the grid.
	 */
	private fun onValueDeleted() {
		val cell = sudokuGameScene.sudokuGrid.selectedCell ?: return
		
		logicController.setValue(cell.boxIndex, cell.rowIndex, cell.colIndex, null)
	}
	
	/**
	 * Moves the cursor.
	 *
	 * @param arrow Pressed arrow key.
	 */
	private fun onCursorMoved(arrow: KeyCode) {
		val selectedCell = sudokuGameScene.sudokuGrid.selectedCell ?: return
		
		var box = selectedCell.boxIndex
		var col = selectedCell.colIndex
		var row = selectedCell.rowIndex
		
		when (arrow) {
			KeyCode.UP -> {
				if (row == 0) {
					box = Math.floorMod(box - 3, 9)
					row = 2
				} else {
					row--
				}
			}
			KeyCode.DOWN -> {
				if (row == 2) {
					box = (box + 3) % 9
					row = 0
				} else {
					row++
				}
			}
			KeyCode.LEFT -> {
				if (col == 0) {
					box += if (box % 3 != 0) -1 else 2
					col = 2
				} else {
					col--
				}
			}
			KeyCode.RIGHT -> {
				if (col == 2) {
					box -= if (box % 3 != 2) -1 else 2
					col = 0
				} else {
					col++
				}
			}
			else -> throw IllegalArgumentException()
		}
		
		sudokuGameScene.sudokuGrid.getCell(box, row, col).select()
	}
}