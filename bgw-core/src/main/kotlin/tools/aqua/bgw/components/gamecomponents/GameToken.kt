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

package tools.aqua.bgw.components.gamecomponents

import tools.aqua.bgw.components.gamecomponents.GameToken.Companion.DEFAULT_TOKEN_HEIGHT
import tools.aqua.bgw.components.gamecomponents.GameToken.Companion.DEFAULT_TOKEN_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * A [GameToken] may be used to visualize any kind of token.
 *
 * Visualization:
 * The current [Visual] is used to visualize the token.
 *
 * @param height height for this TokenView. Default: [DEFAULT_TOKEN_HEIGHT].
 * @param width width for this TokenView. Default: [DEFAULT_TOKEN_WIDTH].
 * @param posX horizontal coordinate for this TokenView. Default: 0.
 * @param posY vertical coordinate for this TokenView. Default: 0.
 * @param visual visual for this TokenView.
 */
open class GameToken(
	height: Number = DEFAULT_TOKEN_HEIGHT,
	width: Number = DEFAULT_TOKEN_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual
) : GameComponent(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [GameToken].
	 */
	companion object {
		/**
		 * Suggested [GameToken] [height].
		 */
		const val DEFAULT_TOKEN_HEIGHT: Int = 50
		
		/**
		 * Suggested [GameToken] [width].
		 */
		const val DEFAULT_TOKEN_WIDTH: Int = 50
	}
}