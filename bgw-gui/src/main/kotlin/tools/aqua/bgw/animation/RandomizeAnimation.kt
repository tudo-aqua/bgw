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

@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED
import tools.aqua.bgw.visual.Visual

/**
 * A randomization [Animation] that shuffles between different visuals.
 *
 * Shuffles through visuals in the given [visuals] [List] for given [duration] and shows [toVisual]
 * in the end. Use the [speed] parameter to define how many steps the animation should have.
 *
 * For example:
 *
 * An animation with [duration] = 1s and [speed] = 50 will change the visual 50 times within the
 * [duration] of one second.
 *
 * @constructor Creates a [RandomizeAnimation] for the given [GameComponentView].
 *
 * @param T Generic [GameComponentView].
 * @param componentView [GameComponentView] to animate.
 * @property visuals [List] of [Visual]s to shuffle through.
 * @property toVisual Resulting [Visual] after shuffle.
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_DURATION].
 * @param speed Count of steps. Default: [DEFAULT_ANIMATION_SPEED].
 */
class RandomizeAnimation<T : GameComponentView>(
    componentView: T,
    val visuals: List<Visual>,
    val toVisual: Visual,
    duration: Int = DEFAULT_ANIMATION_DURATION,
    speed: Int = DEFAULT_ANIMATION_SPEED
) : SteppedComponentAnimation<T>(componentView = componentView, duration = duration, speed = speed)
