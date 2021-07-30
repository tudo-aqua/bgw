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

@file:Suppress("unused", "unused")

package tools.aqua.bgw.animation

import tools.aqua.bgw.components.ComponentView

/**
 * A rotation animation.
 * Rotates [ComponentView] to given angle.
 *
 * @param componentView [ComponentView] to animate.
 * @param fromAngle initial angle. Default: Current [ComponentView.rotation].
 * @param toAngle resulting angle. Default: Current [ComponentView.rotation].
 * @param duration duration in milliseconds. Default: 1 second.
 */
class RotationAnimation<T : ComponentView>(
	componentView: T,
	val fromAngle: Double = componentView.rotation,
	val toAngle: Double = componentView.rotation,
	duration: Int = 1000
) : ComponentAnimation<T>(componentView = componentView, duration = duration) {
	
	/**
	 * A rotation animation.
	 * Rotates given [ComponentView] to given angle.
	 *
	 * @param componentView [ComponentView] to animate
	 * @param byAngle relative angle. Default: 0
	 * @param duration [Animation] duration in milliseconds. Default: 1 second
	 */
	constructor(componentView: T, byAngle: Double = 0.0, duration: Int = 1000) : this(
		componentView = componentView,
		toAngle = componentView.rotation + byAngle,
		duration = duration
	)
}