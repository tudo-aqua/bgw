/*
 * Copyright 2023-2026 The BoardGameWork Authors
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

import kotlin.math.sqrt
import tools.aqua.bgw.components.container.HexagonGrid.CoordinateSystem
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.Visual

private typealias HexCoordinate = Pair<Int, Int>

/**
 * A class representing a grid of hexagons.
 *
 * @param posX The x-coordinate of the hexagon grid's position on the screen. Default is 0.
 * @param posY The y-coordinate of the hexagon grid's position on the screen. Default is 0.
 * @param width The width of the hexagon grid. It grows dynamically by the amount hexagons in it.
 * @param height The height of the hexagon grid. It grows dynamically by the amount hexagons in it.
 * @param visual The visual representation of the hexagon grid. Default is an empty visual.
 * @param coordinateSystem The coordinate system to use for the grid. Default is
 *   [CoordinateSystem.OFFSET].
 * @param orientation The orientation of the hexagons in the grid. Default is
 *   [HexOrientation.POINTY_TOP].
 * @see CoordinateSystem
 * @see HexOrientation
 * @see HexagonView
 * @see Visual
 * @see GameComponentContainer
 * @since 0.8
 */
class HexagonGrid<T : HexagonView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = 0,
    height: Number = 0,
    visual: Visual = Visual.EMPTY,

    /** The coordinate system to use for the grid. Default is [CoordinateSystem.OFFSET]. */
    val coordinateSystem: CoordinateSystem = CoordinateSystem.OFFSET,

    /**
     * The orientation of the hexagons in the grid. Default is [HexOrientation.POINTY_TOP].
     *
     * @since 0.10
     */
    orientation: HexOrientation = HexOrientation.POINTY_TOP
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual) {

  /** A mutable map that stores the hexagons in the grid. */
  internal val map: MutableMap<HexCoordinate, T> = mutableMapOf()

  /** Property for the orientation of all hexagons in this grid. */
  internal val orientationProperty: Property<HexOrientation> = Property(orientation)

  /** Orientation of all hexagons in this grid. */
  var orientation: HexOrientation
    get() = orientationProperty.value
    set(value) {
      orientationProperty.value = value
    }

  init {
    observableComponents.setInternalListenerAndInvoke(emptyList()) { _, _ ->
      layout(coordinateSystem)
    }
    orientationProperty.internalListener = { _, _ -> layout(coordinateSystem) }
  }

  /**
   * Gets the hexagon at the specified column index and row index.
   *
   * @param columnIndex The column index of the hexagon.
   * @param rowIndex The row index of the hexagon.
   * @return The hexagon at the specified coordinates, or null if no hexagon is found.
   * @see components
   */
  operator fun get(columnIndex: Int, rowIndex: Int): T? = map[columnIndex to rowIndex]

  /**
   * Sets the hexagon at the specified column index and row index.
   *
   * @param columnIndex The column index of the hexagon.
   * @param rowIndex The row index of the hexagon.
   * @param component The hexagon component to set.
   * @see components
   */
  operator fun set(columnIndex: Int, rowIndex: Int, component: T) {
    val coordinate = columnIndex to rowIndex
    val previous = map[coordinate]
    if (previous === component) return

    require(component.parent == null) {
      "Component $component is already contained in another container."
    }

    previous?.let { remove(it) }
    component.orientation = orientation
    map[coordinate] = component
    component.onAdd()
    observableComponents.add(component)
    onAdd?.invoke(component)
  }

  /**
   * Returns all hexagons in the grid as a map from [HexCoordinate] to [T].
   *
   * @return A map from [HexCoordinate] to [T] containing all hexagons in the grid.
   * @see components
   * @since 0.10
   */
  fun getCoordinateMap(): Map<HexCoordinate, T> {
    return map.toMap()
  }

  /**
   * Removes the hexagon at the specified column index and row index.
   *
   * @param columnIndex The column index of the hexagon.
   * @param rowIndex The row index of the hexagon.
   * @see components
   * @since 0.10
   */
  fun remove(columnIndex: Int, rowIndex: Int) {
    map[columnIndex to rowIndex]?.let { remove(it) }
  }

  /**
   * Internal function to lay out the hexagons in the grid based on the specified coordinate system.
   *
   * @param coordinateSystem The coordinate system to use for the layout.
   */
  private fun layout(coordinateSystem: CoordinateSystem) {
    if (map.isEmpty()) {
      widthProperty.setSilent(0.0)
      heightProperty.setSilent(0.0)
      return
    }

    var minX = Double.MAX_VALUE
    var minY = Double.MAX_VALUE

    var maxX = Double.NEGATIVE_INFINITY
    var maxY = Double.NEGATIVE_INFINITY

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
          val hexWidth = width / 2 * sqrt(3.0)
          inParentPosXProperty.setSilent(hexWidth * q + if (r % 2 == 0) 0.0 else hexWidth / 2)
          inParentPosYProperty.setSilent(height * r - r * height / 4)

          if (inParentPosXProperty.value < minX) minX = inParentPosXProperty.value
          if (inParentPosYProperty.value < minY) minY = inParentPosYProperty.value

          if (inParentPosXProperty.value + hexWidth > maxX)
              maxX = inParentPosXProperty.value + hexWidth
          if (inParentPosYProperty.value + height > maxY) maxY = inParentPosYProperty.value + height
        } else {
          hexagon.orientation = HexOrientation.FLAT_TOP
          val hexHeight = height / 2 * sqrt(3.0)
          inParentPosYProperty.setSilent(hexHeight * q + if (r % 2 == 0) 0.0 else hexHeight / 2)
          inParentPosXProperty.setSilent(width * r - r * width / 4)

          if (inParentPosXProperty.value < minX) minX = inParentPosXProperty.value
          if (inParentPosYProperty.value < minY) minY = inParentPosYProperty.value

          if (inParentPosXProperty.value + width > maxX) maxX = inParentPosXProperty.value + width
          if (inParentPosYProperty.value + hexHeight > maxY)
              maxY = inParentPosYProperty.value + hexHeight
        }
      }
    }

    widthProperty.setSilent(maxX - minX)
    heightProperty.setSilent(maxY - minY)

    components.forEach {
      it.inParentPosXProperty.setSilent(it.inParentPosXProperty.value - minX)
      it.inParentPosYProperty.setSilent(it.inParentPosYProperty.value - minY)
    }
  }

  override fun T.onRemove() {
    widthProperty.internalListener = null
    heightProperty.internalListener = null
    map.entries.firstOrNull { it.value === this }?.key?.let { map.remove(it) }
    layout(coordinateSystem)
  }

  override fun T.onAdd() {
    this.parent = this@HexagonGrid
    widthProperty.internalListener = { _, _ -> layout(coordinateSystem) }
    heightProperty.internalListener = { _, _ -> layout(coordinateSystem) }
  }

  /** Enumeration class representing the coordinate system options for the hexagon grid. */
  enum class CoordinateSystem {
    OFFSET,
    AXIAL
  }
}
