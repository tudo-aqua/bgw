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

package tools.aqua.bgw.components.layoutviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all layout component.
 *
 * @param posX horizontal coordinate for this [LayoutView].
 * @param posY vertical coordinate for this [LayoutView].
 * @param width width for this [LayoutView].
 * @param height height for this [LayoutView].
 * @param visual initial visual for this [LayoutView].
 */
sealed class LayoutView<T : ComponentView>(
	posX: Number,
	posY: Number,
	width: Number,
	height: Number,
	visual: Visual
) : StaticComponentView<T>(posX, posY, width, height, visual)