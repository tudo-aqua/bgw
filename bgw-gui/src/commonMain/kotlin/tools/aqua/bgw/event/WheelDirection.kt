/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.event

import kotlinx.serialization.Serializable

/**
 * Enum indicating direction of a [WheelEvent].
 *
 * @since 0.7.1
 */
@Serializable
enum class WheelDirection {
  /** Upwards scroll. */
  UP,

  /** Downwards scroll. */
  DOWN;

  /**
   * Multiplies [scalar] by 1 ([UP]) or -1 ([DOWN]).
   *
   * @param scalar Scalar to be multiplied.
   * @since 0.7.1
   */
  operator fun times(scalar: Number): Double = scalar.toDouble() * (if (this == UP) 1 else -1)

  companion object {
    /** Returns [WheelDirection] based on scrolled delta value. */
    internal fun of(delta: Number): WheelDirection = if (delta.toDouble() < 0) DOWN else UP
  }
}
