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

package tools.aqua.bgw.components.gamecomponentviews

import kotlin.math.sqrt
import tools.aqua.bgw.core.DEFAULT_HEXAGON_SIZE
import tools.aqua.bgw.visual.Visual

/**
 * A [HexagonView] represents a hexagonal shaped game component view.
 *
 * @constructor Creates a [HexagonView] with a given [Visual].
 *
 * @param posX Horizontal coordinate for this [HexagonView]. Default: 0.
 * @param posY Vertical coordinate for this [HexagonView]. Default: 0.
 * @param size Represents the radius of the outer circle of the [HexagonView] all six points lie on.
 * Default: [DEFAULT_HEXAGON_SIZE].
 * @param visual Visual for this [HexagonView].
 */
open class HexagonView(
    posX: Number = 0,
    posY: Number = 0,
    val size: Number = DEFAULT_HEXAGON_SIZE,
    visual: Visual
) :
    GameComponentView(
        posX = posX,
        posY = posY,
        width = 2 * size.toDouble(),
        height = 2 * size.toDouble(),
        visual = visual)
