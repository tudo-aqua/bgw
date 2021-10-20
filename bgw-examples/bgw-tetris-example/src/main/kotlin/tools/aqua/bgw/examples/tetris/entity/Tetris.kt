package tools.aqua.bgw.examples.tetris.entity

import tools.aqua.bgw.observable.properties.IntegerProperty

class Tetris(pieces : Array<Piece>) {
	val tetris : Array<Array<Tile>> = Array(20) { Array(10) { Tile(null) } }
	
	val preview : Array<Piece> = pieces
	
	var currentPiece : Piece = Piece(Array(2) { Array(3) {null}})
	var currentYPosition : Int = 0
	var currentXPosition : Int = 5
	var currentRotation : Int = 0
	
	var points: IntegerProperty = IntegerProperty(0)
	
	fun nextPiece(nextPreviewPiece: Piece) : Piece {
		currentPiece  = preview[2]
		currentYPosition = 0
		currentXPosition = 5
		currentRotation = 0
		
		preview[2] = preview[1]
		preview[1] = preview[0]
		preview[0] = nextPreviewPiece
		
		return currentPiece
	}
	
	fun movePiece() : Tetris = this.apply {
		currentYPosition++
	}
	
	
	fun left() {
		currentXPosition--
	}
	
	fun right() {
		currentXPosition++
	}
	
	fun rotate() {
		currentRotation = if(currentRotation == 3) 0 else currentRotation + 1
	}
	
	fun addPoints(points : Int) {
		this.points.value += points
	}
}
