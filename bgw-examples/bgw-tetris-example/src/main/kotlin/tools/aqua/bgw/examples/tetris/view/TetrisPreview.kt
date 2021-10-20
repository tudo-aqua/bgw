package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tile
import tools.aqua.bgw.visual.ColorVisual

class TetrisPreview(posX : Number, posY : Number, cellSize : Number) : GridPane<Label>(
	posX = posX,
	posY = posY,
	columns = 3,
	rows = 4,
	spacing = 0
) {

	init{
		for(i in 0 until columns) {
			for(j in 0 until  rows) {
				this[i, j] = Label(height = cellSize, width = cellSize)
			}
		}
	}
	
	fun show(piece : Piece) {
		val minY = 4 - piece.tiles.size
		val minX = if(piece.tiles[0].size == 3) 0 else 1
		val maxX = minX + piece.tiles[0].size
		
		val grid : Array<Array<Tile?>> = Array(4) { Array(3) { null } }
		
		for (y in minY until 4) {
			for(x in minX until maxX) {
				grid[y][x] = piece.tiles[y - minY][x - minX]
			}
		}
		
		for (y in 0 until rows) {
			for(x in 0 until columns) {
				this[x,y]?.visual = grid[y][x]?.imageVisual ?: ColorVisual.WHITE
			}
		}
	}
}