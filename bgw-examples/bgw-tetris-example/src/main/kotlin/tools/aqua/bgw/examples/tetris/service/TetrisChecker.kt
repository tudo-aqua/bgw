package tools.aqua.bgw.examples.tetris.service

import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tetris

/**
 * Utility class to check tetris rules.
 */
class TetrisChecker {
	companion object {
		/**
		 * Checks for collisions between current tetris grid and the piece.
		 *
		 * @param tetris Fixed tetris grid.
		 * @param piece Current piece.
		 * @param offsetY Y offset between currentYPosition and position to check.
		 * @param offsetX X offset between currentXPosition and position to check.
		 */
		fun checkCollision(
			tetris: Tetris,
			piece: Piece = tetris.currentPiece,
			offsetY: Int = 0,
			offsetX: Int = 0
		): Boolean {
			val grid = tetris.tetris
			
			for (y in 0 until piece.height) {
				for (x in 0 until piece.width) {
					val yPos = y + tetris.currentYPosition + offsetY - 1
					val xPos = x + tetris.currentXPosition + offsetX - 1
					
					if (yPos < 0)
						continue
					
					if (yPos > 19 || xPos > 9 || xPos < 0)
						return true
					
					if (piece.tiles[y][x] != null && grid[yPos][xPos].imageVisual != null)
						return true
				}
			}
			return false
		}
		
		/**
		 * Checks whether given row is full.
		 *
		 * @param tetris Tetris to check.
		 * @param row Row to check.
		 */
		fun isRowFull(tetris: Tetris, row: Int): Boolean {
			for (x in 0 until 10) {
				if (tetris.tetris[row][x].imageVisual == null)
					return false
			}
			return true
		}
	}
}