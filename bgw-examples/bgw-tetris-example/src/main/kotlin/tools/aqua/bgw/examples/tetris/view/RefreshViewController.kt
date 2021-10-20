package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tetris
import tools.aqua.bgw.visual.ColorVisual

class RefreshViewController(private val viewController: TetrisViewController) : Refreshable {
	override fun refresh(tetris: Tetris) {
		refreshPreview(tetris.preview)
		refreshTetris(tetris)
		refreshActivePiece(tetris.currentPiece, tetris.currentYPosition, tetris.currentXPosition, tetris.currentRotation)
	}
	
	override fun hideStartInstructions() {
		viewController.tetrisGameScene.startLabel.isVisible = false
	}
	
	override fun loose() {
		viewController.tetrisGameScene.looseLabel.isVisible = true
	}
	
	private fun refreshPreview(preview: Array<Piece>) {
		viewController.tetrisGameScene.preview1.show(preview[0])
		viewController.tetrisGameScene.preview2.show(preview[1])
		viewController.tetrisGameScene.preview3.show(preview[2])
	}
	
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
	
	private fun refreshActivePiece(piece: Piece, yPosition: Int, xPosition : Int, rotation: Int) {
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