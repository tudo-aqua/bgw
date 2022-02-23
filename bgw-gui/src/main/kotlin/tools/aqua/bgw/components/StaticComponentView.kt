/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

package tools.aqua.bgw.components

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [ComponentView]s that are considered static.
 *
 * This class is used to distinguish between [ComponentView]s that can be used in [MenuScene]s and
 * those that can't.
 *
 * Only StaticViews are allowed in [MenuScene]s.
 *
 * @constructor Creates a [StaticComponentView].
 *
 * @param T Generic [ComponentView].
 * @param posX The X coordinate for this [StaticComponentView] relative to its container.
 * @param posY The Y coordinate for this [StaticComponentView] relative to its container.
 * @param width Width for this [StaticComponentView].
 * @param height Height for this [StaticComponentView].
 * @param visual Visual for this [StaticComponentView].
 *
 * @see MenuScene
 */
abstract class StaticComponentView<T : ComponentView>
internal constructor(posX: Number, posY: Number, width: Number, height: Number, visual: Visual) :
    ComponentView(posX = posX, posY = posY, width = width, height = height, visual = visual)
