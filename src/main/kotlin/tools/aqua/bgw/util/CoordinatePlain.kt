package tools.aqua.bgw.util

/**
 * A CoordinatePlain with it's four corners as Coordinates.
 *
 * @param topLeftX xCoord of top left corner
 * @param topLeftY yCoord of top left corner
 * @param bottomRightX xCoord of bottom right corner
 * @param bottomRightY yCoord of bottom right corner
 */
class CoordinatePlain(topLeftX: Number, topLeftY: Number, bottomRightX: Number, bottomRightY: Number) {
	
	/**
	 * Top left corner coordinate.
	 */
	val topLeft: Coordinate
	
	/**
	 * Top right corner coordinate.
	 */
	val topRight: Coordinate
	
	/**
	 * Bottom left corner coordinate.
	 */
	val bottomLeft: Coordinate
	
	/**
	 * Bottom right corner coordinate.
	 */
	val bottomRight: Coordinate
	
	/**
	 * The width of this plain.
	 */
	val width: Double
	
	/**
	 * The height of this plain.
	 */
	val height: Double
	
	init {
		require(topLeftX.toDouble() <= bottomRightX.toDouble()) {
			"Top left corner is not to the right of bottom left corner."
		}
		require(topLeftY.toDouble() <= bottomRightY.toDouble()) {
			"Top left corner is not to the right of bottom left corner."
		}
		
		topLeft = Coordinate(topLeftX, topLeftY)
		topRight = Coordinate(bottomRightX, topLeftY)
		bottomLeft = Coordinate(topLeftX, bottomRightY)
		bottomRight = Coordinate(bottomRightX, bottomRightY)
		width = bottomRightX.toDouble() - topLeftX.toDouble()
		height = bottomRightY.toDouble() - topLeftY.toDouble()
	}
	
	/**
	 * A CoordinatePlain with it's four corners as Coordinates.
	 *
	 * @param topLeft Coordinate of top left corner
	 * @param bottomRight Coordinate of bottom right corner
	 */
	constructor(topLeft: Coordinate, bottomRight: Coordinate) : this(
		topLeft.xCoord,
		topLeft.yCoord,
		bottomRight.xCoord,
		bottomRight.yCoord
	)
}