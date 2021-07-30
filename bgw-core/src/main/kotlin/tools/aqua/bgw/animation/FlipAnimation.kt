/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.visual.Visual

/**
 * A flip animation.
 * Sets background to given [fromVisual] than contracts background in half the given duration, switches to [toVisual]
 * and extends again in half the given duration.
 *
 * @param componentView [ComponentView] to animate.
 * @param fromVisual initial [Visual].
 * @param toVisual resulting [Visual].
 * @param duration duration in milliseconds. Default: 1 second.
 */
class FlipAnimation<T : ComponentView>(
	componentView: T,
	val fromVisual: Visual,
	val toVisual: Visual,
	duration: Int = 500
) : ComponentAnimation<T>(componentView = componentView, duration = duration)