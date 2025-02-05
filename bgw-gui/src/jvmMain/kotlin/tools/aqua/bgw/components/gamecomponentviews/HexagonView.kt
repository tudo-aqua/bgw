/*
 * Copyright 2023-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.core.DEFAULT_HEXAGON_SIZE
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.Visual

/**
 * A [HexagonView] represents a hexagonal shaped game component view.
 *
 * @constructor Creates a [HexagonView] with a given [Visual].
 *
 * @param posX Horizontal coordinate for this [HexagonView]. Default: 0.
 * @param posY Vertical coordinate for this [HexagonView]. Default: 0.
 * @param size Represents the distance to the outermost corner of the [HexagonView]. Default:
 * [DEFAULT_HEXAGON_SIZE].
 * @param visual Visual for this [HexagonView].
 * @param orientation Orientation of the [HexagonView]. Default: [HexOrientation.POINTY_TOP].
 *
 * @see HexOrientation
 * @see HexagonView
 *
 * @since 0.8
 */
open class HexagonView(
    posX: Number = 0,
    posY: Number = 0,
    size: Number = DEFAULT_HEXAGON_SIZE,
    visual: Visual,

    /**
     * Orientation of the [HexagonView].
     *
     * @see HexOrientation
     *
     * @since 0.10
     */
    var orientation: HexOrientation = HexOrientation.POINTY_TOP
) :
    GameComponentView(
        posX = posX,
        posY = posY,
        width = 2 * size.toDouble(),
        height = 2 * size.toDouble(),
        visual = visual) {

  /** [Property] for the size of the [HexagonView]. */
  internal val sizeProperty: DoubleProperty = DoubleProperty(size.toDouble())

  /**
   * Size of the [HexagonView]. For [HexOrientation.POINTY_TOP] this is the distance from the center
   * to the top or bottom corner representing half the height of the container. For
   * [HexOrientation.FLAT_TOP] this is the distance from the center to the left or right corner
   * representing half the width of the container.
   */
  var size: Double
    get() = sizeProperty.value
    set(value) {
      sizeProperty.value = value
    }
}
