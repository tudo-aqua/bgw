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

import tools.aqua.bgw.core.DEFAULT_PROGRESSBAR_HEIGHT
import tools.aqua.bgw.core.DEFAULT_PROGRESSBAR_WIDTH
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual
import java.awt.Color

/**
 * A [ProgressBar].
 *
 * @constructor Creates a [ProgressBar].
 *
 * @param posX Horizontal coordinate for this [ProgressBar]. Default: 0.
 * @param posY Vertical coordinate for this [ProgressBar]. Default: 0.
 * @param width Width for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_WIDTH].
 * @param height Height for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_HEIGHT].
 * @param progress The initial progress of this [ProgressBar]. Default 0.
 * @param barColor The initial bar color of this [ProgressBar]. Default [Color.CYAN].
 */
open class ProgressBar(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_PROGRESSBAR_WIDTH,
	height: Number = DEFAULT_PROGRESSBAR_HEIGHT,
	progress: Double = 0.0,
	barColor: Color = Color.CYAN
) : UIComponent(
	posX = posX,
	posY = posY,
	width = width,
	height = height,
	font = Font(),
	visual = Visual.EMPTY) {
	/**
	 * [Property] for the progress state of this [ProgressBar].
	 *
	 * Should be in range of 0 to 1.
	 *
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress. Any value
	 * less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 *
	 * @see progress
	 */
	val progressProperty: DoubleProperty = DoubleProperty(progress)
	
	/**
	 * Progress state of this [ProgressBar].
	 *
	 * Should be in range of 0 to 1.
	 *
	 * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100% progress. Any value
	 * less than 0 gets represented as 0% progress, while any value greater than 1 gets represented as 100% progress.
	 *
	 * @see progressProperty
	 */
	var progress: Double
		get() = progressProperty.value
		set(value) {
			progressProperty.value = value
		}
	
	/**
	 * [Property] for the bar [Color] of this [ProgressBar].
	 *
	 * @see barColor
	 */
	val barColorProperty: Property<Color> = Property(barColor)
	
	/**
	 * Bar [Color] of this [ProgressBar].
	 *
	 * @see barColorProperty
	 */
	var barColor: Color
		get() = barColorProperty.value
		set(value) {
			barColorProperty.value = value
		}
}