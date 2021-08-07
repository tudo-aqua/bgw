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

import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * A [ColorPicker] that allows to choose a [Color].
 *
 * @param posX horizontal coordinate for this [ColorPicker]. Default: 0.
 * @param posY vertical coordinate for this [ColorPicker]. Default: 0.
 * @param width width for this [ColorPicker]. Default: [ColorPicker.DEFAULT_COLOR_PICKER_WIDTH].
 * @param height height for this [ColorPicker]. Default: [ColorPicker.DEFAULT_COLOR_PICKER_HEIGHT].
 * @param initialColor the color that is initially selected. Default: [Color.WHITE].
 */
open class ColorPicker(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_COLOR_PICKER_WIDTH,
	height: Number = DEFAULT_COLOR_PICKER_HEIGHT,
	initialColor: Color = Color.WHITE
) : UIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = Font(),
	visual = Visual.EMPTY) {
	/**
	 * [tools.aqua.bgw.observable.Property] for the currently selected [Color].
	 */
	val selectedColorProperty: Property<Color> = Property(initialColor)
	
	/**
	 * The currently selected [Color].
	 */
	var selectedColor: Color
		get() = selectedColorProperty.value
		set(value) {
			selectedColorProperty.value = value
		}

	/**
	 * Defines some static constants that can be used as suggested properties of a [ColorPicker].
	 */
	companion object {
		/**
		 * Suggested [ColorPicker] [height].
		 */
		const val DEFAULT_COLOR_PICKER_HEIGHT: Int = 30
		
		/**
		 * Suggested [ColorPicker] [width].
		 */
		const val DEFAULT_COLOR_PICKER_WIDTH: Int = 120
	}
}