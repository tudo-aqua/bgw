/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

/**
 * An [Animation] consisting of multiple Animations that are played in parallel, when this
 * [ParallelAnimation] is played.
 *
 * @constructor creates a new [ParallelAnimation].
 *
 * @param animations The [Animation]s that this [ParallelAnimation] should contain.
 */
data class ParallelAnimation(val animations: List<Animation>) :
    Animation(animations.maxOf(Animation::duration)) {

  /**
   * Creates a new [ParallelAnimation]. Additional constructor that enables the use of varargs for
   * the animations.
   *
   * @param animation The [Animation]s that this [ParallelAnimation] should contain.
   */
  constructor(vararg animation: Animation) : this(animation.toList())
}
