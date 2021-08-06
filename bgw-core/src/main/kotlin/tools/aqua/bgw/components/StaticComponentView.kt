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

package tools.aqua.bgw.components

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [ComponentView]s that are considered static.
 *
 * This class is used to distinguish between [ComponentView]s that can be used in [MenuScene]s
 * and those that can't.
 *
 * Only StaticViews are allowed in [MenuScene]s.
 *
 * @param width width for this [StaticComponentView].
 * @param height height for this [StaticComponentView].
 * @param posX the X coordinate for this [StaticComponentView] relative to its container.
 * @param posY the Y coordinate for this [StaticComponentView] relative to its container.
 * @param visual visual for this [StaticComponentView].
 *
 * @see MenuScene
 */
abstract class StaticComponentView<T : ComponentView>(
	width: Number,
	height: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : ComponentView(width = width, height = height, posX = posX, posY = posY, visual = visual)