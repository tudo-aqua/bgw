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

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all layout elements.
 *
 * @param height height for this [LayoutElement].
 * @param width width for this [LayoutElement].
 * @param posX horizontal coordinate for this [LayoutElement].
 * @param posY vertical coordinate for this [LayoutElement].
 * @param visual initial visual for this [LayoutElement].
 */
sealed class LayoutElement<T : ElementView>(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : StaticView<T>(height = height, width = width, posX = posX, posY = posY, visual = visual)