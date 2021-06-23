package tools.aqua.bgw.util

/**
 * A Coordinate containing xCoord and yCoord.
 */
class Coordinate(xCoord: Number = 0, yCoord: Number = 0) {
	
	/**
	 * X coordinate.
	 */
	val xCoord = xCoord.toDouble()
	
	/**
	 * Y coordinate.
	 */
	val yCoord = yCoord.toDouble()
	
	/**
	 * Rotates coordinate by angle degrees around center point.
	 * With
	 * - Angle t,
	 * - Center point Z
	 * - Coordinate P
	 *
	 * [ cos(phi)  -sin(phi) ] * [ Px - Zx ] + [ Zx ] = [ cos(phi)*(Px-Zx) - sin(phi)*(Py-Zy) + Zx ]
	 * [ sin(phi)   cos(phi) ]   [ Py - Zy ]   [ Zy ]   [ sin(phi)*(Px-Zx) + cos(phi)*(Py-Zy) + Zy ]
	 */
	fun rotated(angle: Double, center: Coordinate) = Coordinate(
		xCoord = cos(angle) * (xCoord - center.xCoord) - sin(angle) * (yCoord - center.yCoord) + center.xCoord,
		yCoord = sin(angle) * (xCoord - center.xCoord) + cos(angle) * (yCoord - center.yCoord) + center.yCoord
	)
	
	/**
	 * Rotates coordinate by angle degrees around point (0,0).
	 * With
	 * - Angle t,
	 * - Center point Z
	 * - Coordinate P
	 *
	 * [ cos(phi)  -sin(phi) ] * [ Px - Zx ] + [ Zx ] = [ cos(phi)*(Px-Zx) - sin(phi)*(Py-Zy) + Zx ]
	 * [ sin(phi)   cos(phi) ]   [ Py - Zy ]   [ Zy ]   [ sin(phi)*(Px-Zx) + cos(phi)*(Py-Zy) + Zy ]
	 */
	fun rotated(angle: Double) = rotated(angle, Coordinate(0, 0))
	
	/**
	 * Sin function for angles in degrees.
	 */
	private fun sin(degrees: Double) = kotlin.math.sin(Math.toRadians(degrees))
	
	/**
	 * Cos function for angles in degrees.
	 */
	private fun cos(degrees: Double) = kotlin.math.cos(Math.toRadians(degrees))
	
	/**
	 * Prints xCoord and yCoord as String.
	 */
	override fun toString() = "X = $xCoord, Y = $yCoord"
}