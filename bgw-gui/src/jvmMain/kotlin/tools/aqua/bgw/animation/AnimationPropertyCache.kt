/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.animation

import tools.aqua.bgw.visual.Visual

/**
 * Cache for component properties that are currently being animated. This prevents user updates from
 * interfering with running animations.
 *
 * @since 0.11
 */
internal data class AnimationPropertyCache(
    // Movement properties
    var posX: Int,
    var posY: Int,

    // Scale properties
    var scaleX: Double,
    var scaleY: Double,

    // Rotation property
    var rotation: Double,

    // Fade property
    var opacity: Double,

    // Flip/Stepped properties
    var visual: Visual
)
