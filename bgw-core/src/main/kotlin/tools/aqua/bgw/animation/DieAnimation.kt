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

import tools.aqua.bgw.components.gamecomponents.Die

/**
 * A die roll [Animation].
 * Shuffles through die visuals for given [duration] and shows [toSide] in the end. Use the [speed] parameter
 * to define how many steps the animation should have.
 * For example:
 * An animation with [duration] = 1s and [speed] = 50 will change the visual 50 times within the [duration] of one
 * second.
 *
 * @param die [Die] to animate.
 * @param toSide resulting side after roll.
 * @param duration duration in milliseconds. Default: 1 second.
 * @param speed count of steps. Default: 50 steps.
 */
class DieAnimation<T : Die>(
	die: T,
	val toSide: Int,
	duration: Int = 1000,
	val speed: Int = 50
) : ComponentAnimation<T>(componentView = die, duration = duration)