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
import tools.aqua.bgw.visual.Visual

private typealias OffsetCoordinate = Pair<Int, Int>
private typealias AxialCoordinate = Pair<Int, Int>

class HexagonGrid<T : HexagonView>(
    posX: Number = 0,
    posY: Number = 0,
    /* TODO: Grow width and height according to components or set it to rows and cols times component width and height */
    width: Number = 0,
    height: Number = 0,
    visual: Visual = Visual.EMPTY,
    coordinateSystem: CoordinateSystem = CoordinateSystem.OFFSET
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual
    ) {
    val map: MutableMap<OffsetCoordinate, T> = mutableMapOf()
    operator fun get(columnIndex: Int, rowIndex: Int): T? = map[columnIndex to rowIndex]
    operator fun set(columnIndex: Int, rowIndex: Int, component: T) {
        map[columnIndex to rowIndex] = component
        /* TODO: Remove if component was replaced */
        observableComponents.add(component)
    }

    init {
        observableComponents.setInternalListenerAndInvoke(emptyList()) { _, _ -> layout(coordinateSystem) }
    }

    private fun layout(coordinateSystem: CoordinateSystem) {
        /* TODO: Pass this as a constructor argument */
        val (rows, cols) = 5 to 5
        for (y in 0 until rows) {
            for (x in 0 until cols) {
                map[x to y]?.run {
                    posXProperty.setSilent(width * x + if (y % 2 == 0) 0.0 else width / 2)
                    posYProperty.setSilent(height * y - y * height / 4)
                }
            }
        }
    }

    override fun T.onRemove() {}
    override fun T.onAdd() {}

    enum class CoordinateSystem {
        OFFSET,
        AXIAL;
    }

}