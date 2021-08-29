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

import tools.aqua.bgw.core.DEFAULT_DICE_HEIGHT
import tools.aqua.bgw.core.DEFAULT_DICE_WIDTH
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.visual.Visual

/**
 * A [DiceView] may be used to visualize a dice.
 *
 * Visualization:
 *
 * The Visual at the [currentSide] value is used to visualize the dice.
 *
 * @constructor Creates a [DiceView] with given [Visual]s.
 *
 * @param posX Horizontal coordinate for this [DiceView]. Default: 0.
 * @param posY Vertical coordinate for this [DiceView]. Default: 0.
 * @param width Width for this [DiceView]. Default: [DEFAULT_DICE_WIDTH].
 * @param height Height for this [DiceView]. Default: [DEFAULT_DICE_HEIGHT].
 * @param visuals List of visuals to represent the sides of the die.
 */
open class DiceView(
	posX: Number = 0,
	posY: Number = 0,
	width: Number = DEFAULT_DICE_WIDTH,
	height: Number = DEFAULT_DICE_HEIGHT,
	visuals: List<Visual>
) : GameComponentView(posX = posX, posY = posY, width = width, height = height, visual = Visual.EMPTY) {
	
	/**
	 * [Visual]s for this [DiceView].
	 */
	internal val visuals: ObservableArrayList<Visual> = ObservableArrayList(visuals.onEach { it.copy() })
	
	/**
	 * Current side that is displayed, 0-based.
	 *
	 * If index is greater than the amount of visuals stored in [visuals] or negative,
	 * [Visual.EMPTY] will be displayed.
	 */
	var currentSide: Int = 0
		set(value) {
			if (field != value) {
				field = value
				
				visualProperty.value = if (value in visuals.indices)
					visuals[value]
				else
					Visual.EMPTY
			}
		}
	
	init {
		visualProperty.value = if (visuals.isNotEmpty())
			visuals.first()
		else
			Visual.EMPTY
	}
	
	/**
	 * Sets all visuals for this DiceView.
	 * Clears old visuals.
	 * All [visuals] get copied before being added.
	 * If [currentSide] is out of range, a [Visual.EMPTY] will be shown.
	 */
	fun setVisuals(visuals: List<Visual>) {
		val snapshot = visuals.toList()
		this.visuals.setSilent(visuals.onEach { it.copy() })
		this.visuals.notifyChange(oldValue = snapshot, newValue = visuals.toList())
		
		visualProperty.value = if (currentSide in visuals.indices)
			visuals[currentSide]
		else
			Visual.EMPTY
	}
}