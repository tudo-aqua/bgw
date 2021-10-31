package tools.aqua.bgw.examples.tetris.entity

/**
 * Piece representation boxing array type of [Tile]s.
 *
 * @param tiles Tiles array, rows first
 */
class Piece(val tiles : Array<Array<Tile?>>) {
	/**
	 * The height of this piece.
	 */
	val height : Int = tiles.size
	
	/**
	 * The width of this piece,.,
	 */
	val width : Int = tiles[0].size
}
