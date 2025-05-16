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
import web.cssom.ElementCSSInlineStyle
import web.dom.Element
import web.timers.clearInterval
import web.timers.setInterval
import web.timers.setTimeout

internal class Animator {
  private val animations = mutableMapOf<String, Node>()

  companion object {
    const val DELTA_MS = 20
  }

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
    console.log("Starting delay animation")
    setTimeout({ callback.invoke(animation.id) }, animation.duration)
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
      setTimeout({ startAnimation(anim, animations, callback = callback) }, currentDuration)
      currentDuration += anim.duration
      if (anim == animations.last()) {
        val totalDuration = currentDuration + DELTA_MS
        setTimeout({ callback.invoke(animation.id) }, totalDuration)
      }
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
      startAnimation(anim, animations, callback)
      if (anim == animations.last()) {
        val maxDuration = animations.maxOfOrNull { it.duration } ?: 0
        setTimeout({ callback.invoke(animation.id) }, maxDuration + DELTA_MS)
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
      (element as ElementCSSInlineStyle).style.scale =
          "${animation.fromScaleX} ${animation.fromScaleY}"
    }

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

          setTimeout(
              {
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
                callback.invoke(animation.id)
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

          setTimeout(
              {
                val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
                if (oldVisuals != null) {
                  render(VisualBuilder.build(animation.toVisual), oldVisuals)
                }
              },
              duration / 2)

          setTimeout(
              {
                // Toggle new animation off
                element.classList.toggle("${componentId}--$type--props", false)
                callback.invoke(animation.id)
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

    val interval =
        setInterval(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                render(VisualBuilder.build(animation.visuals.random()), oldVisuals)
              }
            },
            duration / animation.speed)

    setTimeout(
        {
          clearInterval(interval)
          val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
          if (oldVisuals != null) {
            render(VisualBuilder.build(animation.toVisual), oldVisuals)
          }
          callback.invoke(animation.id)
        },
        duration)
  }

  private fun startDiceAnimation(animation: DiceAnimationData, callback: (ID) -> Unit) {
    val type = "dice"
    // Get animation properties from data
    val componentId = animation.componentView?.id.toString()
    val dice = animation.componentView as? DiceViewData ?: return
    val duration = animation.duration

    val interval =
        setInterval(
            {
              val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
              if (oldVisuals != null) {
                render(VisualBuilder.build(dice.visuals.random()), oldVisuals)
              }
            },
            duration / animation.speed)

    setTimeout(
        {
          clearInterval(interval)
          val oldVisuals = document.querySelector("#${componentId} > bgw_visuals") as Element?
          if (oldVisuals != null) {
            render(VisualBuilder.build(dice.visuals[animation.toSide]), oldVisuals)
          }
          callback.invoke(animation.id)
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
    println("Getting animation CSS for $type and data ${animationData is MovementAnimationData}")
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
                    translate: ${animationData.byX}em ${animationData.byY}em;
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
