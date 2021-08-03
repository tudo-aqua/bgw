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
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font

/**
 * A basic [Label] displaying text.
 *
 * @param height height for this [Label]. Default: [Label.DEFAULT_LABEL_HEIGHT].
 * @param width width for this [Label]. Default: [Label.DEFAULT_LABEL_WIDTH].
 * @param posX horizontal coordinate for this [Label]. Default: 0.
 * @param posY vertical coordinate for this [Label]. Default: 0.
 * @param label label for this [Label]. Default: empty String.
 * @param font font to be used for the [label]. Default: default [Font] constructor.
 * @param alignment alignment to be used for the [label] Default: [Alignment.CENTER].
 */
open class Label(
	height: Number = DEFAULT_LABEL_HEIGHT,
	width: Number = DEFAULT_LABEL_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	label: String = "",
	font: Font = Font(),
	alignment: Alignment = Alignment.CENTER,
) : LabeledUIComponent(height = height, width = width, posX = posX, posY = posY, label = label, font = font, alignment = alignment) {
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