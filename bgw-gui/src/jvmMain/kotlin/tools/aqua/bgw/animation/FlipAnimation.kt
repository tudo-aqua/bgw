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

import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.core.DEFAULT_ANIMATION_SPEED
import tools.aqua.bgw.visual.Visual

/**
 * A flip [Animation].
 *
 * Sets background to given [fromVisual] than contracts background in half the given duration,
 * switches to [toVisual] and extends again in half the given duration.
 *
 * @param T Generic [GameComponentView].
 * @param gameComponentView [GameComponentView] to animate.
 * @param fromVisual Initial [Visual].
 * @param toVisual Resulting [Visual].
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_SPEED].
 * @constructor Creates a [FlipAnimation] for the given [GameComponentView].
 * @see ComponentAnimation
 * @see GameComponentView
 * @since 0.1
 */
class FlipAnimation<T : GameComponentView>(
    gameComponentView: T,
    /** Initial [Visual]. */
    val fromVisual: Visual = gameComponentView.visual,
    /** Resulting [Visual]. */
    val toVisual: Visual,
    duration: Int = DEFAULT_ANIMATION_SPEED
) : ComponentAnimation<T>(componentView = gameComponentView, duration = duration)
