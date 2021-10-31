package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tetris
import tools.aqua.bgw.visual.ColorVisual

/**
 * ViewController for refreshes.
 */
class RefreshViewController(private val viewController: TetrisViewController) : Refreshable {
	/**
	 * Refreshes tetris grid.
	 *
	 * @param tetris Tetris to display.
	 */
	override fun refresh(tetris: Tetris) {
		refreshPreview(tetris.preview)
		refreshTetris(tetris)
		refreshActivePiece(tetris.currentPiece, tetris.currentYPosition, tetris.currentXPosition)
	}
	
	/**
	 * Hides start instructions.
	 */
	override fun hideStartInstructions() {
		viewController.tetrisGameScene.startLabel.isVisible = false
	}
	
	/**
	 * Shows loose text.
	 */
	override fun loose() {
		viewController.tetrisGameScene.looseLabel.isVisible = true
	}
	
	/**
	 * Refreshes speed label.
	 *
	 * @param speed New speed to display.
	 */
	override fun refreshSpeed(speed: Long) {
		viewController.tetrisGameScene.speedLabel.text = "Current delay: $speed"
	}
	
	/**
	 * Refreshes points label.
	 *
	 * @param points New points to display.
	 */
	override fun refreshPoints(points: Int) {
		viewController.tetrisGameScene.pointsLabel.text = "$points Points"
	}
	
	/**
	 * Refreshes preview fields.
	 *
	 * @param preview Preview pieces. Must be size 3.
	 *
	 * @throws IllegalArgumentException If array does not contain 3 pieces.
	 */
	private fun refreshPreview(preview: Array<Piece>) {
		require(preview.size == 3) { "There need to be three pieces as preview." }
		viewController.tetrisGameScene.preview1.show(preview[0])
		viewController.tetrisGameScene.preview2.show(preview[1])
		viewController.tetrisGameScene.preview3.show(preview[2])
	}
	
	/**
	 * Refreshes tetris grid.
	 *
	 * @param tetris Tetris to display.
	 */
	private fun refreshTetris(tetris: Tetris) {
		val viewGrid = viewController.tetrisGameScene.tetrisGrid
		val tetrisGrid = tetris.tetris
		
		for (y in tetrisGrid.indices) {
			for (x in 0 until tetrisGrid[0].size) {
				viewGrid[x+1, y+1]?.visual =
					tetrisGrid[y][x].imageVisual ?: ColorVisual.BLACK
			}
		}
	}
	
	/**
	 * Refreshes active piece.
	 *
	 * @param piece active piece.
	 * @param yPosition Y position.
	 * @param xPosition X position.
	 */
	private fun refreshActivePiece(piece: Piece, yPosition: Int, xPosition : Int) {
		for(y in 0 until piece.tiles.size) {
			for(x in 0 until piece.tiles[0].size) {
				piece.tiles[y][x]?.imageVisual?.let {
					if(yPosition + y > 0)
						viewController.tetrisGameScene.tetrisGrid[xPosition + x, yPosition + y]?.visual = it
				}
			}
		}
	}
}