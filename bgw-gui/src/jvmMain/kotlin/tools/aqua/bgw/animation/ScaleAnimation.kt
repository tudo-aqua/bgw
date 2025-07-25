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
import tools.aqua.bgw.core.AnimationInterpolation
import tools.aqua.bgw.core.AnimationInterpolation.SMOOTH
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED

/**
 * A scale [Animation].
 *
 * Scales given [ComponentView] to given scalar.
 *
 * @constructor Creates a [ScaleAnimation] for the given [ComponentView].
 *
 * @param T Generic [ComponentView].
 * @param componentView [ComponentView] to animate
 * @param fromScaleX Initial X position. Default: Current [ComponentView.posX].
 * @param toScaleX Resulting X position. Default: Current [ComponentView.posX].
 * @param fromScaleY Initial Y position. Default: Current [ComponentView.posY].
 * @param toScaleY Resulting Y position. Default: Current [ComponentView.posY].
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_SPEED].
 * @param interpolation [AnimationInterpolation] to use for the animation. Default:
 * [AnimationInterpolation.SMOOTH].
 *
 * @see ComponentAnimation
 * @see Animation
 * @see ComponentView
 *
 * @since 0.3
 */
class ScaleAnimation<T : ComponentView>(
    componentView: T,
    fromScaleX: Number = componentView.scaleX,
    toScaleX: Number = componentView.scaleX,
    fromScaleY: Number = componentView.scaleY,
    toScaleY: Number = componentView.scaleY,
    duration: Int = DEFAULT_ANIMATION_SPEED,
    /**
     * Interpolation to use for the animation. Default: [AnimationInterpolation.SMOOTH].
     *
     * @see AnimationInterpolation
     *
     * @since 0.10
     */
    val interpolation: AnimationInterpolation = AnimationInterpolation.SMOOTH
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {

  /** Initial X scale. */
  val fromScaleX: Double = fromScaleX.toDouble()

  /** Resulting X scale. */
  val toScaleX: Double = toScaleX.toDouble()

  /** Initial Y scale. */
  val fromScaleY: Double = fromScaleY.toDouble()

  /** Resulting Y scale. */
  val toScaleY: Double = toScaleY.toDouble()

  /**
   * A scale animation. Scales given [ComponentView] by given x and y scalar.
   *
   * @param componentView [ComponentView] to animate
   * @param byScaleX Relative X scale.
   * @param byScaleY Relative Y scale.
   * @param duration [Animation] duration in milliseconds. Default: 1 second
   * @param interpolation [AnimationInterpolation] to use for the animation. Default:
   * [AnimationInterpolation.SMOOTH].
   *
   * @see ComponentAnimation
   * @see Animation
   * @see ComponentView
   *
   * @since 0.3
   */
  constructor(
      componentView: T,
      byScaleX: Number = 0.0,
      byScaleY: Number = 0.0,
      duration: Int = 1000,
      interpolation: AnimationInterpolation = AnimationInterpolation.SMOOTH
  ) : this(
      componentView = componentView,
      toScaleX = componentView.scaleX * byScaleX.toDouble(),
      toScaleY = componentView.scaleY * byScaleY.toDouble(),
      duration = duration,
      interpolation = interpolation)

  /**
   * A scale animation. Scales given [ComponentView] by given scalar.
   *
   * @param componentView [ComponentView] to animate
   * @param byScale Relative scale.
   * @param duration [Animation] duration in milliseconds. Default: 1 second
   * @param interpolation [AnimationInterpolation] to use for the animation. Default:
   * [AnimationInterpolation.SMOOTH].
   *
   * @see ComponentAnimation
   * @see Animation
   * @see ComponentView
   *
   * @since 0.3
   */
  constructor(
      componentView: T,
      byScale: Number = 0.0,
      duration: Int = 1000,
      interpolation: AnimationInterpolation = AnimationInterpolation.SMOOTH
  ) : this(
      componentView = componentView,
      toScaleX = componentView.scaleX * byScale.toDouble(),
      toScaleY = componentView.scaleY * byScale.toDouble(),
      duration = duration,
      interpolation = interpolation)
}
