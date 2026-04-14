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

@file:JsModule("animejs")
@file:JsNonModule

package tools.aqua.bgw

import js.objects.Record
import org.w3c.dom.Element

internal external interface DefaultsParams {
  var id: Any?
}

internal external interface TickableCallbacks<T : Any?> {
  var onBegin: Callback<JSAnimation>?
  var onBeforeUpdate: Callback<JSAnimation>?
  var onUpdate: Callback<JSAnimation>?
  var onLoop: Callback<JSAnimation>?
  var onPause: Callback<JSAnimation>?
  var onComplete: Callback<JSAnimation>?
}

internal external interface RenderableCallbacks<T : Any?>

internal external interface TimerOptions {
  var id: Any?
  var duration: Int?
  var loopDelay: Double?
  var reversed: Boolean?
  var alternate: Boolean?
  var loop: Any?
  var autoplay: Any?
  var frameRate: Double?
  var playbackRate: Double?
}

internal external interface TimerParams : TimerOptions, TickableCallbacks<Timer> {}

internal external interface TweenDecomposedValue {
  /** - Type */
  var t: Double
  /** - Single number value */
  var n: Double
  /** - Value unit */
  var u: String
  /** - Value operator */
  var o: String
  /** - Array of Numbers (in case of complex value type) */
  var d: Array<Double>
  /** - Strings (in case of complex value type) */
  var s: Array<String>
}

internal external interface AnimationOptions {
  var keyframes: Any?
  var playbackEase: String?
}

internal abstract external class AnimationParams :
    Record<String, Any>,
    TimerOptions,
    AnimationOptions,
    TickableCallbacks<JSAnimation>,
    RenderableCallbacks<JSAnimation> {
  var `--txAnim`: TweenParams? = definedExternally
  var `--tyAnim`: TweenParams? = definedExternally
  var `--rotAnim`: TweenParams? = definedExternally
  var `--rot`: TweenParams? = definedExternally
  var `--sxAnim`: TweenParams? = definedExternally
  var `--syAnim`: TweenParams? = definedExternally
  var `--opaAnim`: TweenParams? = definedExternally
  var `--flipAnim`: Array<out TweenParams>
  var `--steppedAnim`: Array<out TweenParams>
}

internal external interface TweenParams {
  var from: String?
  var to: String
  var duration: Int
  var delay: Int
  var ease: Any
  var modifier: ((v: Number) -> Any?)?
}

internal external interface TimelineParams :
    TimerOptions, TimelineOptions, TickableCallbacks<Timeline>, RenderableCallbacks<Timeline>

internal external class Timeline : Timer {
  fun add(a1: Any, a2: AnimationParams): Unit

  fun add(a1: Any, a2: AnimationParams, a3: TimelinePosition = definedExternally): Unit

  fun add(a1: TimerParams, a2: TimelinePosition = definedExternally): Unit

  fun sync(synced: Any = definedExternally, position: TimelinePosition = definedExternally): Unit

  fun set(
      targets: Any,
      parameters: AnimationParams,
      position: TimelinePosition = definedExternally
  ): Unit
}

internal external fun createTimeline(parameters: TimelineParams = definedExternally): Timeline

internal open external class Timer {
  constructor(
      parameters: TimerParams = definedExternally,
      parent: Timeline = definedExternally,
      parentPosition: Double = definedExternally
  )

  var id: Any
  var currentTime: Double

  fun cancel(): Unit
}

internal external class JSAnimation : Timer

internal external fun animate(targets: Element, parameters: AnimationParams): JSAnimation

internal external fun steps(steps: Int): Any = definedExternally
