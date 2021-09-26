package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple
import tools.aqua.bgw.examples.sudoku.service.LogicController

/**
 * Main tools.aqua.bgw.examples.sudoku.view controller.
 */
class SudokuViewController : BoardGameApplication(windowTitle = "Sudoku") {
	
	/**
	 * The tools.aqua.bgw.examples.sudoku.main menu scene.
	 */
	private val sudokuMenuScene: SudokuMenuScene = SudokuMenuScene()
	
	/**
	 * The tools.aqua.bgw.examples.sudoku.main game scene.
	 */
	val sudokuGameScene: SudokuGameScene = SudokuGameScene()
	
	/**
	 * Refresh tools.aqua.bgw.examples.sudoku.view controller instance for the [LogicController] callbacks.
	 */
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	/**
	 * Logic controller instance.
	 */
	private val logicController: LogicController = LogicController(refreshViewController)
	
	
	init {
		registerMenuEvents()
		registerGameEvents()
		
		showGameScene(sudokuGameScene)
		showMenuScene(sudokuMenuScene)
		show()
	}
	
	private fun registerMenuEvents() {
		sudokuMenuScene.continueGameButton.onMouseClicked = {
			hideMenuScene()
		}
		
		sudokuMenuScene.newGameEasyButton.onMouseClicked = {
			logicController.newGame(Difficulty.EASY)
			hideMenuScene()
		}
		
		sudokuMenuScene.newGameMediumButton.onMouseClicked = {
			logicController.newGame(Difficulty.MEDIUM)
			hideMenuScene()
		}
		
		sudokuMenuScene.newGameHardButton.onMouseClicked = {
			logicController.newGame(Difficulty.HARD)
			hideMenuScene()
		}
		
		sudokuMenuScene.exitButton.onMouseClicked = {
			exit()
		}
	}
	
	private fun registerGameEvents() {
		sudokuGameScene.hintButton.onKeyPressed = { setValue(it) }
		sudokuGameScene.hintButton.onMouseClicked = {
			logicController.requestHint()
		}
	}
	
	private fun setValue(e: KeyEvent) {
		val cell = sudokuGameScene.sudokuGrid.selectedCell ?: return
		
		if (cell.isFixed)
			return
		
		if (e.keyCode.isDigit() && e.keyCode != KeyCode.NUMPAD0) {
			logicController.setValue(SudokuTuple(cell.boxIndex, cell.rowIndex, cell.colIndex, e.keyCode.string.toInt()))
		} else if (e.keyCode == KeyCode.DELETE) {
			logicController.setValue(SudokuTuple(cell.boxIndex, cell.rowIndex, cell.colIndex, null))
		}
	}
}
