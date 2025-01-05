/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

/**
 * [SteppedComponentAnimation] baseclass.
 *
 * @param T Generic [GameComponentView].
 * @param gameComponentView [GameComponentView] to animate.
 * @param duration Duration in milliseconds.
 * @param speed Count of changes to be performed in [duration].
 *
 * @see ComponentAnimation
 * @see Animation
 * @see GameComponentView
 *
 * @since 0.2
 */
sealed class SteppedComponentAnimation<T : GameComponentView>(
    gameComponentView: T, duration: Int,
    /** Count of changes to be performed in [duration]. */
    val speed: Int
) : ComponentAnimation<T>(componentView = gameComponentView, duration = duration)
