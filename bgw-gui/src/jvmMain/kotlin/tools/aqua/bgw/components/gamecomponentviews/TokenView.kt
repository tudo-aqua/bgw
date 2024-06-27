/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.core.DEFAULT_TOKEN_HEIGHT
import tools.aqua.bgw.core.DEFAULT_TOKEN_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * A [TokenView] may be used to visualize any kind of token.
 *
 * Visualization:
 *
 * The current [Visual] is used to visualize the token.
 *
 * @constructor Creates a [TokenView] with given [Visual].
 *
 * @param posX Horizontal coordinate for this TokenView. Default: 0.
 * @param posY Vertical coordinate for this TokenView. Default: 0.
 * @param width Width for this TokenView. Default: [DEFAULT_TOKEN_WIDTH].
 * @param height Height for this TokenView. Default: [DEFAULT_TOKEN_HEIGHT].
 * @param visual Visual for this TokenView.
 */
open class TokenView(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_TOKEN_WIDTH,
    height: Number = DEFAULT_TOKEN_HEIGHT,
    visual: Visual
) : GameComponentView(posX = posX, posY = posY, width = width, height = height, visual = visual)
