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

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Scene

/**
 * A movement animation.
 * Moves given [ComponentView] relative to parents anchor point.
 *
 * @param componentView [ComponentView] to animate
 * @param fromX initial X position. Default: Current [ComponentView.posX].
 * @param toX resulting X position. Default: Current [ComponentView.posX].
 * @param fromY initial Y position. Default: Current [ComponentView.posY].
 * @param toY resulting Y position. Default: Current [ComponentView.posY].
 * @param duration duration in milliseconds. Default: 1 second.
 */
class MovementAnimation<T : ComponentView>(
	componentView: T,
	fromX: Number = componentView.posX,
	toX: Number = componentView.posX,
	fromY: Number = componentView.posY,
	toY: Number = componentView.posY,
	duration: Int = 1000
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {
	
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
	 * Moves given [ComponentView] relative to parents anchor point.
	 *
	 * @param componentView [ComponentView] to animate
	 * @param byX Relative X movement.
	 * @param byY Relative Y movement.
	 * @param duration [Animation] duration in milliseconds. Default: 1 second
	 */
	constructor(componentView: T, byX: Number = 0.0, byY: Number = 0.0, duration: Int = 1000) : this(
		componentView = componentView,
		toX = componentView.posX + byX.toDouble(),
		toY = componentView.posY + byY.toDouble(),
		duration = duration
	)
	
	companion object {
		/**
		 * Creates a [MovementAnimation] to another component's position.
		 * Moves given [ComponentView] relative to parents anchor point.
		 *
		 * @param componentView [ComponentView] to animate
		 * @param toComponentViewPosition Defines the destination [ComponentView] to move the given component to.
		 * @param scene The [Scene].
		 * @param duration [Animation] duration in milliseconds. Default: 1 second
		 */
		fun <T : ComponentView> toComponentView(
			componentView: T,
			toComponentViewPosition: T,
			scene: Scene<*>,
			duration: Int = 1000
		): MovementAnimation<T> {
			//Find visual tree for components and drop root node
			val pathToComponent = scene.findPathToChild(componentView).dropLast(1)
			val pathToDestination = scene.findPathToChild(toComponentViewPosition).dropLast(1)
			
			//Sum relative positions
			val componentAbsoluteX = pathToComponent.sumOf { it.posX }
			val componentAbsoluteY = pathToComponent.sumOf { it.posY }
			val destinationAbsoluteX = pathToDestination.sumOf { it.posX }
			val destinationAbsoluteY = pathToDestination.sumOf { it.posY }
			
			return MovementAnimation(
				componentView = componentView,
				byX = destinationAbsoluteX - componentAbsoluteX,
				byY = destinationAbsoluteY - componentAbsoluteY,
				duration = duration
			)
		}
	}
}