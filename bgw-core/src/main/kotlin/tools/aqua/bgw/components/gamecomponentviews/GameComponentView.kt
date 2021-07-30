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

package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.visual.Visual

/**
 * Abstract baseclass for game components like [CardView]s or [TokenView]s.
 * This class is used to restrict the type argument of containers.
 *
 * @param height height for this [GameComponentView].
 * @param width width for this [GameComponentView].
 * @param posX horizontal coordinate for this [GameComponentView].
 * @param posY vertical coordinate for this [GameComponentView].
 * @param visual visual for this [GameComponentView].
 *
 * @see tools.aqua.bgw.components.container
 */
sealed class GameComponentView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	visual: Visual
) : DynamicComponentView(
	height = height,
	width = width,
	posX = posX,
	posY = posY,
	visual = visual
) {
	/**
	 * @throws RuntimeException [GameComponentView] does not support children.
	 */
	override fun removeChild(component: ComponentView) {
		throw RuntimeException("This $this component has no children.")
	}
}