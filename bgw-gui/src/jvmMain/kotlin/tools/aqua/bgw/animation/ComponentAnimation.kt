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

/**
 * [ComponentAnimation] baseclass.
 *
 * @param T Generic [ComponentView].
 * @param componentView [ComponentView] to animate.
 * @param duration Duration in milliseconds.
 * @see Animation
 * @since 0.1
 */
sealed class ComponentAnimation<T : ComponentView>(
    /** [ComponentView] to animate. */
    val componentView: T,
    duration: Int
) : Animation(duration = duration)
