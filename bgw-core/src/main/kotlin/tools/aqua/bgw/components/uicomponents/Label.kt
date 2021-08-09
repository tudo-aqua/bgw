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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * A basic [Label] displaying text.
 *
 * @param posX horizontal coordinate for this [Label]. Default: 0.
 * @param posY vertical coordinate for this [Label]. Default: 0.
 * @param width width for this [Label]. Default: [Label.DEFAULT_LABEL_WIDTH].
 * @param height height for this [Label]. Default: [Label.DEFAULT_LABEL_HEIGHT].
 * @param label label for this [Label]. Default: empty String.
 * @param font font to be used for the [text]. Default: default [Font] constructor.
 * @param alignment alignment to be used for the [text] Default: [Alignment.CENTER].
 * @param visual background [Visual]. Default: [Visual.EMPTY]
 */
open class Label(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_LABEL_WIDTH,
	height: Number = DEFAULT_LABEL_HEIGHT,
	label: String = "",
	font: Font = Font(),
	alignment: Alignment = Alignment.CENTER,
	visual: Visual = Visual.EMPTY
) : LabeledUIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	label = label,
	font = font,
	alignment = alignment,
	visual = visual) {
	/**
	 * Defines some static constants that can be used as suggested properties of a [Label].
	 */
	companion object {
		/**
		 * Suggested [Label] [height].
		 */
		const val DEFAULT_LABEL_HEIGHT: Int = 30
		
		/**
		 * Suggested [Label] [width].
		 */
		const val DEFAULT_LABEL_WIDTH: Int = 120
	}


}