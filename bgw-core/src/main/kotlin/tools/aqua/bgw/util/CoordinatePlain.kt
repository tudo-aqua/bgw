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
open class CoordinatePlain(topLeftX: Number, topLeftY: Number, bottomRightX: Number, bottomRightY: Number) {
	
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
	 * A CoordinatePlain with its four corners as Coordinates.
	 * Creates a coordinate plain out of the two corners TOP_LEFT and BOTTOM_RIGHT as Coordinates.
	 * TOP_LEFT corner coordinates must be smaller or equal in x and y direction than BOTTOM_RIGHT corner coordinate.
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