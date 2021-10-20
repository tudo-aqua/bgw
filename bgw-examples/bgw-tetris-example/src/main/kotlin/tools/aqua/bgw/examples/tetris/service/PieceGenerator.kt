package tools.aqua.bgw.examples.tetris.service

import tools.aqua.bgw.examples.tetris.entity.Color
import tools.aqua.bgw.examples.tetris.entity.Piece
import tools.aqua.bgw.examples.tetris.entity.Tile

class PieceGenerator {
	private val pieces : Set<Piece>
	
	init {
		this.pieces = generatePieces()
	}
	
	private fun generatePieces(): Set<Piece> {
		val pieceBox = Piece(Array(2) { Array(2) { Tile(Color.YELLOW) } })
		val pieceString = Piece(Array(4) { Array(1) { Tile(Color.CYAN) } })
		val pieceT = Piece(Array(3) { y -> Array(2) { x -> if(y == 1 || x == 1) Tile(Color.PURPLE) else null } })
		val pieceL1 = Piece(Array(2) { y -> Array(3) { x -> if(y == 1 || x == 0) Tile(Color.BLUE) else null } })
		val pieceL2 = Piece(Array(2) { y -> Array(3) { x -> if(y == 1 || x == 2) Tile(Color.ORANGE) else null } })
		val pieceZ1 = Piece(Array(2) { y -> Array(3) { x ->
			if((y == 0 && x == 0) || (y == 1 && x == 2)) null else Tile(Color.GREEN)  }
		})
		val pieceZ2 = Piece(Array(2) { y -> Array(3) { x ->
			if((y == 1 && x == 0) || (y == 0 && x == 2)) null else Tile(Color.RED)  }
		})
		
		return setOf(pieceBox,pieceString, pieceT, pieceL1, pieceL2, pieceZ1, pieceZ2)
	}
	
	fun getRandomPiece() : Piece = pieces.random()
	fun generate3(): Array<Piece> = arrayOf(getRandomPiece(), getRandomPiece(), getRandomPiece())
}