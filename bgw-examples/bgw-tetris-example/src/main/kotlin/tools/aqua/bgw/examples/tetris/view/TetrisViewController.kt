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
	val logicController: LogicController = LogicController(refreshViewController)
	
	init {
		tetrisGameScene.registerEvents()
		
		onWindowClosed = {
			logicController.timer.cancel()
		}
		
		logicController.tetris.points.addListener{ _, nV -> tetrisGameScene.pointsLabel.text = "$nV Points"}
		
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
				it.keyCode == KeyCode.ENTER -> logicController.apply { if(!running) { nextPiece(); start(400) } }
				it.keyCode == KeyCode.BACK_SPACE -> logicController.nextPiece()
			}
		}
	}
}