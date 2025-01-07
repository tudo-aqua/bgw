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

/**
 * A fade [Animation].
 *
 * Fades given [ComponentView]'s opacity i.e. alpha channel.
 *
 * @constructor Creates a [FadeAnimation] for the given [ComponentView].
 *
 * @param T Generic [ComponentView].
 * @param componentView [ComponentView] to animate.
 * @param fromOpacity Initial opacity. Default: Current [ComponentView.opacity].
 * @param toOpacity Resulting opacity. Default: Current [ComponentView.opacity].
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_SPEED].
 *
 * @see ComponentAnimation
 * @see Animation
 * @see ComponentView
 *
 * @since 0.3
 */
class FadeAnimation<T : ComponentView>(
    componentView: T,
    fromOpacity: Number = componentView.opacity,
    toOpacity: Number = componentView.opacity,
    duration: Int = DEFAULT_ANIMATION_SPEED
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {
  /** Initial opacity. */
  val fromOpacity: Double = fromOpacity.toDouble()

  /** Resulting opacity. */
  val toOpacity: Double = toOpacity.toDouble()
}
