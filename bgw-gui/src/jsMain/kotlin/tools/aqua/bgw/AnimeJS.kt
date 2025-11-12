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

@file:JsModule("animejs")
@file:JsNonModule

package tools.aqua.bgw

import js.objects.Record
import kotlin.js.Promise
import org.w3c.dom.Element
import web.animations.CompositeOperation

internal external interface DefaultsParams {
  var id: (Any /* number | string */)?
  var keyframes: (Any /* PercentageKeyframes | DurationKeyframes */)?
  var playbackEase: String?
  var playbackRate: Double?
  var frameRate: Double?
  var loop: (Any /* number | boolean */)?
  var reversed: Boolean?
  var alternate: Boolean?
  var persist: Boolean?
  var autoplay: (Any /* boolean | ScrollObserver */)?
  var duration: (Any /* number | FunctionValue */)?
  var delay: (Any /* number | FunctionValue */)?
  var loopDelay: Double?
  var ease: String?
  var composition: (Any /* "none" | "replace" | "blend" | compositionTypes */)?
  var modifier: ((v: Any?) -> Any?)?
  var onBegin: Callback<Any>?
  var onBeforeUpdate: Callback<Any>?
  var onUpdate: Callback<Any>?
  var onLoop: Callback<Any>?
  var onPause: Callback<Any>?
  var onComplete: Callback<Any>?
  var onRender: Callback<Any>?
}

internal external interface StaggerParams {
  var start: (Any /* number | string */)?
  var from: (Any /* number | "first" | "center" | "last" | "random" */)?
  var reversed: Boolean?
  var grid: Array<Double>?
  var use: (Any /* string | ((target: Target, i: number, length: number) => number) */)?
  var total: Double?
  var ease: String?
}

internal external interface SpringParams {
  /** - Mass, default 1 */
  var mass: Double?
  /** - Stiffness, default 100 */
  var stiffness: Double?
  /** - Damping, default 10 */
  var damping: Double?
  /** - Initial velocity, default 0 */
  var velocity: Double?
  /** - Initial bounce, default 0 */
  var bounce: Double?
  /** - The perceived duration, default 0 */
  var duration: Double?
  /** - Callback function called when the spring currentTime hits the perceived duration */
  var onComplete: Callback<JSAnimation>?
}

internal external interface TickableCallbacks<T : Any?> {
  var onBegin: Callback<JSAnimation>?
  var onBeforeUpdate: Callback<JSAnimation>?
  var onUpdate: Callback<JSAnimation>?
  var onLoop: Callback<JSAnimation>?
  var onPause: Callback<JSAnimation>?
  var onComplete: Callback<JSAnimation>?
}

internal external interface RenderableCallbacks<T : Any?> {
  var onRender: Callback<T>?
}

internal external interface TimerOptions {
  var id: (Any /* number | string */)?
  var loopDelay: Double?
  var reversed: Boolean?
  var alternate: Boolean?
  var loop: (Any /* boolean | number */)?
  var autoplay: (Any /* boolean | ScrollObserver */)?
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

internal external interface PercentageKeyframeOptions {
  var ease: String?
}

internal external interface AnimationOptions {
  var keyframes: (Any /* PercentageKeyframes | DurationKeyframes */)?
  var playbackEase: String?
}

internal external abstract class AnimationParams :
    Record<String, Any>,
    TimerOptions,
    AnimationOptions,
    TickableCallbacks<JSAnimation>,
    RenderableCallbacks<JSAnimation> {
  var translateX: TweenParams? = definedExternally
  var translateY: TweenParams? = definedExternally
  var rotateZ: TweenParams? = definedExternally
  var scaleX: TweenParams? = definedExternally
  var scaleY: TweenParams? = definedExternally
  var opacity: TweenParams? = definedExternally
    var `--translateX`: TweenParams? = definedExternally
}

internal external interface TweenParams {
  var from: String?
  var to: String
  var duration: Int
  var modifier: ((v: Number) -> Any?)?
}

internal external interface Animatable {
  /**
   * @param {TargetsParam} targets
   * @param {AnimatableParams} parameters
   */
  var targets:
      js.array.ReadonlyArray<
          (Any? /* HTMLElement | SVGElement | import("../types/index.js").JSTarget */)>
  var animations: Any
  /** @type {JSAnimation|null} */
  var callbacks: JSAnimation?

  fun revert(): Unit /* this */
}

internal external interface TimelineParams :
    TimerOptions, TimelineOptions, TickableCallbacks<Timeline>, RenderableCallbacks<Timeline> {}

internal external interface WAAPIAnimationOptions {
  var loop: (Any /* number | boolean */)?
  var Reversed: Boolean?
  var Alternate: Boolean?
  var autoplay: (Any /* boolean | ScrollObserver */)?
  var playbackRate: Double?
  var duration: (Any /* number | WAAPIFunctionValue */)?
  var delay: (Any /* number | WAAPIFunctionValue */)?
  var ease: String?
  var composition: CompositeOperation
  var persist: Boolean?
}

internal external interface WAAPIAnimationParams :
    Record<
        String,
        Any /* WAAPIKeyframeValue | WAAPIAnimationOptions | boolean | ScrollObserver | Callback<WAAPIAnimation> | WAAPIEasingParam | WAAPITweenOptions */>,
    WAAPIAnimationOptions {}

internal external interface AnimatablePropertyParamsOptions {
  var unit: String?
  var ease: String?
}

internal external interface AnimatableParams :
    Record<
        String,
        Any /* TweenParamValue | EasingParam | TweenModifier | TweenComposition | AnimatablePropertyParamsOptions */>,
    AnimatablePropertyParamsOptions {}

internal external interface ReactRef {
  var current: Any? /* HTMLElement | SVGElement | null */
}

internal external interface AngularRef {
  var nativeElement: Any? /* HTMLElement | SVGElement */
}

internal external interface ScopeParams {
  var root: Any? /* DOMTargetSelector | ReactRef | AngularRef */
  var defaults: DefaultsParams?
  var mediaQueries: Record<String, String>?
}

internal external interface DraggableAxisParam {
  var mapTo: String?
  var snap: (Any /* number | Array<number> | ((draggable: Draggable) => number | Array<number>) */)?
}

internal external interface DraggableCursorParams {
  var onHover: String?
  var onGrab: String?
}

internal external interface DraggableDragThresholdParams {
  var mouse: Double?
  var touch: Double?
}

internal external interface DraggableParams {
  var trigger: Any
  var container:
      Any? /* DOMTargetSelector | Array<number> | ((draggable: Draggable) => DOMTargetSelector | Array<number>) */
  var x: (Any /* boolean | DraggableAxisParam */)?
  var y: (Any /* boolean | DraggableAxisParam */)?
  var snap: (Any /* number | Array<number> | ((draggable: Draggable) => number | Array<number>) */)?
  var containerPadding:
      (Any /* number | Array<number> | ((draggable: Draggable) => number | Array<number>) */)?
  var containerFriction: (Any /* number | ((draggable: Draggable) => number) */)?
  var releaseContainerFriction: (Any /* number | ((draggable: Draggable) => number) */)?
  var dragSpeed: (Any /* number | ((draggable: Draggable) => number) */)?
  var dragThreshold:
      (Any /* number | DraggableDragThresholdParams | ((draggable: Draggable) => number | DraggableDragThresholdParams) */)?
  var scrollSpeed: (Any /* number | ((draggable: Draggable) => number) */)?
  var scrollThreshold: (Any /* number | ((draggable: Draggable) => number) */)?
  var minVelocity: (Any /* number | ((draggable: Draggable) => number) */)?
  var maxVelocity: (Any /* number | ((draggable: Draggable) => number) */)?
  var velocityMultiplier: (Any /* number | ((draggable: Draggable) => number) */)?
  var releaseMass: Double?
  var releaseStiffness: Double?
  var releaseDamping: Double?
  var releaseEase: String?
  var cursor:
      (Any /* boolean | DraggableCursorParams | ((draggable: Draggable) => boolean | DraggableCursorParams) */)?
}

internal external interface SplitTemplateParams {
  var `class`: (Any /* false | string */)?
  var wrap: (Any /* boolean | "hidden" | "clip" | "visible" | "scroll" | "auto" */)?
  var clone: (Any /* boolean | "top" | "right" | "bottom" | "left" | "center" */)?
}

internal external interface TextSplitterParams {
  var lines: (Any /* SplitValue | SplitTemplateParams | SplitFunctionValue */)?
  var words: (Any /* SplitValue | SplitTemplateParams | SplitFunctionValue */)?
  var chars: (Any /* SplitValue | SplitTemplateParams | SplitFunctionValue */)?
  var accessible: Boolean?
  var includeSpaces: Boolean?
  var debug: Boolean?
}

internal external class Timeline : Timer {
  /** @param {TimelineParams} [parameters] */
  constructor(parameters: TimelineParams = definedExternally)

  /** @type {Record<String, Number>} */
  var labels: Record<String, Double>
  /** @type {DefaultsParams} */
  var defaults: DefaultsParams
  /** @type {Callback<this>} */
  var onRender: Callback<Unit /* this */>

  /**
   * @param {TargetsParam} a1
   * @param {AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @param {TimerParams} a1
   * @param {TimelinePosition} [a2]
   * @param {TargetsParam|TimerParams} a1
   * @param {TimelinePosition|AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @return {this}
   * @return {this}
   * @overload
   * @overload
   */
  fun add(a1: Any, a2: AnimationParams): Unit /* this */

  /**
   * @param {TargetsParam} a1
   * @param {AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @param {TimerParams} a1
   * @param {TimelinePosition} [a2]
   * @param {TargetsParam|TimerParams} a1
   * @param {TimelinePosition|AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @return {this}
   * @return {this}
   * @overload
   * @overload
   */
  fun add(a1: Any, a2: AnimationParams, a3: TimelinePosition = definedExternally): Unit /* this */

  /**
   * @param {TargetsParam} a1
   * @param {AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @param {TimerParams} a1
   * @param {TimelinePosition} [a2]
   * @param {TargetsParam|TimerParams} a1
   * @param {TimelinePosition|AnimationParams} a2
   * @param {TimelinePosition|StaggerFunction<Number|String>} [a3]
   * @return {this}
   * @return {this}
   * @overload
   * @overload
   */
  fun add(a1: TimerParams, a2: TimelinePosition = definedExternally): Unit /* this */

  /**
   * @param {Tickable} [synced]
   * @param {TimelinePosition} [position]
   * @param {globalThis.Animation} [synced]
   * @param {TimelinePosition} [position]
   * @param {WAAPIAnimation} [synced]
   * @param {TimelinePosition} [position]
   * @param {Tickable|WAAPIAnimation|globalThis.Animation} [synced]
   * @param {TimelinePosition} [position]
   * @return {this}
   * @return {this}
   * @return {this}
   * @overload
   * @overload
   * @overload
   */
  fun sync(
      synced: Any = definedExternally,
      position: TimelinePosition = definedExternally
  ): Unit /* this */

  /**
   * @param {Tickable} [synced]
   * @param {TimelinePosition} [position]
   * @param {globalThis.Animation} [synced]
   * @param {TimelinePosition} [position]
   * @param {WAAPIAnimation} [synced]
   * @param {TimelinePosition} [position]
   * @param {Tickable|WAAPIAnimation|globalThis.Animation} [synced]
   * @param {TimelinePosition} [position]
   * @return {this}
   * @return {this}
   * @return {this}
   * @overload
   * @overload
   * @overload
   */
  fun set(
      targets: Any,
      parameters: AnimationParams,
      position: TimelinePosition = definedExternally
  ): Unit /* this */

  /**
   * @param {Callback<Timer>} callback
   * @param {TimelinePosition} [position]
   * @return {this}
   */
  fun call(
      callback: Callback<Timer>,
      position: TimelinePosition = definedExternally
  ): Unit /* this */

  /**
   * @param {String} labelName
   * @param {TimelinePosition} [position]
   * @return {this}
   */
  fun label(labelName: String, position: TimelinePosition = definedExternally): Unit /* this */

  /**
   * @param {TargetsParam} targets
   * @param {String} [propertyName]
   * @return {this}
   */
  fun remove(targets: Any, propertyName: String = definedExternally): Unit /* this */

  /** @return {this} */
  fun refresh(): Unit /* this */

  /** @typedef {this & {then: null}} ResolvedTimeline */
}

internal external fun createTimeline(parameters: TimelineParams = definedExternally): Timeline

/** Base class used to create Timers, Animations and Timelines */
internal open external class Timer {
  /**
   * @param {TimerParams} [parameters]
   * @param {Timeline} [parent]
   * @param {Number} [parentPosition]
   */
  constructor(
      parameters: TimerParams = definedExternally,
      parent: Timeline = definedExternally,
      parentPosition: Double = definedExternally
  )

  var id: Any /* string | number */
  /** @type {Timeline} */
  var parent: Timeline
  var duration: Double
  /** @type {Boolean} */
  var backwards: Boolean
  /** @type {Boolean} */
  var paused: Boolean
  /** @type {Boolean} */
  var began: Boolean
  /** @type {Boolean} */
  var completed: Boolean
  /** @type {Callback<this>} */
  var onBegin: Callback<Unit /* this */>
  /** @type {Callback<this>} */
  var onBeforeUpdate: Callback<Unit /* this */>
  /** @type {Callback<this>} */
  var onUpdate: Callback<Unit /* this */>
  /** @type {Callback<this>} */
  var onLoop: Callback<Unit /* this */>
  /** @type {Callback<this>} */
  var onPause: Callback<Unit /* this */>
  /** @type {Callback<this>} */
  var onComplete: Callback<Unit /* this */>
  /** @type {Number} */
  var iterationDuration: Double
  /** @type {Number} */
  var iterationCount: Double
  /** @type {Boolean|ScrollObserver} */
  var _autoplay: Any /* boolean | ScrollObserver */
  /** @type {Number} */
  var _offset: Double
  /** @type {Number} */
  var _delay: Double
  /** @type {Number} */
  var _loopDelay: Double
  /** @type {Number} */
  var _iterationTime: Double
  /** @type {Number} */
  var _currentIteration: Double
  /** @type {Boolean} */
  var _running: Boolean
  /** @type {Number} */
  var _reversed: Double
  /** @type {Number} */
  var _reverse: Double
  /** @type {Number} */
  var _cancelled: Double
  /** @type {Boolean} */
  var _alternate: Boolean
  var cancelled: Boolean
  var currentTime: Double
  var iterationCurrentTime: Double
  var progress: Double
  var iterationProgress: Double
  var currentIteration: Double
  var reversed: Boolean

  /**
   * @param {Boolean} [softReset]
   * @return {this}
   */
  fun reset(softReset: Boolean = definedExternally): Unit /* this */

  /**
   * @param {Boolean} internalRender
   * @return {this}
   */
  fun init(internalRender: Boolean = definedExternally): Unit /* this */

  /** @return {this} */
  fun resetTime(): Unit /* this */

  /** @return {this} */
  fun pause(): Unit /* this */

  /** @return {this} */
  fun resume(): Unit /* this */

  /** @return {this} */
  fun restart(): Unit /* this */

  /**
   * @param {Number} time
   * @param {Boolean|Number} [muteCallbacks]
   * @param {Boolean|Number} [internalRender]
   * @return {this}
   */
  fun seek(time: Double): Unit /* this */

  /**
   * @param {Number} time
   * @param {Boolean|Number} [muteCallbacks]
   * @param {Boolean|Number} [internalRender]
   * @return {this}
   */
  fun seek(
      time: Double,
      muteCallbacks: Boolean = definedExternally,
      internalRender: Boolean = definedExternally
  ): Unit /* this */

  /**
   * @param {Number} time
   * @param {Boolean|Number} [muteCallbacks]
   * @param {Boolean|Number} [internalRender]
   * @return {this}
   */
  fun seek(
      time: Double,
      muteCallbacks: Boolean = definedExternally,
      internalRender: Double = definedExternally
  ): Unit /* this */

  /**
   * @param {Number} time
   * @param {Boolean|Number} [muteCallbacks]
   * @param {Boolean|Number} [internalRender]
   * @return {this}
   */
  fun seek(
      time: Double,
      muteCallbacks: Double = definedExternally,
      internalRender: Boolean = definedExternally
  ): Unit /* this */

  /**
   * @param {Number} time
   * @param {Boolean|Number} [muteCallbacks]
   * @param {Boolean|Number} [internalRender]
   * @return {this}
   */
  fun seek(
      time: Double,
      muteCallbacks: Double = definedExternally,
      internalRender: Double = definedExternally
  ): Unit /* this */

  /** @return {this} */
  fun alternate(): Unit /* this */

  /** @return {this} */
  fun play(): Unit /* this */

  /** @return {this} */
  fun reverse(): Unit /* this */

  /** @return {this} */
  fun cancel(): Unit /* this */

  /**
   * @param {Number} newDuration
   * @return {this}
   */
  fun stretch(newDuration: Double): Unit /* this */

  /**
   * Cancels the timer by seeking it back to 0 and reverting the attached scroller if necessary
   *
   * @return {this}
   */
  fun revert(): Unit /* this */

  /**
   * Imediatly completes the timer, cancels it and triggers the onComplete callback
   *
   * @return {this}
   */
  fun complete(): Unit /* this */

  /** @typedef {this & {then: null}} ResolvedTimer */
  /**
   * @param {Callback<ResolvedTimer>} [callback]
   * @return Promise<this>
   */
  fun then(callback: Callback<Any /* this & {
    then: null;
} */> = definedExternally): Promise<Any?>
}

internal external class JSAnimation : Timer {
  /**
   * @param {TargetsParam} targets
   * @param {AnimationParams} parameters
   * @param {Timeline} [parent]
   * @param {Number} [parentPosition]
   * @param {Boolean} [fastSet=false]
   * @param {Number} [index=0]
   * @param {Number} [length=0]
   */
  constructor(
      targets: Element,
      parameters: AnimationParams,
      parent: Timeline = definedExternally,
      parentPosition: Double = definedExternally,
      fastSet: Boolean = definedExternally,
      index: Double = definedExternally,
      length: Double = definedExternally
  )

  /** @type {TargetsArray} */
  var targets: MutableList<Element>
  /** @type {Callback<this>} */
  var onRender: Callback<Unit /* this */>
}

internal external fun animate(targets: Element, parameters: AnimationParams): JSAnimation
