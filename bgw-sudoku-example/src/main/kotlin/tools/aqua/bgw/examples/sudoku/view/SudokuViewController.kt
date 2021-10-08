package tools.aqua.bgw.examples.sudoku.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.examples.sudoku.entity.Difficulty
import tools.aqua.bgw.examples.sudoku.entity.SudokuTuple
import tools.aqua.bgw.examples.sudoku.service.LogicController
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuGameScene
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuMenuScene
import tools.aqua.bgw.examples.sudoku.view.scenes.SudokuSettingsScene
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.scheduleAtFixedRate


/**
 * Main tools.aqua.bgw.examples.sudoku.view controller.
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
	
	/**
	 * Timer
	 */
	private val timer : Timer = Timer()
	private val timerRunning : Boolean = false
	private val startTime : Long = 0L
	
	init {
		sudokuMenuScene.registerEvents()
		sudokuSettingsScene.registerEvents()
		sudokuGameScene.registerEvents()
		
		timer.scheduleAtFixedRate(
			delay = 0,
			period = 100
		) {
			if(timerRunning) {
				val millis = System.currentTimeMillis() - startTime
				
				sudokuGameScene.timer.text = String.format("%02d:%02d:%02d",
					TimeUnit.MILLISECONDS.toHours(millis),
					TimeUnit.MILLISECONDS.toMinutes(millis) -
							TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
					TimeUnit.MILLISECONDS.toSeconds(millis) -
							TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
				)
			}
		}
		
		showGameScene(sudokuGameScene)
		showMenuScene(sudokuMenuScene)
		show()
	}
	
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
	
	private fun SudokuSettingsScene.registerEvents() {
		showTimerToggleButton.button.selectedProperty.addListener{ _, nV ->
			logicController.settings.showTimer.value = nV
		}
		
		instantCheckToggleButton.button.selectedProperty.addListener{ _, nV ->
			logicController.settings.instantCheck.value = nV
			
			if(nV)
				logicController.checkSudoku()
			else
				sudokuGameScene.sudokuGrid.clearHints()
		}
		
		continueGameButton.onMouseClicked = {
			hideMenuScene()
		}
	}
	
	private fun SudokuGameScene.registerEvents() {
		onKeyPressed = { setValue(it) }
		
		logicController.settings.showTimer.addListener{ _, nV ->
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
