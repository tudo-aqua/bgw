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

package tools.aqua.bgw.elements.gameelements

import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.Visual

/**
 * Abstract baseclass for game elements like [CardView]s or [TokenView]s.
 * This class is used to restrict the type argument of containers.
 *
 * @param height height for this [GameElementView].
 * @param width width for this [GameElementView].
 * @param posX horizontal coordinate for this [GameElementView].
 * @param posY vertical coordinate for this [GameElementView].
 * @param visual visual for this [GameElementView].
 *
 * @see tools.aqua.bgw.elements.container
 */
sealed class GameElementView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : DynamicView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	visual = visual
) {
	/**
	 * @throws RuntimeException [GameElementView] does not support children.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this element has no children.")
	}
}