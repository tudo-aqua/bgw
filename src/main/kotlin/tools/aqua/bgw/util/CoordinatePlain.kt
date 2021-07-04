@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.util

/**
 * A CoordinatePlain with its four corners as [Coordinate]s.
 *
 * @constructor Creates a coordinate plain out of the two corners TOP_LEFT and BOTTOM_RIGHT.
 * TOP_LEFT corner coordinates must be smaller or equal in x and y direction than BOTTOM_RIGHT corner coordinate.
 *
 * @param topLeftX xCoord of top left corner
 * @param topLeftY yCoord of top left corner
 * @param bottomRightX xCoord of bottom right corner
 * @param bottomRightY yCoord of bottom right corner
 *
 * @throws IllegalArgumentException if TOP_LEFT corner coordinates are larger in x or y direction
 * than BOTTOM_RIGHT corner coordinate.
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
	 * Creates a coordinate plain out of the two corners TOP_LEFT and BOTTOM_RIGHT as Coordinates.
	 * TOP_LEFT corner coordinates must be smaller or equal in x and y direction than BOTTOM_RIGHT corner coordinate,.
	 *
	 * @param topLeft [Coordinate] of top left corner
	 * @param bottomRight [Coordinate] of bottom right corner
	 *
	 * @throws IllegalArgumentException if TOP_LEFT corner coordinates are larger in x or y direction
	 * than BOTTOM_RIGHT corner coordinate.
	 */
	constructor(topLeft: Coordinate, bottomRight: Coordinate) : this(
		topLeft.xCoord,
		topLeft.yCoord,
		bottomRight.xCoord,
		bottomRight.yCoord
	)
}