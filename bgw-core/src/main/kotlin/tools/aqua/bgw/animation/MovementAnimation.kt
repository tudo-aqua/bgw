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

package tools.aqua.bgw.animation

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView

/**
 * A movement animation.
 * Moves given [ElementView] relative to parents anchor point.
 *
 * @param elementView [ElementView] to animate
 * @param fromX initial X position. Default: Current [ElementView.posX].
 * @param toX resulting X position. Default: Current [ElementView.posX].
 * @param fromY initial Y position. Default: Current [ElementView.posY].
 * @param toY resulting Y position. Default: Current [ElementView.posY].
 * @param duration duration in milliseconds. Default: 1 second.
 */
class MovementAnimation<T : ElementView>(
	elementView: T,
	fromX: Number = elementView.posX,
	toX: Number = elementView.posX,
	fromY: Number = elementView.posY,
	toY: Number = elementView.posY,
	duration: Int = 1000
) : ElementAnimation<T>(element = elementView, duration = duration) {
	
	/**
	 * Initial X position.
	 */
	val fromX: Double = fromX.toDouble()
	
	/**
	 * Resulting X position.
	 */
	val toX: Double = toX.toDouble()
	
	/**
	 * Initial Y position.
	 */
	val fromY: Double = fromY.toDouble()
	
	/**
	 * Resulting Y position.
	 */
	val toY: Double = toY.toDouble()
	
	/**
	 * A movement animation.
	 * Moves given [ElementView] relative to parents anchor point.
	 *
	 * @param elementView [ElementView] to animate
	 * @param byX Relative X movement.
	 * @param byY Relative Y movement.
	 * @param duration [Animation] duration in milliseconds. Default: 1 second
	 */
	constructor(elementView: T, byX: Number = 0.0, byY: Number = 0.0, duration: Int = 1000) : this(
		elementView = elementView,
		toX = elementView.posX + byX.toDouble(),
		toY = elementView.posY + byY.toDouble(),
		duration = duration
	)
	
	companion object {
		/**
		 * Creates a [MovementAnimation] to another element's position.
		 * Moves given [ElementView] relative to parents anchor point.
		 *
		 * @param elementView [ElementView] to animate
		 * @param toElementViewPosition Defines the destination [ElementView] to move the given element to.
		 * @param scene The [Scene].
		 * @param duration [Animation] duration in milliseconds. Default: 1 second
		 */
		fun <T : ElementView> toElementView(
			elementView: T,
			toElementViewPosition: T,
			scene: Scene<*>,
			duration: Int = 1000
		): MovementAnimation<T> {
			//Find visual tree for elements and drop root node
			val pathToElement = scene.findPathToChild(elementView).dropLast(1)
			val pathToDestination = scene.findPathToChild(toElementViewPosition).dropLast(1)
			
			//Sum relative positions
			val elementAbsoluteX = pathToElement.sumOf { it.posX }
			val elementAbsoluteY = pathToElement.sumOf { it.posY }
			val destinationAbsoluteX = pathToDestination.sumOf { it.posX }
			val destinationAbsoluteY = pathToDestination.sumOf { it.posY }
			
			return MovementAnimation(
				elementView = elementView,
				byX = destinationAbsoluteX - elementAbsoluteX,
				byY = destinationAbsoluteY - elementAbsoluteY,
				duration = duration
			)
		}
	}
}