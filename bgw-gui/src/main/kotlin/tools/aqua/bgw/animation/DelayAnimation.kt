/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

import tools.aqua.bgw.core.DEFAULT_ANIMATION_DURATION

/**
 * An asynchronous delay that fires [onFinished] after given duration.
 *
 * @constructor Creates a [DelayAnimation] with given [duration].
 *
 * @param duration Duration in milliseconds. Default: [DEFAULT_ANIMATION_DURATION].
 */
class DelayAnimation(duration: Int = DEFAULT_ANIMATION_DURATION) : Animation(duration = duration)
