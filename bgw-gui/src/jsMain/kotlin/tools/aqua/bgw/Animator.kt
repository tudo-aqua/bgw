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

package tools.aqua.bgw

import AnimationData
import ComponentViewData
import DiceViewData
import ID
import data.animation.*
import kotlin.js.js
import kotlinx.browser.document
import org.w3c.dom.Node
import org.w3c.dom.pointerevents.PointerEvent
import react.dom.client.Root
import react.dom.client.createRoot
import tools.aqua.bgw.builder.VisualBuilder
import web.dom.Element
import web.timers.Interval
import web.timers.Timeout
import web.timers.clearInterval
import web.timers.clearTimeout
import web.timers.setInterval
import web.timers.setTimeout
import kotlin.collections.get

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
  val timelines = mutableListOf<Pair<AnimationData, Timeline>>()
  val initialState: ComponentViewData? = null
}

internal object Animator {
  private val animations = mutableMapOf<ID, AnimationDetails>()

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
      is ComponentAnimationData, is DelayAnimationData -> {
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
        is ComponentAnimationData, is DelayAnimationData -> {
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
        is ComponentAnimationData, is DelayAnimationData -> {
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
      existingTimeline : Timeline? = null,
      callback: (ID, ID?) -> Unit
  ) {
//    if (!animationData.isRunning && animationData is ComponentAnimationData) {
//      revertSpecificAnimation(animationData)
//      return
//    }

      var timeline = createTimeline()
      timeline.onComplete = { t, e -> t.revert() }

      if(existingTimeline != null) timeline = existingTimeline

      val flatAnimations = flattenAnimation(animation)

      flatAnimations.forEach { animationData ->
          when (animationData) {
              is ComponentAnimationData -> {
                  when (animationData) {
                      is FadeAnimationData,
                      is MovementAnimationData,
                      is RotationAnimationData,
                      is ScaleAnimationData ->
                          startComponentAnimation(animationData, timeline, callback)

                      else -> throw IllegalArgumentException("Unknown animation type")
                  }
              }
              //            is DelayAnimationData -> startDelayAnimation(animationData, callback)
              else -> throw IllegalArgumentException("Unknown animation type")
          }
      }
  }

  fun startComponentAnimation(
      animationData: ComponentAnimationData,
      timeline : Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    val componentId = animationData.componentView?.id ?: return
    if (animations[componentId] == null) animations[componentId] = AnimationDetails()

    document.getElementById(componentId)?.let { comp ->
      animations[componentId]?.apply {
        when (animationData) {
          is FadeAnimationData ->
            animateFade(comp, animationData, timeline, callback)
          is MovementAnimationData ->
            animateMovement(comp, animationData, timeline, callback)
          is RotationAnimationData ->
            animateRotation(comp, animationData, timeline, callback)
          is ScaleAnimationData ->
            animateScale(comp, animationData, timeline, callback)
        }
      }
    }
  }

  fun animateFade(
      component: org.w3c.dom.Element,
      animationData: FadeAnimationData,
      timeline : Timeline,
      callback: (ID, ID?) -> Unit
  ) {
    timeline.add(
        component,
        js("({})").unsafeCast<AnimationParams>().apply {
          opacity =
              js("({})").unsafeCast<TweenParams>().apply {
                from = animationData.fromOpacity.toString()
                to = animationData.toOpacity.toString()
                duration = animationData.duration
              }
            onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
        }, animationData.initialDelay)
  }

  fun animateMovement(
      component: org.w3c.dom.Element,
      animationData: MovementAnimationData,
      timeline : Timeline,
      callback: (ID, ID?) -> Unit
  ) {
      timeline.add(
        component,
        js("({})").unsafeCast<AnimationParams>().apply {
          `--tx` =
              js("({})").unsafeCast<TweenParams>().apply {
                  to = "calc(var(--bgwUnit) * ${animationData.byX})"
                duration = animationData.duration
              }
          `--ty` =
              js("({})").unsafeCast<TweenParams>().apply {
                to = "calc(var(--bgwUnit) * ${animationData.byY})"
                duration = animationData.duration
              }
          onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
        }, animationData.initialDelay)
  }

  fun animateRotation(
      component: org.w3c.dom.Element,
      animationData: RotationAnimationData,
      timeline : Timeline,
      callback: (ID, ID?) -> Unit
  ) {
      timeline.add(
        component,
        js("({})").unsafeCast<AnimationParams>().apply {
          `--rot` =
              js("({})").unsafeCast<TweenParams>().apply {
                to = "${animationData.byAngle}deg"
                duration = animationData.duration
              }
            onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
        }, animationData.initialDelay)
  }

    fun animateScale(
        component: org.w3c.dom.Element,
        animationData: ScaleAnimationData,
        timeline : Timeline,
        callback: (ID, ID?) -> Unit
    ) {
        timeline.add(
            component,
            js("({})").unsafeCast<AnimationParams>().apply {
                scaleX =
                    js("({})").unsafeCast<TweenParams>().apply {
                        from = animationData.fromScaleX.toString()
                        to = animationData.toScaleX.toString()
                        duration = animationData.duration
                    }
                scaleY =
                    js("({})").unsafeCast<TweenParams>().apply {
                        from = animationData.fromScaleY.toString()
                        to = animationData.toScaleY.toString()
                        duration = animationData.duration
                    }
                onComplete = { anim, e -> callback.invoke(animationData.id, component.id) }
            }, animationData.initialDelay)
    }

  fun revertSpecificAnimation(animationData: ComponentAnimationData) {
    animations[animationData.componentView?.id]
        ?.timelines
        ?.first { anim -> anim.first.id == animationData.id }
        ?.second
        ?.revert()
    animations[animationData.componentView?.id]?.timelines?.removeAll { anim ->
      anim.first.id == animationData.id
    }
  }

    fun revertSpecificAnimations(anims : MutableMap<String, String?>) {
        anims.forEach { (id, compId) ->
            animations[compId]
                ?.timelines
                ?.first { anim -> anim.first.id == id }
                ?.second
                ?.revert()
            animations[compId]?.timelines?.removeAll { anim ->
                anim.first.id == id
            }
        }
    }

  fun revertAnimations(id: ID, clear: Boolean = false) {
    animations[id]?.let {
      it.timelines.asReversed().forEach { anim ->
        // Revert every animation that was played
        anim.second.revert()
        // Reset element to before the animation if special animations were played
        // TODO
      }
    }
    if (!clear) animations.remove(id)
  }

  fun stopAllAnimations() {
    animations.forEach { revertAnimations(it.key, true) }
    animations.clear()
  }
}

internal object AnimatorBackup {
  private val animations = mutableMapOf<String, Node>()
  private val timeouts = mutableMapOf<String, Timeout>()
  private val intervals = mutableMapOf<String, Interval>()
  private val resetFunctions = mutableMapOf<String, () -> Unit>()

  const val DELTA_MS = 20
  const val CLEANUP_MS = 100

  fun startAnimation(
      animationData: AnimationData,
      parallelAnimations: List<AnimationData> = listOf(),
      callback: (ID) -> Unit
  ) {
    when (animationData) {
      is ComponentAnimationData -> {
        when (animationData) {
          is FadeAnimationData ->
              startComponentAnimation("fade", animationData, parallelAnimations, callback)
          is MovementAnimationData ->
              startComponentAnimation("move", animationData, parallelAnimations, callback)
          is RotationAnimationData ->
              startComponentAnimation("rotate", animationData, parallelAnimations, callback)
          is ScaleAnimationData ->
              startComponentAnimation("scale", animationData, parallelAnimations, callback)
          is FlipAnimationData -> startFlipAnimation(animationData, callback)
          is SteppedComponentAnimationData -> {
            when (animationData) {
              is RandomizeAnimationData -> startRandomizeAnimation(animationData, callback)
              is DiceAnimationData -> startDiceAnimation(animationData, callback)
            }
          }
          else -> throw IllegalArgumentException("Unknown animation type")
        }
      }
      is DelayAnimationData -> startDelayAnimation(animationData, callback)
      is ParallelAnimationData -> startParallelAnimation(animationData, callback)
      is SequentialAnimationData -> startSequentialAnimation(animationData, callback)
      else -> throw IllegalArgumentException("Unknown animation type")
    }
  }

  private fun clearComponentAnimations(componentId: ID, types: List<String> = mutableListOf()) {
    if (types.isEmpty()) {
      this.animations.keys
          .filter { it.startsWith(componentId) }
          .forEach {
            if (document.body != null &&
                this.animations[it] != null &&
                document.body?.contains(this.animations[it]!!) == true) {
              document.body?.removeChild(this.animations[it]!!)
            }
            if (this.animations[it] != null) {
              this.animations.remove(it)
            }
          }
    } else {
      types.forEach {
        if (document.body != null &&
            this.animations["$componentId--$it"] != null &&
            document.body?.contains(this.animations["$componentId--$it"]!!) == true) {
          document.body?.removeChild(this.animations["$componentId--$it"]!!)
        }
        if (this.animations["$componentId--$it"] != null) {
          this.animations.remove("$componentId--$it")
        }
      }
    }
  }

  private fun startDelayAnimation(animation: DelayAnimationData, callback: (ID) -> Unit) {
    timeouts["${animation.id}-delay"] =
        setTimeout(
            {
              clearSingleTimeoutAndInterval(animation.id)
              callback.invoke(animation.id)
            },
            animation.duration)
  }

  private fun startSequentialAnimation(animation: SequentialAnimationData, callback: (ID) -> Unit) {
    val animations = animation.animations

    animations.forEach {
      if (it is DelayAnimationData ||
          it is SequentialAnimationData ||
          it is ParallelAnimationData ||
          it is SteppedComponentAnimationData)
          return@forEach
      val component = it as? ComponentAnimationData ?: return
      val componentId = component.componentView?.id.toString()

      clearComponentAnimations(componentId)
    }

    var currentDuration = 0
    for (anim in animations) {
      val component = anim as? ComponentAnimationData ?: return
      val componentId = component.componentView?.id.toString()

      timeouts["${anim.id}-all"] =
          setTimeout({ startAnimation(anim, animations, callback = callback) }, currentDuration)
      currentDuration += anim.duration
      if (anim == animations.last()) {
        val totalDuration = currentDuration + DELTA_MS
        val callbackTimeout =
            setTimeout(
                {
                  clearSingleTimeoutAndInterval(animation.id)
                  callback.invoke(animation.id)
                  timeouts["${animation.id}-cleanup"] =
                      setTimeout({ clearComponentAnimations(componentId) }, CLEANUP_MS)
                },
                totalDuration)
        timeouts["${animation.id}-callback"] = callbackTimeout
      }
    }
  }

  fun cancelCleanupTimeouts() {
    val matchingCleanups = timeouts.keys.filter { it.endsWith("-cleanup") }

    for (i in 0 until matchingCleanups.size) {
      val key = matchingCleanups.elementAt(i)
      timeouts[key]?.let { clearTimeout(it) }
      timeouts.remove(key)
    }

    // Also remove any reset functions as they would reset visuals too
    val cleanupResetFunctions = resetFunctions.keys.filter { it.contains("-cleanup") }
    cleanupResetFunctions.forEach { key -> resetFunctions.remove(key) }
  }

  fun clearAllTimeoutsAndIntervals(excludeDelay: Boolean = false) {
    for (i in 0 until intervals.size) {
      intervals.values.elementAt(i).let { clearInterval(it) }
    }

    for (i in 0 until timeouts.size) {
      if (excludeDelay && timeouts.keys.elementAt(i).endsWith("-delay")) {
        println("[S] Skipping delay timeout: ${timeouts.keys.elementAt(i)}")
        continue
      }
      timeouts.values.elementAt(i).let { clearTimeout(it) }
    }

    for (i in 0 until resetFunctions.size) {
      resetFunctions.values.elementAt(i).invoke()
    }

    intervals.clear()
    timeouts.clear()
    resetFunctions.clear()
  }

  private fun clearSingleTimeoutAndInterval(animationId: ID, excludeCleanup: Boolean = false) {
    val matchingIntervals = intervals.keys.filter { it.startsWith(animationId) }
    val matchingTimeouts = timeouts.keys.filter { it.startsWith(animationId) }
    val matchingResetFunctions = resetFunctions.keys.filter { it.startsWith(animationId) }

    for (i in 0 until matchingIntervals.size) {
      intervals[matchingIntervals.elementAt(i)]?.let { clearInterval(it) }
      intervals.remove(matchingIntervals.elementAt(i))
    }

    for (i in 0 until matchingTimeouts.size) {
      if (excludeCleanup && matchingTimeouts.elementAt(i).endsWith("-cleanup")) {} else {
        timeouts[matchingTimeouts.elementAt(i)]?.let { clearTimeout(it) }
        timeouts.remove(matchingTimeouts.elementAt(i))
      }
    }

    for (i in 0 until matchingResetFunctions.size) {
      // resetFunctions[matchingResetFunctions.elementAt(i)]?.invoke()
      resetFunctions.remove(matchingResetFunctions.elementAt(i))
    }
  }

  private fun startParallelAnimation(animation: ParallelAnimationData, callback: (ID) -> Unit) {
    val animations = animation.animations

    animations.forEach {
      if (it is DelayAnimationData ||
          it is SequentialAnimationData ||
          it is ParallelAnimationData ||
          it is SteppedComponentAnimationData)
          return@forEach
      val component = it as? ComponentAnimationData ?: return
      val componentId = component.componentView?.id.toString()

      clearComponentAnimations(componentId)
    }

    for (anim in animations) {
      val component = anim as? ComponentAnimationData ?: return
      val componentId = component.componentView?.id.toString()

      startAnimation(anim, animations, callback)
      if (anim == animations.last()) {
        val maxDuration = animations.maxOfOrNull { it.duration } ?: 0
        timeouts["${animation.id}-callback"] =
            setTimeout(
                {
                  clearSingleTimeoutAndInterval(animation.id)
                  callback.invoke(animation.id)
                  timeouts["${animation.id}-cleanup"] =
                      setTimeout({ clearComponentAnimations(componentId) }, CLEANUP_MS)
                },
                maxDuration + DELTA_MS)
      }
    }
  }

  private fun startComponentAnimation(
      type: String,
      animation: ComponentAnimationData,
      parallelAnimation: List<AnimationData> = listOf(),
      callback: (ID) -> Unit
  ) {
    // Get animation properties from data
    val componentId = animation.componentView?.id.toString()
    val duration = animation.duration

    // Get matching component element
    val element = document.getElementById(componentId) ?: return

    // Get old style element (if exists) and remove it
    clearComponentAnimations(componentId, listOf(type))

    // Toggle old animation off
    element.classList.toggle("${componentId}--$type--props", false)

    if (animation is ScaleAnimationData) {
      // Create initialization style element for initial scale
      val initStyleElement = document.createElement("style")
      initStyleElement.id = "${componentId}--$type--init"

      // Add CSS to set initial scale
      initStyleElement.innerHTML =
          """
            #${componentId} {
              scale: ${animation.fromScaleX} ${animation.fromScaleY};
            }
          """
              .trimIndent()

      document.body?.appendChild(initStyleElement)
      animations["$componentId--$type--init"] = initStyleElement
    }

    timeouts["${animation.id}-init"] =
        setTimeout(
            {
              // Create new style element
              val newElement = document.createElement("style")
              newElement.id = "${componentId}--$type"

              // Add new style element to body
              newElement.innerHTML =
                  getAnimationCSS(type, componentId, animation, parallelAnimation.toMutableList())
              document.body?.appendChild(newElement)

              // Toggle new animation on and save style element
              element.classList.toggle("${componentId}--$type--props", true)
              element.classList.toggle("${componentId}--$type", true)
              animations["$componentId--$type"] = newElement

              timeouts["${animation.id}-callback"] =
                  setTimeout(
                      {
                        // Toggle new animation off
                        element.classList.toggle("${componentId}--$type--props", false)
                        clearSingleTimeoutAndInterval(animation.id)
                        callback.invoke(animation.id)
                        if (parallelAnimation.isEmpty()) {
                          timeouts["${animation.id}-cleanup"] =
                              setTimeout(
                                  { clearComponentAnimations(componentId, listOf(type)) },
                                  CLEANUP_MS)
                        }
                      },
                      duration)
            },
            50)
  }

  private fun startFlipAnimation(animation: FlipAnimationData, callback: (ID) -> Unit) {
    val type = "flip"
    // Get animation properties from data
    val componentId = animation.componentView?.id.toString()
    val duration = animation.duration

    // Get matching component element
    val element = document.getElementById(componentId) ?: return

    // Get old style element (if exists) and remove it
    clearComponentAnimations(componentId, listOf(type))

    // Toggle old animation off
    element.classList.toggle("${componentId}--$type--props", false)

    val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
    var visualRoot: Root? = null

    resetFunctions["${animation.id}-flip-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          visualRoot?.render(VisualBuilder.build(animation.componentView?.visual))
        } catch (e: Exception) {
          println("Error rendering dice side: ${e.message}")
        }
      }
    }

    timeouts["${animation.id}-init"] =
        setTimeout(
            {
              // Create new style element
              val newElement = document.createElement("style")
              newElement.id = "${componentId}--$type"

              // Add new style element to body
              newElement.innerHTML = getAnimationCSS(type, componentId, animation)
              document.body?.appendChild(newElement)

              // Toggle new animation on and save style element
              element.classList.toggle("${componentId}--$type--props", true)
              element.classList.toggle("${componentId}--$type", true)
              animations["$componentId--$type"] = newElement

              if (oldVisuals != null) {
                visualRoot = createRoot(oldVisuals)
                visualRoot.render(VisualBuilder.build(animation.fromVisual))
              }

              timeouts["${animation.id}-start"] =
                  setTimeout(
                      {
                        val oldVisuals =
                            document.querySelector("#${componentId} > bgw_visuals") as Element?
                        if (oldVisuals != null) {
                          visualRoot?.render(VisualBuilder.build(animation.toVisual))
                        }
                      },
                      duration / 2)

              timeouts["${animation.id}-callback"] =
                  setTimeout(
                      {
                        // Toggle new animation off
                        element.classList.toggle("${componentId}--$type--props", false)
                        callback.invoke(animation.id)
                        timeouts["${animation.id}-flip-cleanup"] =
                            setTimeout(
                                {
                                  if (oldVisuals != null) {
                                    // Render the old visual after the animation is done
                                    visualRoot?.render(
                                        VisualBuilder.build(animation.componentView?.visual))
                                  }
                                },
                                CLEANUP_MS)
                        clearSingleTimeoutAndInterval(animation.id, true)
                      },
                      duration)
            },
            50)
  }

  private fun startRandomizeAnimation(animation: RandomizeAnimationData, callback: (ID) -> Unit) {
    val type = "random"
    // Get animation properties from data
    val componentId = animation.componentView?.id.toString()
    val duration = animation.duration

    val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
    var visualRoot: Root? = null

    if (oldVisuals != null) {
      visualRoot = createRoot(oldVisuals)
    }

    intervals["${animation.id}-random"] =
        setInterval(
            {
              if (oldVisuals != null) {
                visualRoot?.render(VisualBuilder.build(animation.visuals.random()))
              }
            },
            duration / animation.speed)

    resetFunctions["${animation.id}-random-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          visualRoot?.render(VisualBuilder.build(animation.componentView?.visual))
        } catch (e: Exception) {
          println("Error rendering dice side: ${e.message}")
        }
      }
    }

    timeouts["${animation.id}-callback"] =
        setTimeout(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                visualRoot?.render(VisualBuilder.build(animation.toVisual))
              }
              callback.invoke(animation.id)
              timeouts["${animation.id}-random-cleanup"] =
                  setTimeout(
                      {
                        if (oldVisuals != null) {
                          // Render the old visual after the animation is done
                          visualRoot?.render(VisualBuilder.build(animation.componentView?.visual))
                        }
                      },
                      CLEANUP_MS)
              clearSingleTimeoutAndInterval(animation.id, true)
            },
            duration)
  }

  private fun startDiceAnimation(animation: DiceAnimationData, callback: (ID) -> Unit) {
    val type = "dice"
    // Get animation properties from data
    val componentId = animation.componentView?.id.toString()
    val dice = animation.componentView as? DiceViewData ?: return
    val duration = animation.duration

    val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
    var visualRoot: Root? = null

    if (oldVisuals != null) {
      visualRoot = createRoot(oldVisuals)
    }

    intervals["${animation.id}-dice"] =
        setInterval(
            {
              if (oldVisuals != null) {
                visualRoot?.render(VisualBuilder.build(dice.visuals.random()))
              }
            },
            duration / animation.speed)

    resetFunctions["${animation.id}-dice-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          visualRoot?.render(VisualBuilder.build(dice.visuals[dice.currentSide]))
        } catch (e: Exception) {
          println("Error rendering dice side: ${e.message}")
        }
      }
    }

    timeouts["${animation.id}-callback"] =
        setTimeout(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                visualRoot?.render(VisualBuilder.build(dice.visuals[animation.toSide]))
              }
              callback.invoke(animation.id)
              timeouts["${animation.id}-dice-cleanup"] =
                  setTimeout(
                      {
                        if (oldVisuals != null) {
                          // Render the old visual after the animation is done
                          visualRoot?.render(VisualBuilder.build(dice.visuals[dice.currentSide]))
                        }
                      },
                      CLEANUP_MS)
              clearSingleTimeoutAndInterval(animation.id, true)
            },
            duration)
  }

  private fun easingFromInterpolation(interpolation: String): String {
    return when (interpolation) {
      "linear" -> "linear"
      "smooth" -> "ease-in-out"
      "spring" -> "cubic-bezier(.41,0,.41,1.49)"
      "steps" -> "steps(10, end)"
      else -> "ease-in-out" // Default fallback
    }
  }

  private fun getTransitionCSS(animationList: List<AnimationData>): String {
    val transitions =
        animationList.map {
          when (it) {
            is FadeAnimationData ->
                "opacity ${it.duration}ms ${easingFromInterpolation(it.interpolation)}"
            is MovementAnimationData ->
                "translate ${it.duration}ms ${easingFromInterpolation(it.interpolation)}"
            is RotationAnimationData ->
                "rotate ${it.duration}ms ${easingFromInterpolation(it.interpolation)}"
            is ScaleAnimationData ->
                "scale ${it.duration}ms ${easingFromInterpolation(it.interpolation)}"
            else -> ""
          }
        }

    return """
            transition: ${transitions.filter { it.isNotEmpty() }.joinToString(", ")};
        """
        .trimIndent()
  }

  private fun getAnimationCSS(
      type: String,
      componentId: String,
      animationData: AnimationData,
      parallelAnimations: MutableList<AnimationData> = mutableListOf()
  ): String {
    if (parallelAnimations.isEmpty()) {
      parallelAnimations.add(animationData)
    }
    return when (animationData) {
      is FadeAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    opacity: ${animationData.toOpacity};
                }
            """
              .trimIndent()
      is MovementAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    translate: calc(var(--bgwUnit) * ${animationData.byX}) calc(var(--bgwUnit) * ${animationData.byY});
                }
            """
              .trimIndent()
      is RotationAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    rotate: ${animationData.byAngle}deg;
                }
            """
              .trimIndent()
      is ScaleAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    scale: ${animationData.toScaleX} ${animationData.toScaleY} !important;
                }
            """
              .trimIndent()
      is FlipAnimationData ->
          """                
                .${componentId}--${type}--props {
                    animation: ${componentId}--${type}--flip ${animationData.duration}ms linear;
                }
                
                @keyframes ${componentId}--${type}--flip {
                    0% {
                        transform: rotateY(0deg);
                    }
                    50% {
                        transform: rotateY(90deg);
                    }
                    100% {
                        transform: rotateY(0deg);
                    }
                }
            """
              .trimIndent()
      else -> ""
    }
  }
}
