/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

@file:Suppress("unused", "unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION

/**
 * A rotation [Animation].
 *
 * Rotates [ComponentView] to given angle.
 *
 * @constructor Creates a [RotationAnimation] for the given [ComponentView].
 *
 * @param componentView [ComponentView] to animate.
 * @param fromAngle Initial angle. Default: Current [ComponentView.rotation].
 * @param toAngle Resulting angle. Default: Current [ComponentView.rotation].
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_DURATION].
 */
class RotationAnimation<T : ComponentView>(
    componentView: T,
    fromAngle: Number = componentView.rotation,
    toAngle: Number = componentView.rotation,
    duration: Int = DEFAULT_ANIMATION_DURATION
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {

  /** Initial angle. */
  val fromAngle: Double = fromAngle.toDouble()

  /** Resulting angle. */
  val toAngle: Double = toAngle.toDouble()

  /**
   * A rotation animation. Rotates given [ComponentView] to given angle.
   *
   * @param componentView [ComponentView] to animate
   * @param byAngle relative angle. Default: 0
   * @param duration [Animation] duration in milliseconds. Default: 1 second
   */
  constructor(
      componentView: T,
      byAngle: Double = 0.0,
      duration: Int = 1000
  ) : this(
      componentView = componentView,
      toAngle = componentView.rotation + byAngle,
      duration = duration)
}
