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

@file:Suppress("unused", "SpreadOperator")

package tools.aqua.bgw.builder

import javafx.animation.*
import javafx.application.Platform
import javafx.event.EventHandler
import javafx.util.Duration
import kotlin.random.Random
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.AnimationFinishedEvent

/** AnimationBuilder. Factory for BGW animations. */
object AnimationBuilder {
  /** Switches between Animations. */
  internal fun build(scene: Scene<out ComponentView>, anim: Animation): javafx.animation.Animation =
      when (anim) {
        is MovementAnimation<*> -> buildMovementAnimation(scene, anim)
        is RotationAnimation<*> -> buildRotateAnimation(scene, anim)
        is ScaleAnimation<*> -> buildScaleAnimation(scene, anim)
        is FadeAnimation<*> -> buildFadeAnimation(scene, anim)
        is FlipAnimation<*> -> buildFlipAnimation(scene, anim)
        is DelayAnimation -> buildDelayAnimation(scene, anim)
        is DiceAnimation<*> -> buildDiceAnimation(scene, anim)
        is RandomizeAnimation<*> -> buildRandomizeAnimation(scene, anim)
        is SequentialAnimation -> buildSequentialAnimation(scene, anim)
        is ParallelAnimation -> buildParallelAnimation(scene, anim)
      }

  /** Builds [ParallelAnimation]. */
  private fun buildParallelAnimation(
      scene: Scene<out ComponentView>,
      animation: ParallelAnimation
  ): javafx.animation.Animation =
      ParallelTransition(*animation.animations.map { build(scene, it) }.toTypedArray()).apply {
        onFinished = EventHandler { onFinished(scene, animation) }
      }

  /** Builds [SequentialAnimation]. */
  private fun buildSequentialAnimation(
      scene: Scene<out ComponentView>,
      animation: SequentialAnimation
  ): javafx.animation.Animation =
      SequentialTransition(*animation.animations.map { build(scene, it) }.toTypedArray()).apply {
        onFinished = EventHandler { onFinished(scene, animation) }
      }

  /** Builds [MovementAnimation]. */
  private fun buildMovementAnimation(
      scene: Scene<out ComponentView>,
      anim: MovementAnimation<*>
  ): Transition {
    val node = mapNode(scene, anim.componentView)

    // Move node to initial position
    node.layoutX = anim.fromX
    node.layoutY = anim.fromY

    // set transition as relative movement
    return TranslateTransition(Duration.millis(anim.duration.toDouble()), node).apply {
      byX = anim.toX - anim.fromX
      byY = anim.toY - anim.fromY

      // set on finished
      onFinished =
          EventHandler {
            node.layoutX = anim.toX
            node.layoutY = anim.toY
            node.translateX = 0.0
            node.translateY = 0.0
            onFinished(scene, anim)
          }
    }
  }

  /** Builds [RotationAnimation]. */
  private fun buildRotateAnimation(
      scene: Scene<out ComponentView>,
      anim: RotationAnimation<*>
  ): Transition {
    val node = mapNode(scene, anim.componentView)

    // Move node to initial position
    node.rotate = anim.fromAngle

    // set transition as relative movement
    return RotateTransition(Duration.millis(anim.duration.toDouble()), node).apply {
      byAngle = anim.toAngle - anim.fromAngle

      // set on finished
      onFinished =
          EventHandler {
            node.rotate = anim.toAngle
            node.translateX = 0.0
            node.translateY = 0.0
            onFinished(scene, anim)
          }
    }
  }

  /** Builds [FlipAnimation]. */
  private fun buildScaleAnimation(
      scene: Scene<out ComponentView>,
      anim: ScaleAnimation<*>
  ): Transition {
    val node = mapNode(scene, anim.componentView)

    // Set initial scale
    node.scaleX = anim.fromScaleX
    node.scaleY = anim.fromScaleY

    return ScaleTransition(Duration.millis(anim.duration.toDouble()), node).apply {
      toX = anim.toScaleX
      toY = anim.toScaleY

      // set on finished
      onFinished = EventHandler { onFinished(scene, anim) }
    }
  }

  /** Builds [FlipAnimation]. */
  private fun buildFadeAnimation(
      scene: Scene<out ComponentView>,
      anim: FadeAnimation<*>
  ): Transition {
    val node = mapNode(scene, anim.componentView)

    // Set initial opacity
    node.opacity = anim.fromOpacity

    return FadeTransition(Duration.millis(anim.duration.toDouble()), node).apply {
      fromValue = anim.fromOpacity
      toValue = anim.toOpacity

      onFinished = EventHandler { onFinished(scene, anim) }
    }
  }

  /** Builds [FlipAnimation]. */
  private fun buildFlipAnimation(
      scene: Scene<out ComponentView>,
      anim: FlipAnimation<out GameComponentView>
  ): Transition {
    val componentView = anim.componentView
    val node = mapNode(scene, componentView)

    val animation1 =
        ScaleTransition(Duration.millis(anim.duration / 2.0), node.children[0]).apply {
          fromX = 1.0
          toX = 0.0
        }
    val animation2 =
        ScaleTransition(Duration.millis(anim.duration / 2.0), node.children[0]).apply {
          fromX = 0.0
          toX = 1.0
        }

    when (componentView) {
      is TokenView -> componentView.visual = anim.fromVisual
      is DiceView -> componentView.visuals[componentView.currentSide] = anim.fromVisual
      is CardView -> {
        componentView.backVisual =
            if (componentView.currentSide == CardView.CardSide.BACK) anim.fromVisual
            else anim.toVisual
        componentView.frontVisual =
            if (componentView.currentSide == CardView.CardSide.FRONT) anim.fromVisual
            else anim.toVisual
      }
    }

    animation1.setOnFinished {
      when (componentView) {
        is TokenView -> componentView.visual = anim.toVisual
        is DiceView -> componentView.visuals[componentView.currentSide] = anim.toVisual
        is CardView -> componentView.flip()
      }

      animation2.play()
    }

    // set on finished
    animation2.setOnFinished { onFinished(scene, anim) }

    return animation1
  }

  /** Builds [DelayAnimation]. */
  private fun buildDelayAnimation(
      scene: Scene<out ComponentView>,
      anim: DelayAnimation
  ): Transition =
      PauseTransition(Duration.millis(anim.duration.toDouble())).apply {
        // set on finished
        onFinished = EventHandler { onFinished(scene, anim) }
      }

  /** Builds [DiceAnimation]. */
  private fun buildDiceAnimation(
      scene: Scene<out ComponentView>,
      anim: DiceAnimation<*>
  ): Transition {
    val seq = SequentialTransition()

    repeat(anim.speed) {
      seq.children +=
          PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
            onFinished =
                EventHandler {
                  anim.componentView.currentSide = Random.nextInt(anim.componentView.visuals.size)
                }
          }
    }

    seq.setOnFinished {
      anim.componentView.currentSide = anim.toSide
      onFinished(scene, anim)
    }

    return seq
  }

  /** Builds [RandomizeAnimation]. */
  private fun buildRandomizeAnimation(
      scene: Scene<out ComponentView>,
      anim: RandomizeAnimation<*>
  ): Transition {
    val seq = SequentialTransition()

    repeat(anim.speed) {
      seq.children +=
          PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
            setOnFinished {
              (anim.componentView as CardView).backVisual =
                  anim.visuals[Random.nextInt(anim.visuals.size)]
            }
          }
    }

    seq.setOnFinished {
      (anim.componentView as CardView).backVisual = anim.toVisual
      onFinished(scene, anim)
    }

    return seq
  }

  /** Removes [anim] from animations list and invokes [Animation.onFinished]. */
  private fun onFinished(scene: Scene<out ComponentView>, anim: Animation) {
    scene.animations.remove(anim.also { it.isRunning = false })

    Platform.runLater { anim.onFinished?.invoke(AnimationFinishedEvent()) }
  }

  /** Maps [ComponentView] to FX node. */
  private fun mapNode(scene: Scene<out ComponentView>, componentView: ComponentView) =
      checkNotNull(scene.componentsMap[componentView]) {
        "Creating animation for node that is not in scene."
      }
}
