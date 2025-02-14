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

package tools.aqua.bgw.util

import kotlin.math.PI

/**
 * Utility class for trigonometry functions.
 *
 * @since 0.3
 */
object Trig {
  /** Sin function for angles in degrees. */
  fun sinD(degrees: Number): Double = kotlin.math.sin(degrees.toDouble().toRadians())

  /** Cos function for angles in degrees. */
  fun cosD(degrees: Number): Double = kotlin.math.cos(degrees.toDouble().toRadians())

  private fun Double.toRadians(): Double = this * (PI / 180.0)
}
