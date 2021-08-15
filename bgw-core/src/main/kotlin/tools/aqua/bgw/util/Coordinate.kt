/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.util

/**
 * A Coordinate containing [xCoord] and [yCoord].
 */
open class Coordinate(xCoord: Number = 0, yCoord: Number = 0) {
	
	/**
	 * X coordinate.
	 */
	val xCoord: Double = xCoord.toDouble()
	
	/**
	 * Y coordinate.
	 */
	val yCoord: Double = yCoord.toDouble()
	
	/**
	 * Returns a new coordinate object containing added [xCoord] and [yCoord].
	 *
	 * @param other [Coordinate] to add.
	 *
	 * @return New coordinate object containing added [xCoord] and [yCoord].
	 *
	 * @see minus
	 */
	operator fun plus(other: Coordinate): Coordinate = Coordinate(
		xCoord + other.xCoord,
		yCoord + other.yCoord
	)
	
	/**
	 * Returns a new coordinate object containing subtracted [xCoord] and [yCoord].
	 *
	 * @param other [Coordinate] to subtract.
	 *
	 * @return New coordinate object containing subtracted [xCoord] and [yCoord].
	 *
	 * @see minus
	 */
	operator fun minus(other: Coordinate): Coordinate = Coordinate(
		xCoord - other.xCoord,
		yCoord - other.yCoord
	)
	
	
	/**
	 * Rotates coordinate by angle degrees around center point.
	 *
	 * With
	 *
	 * - Angle t,
	 *
	 * - Center point Z
	 *
	 * - Coordinate P
	 *
	 *
	 * [ cos(phi)  -sin(phi) ] * [ Px - Zx ] + [ Zx ] = [ cos(phi)*(Px-Zx) - sin(phi)*(Py-Zy) + Zx ]
	 *
	 * [ sin(phi)   cos(phi) ]   [ Py - Zy ]   [ Zy ]   [ sin(phi)*(Px-Zx) + cos(phi)*(Py-Zy) + Zy ]
	 */
	fun rotated(angle: Double, center: Coordinate): Coordinate = Coordinate(
		xCoord = cos(angle) * (xCoord - center.xCoord) - sin(angle) * (yCoord - center.yCoord) + center.xCoord,
		yCoord = sin(angle) * (xCoord - center.xCoord) + cos(angle) * (yCoord - center.yCoord) + center.yCoord
	)
	
	/**
	 * Rotates coordinate by angle degrees around point (0,0).
	 *
	 * With
	 *
	 * - Angle t,
	 *
	 * - Center point Z
	 *
	 * - Coordinate P
	 *
	 *
	 * [ cos(phi)  -sin(phi) ] * [ Px - Zx ] + [ Zx ] = [ cos(phi)*(Px-Zx) - sin(phi)*(Py-Zy) + Zx ]
	 *
	 * [ sin(phi)   cos(phi) ]   [ Py - Zy ]   [ Zy ]   [ sin(phi)*(Px-Zx) + cos(phi)*(Py-Zy) + Zy ]
	 */
	fun rotated(angle: Double): Coordinate = rotated(angle, Coordinate(0, 0))
	
	/**
	 * Sin function for angles in degrees.
	 */
	@Suppress("GrazieInspection")
	private fun sin(degrees: Double) = kotlin.math.sin(Math.toRadians(degrees))
	
	/**
	 * Cos function for angles in degrees.
	 */
	@Suppress("GrazieInspection")
	private fun cos(degrees: Double) = kotlin.math.cos(Math.toRadians(degrees))
	
	/**
	 * Prints [xCoord] and [yCoord] as String.
	 */
	override fun toString(): String = "X = $xCoord, Y = $yCoord"
	
	/**
	 * Compares coordinate to another Object.
	 *
	 * @return `true` if other object is Coordinate and [xCoord] == other.xCoord && [yCoord] == other.yCoord.
	 */
	override fun equals(other: Any?): Boolean {
		if (other !is Coordinate)
			return false
		
		return this.xCoord == other.xCoord && this.yCoord == other.yCoord
	}
	
	/**
	 * HashCode for this object.
	 *
	 * @return HashCode containing [xCoord] and [yCoord].
	 */
	override fun hashCode(): Int {
		return xCoord.hashCode() - yCoord.hashCode()
	}
}