/*
 * Copyright 2025 The BoardGameWork Authors
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
 * Enum class representing different types of animations that can be applied to UI components.
 *
 * @since 0.10
 */
enum class AnimationInterpolation {
  /** Animation type that animates the change in a linear fashion. */
  LINEAR,
  /** Animation type that animates the change in a smooth, non-linear fashion. */
  SMOOTH,
  /** Animation type that animates the change with a spring effect. */
  SPRING,
  /** Animation type that animates the change in 10 steps, without smoothing. */
  STEPS
}
