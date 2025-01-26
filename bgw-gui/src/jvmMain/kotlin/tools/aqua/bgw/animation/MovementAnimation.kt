/*
 * Copyright 2021-2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.util.Coordinate

/**
 * A movement [Animation].
 *
 * Moves given [ComponentView] relative to parents anchor point.
 *
 * @constructor Creates a [MovementAnimation] for the given [ComponentView].
 *
 * @param T Generic [ComponentView].
 * @param componentView [ComponentView] to animate
 * @param fromX Initial X position. Default: Current [ComponentView.posX].
 * @param toX Resulting X position. Default: Current [ComponentView.posX].
 * @param fromY Initial Y position. Default: Current [ComponentView.posY].
 * @param toY Resulting Y position. Default: Current [ComponentView.posY].
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_SPEED].
 *
 * @see ComponentAnimation
 * @see Animation
 * @see ComponentView
 *
 * @since 0.1
 *
 * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.greyToken
 * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.movementAnimationTo
 * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.playMovementAnimationTo
 */
class MovementAnimation<T : ComponentView>(
    componentView: T,
    fromX: Number = componentView.posX,
    toX: Number = componentView.posX,
    fromY: Number = componentView.posY,
    toY: Number = componentView.posY,
    duration: Int = DEFAULT_ANIMATION_SPEED
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {

  /** Initial X position. */
  val fromX: Double = fromX.toDouble()

  /** Resulting X position. */
  val toX: Double = toX.toDouble()

  /** Initial Y position. */
  val fromY: Double = fromY.toDouble()

  /** Resulting Y position. */
  val toY: Double = toY.toDouble()

  /**
   * A movement animation. Moves given [ComponentView] relative to parents anchor point.
   *
   * @param componentView [ComponentView] to animate
   * @param byX Relative X movement.
   * @param byY Relative Y movement.
   * @param duration [Animation] duration in milliseconds. Default: 1 second
   *
   * @see ComponentAnimation
   * @see Animation
   * @see ComponentView
   *
   * @since 0.1
   *
   * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.redToken
   * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.movementAnimationBy
   * @sample tools.aqua.bgw.main.examples.ExampleAnimationScene.playMovementAnimationBy
   */
  constructor(
      componentView: T,
      byX: Number = 0.0,
      byY: Number = 0.0,
      duration: Int = 1000
  ) : this(
      componentView = componentView,
      fromX = componentView.parent?.getChildPosition(componentView)?.xCoord ?: componentView.posX,
      fromY = componentView.parent?.getChildPosition(componentView)?.yCoord ?: componentView.posY,
      toX = (componentView.parent?.getChildPosition(componentView)?.xCoord
              ?: componentView.posX) + byX.toDouble(),
      toY = (componentView.parent?.getChildPosition(componentView)?.yCoord
              ?: componentView.posY) + byY.toDouble(),
      duration = duration)

  companion object {
    /**
     * Creates a [MovementAnimation] to another component's position. Moves given [ComponentView]
     * relative to parents anchor point.
     *
     * @param T Generic [ComponentView].
     * @param componentView [ComponentView] to animate
     * @param toComponentViewPosition Defines the destination [ComponentView] to move the given
     * component to.
     * @param scene The [Scene].
     * @param duration [Animation] duration in milliseconds. Default: 1 second
     */
    fun <T : ComponentView> toComponentView(
        componentView: T,
        toComponentViewPosition: T,
        scene: Scene<*>,
        duration: Int = 1000
    ): MovementAnimation<T> {
      // Find visual tree for components and drop root node
      val pathToComponent = scene.findPathToChild(componentView).dropLast(1)
      val pathToDestination = scene.findPathToChild(toComponentViewPosition).dropLast(1)

      // Sum relative positions and rotation
      var componentRotation = 0.0
      var componentAbsoluteX = 0.0
      var componentAbsoluteY = 0.0
      var destinationAbsoluteX = 0.0
      var destinationAbsoluteY = 0.0

      pathToComponent.forEach {
        val pos = it.parent?.getActualChildPosition(it)
        componentAbsoluteX += pos?.xCoord ?: it.actualPosX
        componentAbsoluteY += pos?.yCoord ?: it.actualPosY
        componentRotation += it.rotation
      }

      pathToDestination.forEach {
        val pos = it.parent?.getActualChildPosition(it)
        destinationAbsoluteX += pos?.xCoord ?: it.actualPosX
        destinationAbsoluteY += pos?.yCoord ?: it.actualPosY
      }

      val vector =
          Coordinate(
                  xCoord = destinationAbsoluteX - componentAbsoluteX,
                  yCoord = destinationAbsoluteY - componentAbsoluteY)
              .rotated(-componentRotation)

      return MovementAnimation(
          componentView = componentView,
          byX = vector.xCoord,
          byY = vector.yCoord,
          duration = duration)
    }
  }
}
