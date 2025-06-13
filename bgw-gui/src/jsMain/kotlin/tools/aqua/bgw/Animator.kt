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
import DiceViewData
import ID
import data.animation.*
import kotlinx.browser.document
import org.w3c.dom.Node
import react.dom.render
import tools.aqua.bgw.builder.VisualBuilder
import web.dom.Element
import web.timers.Interval
import web.timers.Timeout
import web.timers.clearInterval
import web.timers.clearTimeout
import web.timers.setInterval
import web.timers.setTimeout

internal object Animator {
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
          // is ShakeAnimation<*> -> TODO()

          is SteppedComponentAnimationData -> {
            when (animationData) {
              is RandomizeAnimationData -> startRandomizeAnimation(animationData, callback)
              is DiceAnimationData -> startDiceAnimation(animationData, callback)
              else -> throw IllegalArgumentException("Unknown animation type")
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
    timeouts["${animation.id}-setup"] =
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

  fun clearAllTimeoutsAndIntervals() {
    for (i in 0 until intervals.size) {
      intervals.values.elementAt(i).let { clearInterval(it) }
    }

    for (i in 0 until timeouts.size) {
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
      if (excludeCleanup && matchingTimeouts.elementAt(i).endsWith("-cleanup")) {
        println("[S] Skipping timeout: ${matchingTimeouts.elementAt(i)}")
      } else {
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
          """.trimIndent()

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

              timeouts["${animation.id}-start"] =
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

    resetFunctions["${animation.id}-flip-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          render(VisualBuilder.build(animation.componentView?.visual), visuals)
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

              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                render(VisualBuilder.build(animation.fromVisual), oldVisuals)
              }

              timeouts["${animation.id}-start"] =
                  setTimeout(
                      {
                        val oldVisuals =
                            document.querySelector("#${componentId} > bgw_visuals") as Element?
                        if (oldVisuals != null) {
                          render(VisualBuilder.build(animation.toVisual), oldVisuals)
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
                                    render(
                                        VisualBuilder.build(animation.componentView?.visual),
                                        oldVisuals)
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

    intervals["${animation.id}-random"] =
        setInterval(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                render(VisualBuilder.build(animation.visuals.random()), oldVisuals)
              }
            },
            duration / animation.speed)

    resetFunctions["${animation.id}-random-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          render(VisualBuilder.build(animation.componentView?.visual), visuals)
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
                render(VisualBuilder.build(animation.toVisual), oldVisuals)
              }
              callback.invoke(animation.id)
              timeouts["${animation.id}-random-cleanup"] =
                  setTimeout(
                      {
                        if (oldVisuals != null) {
                          // Render the old visual after the animation is done
                          render(VisualBuilder.build(animation.componentView?.visual), oldVisuals)
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

    intervals["${animation.id}-dice"] =
        setInterval(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                render(VisualBuilder.build(dice.visuals.random()), oldVisuals)
              }
            },
            duration / animation.speed)

    resetFunctions["${animation.id}-dice-cleanup"] = {
      val visuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
      if (visuals != null) {
        try {
          render(VisualBuilder.build(dice.visuals[dice.currentSide]), visuals)
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
                render(VisualBuilder.build(dice.visuals[animation.toSide]), oldVisuals)
              }
              callback.invoke(animation.id)
              timeouts["${animation.id}-dice-cleanup"] =
                  setTimeout(
                      {
                        if (oldVisuals != null) {
                          // Render the old visual after the animation is done
                          render(VisualBuilder.build(dice.visuals[dice.currentSide]), oldVisuals)
                        }
                      },
                      CLEANUP_MS)
              clearSingleTimeoutAndInterval(animation.id, true)
            },
            duration)
  }

  private fun getTransitionCSS(animationList: List<AnimationData>): String {
    val transitions =
        animationList.map {
          when (it) {
            is FadeAnimationData -> "opacity ${it.duration}ms ease-in-out"
            is MovementAnimationData -> "translate ${it.duration}ms ease-in-out"
            is RotationAnimationData -> "rotate ${it.duration}ms ease-in-out"
            is ScaleAnimationData -> "scale ${it.duration}ms ease-in-out"
            else -> ""
          }
        }

    return """
            transition: ${transitions.filter { it.isNotEmpty() }.joinToString(", ")};
        """.trimIndent()
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
            """.trimIndent()
      is MovementAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    translate: calc(var(--bgwUnit) * ${animationData.byX}) calc(var(--bgwUnit) * ${animationData.byY});
                }
            """.trimIndent()
      is RotationAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    rotate: ${animationData.byAngle}deg;
                }
            """.trimIndent()
      is ScaleAnimationData ->
          """
                .${componentId}--${type}--props {
                    ${getTransitionCSS(parallelAnimations)}
                }
                
                .${componentId}--${type} {
                    scale: ${animationData.toScaleX} ${animationData.toScaleY} !important;
                }
            """.trimIndent()
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
            """.trimIndent()
      else -> ""
    }
  }
}
