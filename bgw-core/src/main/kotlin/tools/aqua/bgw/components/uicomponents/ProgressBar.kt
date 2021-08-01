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

import tools.aqua.bgw.observable.DoubleProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.util.Font
import java.awt.Color

/**
 * A [ProgressBar].
 *
 * @param height height for this [ProgressBar]. Default: [ProgressBar.DEFAULT_PROGRESSBAR_HEIGHT].
 * @param width width for this [ProgressBar]. Default: [ProgressBar.DEFAULT_PROGRESSBAR_WIDTH].
 * @param posX horizontal coordinate for this [ProgressBar]. Default: 0.
 * @param posY vertical coordinate for this [ProgressBar]. Default: 0.
 * @param progress the initial progress of this [ProgressBar]. Default 0.
 * @param barColor the initial bar color of this [ProgressBar]. Default [Color.CYAN].
 */
open class ProgressBar(
	height: Number = DEFAULT_PROGRESSBAR_HEIGHT,
	width: Number = DEFAULT_PROGRESSBAR_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	progress: Double = 0.0,
	barColor: Color = Color.CYAN
) : UIComponent(height = height, width = width, posX = posX, posY = posY, font = Font()) {
	
	/**
	 * [Property] for the progress state of this [ProgressBar].
	 * Should be in range of 0 to 1.
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress.
	 * Any value less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 */
	val progressProperty: DoubleProperty = DoubleProperty(progress)
	
	/**
	 * Progress state of this [ProgressBar].
	 * Should be in range of 0 to 1.
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress.
	 * Any value less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 * @see progressProperty
	 */
	var progress: Double
		get() = progressProperty.value
		set(value) {
			progressProperty.value = value
		}
	
	/**
	 * [Property] for the bar color of this [ProgressBar].
	 */
	val barColorProperty: Property<Color> = Property(barColor)
	
	/**
	 * Bar color of this [ProgressBar].
	 * @see barColorProperty
	 */
	var barColor: Color
		get() = barColorProperty.value
		set(value) {
			barColorProperty.value = value
		}
	
	/**
	 * Defines some static constants that can be used as suggested properties of a [ProgressBar].
	 */
	companion object {
		/**
		 * Suggested [ProgressBar] [height].
		 */
		const val DEFAULT_PROGRESSBAR_HEIGHT: Int = 20
		
		/**
		 * Suggested [ProgressBar] [width].
		 */
		const val DEFAULT_PROGRESSBAR_WIDTH: Int = 250
	}
}