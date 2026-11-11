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

package tools.aqua.bgw

import AnimationData
import DiceViewData
import ID
import VisualData
import data.animation.*
import data.event.AnimationsStoppedEventData
import kotlinx.browser.document
import org.w3c.dom.pointerevents.PointerEvent
import tools.aqua.bgw.animation.AnimationType
import tools.aqua.bgw.core.AnimationInterpolation
import tools.aqua.bgw.elements.jsObject
import tools.aqua.bgw.event.JCEFEventDispatcher

/**
 * Accepts:<br>
 * - `Number` - Absolute position in milliseconds (e.g., `500` places element at exactly 500ms)<br>
 * - `'+=Number'` - Addition: Position element X ms after the last element (e.g., `'+=100'`)<br>
 * - `'-=Number'` - Subtraction: Position element X ms before the last element's end (e.g.,
 *   `'-=100'`)<br>
 * - `'*=Number'` - Multiplier: Position element at a fraction of the total duration (e.g., `'*=.5'`
 *   for halfway)<br>
 * - `'<'` - Previous end: Position element at the end position of the previous element<br>
 * - `'<<'` - Previous start: Position element at the start position of the previous element<br>
 * - `'<<+=Number'` - Combined: Position element relative to previous element's start (e.g.,
 *   `'<<+=250'`)<br>
 * - `'label'` - Label: Position element at a named label position (e.g., `'My Label'`)
 */
internal typealias TimelinePosition =
    Any /* number | `+=${number}` | `-=${number}` | `*=${number}` | "<" | "<<" | `<<+=${number}` | `<<-=${number}` | string */

/**
 * Accepts:<br>
 * - `Number` - Absolute position in milliseconds (e.g., `500` places animation at exactly
 *   500ms)<br>
 * - `'+=Number'` - Addition: Position animation X ms after the last animation (e.g., `'+=100'`)<br>
 * - `'-=Number'` - Subtraction: Position animation X ms before the last animation's end (e.g.,
 *   `'-=100'`)<br>
 * - `'*=Number'` - Multiplier: Position animation at a fraction of the total duration (e.g.,
 *   `'*=.5'` for halfway)<br>
 * - `'<'` - Previous end: Position animation at the end position of the previous animation<br>
 * - `'<<'` - Previous start: Position animation at the start position of the previous animation<br>
 * - `'<<+=Number'` - Combined: Position animation relative to previous animation's start (e.g.,
 *   `'<<+=250'`)<br>
 * - `'label'` - Label: Position animation at a named label position (e.g., `'My Label'`)<br>
 * - `stagger(String|Nummber)` - Stagger multi-elements animation positions (e.g., 10, 20, 30...)
 */
internal external interface TimelineOptions {
  var defaults: DefaultsParams?
  var playbackEase: String?
}

internal typealias Callback<T> = (self: T, e: PointerEvent) -> Unit

/**
 * Plays BGW animations in the browser.
 *
 * Every component animation drives its own progress object (`{ p: 0..1 }`). All animations of one
 * kind on one component form a [Track], and the track composes the CSS value from the progress of
 * all of its segments. Overlapping animations of the same kind add up, and consecutive ones
 * continue from where the previous one ended.
 */
internal object Animator {
  // Stores timelines per component ID (and per delay animation ID)
  private val timelines = mutableMapOf<ID, Timeline>()
  // Stores Timer objects for delay animations
  private val delayTimers = mutableMapOf<ID, Timer>()
  // Active animation tracks per component and animation type
  private val tracks = mutableMapOf<ID, MutableMap<AnimationType, Track>>()

  // Stores current visual state for components undergoing flip/stepped animations
  private val visualStates = mutableMapOf<ID, VisualData?>()

  /** A single component animation inside a [Track]. */
  private class Segment(val data: ComponentAnimationData) {
    /** Plain object animated by anime.js. Its `p` property runs from 0 to 1 (eased). */
    val progress: dynamic = js("({ p: 0 })")
    var completed: Boolean = false

    /** Last step and visual picked by stepped animations. */
    var lastStep: Int = -1
    var steppedVisual: VisualData? = null

    val p: Double
      get() = (progress.p as Number).toDouble()

    val started: Boolean
      get() = completed || p != 0.0
  }

  /** All animations of one [type] on one component, ordered by their start time. */
  private class Track(val componentId: ID, val type: AnimationType) {
    val segments = mutableListOf<Segment>()

    val isFinished: Boolean
      get() = segments.all { it.completed }
  }

  /**
   * Gets the current visual for a component, considering any active flip/stepped animations. If no
   * animation is active, returns null and the component should use its default visual.
   */
  fun getCurrentVisual(componentId: ID): VisualData? = visualStates[componentId]

  /**
   * Flattens a potentially nested animation structure into a list of base animations
   * (ComponentAnimationData or DelayAnimationData) with their initialDelay values calculated.
   *
   * @param animation The animation to flatten (can be Sequential, Parallel, Component, or Delay)
   * @param baseDelay The base delay to add to all animations (used for recursive calls)
   * @return A list of flattened animations with computed initialDelay values
   */
  fun flattenAnimation(animation: AnimationData, baseDelay: Int = 0): List<AnimationData> {
    return when (animation) {
      is SequentialAnimationData -> flattenSequentialAnimation(animation, baseDelay)
      is ParallelAnimationData -> flattenParallelAnimation(animation, baseDelay)
      is ComponentAnimationData,
      is DelayAnimationData -> {
        animation.initialDelay = baseDelay
        listOf(animation)
      }
      else -> emptyList()
    }
  }

  /**
   * Flattens a SequentialAnimation where each animation starts after the previous one completes.
   *
   * @param animation The sequential animation to flatten
   * @param baseDelay The base delay to apply to all child animations
   * @return A list of flattened animations with sequential delays
   */
  private fun flattenSequentialAnimation(
      animation: SequentialAnimationData,
      baseDelay: Int = 0
  ): List<AnimationData> {
    val flattenedAnimations = mutableListOf<AnimationData>()
    var currentDelay = baseDelay

    for (childAnimation in animation.animations) {
      when (childAnimation) {
        is SequentialAnimationData -> {
          // Recursively flatten nested sequential animations
          val flattened = flattenSequentialAnimation(childAnimation, currentDelay)
          flattenedAnimations.addAll(flattened)
          // Update delay to after the last animation in the sequence
          if (flattened.isNotEmpty()) {
            currentDelay = flattened.maxOf { it.initialDelay + it.duration }
          }
        }
        is ParallelAnimationData -> {
          // Recursively flatten nested parallel animations
          val flattened = flattenParallelAnimation(childAnimation, currentDelay)
          flattenedAnimations.addAll(flattened)
          // Update delay to after the longest animation in the parallel group
          if (flattened.isNotEmpty()) {
            currentDelay = flattened.maxOf { it.initialDelay + it.duration }
          }
        }
        is ComponentAnimationData,
        is DelayAnimationData -> {
          // Base case: add the animation with current delay
          childAnimation.initialDelay = currentDelay
          flattenedAnimations.add(childAnimation)
          currentDelay += childAnimation.duration
        }
      }
    }

    return flattenedAnimations
  }

  /**
   * Flattens a ParallelAnimation where all animations start at the same time.
   *
   * @param animation The parallel animation to flatten
   * @param baseDelay The base delay to apply to all child animations
   * @return A list of flattened animations all starting at the same time
   */
  private fun flattenParallelAnimation(
      animation: ParallelAnimationData,
      baseDelay: Int = 0
  ): List<AnimationData> {
    val flattenedAnimations = mutableListOf<AnimationData>()

    for (childAnimation in animation.animations) {
      when (childAnimation) {
        is SequentialAnimationData -> {
          // Recursively flatten nested sequential animations
          val flattened = flattenSequentialAnimation(childAnimation, baseDelay)
          flattenedAnimations.addAll(flattened)
        }
        is ParallelAnimationData -> {
          // Recursively flatten nested parallel animations
          val flattened = flattenParallelAnimation(childAnimation, baseDelay)
          flattenedAnimations.addAll(flattened)
        }
        is ComponentAnimationData,
        is DelayAnimationData -> {
          // Base case: add the animation with base delay (all parallel animations start together)
          childAnimation.initialDelay = baseDelay
          flattenedAnimations.add(childAnimation)
        }
      }
    }

    return flattenedAnimations
  }

  /**
   * Exposes playback controls as `window.bgwAnimator` so the BGW playground can pause, resume and
   * scrub the animations that are currently running.
   */
  private fun exposeControls() {
    val controls = js("{}")
    controls.pause = { timelines.values.toSet().forEach { it.pause() } }
    controls.resume = { timelines.values.toSet().forEach { it.play() } }
    controls.seek = { time: Double ->
      timelines.values.toSet().forEach { it.seek(time) }
      renderAllTracks()
    }
    controls.time = { timelines.values.firstOrNull()?.currentTime ?: 0.0 }
    controls.paused = { timelines.values.any { it.paused } }
    controls.active = { timelines.values.any { !it.completed } }
    kotlinx.browser.window.asDynamic().bgwAnimator = controls
  }

  private fun animationTypeOf(animationData: ComponentAnimationData): AnimationType =
      when (animationData) {
        is FadeAnimationData -> AnimationType.FADE
        is MovementAnimationData -> AnimationType.MOVEMENT
        is RotationAnimationData -> AnimationType.ROTATION
        is ScaleAnimationData -> AnimationType.SCALE
        is FlipAnimationData -> AnimationType.FLIP
        else -> AnimationType.STEPPED
      }

  fun startAnimation(
      animation: AnimationData,
      existingTimeline: Timeline? = null,
      callback: (ID, ID?) -> Unit
  ) {
    exposeControls()
    val timeline =
        existingTimeline
            ?: createTimeline(jsObject<TimelineParams> { onUpdate = { _, _ -> renderAllTracks() } })

    val flatAnimations = flattenAnimation(animation).sortedBy { it.initialDelay }

    // Tracks created by this animation tree. They replace finished tracks of earlier trees.
    val newTracks = mutableMapOf<Pair<ID, AnimationType>, Track>()

    flatAnimations.forEach { animationData ->
      when (animationData) {
        is ComponentAnimationData -> {
          val componentId = animationData.componentView?.id ?: return@forEach
          val type = animationTypeOf(animationData)
          val track =
              newTracks.getOrPut(componentId to type) {
                val componentTracks = tracks.getOrPut(componentId) { mutableMapOf() }
                val existing = componentTracks[type]
                if (existing != null && !existing.isFinished) existing
                else Track(componentId, type).also { componentTracks[type] = it }
              }
          timelines[componentId] = timeline
          startComponentAnimation(animationData, track, timeline, callback)
        }
        is DelayAnimationData -> {
          timelines[animationData.id] = timeline
          startDelayAnimation(animationData, timeline, callback)
        }
        else -> throw IllegalArgumentException("Unknown animation type")
      }
    }
  }

  private fun startComponentAnimation(
      animationData: ComponentAnimationData,
      track: Track,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val segment = Segment(animationData)
    track.segments.add(segment)

    if (!isPlayable(animationData)) {
      segment.completed = true
      callback.invoke(animationData.id, track.componentId)
      return
    }

    val tween = js("{}")
    tween.from = 0
    tween.to = 1
    tween.duration = animationData.duration
    tween.ease =
        when (animationData) {
          is FlipAnimationData,
          is SteppedComponentAnimationData -> "linear"
          else -> easeForInterpolationType(animationData)
        }

    val parameters = js("{}")
    parameters.p = tween
    parameters.autoplay = false
    parameters.composition = "none"
    parameters.onUpdate = { _: dynamic, _: dynamic -> render(track) }
    parameters.onComplete = { _: dynamic, _: dynamic ->
      segment.completed = true
      render(track)
      callback.invoke(animationData.id, track.componentId)
    }

    timeline.sync(
        animateTarget(segment.progress.unsafeCast<Any>(), parameters.unsafeCast<Any>()),
        animationData.initialDelay)
  }

  private fun isPlayable(animationData: ComponentAnimationData): Boolean =
      when (animationData) {
        is FlipAnimationData -> {
          val playable = animationData.fromVisual != null && animationData.toVisual != null
          if (!playable) println("FlipAnimation requires fromVisual and toVisual")
          playable
        }
        is RandomizeAnimationData -> {
          val playable = animationData.toVisual != null && animationData.visuals.isNotEmpty()
          if (!playable) println("RandomizeAnimation requires toVisual and at least one visual")
          playable
        }
        is DiceAnimationData -> {
          val dice = animationData.componentView as? DiceViewData
          val playable = dice?.visuals?.getOrNull(animationData.toSide) != null
          if (!playable) println("DiceAnimation requires a DiceView with a visual for the side")
          playable
        }
        else -> true
      }

  private fun renderAllTracks() {
    tracks.values.toList().forEach { componentTracks -> componentTracks.values.forEach(::render) }
  }

  /** Writes the value the [track] currently composes to its component. */
  private fun render(track: Track) {
    val segments = track.segments
    if (segments.isEmpty()) return
    val style = document.getElementById(track.componentId)?.asDynamic()?.style

    when (track.type) {
      AnimationType.MOVEMENT -> {
        var x = 0.0
        var y = 0.0
        segments.forEach {
          val data = it.data as MovementAnimationData
          x += data.byX * it.p
          y += data.byY * it.p
        }
        style?.setProperty("--txAnim", "$x")
        style?.setProperty("--tyAnim", "$y")
      }
      AnimationType.ROTATION -> {
        var angle = (segments.first().data as RotationAnimationData).fromAngle
        segments.forEach {
          val data = it.data as RotationAnimationData
          angle += (data.toAngle - data.fromAngle) * it.p
        }
        style?.setProperty("--rot", "${angle}deg")
      }
      AnimationType.SCALE -> {
        val first = segments.first().data as ScaleAnimationData
        var scaleX = first.fromScaleX
        var scaleY = first.fromScaleY
        segments.forEach {
          val data = it.data as ScaleAnimationData
          scaleX = chainScale(scaleX, data.fromScaleX, data.toScaleX, it.p)
          scaleY = chainScale(scaleY, data.fromScaleY, data.toScaleY, it.p)
        }
        style?.setProperty("--sxAnim", "$scaleX")
        style?.setProperty("--syAnim", "$scaleY")
      }
      AnimationType.FADE -> {
        var opacity = (segments.first().data as FadeAnimationData).fromOpacity
        segments.forEach {
          val data = it.data as FadeAnimationData
          opacity += (data.toOpacity - opacity) * it.p
        }
        style?.setProperty("--opaAnim", "$opacity")
      }
      AnimationType.FLIP -> {
        var angle = 0.0
        segments.forEach { angle += 90.0 * (1.0 - kotlin.math.abs(1.0 - 2.0 * it.p)) }
        style?.setProperty("--flipAnim", "${angle}deg")

        // The most recently started flip decides the visual: its front half shows the visual
        // it flips from, the back half the visual it flips to.
        segments
            .lastOrNull { it.started }
            ?.let {
              val data = it.data as FlipAnimationData
              updateVisual(track.componentId, if (it.p < 0.5) data.fromVisual else data.toVisual)
            }
      }
      AnimationType.STEPPED -> {
        segments.lastOrNull { it.started }?.let { renderStepped(track.componentId, it) }
      }
      else -> {}
    }
  }

  /**
   * Chains a scale animation onto the [current] scale. Scales are relative to their start value
   * (like the JVM persists them), absolute if the animation starts at zero.
   */
  private fun chainScale(current: Double, from: Double, to: Double, progress: Double): Double =
      if (from == 0.0) current + (to - current) * progress
      else current * (1.0 + (to / from - 1.0) * progress)

  private fun renderStepped(componentId: ID, segment: Segment) {
    val data = segment.data
    val (visuals, toVisual) =
        when (data) {
          is DiceAnimationData -> {
            val diceVisuals = (data.componentView as DiceViewData).visuals
            diceVisuals to diceVisuals[data.toSide]
          }
          is RandomizeAnimationData -> data.visuals to data.toVisual
          else -> return
        }
    val totalSteps = maxOf(1, (data as SteppedComponentAnimationData).speed)
    val step = minOf(totalSteps - 1, (segment.p * totalSteps).toInt())

    // The last step always shows the target visual, all others a random one.
    if (segment.completed || step >= totalSteps - 1) {
      updateVisual(componentId, toVisual)
    } else {
      if (step != segment.lastStep || segment.steppedVisual == null) {
        segment.lastStep = step
        segment.steppedVisual = visuals[kotlin.random.Random.nextInt(visuals.size)]
      }
      updateVisual(componentId, segment.steppedVisual)
    }
  }

  /**
   * Starts a delay animation by creating a Timer and syncing it to the timeline. The timer simply
   * waits for the specified duration and then triggers the callback.
   *
   * @param animationData The delay animation data containing duration and ID
   * @param timeline The timeline to sync the delay timer with
   * @param callback The callback to invoke when the delay completes
   */
  fun startDelayAnimation(
      animationData: DelayAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    // Create a Timer that just waits for the duration
    val timer =
        Timer(
            jsObject<TimerParams> {
              duration = animationData.duration
              onComplete = { _, _ ->
                delayTimers.remove(animationData.id)
                callback.invoke(animationData.id, null)
              }
              autoplay = false
            })

    // Store the timer for potential cleanup
    delayTimers[animationData.id] = timer

    // Sync the timer with the timeline at the specified position
    timeline.sync(timer, animationData.initialDelay)
  }

  /**
   * Updates the visual state for a component and triggers a React re-render. This is used by flip
   * and stepped animations to change visuals.
   */
  private fun updateVisual(componentId: ID, visual: VisualData?) {
    if (visualStates.containsKey(componentId) && visualStates[componentId] === visual) return
    visualStates[componentId] = visual
    notifyVisualUpdate(componentId, visual)
  }

  /** Tells the component to re-read its visual, which is the animated one while one is set. */
  private fun notifyVisualUpdate(componentId: ID, visual: VisualData?) {
    // Force a re-render by dispatching a custom event that React components can listen to
    val element = document.getElementById(componentId)
    if (element != null) {
      val detail = js("{}")
      detail.componentId = componentId
      detail.visual = visual
      val eventInit = js("{}")
      eventInit.detail = detail
      val event =
          js("new CustomEvent('bgw-visual-update', eventInit)")
              .unsafeCast<org.w3c.dom.events.Event>()
      element.dispatchEvent(event)
    }
  }

  /**
   * Removes CSS properties for a specific animation type from a component.
   *
   * @param componentId The ID of the component
   * @param animationType The type of animation whose CSS properties should be removed
   */
  private fun removeCssPropertiesForAnimationType(
      componentId: String,
      animationType: AnimationType
  ) {
    document.getElementById(componentId)?.let { element ->
      val style = element.asDynamic().style
      when (animationType) {
        AnimationType.FADE -> {
          style.removeProperty("--opaAnim")
        }
        AnimationType.MOVEMENT -> {
          style.removeProperty("--txAnim")
          style.removeProperty("--tyAnim")
        }
        AnimationType.ROTATION -> {
          style.removeProperty("--rot")
        }
        AnimationType.SCALE -> {
          style.removeProperty("--sxAnim")
          style.removeProperty("--syAnim")
        }
        AnimationType.FLIP -> {
          style.removeProperty("--flipAnim")
        }
        AnimationType.STEPPED -> {
          style.removeProperty("--steppedAnim")
        }
        else -> {}
      }
    }
  }

  /**
   * Reverts the animated CSS values of the given [animationType] once the JVM reported that all
   * animations of that type on the component finished.
   */
  fun removeAnimationTypesFromTimeline(componentId: String, animationType: AnimationType) {
    val componentTracks = tracks[componentId]
    val track = componentTracks?.get(animationType)

    // Keep the values if a newer animation of this type already runs on the component.
    if (track != null && !track.isFinished) return

    componentTracks?.remove(animationType)
    removeCssPropertiesForAnimationType(componentId, animationType)

    if (animationType == AnimationType.FLIP || animationType == AnimationType.STEPPED) {
      val otherVisualTrack =
          componentTracks?.keys?.any { it == AnimationType.FLIP || it == AnimationType.STEPPED }
      if (otherVisualTrack != true && visualStates.containsKey(componentId)) {
        visualStates.remove(componentId)
        notifyVisualUpdate(componentId, null)
      }
    }

    if (componentTracks != null && componentTracks.isEmpty()) {
      tracks.remove(componentId)
      timelines.remove(componentId)
    }
  }

  /**
   * Stops all currently running animations immediately without calling their onComplete hooks.
   * Cancels all timelines, removes all CSS animation properties, notifies the backend to clean up
   * animation states (isRunning = false, remove animation types from components) WITHOUT triggering
   * onFinished callbacks or persist logic, and cleans up all tracking data.
   */
  fun stopAllAnimations() {
    // Cancel all timelines (this prevents onComplete callbacks from being called)
    timelines.values.toSet().forEach { timeline ->
      try {
        timeline.cancel()
      } catch (e: Exception) {
        console.log("Failed to cancel timeline: ${e.message}")
      }
    }

    delayTimers.values.forEach { timer ->
      try {
        timer.cancel()
      } catch (e: Exception) {
        console.log("Failed to cancel delay timer: ${e.message}")
      }
    }

    // Remove all CSS properties for all active animations
    tracks.forEach { (componentId, componentTracks) ->
      componentTracks.keys.forEach { removeCssPropertiesForAnimationType(componentId, it) }
    }

    // Send a single event to notify backend that all animations were stopped
    // The backend will handle cleanup without triggering onFinished or persist logic
    try {
      JCEFEventDispatcher.dispatchEvent(AnimationsStoppedEventData())
    } catch (e: Exception) {
      console.log("Failed to dispatch animations stopped event: ${e.message}")
    }

    // Clear all tracking data structures
    timelines.clear()
    tracks.clear()
    // Components that showed an animated visual fall back to their own again.
    val animatedVisuals = visualStates.keys.toList()
    visualStates.clear()
    animatedVisuals.forEach { notifyVisualUpdate(it, null) }
    delayTimers.clear()
  }

  internal fun easeForInterpolationType(animationData: ComponentAnimationData): Any {
    return when (animationData.interpolation) {
      AnimationInterpolation.SMOOTH -> "inOut"
      AnimationInterpolation.LINEAR -> "linear"
      AnimationInterpolation.SPRING -> "outElastic(1.05,0.34)"
      AnimationInterpolation.STEPS -> steps(10)
    }
  }
}
