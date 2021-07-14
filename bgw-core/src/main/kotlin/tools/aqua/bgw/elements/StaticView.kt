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

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all [ElementView]s that are considered static.
 *
 * This class is used to distinguish between [ElementView]s that can be used in [MenuScene]s
 * and those that can't.
 *
 * Only StaticViews are allowed in [MenuScene]s.
 *
 * @param height height for this [StaticView].
 * @param width width for this [StaticView].
 * @param posX the X coordinate for this [StaticView] relative to its container.
 * @param posY the Y coordinate for this [StaticView] relative to its container.
 * @param visual visual for this [StaticView].
 *
 * @see MenuScene
 */
abstract class StaticView<T : ElementView>(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : ElementView(height = height, width = width, posX = posX, posY = posY, visual = visual)