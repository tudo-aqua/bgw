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
import ComponentViewData
import DiceViewData
import ID
import VisualData
import data.animation.*
import data.event.AnimationsStoppedEventData
import js.array.jsArrayOf
import kotlin.collections.get
import kotlin.js.js
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

internal class AnimationDetails() {
  val initialState: ComponentViewData? = null
}

internal object Animator {
  private val animations = mutableMapOf<ID, AnimationDetails>()
  // Stores timelines per component ID
  private val timelines = mutableMapOf<ID, Timeline>()
  // Tracks which animation types are active in each component's timeline
  private val componentAnimationTypes = mutableMapOf<ID, MutableSet<AnimationType>>()
  // Stores individual JSAnimation objects per component and animation type for reverting
  private val jsAnimations = mutableMapOf<ID, MutableMap<AnimationType, JSAnimation>>()
  // Stores Timer objects for delay animations
  private val delayTimers = mutableMapOf<ID, Timer>()

  // Stores current visual state for components undergoing flip/stepped animations
  private val visualStates = mutableMapOf<ID, VisualData?>()

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

  fun startAnimation(
      animation: AnimationData,
      existingTimeline: Timeline? = null,
      callback: (ID, ID?) -> Unit
  ) {
    var timeline = createTimeline()

    if (existingTimeline != null) timeline = existingTimeline

    val flatAnimations = flattenAnimation(animation)

    // Store timeline for each component and track animation types
    flatAnimations.forEach { animationData ->
      if (animationData is ComponentAnimationData) {
        val componentId = animationData.componentView?.id
        if (componentId != null) {
          timelines[componentId] = timeline

          // Track animation type for this component
          val animationType =
              when (animationData) {
                is FadeAnimationData -> AnimationType.FADE
                is MovementAnimationData -> AnimationType.MOVEMENT
                is RotationAnimationData -> AnimationType.ROTATION
                is ScaleAnimationData -> AnimationType.SCALE
                is FlipAnimationData -> AnimationType.FLIP
                is RandomizeAnimationData,
                is DiceAnimationData -> AnimationType.STEPPED
                else -> AnimationType.STEPPED
              }

          componentAnimationTypes.getOrPut(componentId) { mutableSetOf() }.add(animationType)
        }
      }
    }

    flatAnimations.forEach { animationData ->
      when (animationData) {
        is ComponentAnimationData -> {
          when (animationData) {
            is FadeAnimationData,
            is MovementAnimationData,
            is RotationAnimationData,
            is ScaleAnimationData,
            is FlipAnimationData,
            is RandomizeAnimationData,
            is DiceAnimationData -> startComponentAnimation(animationData, timeline, callback)

            else -> throw IllegalArgumentException("Unknown animation type")
          }
        }
        is DelayAnimationData -> startDelayAnimation(animationData, timeline, callback)
        else -> throw IllegalArgumentException("Unknown animation type")
      }
    }
  }

  fun startComponentAnimation(
      animationData: ComponentAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val componentId = animationData.componentView?.id ?: return
    if (animations[componentId] == null) animations[componentId] = AnimationDetails()

    document.getElementById(componentId)?.let { comp ->
      animations[componentId]?.apply {
        when (animationData) {
          is FadeAnimationData -> animateFade(comp, animationData, timeline, callback)
          is MovementAnimationData -> animateMovement(comp, animationData, timeline, callback)
          is RotationAnimationData -> animateRotation(comp, animationData, timeline, callback)
          is ScaleAnimationData -> animateScale(comp, animationData, timeline, callback)
          is FlipAnimationData -> animateFlip(comp, animationData, timeline, callback)
          is RandomizeAnimationData -> animateRandomize(comp, animationData, timeline, callback)
          is DiceAnimationData -> animateDice(comp, animationData, timeline, callback)
        }
      }
    }
  }

  fun animateFade(
      component: org.w3c.dom.Element,
      animationData: FadeAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val jsAnim =
        animate(
            component,
            jsObject {
              `--opaAnim` =
                  jsObject<TweenParams> {
                    from = animationData.fromOpacity.toString()
                    to = animationData.toOpacity.toString()
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    val componentId = animationData.componentView?.id
    if (componentId != null) {
      jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.FADE] = jsAnim
    }

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateMovement(
      component: org.w3c.dom.Element,
      animationData: MovementAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val jsAnim =
        animate(
            component,
            jsObject {
              `--txAnim` =
                  jsObject<TweenParams> {
                    to = "${animationData.byX}"
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              `--tyAnim` =
                  jsObject<TweenParams> {
                    to = "${animationData.byY}"
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    val componentId = animationData.componentView?.id
    if (componentId != null) {
      jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.MOVEMENT] = jsAnim
    }

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateRotation(
      component: org.w3c.dom.Element,
      animationData: RotationAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val jsAnim =
        animate(
            component,
            jsObject {
              `--rot` =
                  jsObject<TweenParams> {
                    from = "${animationData.fromAngle}deg"
                    to = "${animationData.toAngle}deg"
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    val componentId = animationData.componentView?.id
    if (componentId != null) {
      jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.ROTATION] = jsAnim
    }

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateScale(
      component: org.w3c.dom.Element,
      animationData: ScaleAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val jsAnim =
        animate(
            component,
            jsObject {
              `--sxAnim` =
                  jsObject<TweenParams> {
                    from = animationData.fromScaleX.toString()
                    to = animationData.toScaleX.toString()
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              `--syAnim` =
                  jsObject<TweenParams> {
                    from = animationData.fromScaleY.toString()
                    to = animationData.toScaleY.toString()
                    duration = animationData.duration
                    ease = easeForInterpolationType(animationData)
                  }
              onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    val componentId = animationData.componentView?.id
    if (componentId != null) {
      jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.SCALE] = jsAnim
    }

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateFlip(
      component: org.w3c.dom.Element,
      animationData: FlipAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    var imageSwitched = false
    val halfDuration = animationData.duration / 2
    val componentId = animationData.componentView?.id!!
    println(
        "Duration: ${animationData.duration}, Delay: ${animationData.initialDelay}, Half Duration: ${halfDuration}")

    // Set initial visual
    updateVisual(componentId, animationData.fromVisual!!)

    // Create a native JavaScript array for keyframes
    val keyframes =
        jsArrayOf<TweenParams>(
            jsObject {
              from = "0deg"
              to = "90deg"
              duration = halfDuration
              ease = "linear"
            },
            jsObject {
              to = "0deg"
              duration = halfDuration
              ease = "linear"
            })

    val jsAnim =
        animate(
            component,
            jsObject {
              `--flipAnim` = keyframes
              onBeforeUpdate = { anim, e ->
                if (anim.currentTime >= halfDuration && !imageSwitched) {
                  imageSwitched = true
                  // Update to target visual at midpoint
                  updateVisual(componentId, animationData.toVisual!!)
                }
              }
              onComplete = { anim, e ->
                // Clear visual state when animation completes
                visualStates.remove(componentId)
                callback.invoke(animationData.id, component.id)
              }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    if (componentId != null) {
      jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.FLIP] = jsAnim
    }

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateRandomize(
      component: org.w3c.dom.Element,
      animationData: RandomizeAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val componentId = animationData.componentView?.id!!
    val visuals = animationData.visuals
    val toVisual = animationData.toVisual!!
    val stepDuration = animationData.duration / animationData.speed
    var currentStep = 0
    val totalSteps = animationData.speed

    // Use animate with a dummy property to trigger updates at intervals
    val jsAnim =
        animate(
            component,
            jsObject {
              `--steppedAnim` =
                  jsArrayOf(
                      jsObject<TweenParams> {
                        from = "0"
                        to = "$totalSteps"
                        duration = animationData.duration
                        ease = "linear"
                      })
              onUpdate = { anim, _ ->
                val newStep = (anim.currentTime / stepDuration).toInt()
                if (newStep != currentStep && newStep < totalSteps - 1) {
                  currentStep = newStep
                  // Randomly pick a visual (except the last frame)
                  val randomIndex = kotlin.random.Random.nextInt(visuals.size)
                  updateVisual(componentId, visuals[randomIndex])
                } else if (newStep >= totalSteps - 1) {
                  // Show target visual in last step
                  updateVisual(componentId, toVisual)
                }
              }
              onComplete = { _, _ ->
                // Ensure final visual is set and clear state
                visualStates.remove(componentId)
                callback.invoke(animationData.id, component.id)
              }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.STEPPED] = jsAnim

    timeline.sync(jsAnim, animationData.initialDelay)
  }

  fun animateDice(
      component: org.w3c.dom.Element,
      animationData: DiceAnimationData,
      timeline: Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val componentId = animationData.componentView?.id!!
    val diceData = animationData.componentView as? DiceViewData

    if (diceData == null) {
      println("DiceAnimation requires DiceView component")
      callback.invoke(animationData.id, component.id)
      return
    }

    val visuals = diceData.visuals
    val toSide = animationData.toSide
    val toVisual = visuals.getOrNull(toSide)

    if (toVisual == null) {
      println("Invalid toSide: $toSide for DiceView with ${visuals.size} sides")
      callback.invoke(animationData.id, component.id)
      return
    }

    val stepDuration = animationData.duration / animationData.speed
    var currentStep = 0
    val totalSteps = animationData.speed

    // Use animate with a dummy property to trigger updates at intervals
    val jsAnim =
        animate(
            component,
            jsObject {
              `--steppedAnim` =
                  jsArrayOf(
                      jsObject<TweenParams> {
                        from = "0"
                        to = "$totalSteps"
                        duration = animationData.duration
                        ease = "linear"
                      })
              onUpdate = { anim, _ ->
                val newStep = (anim.currentTime / stepDuration).toInt()
                if (newStep != currentStep && newStep < totalSteps - 1) {
                  currentStep = newStep
                  // Randomly pick a side (except the last frame)
                  val randomSide = kotlin.random.Random.nextInt(visuals.size)
                  updateVisual(componentId, visuals[randomSide])
                } else if (newStep >= totalSteps - 1) {
                  // Show target side in last step
                  updateVisual(componentId, toVisual)
                }
              }
              onComplete = { _, _ ->
                // Ensure final visual is set and clear state
                visualStates.remove(componentId)
                callback.invoke(animationData.id, component.id)
              }
              autoplay = false
            })

    // Store the JSAnimation for later reverting
    jsAnimations.getOrPut(componentId) { mutableMapOf() }[AnimationType.STEPPED] = jsAnim

    timeline.sync(jsAnim, animationData.initialDelay)
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
              onComplete = { _, _ -> callback.invoke(animationData.id, null) }
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
    visualStates[componentId] = visual

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
          console.log("Removed opacity style for component $componentId")
        }
        AnimationType.MOVEMENT -> {
          style.removeProperty("--txAnim")
          style.removeProperty("--tyAnim")
          console.log("Removed --tx and --ty styles for component $componentId")
        }
        AnimationType.ROTATION -> {
          style.removeProperty("--rot")
          console.log("Removed --rot style for component $componentId")
        }
        AnimationType.SCALE -> {
          style.removeProperty("--sxAnim")
          style.removeProperty("--syAnim")
          console.log("Removed scale styles for component $componentId")
        }
        AnimationType.FLIP -> {
          style.removeProperty("--flipAnim")
          console.log("Removed flip styles for component $componentId")
        }
        AnimationType.STEPPED -> {
          style.removeProperty("--steppedAnim")
          console.log("Removed stepped styles for component $componentId")
        }
        else -> {}
      }
    }
  }

  fun removeAnimationTypesFromTimeline(componentId: String, animationType: AnimationType) {
    // Get the stored JSAnimation for this component and animation type
    val jsAnim = jsAnimations[componentId]?.get(animationType)

    if (jsAnim != null) {
      try {
        // Remove the JSAnimation from storage
        jsAnimations[componentId]?.remove(animationType)
      } catch (e: Exception) {
        console.log(
            "Failed to revert $animationType animation for component $componentId: ${e.message}")
      }
    } else {
      console.log("No JSAnimation found for $animationType on component $componentId")
    }

    // Remove CSS properties
    removeCssPropertiesForAnimationType(componentId, animationType)

    // Remove the animation type from tracking
    componentAnimationTypes[componentId]?.remove(animationType)

    // If no more animation types are active for this component, clean up
    if (componentAnimationTypes[componentId]?.isEmpty() == true) {
      componentAnimationTypes.remove(componentId)
      timelines.remove(componentId)
      jsAnimations.remove(componentId)
      visualStates.remove(componentId)
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
    timelines.values.forEach { timeline ->
      try {
        timeline.cancel()
      } catch (e: Exception) {
        console.log("Failed to cancel timeline: ${e.message}")
      }
    }

    // Remove all CSS properties for all active animations
    componentAnimationTypes.forEach { (componentId, animationTypes) ->
      animationTypes.forEach { animationType ->
        removeCssPropertiesForAnimationType(componentId, animationType)
      }
    }

    // Send a single event to notify backend that all animations were stopped
    // The backend will handle cleanup without triggering onFinished or persist logic
    try {
      JCEFEventDispatcher.dispatchEvent(AnimationsStoppedEventData())
    } catch (e: Exception) {
      console.log("Failed to dispatch animations stopped event: ${e.message}")
    }

    // Clear all tracking data structures
    animations.clear()
    timelines.clear()
    componentAnimationTypes.clear()
    jsAnimations.clear()
    visualStates.clear()
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
