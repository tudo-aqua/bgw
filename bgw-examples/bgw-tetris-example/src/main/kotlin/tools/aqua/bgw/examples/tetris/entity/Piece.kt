package tools.aqua.bgw.examples.tetris.entity

data class Piece(val tiles : Array<Array<Tile?>>) {
	val height = tiles.size
	val width = tiles[0].size
}
