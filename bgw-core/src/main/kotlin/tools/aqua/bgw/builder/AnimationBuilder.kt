/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.builder

import javafx.animation.*
import javafx.application.Platform
import javafx.util.Duration
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.AnimationFinishedEvent
import kotlin.random.Random

internal class AnimationBuilder {
	companion object {
		/**
		 * Switches between Animations.
		 */
		internal fun build(scene: Scene<out ComponentView>, anim: Animation): javafx.animation.Animation =
			when (anim) {
				is MovementAnimation<*> -> buildMovementAnimation(scene, anim)
				is RotationAnimation<*> -> buildRotateAnimation(scene, anim)
				is ScaleAnimation<*> -> buildScaleAnimation(scene, anim)
				is FlipAnimation<*> -> buildFlipAnimation(scene, anim)
				is DelayAnimation -> buildDelayAnimation(scene, anim)
				is DiceAnimation<*> -> buildDiceAnimation(scene, anim)
				is RandomizeAnimation<*> -> buildRandomizeAnimation(scene, anim)
			}
		
		/**
		 * Builds [MovementAnimation].
		 */
		private fun buildMovementAnimation(
			scene: Scene<out ComponentView>,
			anim: MovementAnimation<*>
		): javafx.animation.Animation {
			val node = scene.componentsMap[anim.componentView]!!
			
			//Move node to initial position
			node.layoutX = anim.fromX
			node.layoutY = anim.fromY
			
			//set transition as relative movement
			val animation = TranslateTransition(Duration.millis(anim.duration.toDouble()), node).apply {
				byX = anim.toX - anim.fromX
				byY = anim.toY - anim.fromY
			}
			
			//set on finished
			animation.setOnFinished {
				node.layoutX = anim.toX
				node.layoutY = anim.toY
				node.translateX = 0.0
				node.translateY = 0.0
				onFinished(scene, anim)
			}
			
			return animation
		}
		
		/**
		 * Builds [RotationAnimation].
		 */
		private fun buildRotateAnimation(
			scene: Scene<out ComponentView>,
			anim: RotationAnimation<*>
		): javafx.animation.Animation {
			val node = scene.componentsMap[anim.componentView]!!
			
			//Move node to initial position
			node.rotate = anim.fromAngle
			
			//set transition as relative movement
			val animation = RotateTransition(Duration.millis(anim.duration.toDouble()), node).apply {
				byAngle = anim.toAngle - anim.fromAngle
			}
			
			//set on finished
			animation.setOnFinished {
				node.rotate = anim.toAngle
				node.translateX = 0.0
				node.translateY = 0.0
				onFinished(scene, anim)
			}
			
			return animation
		}
		
		/**
		 * Builds [FlipAnimation].
		 */
		private fun buildScaleAnimation(
			scene: Scene<out ComponentView>,
			anim: ScaleAnimation<*>
		): javafx.animation.Animation {
			val node = scene.componentsMap[anim.componentView]!!
			
			//Move node to initial position
			node.scaleX = anim.fromScaleX
			node.scaleY = anim.fromScaleY
			
			//set transition as relative movement
			val animation = ScaleTransition(Duration.millis(anim.duration.toDouble()), node).apply {
				toX = anim.toScaleX
				toY = anim.toScaleY
			}
			
			return animation
		}
		
		/**
		 * Builds [FlipAnimation].
		 */
		private fun buildFlipAnimation(
			scene: Scene<out ComponentView>,
			anim: FlipAnimation<*>
		): javafx.animation.Animation {
			val node = scene.componentsMap[anim.componentView]!!
			val fromVisual = VisualBuilder.buildVisual(anim.fromVisual)
			val toVisual = VisualBuilder.buildVisual(anim.toVisual).apply { scaleX = 0.0 }
			
			val animation1 = ScaleTransition(Duration.millis(anim.duration / 2.0), fromVisual).apply {
				fromX = 1.0
				toX = 0.0
			}
			val animation2 = ScaleTransition(Duration.millis(anim.duration / 2.0), toVisual).apply {
				fromX = 0.0
				toX = 1.0
			}
			
			
			node.children[0] = fromVisual
			animation1.setOnFinished {
				node.children[0] = toVisual
				animation2.play()
			}
			
			//set on finished
			animation2.setOnFinished {
				onFinished(scene, anim)
			}
			
			return animation1
		}
		
		/**
		 * Builds [DelayAnimation].
		 */
		private fun buildDelayAnimation(
			scene: Scene<out ComponentView>,
			anim: DelayAnimation
		): javafx.animation.Animation {
			val animation = PauseTransition(Duration.millis(anim.duration.toDouble()))
			
			//set on finished
			animation.setOnFinished {
				onFinished(scene, anim)
			}
			
			return animation
		}
		
		/**
		 * Builds [DiceAnimation].
		 */
		private fun buildDiceAnimation(
			scene: Scene<out ComponentView>,
			anim: DiceAnimation<*>
		): javafx.animation.Animation {
			val seq = SequentialTransition()
			
			repeat(anim.speed) {
				seq.children += PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
					setOnFinished {
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
		
		/**
		 * Builds [RandomizeAnimation].
		 */
		private fun buildRandomizeAnimation(
			scene: Scene<out ComponentView>,
			anim: RandomizeAnimation<*>
		): javafx.animation.Animation {
			val seq = SequentialTransition()
			
			repeat(anim.speed) {
				seq.children += PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
					setOnFinished {
						anim.componentView.visual = anim.visuals[Random.nextInt(anim.visuals.size)]
					}
				}
			}
			
			seq.setOnFinished {
				anim.componentView.visual = anim.toVisual
				onFinished(scene, anim)
			}
			
			return seq
		}
		
		/**
		 * Removes [anim] from animations list and invokes [Animation.onFinished].
		 */
		private fun onFinished(scene: Scene<out ComponentView>, anim: Animation) {
			scene.animations.remove(anim)
			Platform.runLater {
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
		}
	}
}