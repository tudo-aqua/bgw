/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

import java.util.concurrent.ConcurrentHashMap

/** The [AnimationType] tracked for this animation on its component. */
internal val ComponentAnimation<*>.animationType: AnimationType
  get() =
      when (this) {
        is MovementAnimation<*> -> AnimationType.MOVEMENT
        is ScaleAnimation<*> -> AnimationType.SCALE
        is RotationAnimation<*> -> AnimationType.ROTATION
        is FadeAnimation<*> -> AnimationType.FADE
        is FlipAnimation<*> -> AnimationType.FLIP
        is SteppedComponentAnimation<*> -> AnimationType.STEPPED
      }

/**
 * All animations that were started and did not finish yet, by ID.
 *
 * Finish notifications from the browser are resolved here, including those of animations whose
 * scene was replaced while they were running.
 */
internal object AnimationRegistry {
  private val running = ConcurrentHashMap<String, Animation>()

  fun register(animation: Animation) {
    running[animation.id] = animation
  }

  fun unregister(animation: Animation) {
    running.remove(animation.id)
  }

  operator fun get(id: String): Animation? = running[id]

  /** Snapshot of all running animations. */
  fun all(): List<Animation> = running.values.toList()

  /** Whether another running animation of [type] targets [componentView]. */
  fun isTypeRunningOn(
      componentView: tools.aqua.bgw.components.ComponentView,
      type: AnimationType?,
      except: Animation
  ): Boolean =
      running.values.any {
        it !== except &&
            it.isRunning &&
            it is ComponentAnimation<*> &&
            it.componentView === componentView &&
            (type == null || it.animationType == type)
      }

  fun clear() = running.clear()
}
