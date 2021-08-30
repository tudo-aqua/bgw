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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.util

/**
 * A CoordinatePlain with its four corners as [Coordinate]s.
 *
 * @constructor Creates a [CoordinatePlain] with given corner coordinates.
 *
 * @param topLeft Top left corner coordinate.
 * @param topRight Top right corner coordinate.
 * @param bottomLeft Bottom left corner coordinate.
 * @param bottomRight Bottom right corner coordinate.
 *
 * @throws IllegalArgumentException If TOP_LEFT corner coordinates are larger in x or y direction
 * than BOTTOM_RIGHT corner coordinate.
 */
open class CoordinatePlain private constructor(
	val topLeft: Coordinate,
	val topRight: Coordinate,
	val bottomLeft: Coordinate,
	val bottomRight: Coordinate) {
	
	/**
	 * Top left bound coordinate.
	 */
	val topLeftBound: Coordinate
	
	/**
	 * Top right bound coordinate.
	 */
	val topRightBound: Coordinate
	
	/**
	 * Bottom left bound coordinate.
	 */
	val bottomLeftBound: Coordinate
	
	/**
	 * Bottom right bound coordinate.
	 */
	val bottomRightBound: Coordinate
	
	/**
	 * The width of this plain.
	 */
	val width: Double
	
	/**
	 * The height of this plain.
	 */
	val height: Double
	
	init {
		val minX = minOf(topLeft.xCoord, topRight.xCoord, bottomLeft.xCoord, bottomRight.xCoord)
		val maxX = maxOf(topLeft.xCoord, topRight.xCoord, bottomLeft.xCoord, bottomRight.xCoord)
		val minY = minOf(topLeft.yCoord, topRight.yCoord, bottomLeft.yCoord, bottomRight.yCoord)
		val maxY = maxOf(topLeft.yCoord, topRight.yCoord, bottomLeft.yCoord, bottomRight.yCoord)
		
		topLeftBound = Coordinate(minX, minY)
		topRightBound = Coordinate(maxX, minY)
		bottomLeftBound = Coordinate(minX, maxY)
		bottomRightBound = Coordinate(maxX, maxY)
		
		width = maxX - minX
		height = maxY - minY
	}
	
	/**
	 * A CoordinatePlain with two corners as Coordinates.
	 * Creates a coordinate plain out of the two corners TOP_LEFT and BOTTOM_RIGHT as Coordinates.
	 * TOP_LEFT corner coordinates must be smaller or equal in x and y direction than BOTTOM_RIGHT corner coordinate.
	 *
	 * @param topLeftX The xCoord of the top left corner
	 * @param topLeftY The yCoord of the top left corner
	 * @param bottomRightX The xCoord of the bottom right corner
	 * @param bottomRightY The yCoord of the bottom right corner
	 */
	constructor(topLeftX: Number, topLeftY: Number, bottomRightX: Number, bottomRightY: Number) : this(
		topLeft = Coordinate(topLeftX, topLeftY),
		topRight = Coordinate(bottomRightX, topLeftY),
		bottomLeft = Coordinate(topLeftX, bottomRightY),
		bottomRight = Coordinate(bottomRightX, bottomRightY),
	) {
		require(topLeftX.toDouble() <= bottomRightX.toDouble()) {
			"Top left corner is not to the right of bottom left corner."
		}
		require(topLeftY.toDouble() <= bottomRightY.toDouble()) {
			"Top left corner is not to the right of bottom left corner."
		}
	}
	
	/**
	 * A CoordinatePlain with two corners as Coordinates.
	 * Creates a coordinate plain out of the two corners TOP_LEFT and BOTTOM_RIGHT as Coordinates.
	 * TOP_LEFT corner coordinates must be smaller or equal in x and y direction than BOTTOM_RIGHT corner coordinate.
	 *
	 * @param topLeft [Coordinate] of top left corner
	 * @param bottomRight [Coordinate] of bottom right corner
	 *
	 * @throws IllegalArgumentException If TOP_LEFT corner coordinates are larger in x or y direction
	 * than BOTTOM_RIGHT corner coordinate.
	 */
	constructor(topLeft: Coordinate, bottomRight: Coordinate) : this(
		topLeft.xCoord,
		topLeft.yCoord,
		bottomRight.xCoord,
		bottomRight.yCoord
	)
	
	/**
	 * Rotates [CoordinatePlain] by angle degrees around center point.
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
	fun rotated(angle: Number, center: Coordinate): CoordinatePlain = CoordinatePlain(
		topLeft = topLeft.rotated(angle, center),
		topRight = topRight.rotated(angle, center),
		bottomLeft = bottomLeft.rotated(angle, center),
		bottomRight = bottomRight.rotated(angle, center),
	)
	
	override fun toString(): String {
		return "CoordinatePlain(topLeft=$topLeft, topRight=$topRight, bottomLeft=$bottomLeft, bottomRight=$bottomRight, width=$width, height=$height)"
	}
}