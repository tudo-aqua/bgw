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

package tools.aqua.bgw.core

/**
 * Used to define vertical centering behaviour.
 *
 * @property positionMultiplier Internal position multiplier.
 * @property pivotMultiplier Internal pivot multiplier.
 *
 * @see BoardGameScene
 * @see BoardGameApplication
 */
enum class VerticalAlignment(
    internal val positionMultiplier: Double,
    internal val pivotMultiplier: Double
) {
  /** [Alignment] on the top. */
  TOP(0.0, -1.0),

  /** [Alignment] centered vertically. */
  CENTER(0.5, 0.0),

  /** [Alignment] on the bottom. */
  BOTTOM(1.0, 1.0)
}
