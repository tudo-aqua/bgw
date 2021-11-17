package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.examples.tetris.service.LogicController

/**
 * Main ViewController.
 */
class TetrisViewController : BoardGameApplication(windowTitle = "Tetris") {
	
	/**
	 * The gameScene.
	 */
	val tetrisGameScene: TetrisGameScene = TetrisGameScene()
	
	/**
	 * RefreshController instance for the [LogicController] callbacks.
	 */
	private val refreshViewController: RefreshViewController = RefreshViewController(this)
	
	/**
	 * Logic controller instance.
	 */
	private val logicController: LogicController = LogicController(refreshViewController)
	
	init {
		tetrisGameScene.registerEvents()
		
		onWindowClosed = {
			logicController.stopTimer()
		}
		
		showGameScene(tetrisGameScene)
		show()
	}
	
	/**
	 * Registers events for [tetrisGameScene].
	 */
	private fun TetrisGameScene.registerEvents() {
		onKeyPressed = {
			when {
				it.keyCode.isArrow() -> logicController.navigate(it.keyCode)
				it.keyCode == KeyCode.ENTER -> logicController.startGame()
			}
		}
	}
}