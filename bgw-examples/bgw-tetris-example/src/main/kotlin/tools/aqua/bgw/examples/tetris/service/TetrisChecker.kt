package tools.aqua.bgw.examples.tetris.service

import tools.aqua.bgw.examples.tetris.entity.Tetris

class TetrisChecker {
	companion object{
		fun checkCollision(tetris: Tetris, offsetY : Int, offsetX : Int) : Boolean {
			val piece = tetris.currentPiece
			val grid = tetris.tetris

			for(y in 0 until piece.height) {
				for(x in 0 until piece.width) {
					val yPos = y + tetris.currentYPosition + offsetY -1
					val xPos = x + tetris.currentXPosition + offsetX -1
					
					if(yPos < 0)
						continue
					
					if(yPos > 19 || xPos > 9 || xPos < 0)
						return true
					
					if(piece.tiles[y][x] != null &&
						grid[yPos][xPos].imageVisual != null)
						return true
				}
			}
			return false
		}
		
		fun isRowFull(tetris: Tetris, row : Int) : Boolean {
			for(x in 0 until 10) {
				if(tetris.tetris[row][x].imageVisual == null)
					return false
			}
			return true
		}
	}
}