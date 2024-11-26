/*
 * Copyright 2023 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.visual.Visual
import kotlin.math.sqrt

private typealias OffsetCoordinate = Pair<Int, Int>

private typealias AxialCoordinate = Pair<Int, Int>

/**
 * A class representing a grid of hexagons.
 *
 * @param posX The x-coordinate of the hexagon grid's position on the screen. Default is 0.
 * @param posY The y-coordinate of the hexagon grid's position on the screen. Default is 0.
 * @param width The width of the hexagon grid. It grows dynamically by the amount hexagons in it.
 * @param height The height of the hexagon grid. It grows dynamically by the amount hexagons in it.
 * @param visual The visual representation of the hexagon grid. Default is an empty visual.
 * @param coordinateSystem The coordinate system to use for the grid. Default is
 * `CoordinateSystem.OFFSET`.
 * @param orientation The orientation of the hexagons in the grid. Default is
 *  `HexOrientation.POINTY_TOP`.
 */
class HexagonGrid<T : HexagonView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = 0,
    height: Number = 0,
    visual: Visual = Visual.EMPTY,
    val coordinateSystem: CoordinateSystem = CoordinateSystem.OFFSET,
    var orientation: HexOrientation = HexOrientation.POINTY_TOP
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual
    ) {

    //TODO: Make this internal
    /** A mutable map that stores the hexagons in the grid. */
    val map: MutableMap<OffsetCoordinate, T> = mutableMapOf()

    init {
        observableComponents.setInternalListenerAndInvoke(emptyList()) { _, _ ->
            layout(coordinateSystem)
        }
    }

    /**
     * Gets the hexagon at the specified column index and row index.
     *
     * @param columnIndex The column index of the hexagon.
     * @param rowIndex The row index of the hexagon.
     * @return The hexagon at the specified coordinates, or null if no hexagon is found.
     */
    operator fun get(columnIndex: Int, rowIndex: Int): T? = map[columnIndex to rowIndex]

    /**
     * Sets the hexagon at the specified column index and row index.
     *
     * @param columnIndex The column index of the hexagon.
     * @param rowIndex The row index of the hexagon.
     * @param component The hexagon component to set.
     */
    operator fun set(columnIndex: Int, rowIndex: Int, component: T) {
        map[columnIndex to rowIndex]?.run { observableComponents.remove(this) }
        component.orientation = orientation
        map[columnIndex to rowIndex] = component
        observableComponents.add(component)
    }

    /**
     * Internal function to layout the hexagons in the grid based on the specified coordinate system.
     *
     * @param coordinateSystem The coordinate system to use for the layout.
     */
    private fun layout(coordinateSystem: CoordinateSystem) {
        map.forEach { (coords, hexagon) ->
            val (x, y) = coords
            val (q, r) =
                when (coordinateSystem) {
                    CoordinateSystem.OFFSET -> {
                        if (orientation == HexOrientation.POINTY_TOP) x to y else y to x
                    }
                    CoordinateSystem.AXIAL -> {
                        if (orientation == HexOrientation.POINTY_TOP) x + (y - (y and 1)) / 2 to y
                        else y + (x - (x and 1)) / 2 to x
                    }
                }
            with(hexagon) {
                if (orientation == HexOrientation.POINTY_TOP) {
                    hexagon.orientation = HexOrientation.POINTY_TOP
                    val actualWidth = width / 2 * sqrt(3.0)
                    posXProperty.setSilent(actualWidth * q + if (r % 2 == 0) 0.0 else actualWidth / 2)
                    posYProperty.setSilent(height * r - r * height / 4)
                } else {
                    hexagon.orientation = HexOrientation.FLAT_TOP
                    val actualHeight = height / 2 * sqrt(3.0)
                    posYProperty.setSilent(actualHeight * q + if (r % 2 == 0) 0.0 else actualHeight / 2)
                    posXProperty.setSilent(width * r - r * width / 4)
                }
            }
        }
    }

    override fun T.onRemove() {}
    override fun T.onAdd() {}

    /** Enumeration class representing the coordinate system options for the hexagon grid. */
    enum class CoordinateSystem {
        OFFSET,
        AXIAL
    }
}
