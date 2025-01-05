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

package tools.aqua.bgw.core

/**
 * Class representing an aspect ratio between the window sides.
 *
 * @property ratio Ratio as fraction.
 *
 * @since 0.3
 */
data class AspectRatio
internal constructor(internal val ratio: Double = DEFAULT_WINDOW_WIDTH / DEFAULT_WINDOW_HEIGHT) {
    companion object {
        /**
         * Creates an aspect ratio out of width and height. May be for example 1920 : 1080 as well as 16
         * : 9.
         *
         * @param width Width of ratio. Default: [DEFAULT_WINDOW_WIDTH].
         * @param height Height of ratio. Default: [DEFAULT_WINDOW_HEIGHT].
         */
        fun of(
            width: Number = DEFAULT_WINDOW_WIDTH, height: Number = DEFAULT_WINDOW_HEIGHT
        ): AspectRatio = AspectRatio(width.toDouble() / height.toDouble())
    }
}
