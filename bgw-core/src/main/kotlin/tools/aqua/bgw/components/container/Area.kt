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

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.core.DEFAULT_AREA_HEIGHT
import tools.aqua.bgw.core.DEFAULT_AREA_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * An [Area] may be used to visualize a zone containing [GameComponentView]s.
 *
 * Visualization:
 *
 * The [Visual] is used to visualize a background.
 *
 * The positioning of the contained [GameComponentView]s is used to place them relative
 * to the top left corner of this [Area].
 *
 * Components that are out of bounds for this [Area] will still get rendered.
 *
 * @param posX Horizontal coordinate for this [Area]. Default: 0.
 * @param posY Vertical coordinate for this [Area]. Default: 0.
 * @param width Width for this [Area]. Default: [DEFAULT_AREA_WIDTH].
 * @param height Height for this [Area]. Default: [DEFAULT_AREA_HEIGHT].
 * @param visual Visual for this [Area]. Default: [Visual.EMPTY].
 */
open class Area<T : GameComponentView>(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_AREA_WIDTH,
	height: Number = DEFAULT_AREA_HEIGHT,
	visual: Visual = Visual.EMPTY
) : GameComponentContainer<T>(posX = posX, posY = posY, width = width, height = height, visual = visual) {
	/**
	 * Internal onRemove handler.
	 */
	override fun T.onRemove() = Unit
	
	/**
	 * Internal onAdd handler.
	 */
	override fun T.onAdd() = Unit
}
