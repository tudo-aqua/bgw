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
import javafx.util.Duration
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.event.AnimationFinishedEvent
import kotlin.random.Random

internal class AnimationBuilder {
	companion object {
		/**
		 * Switches between Animations.
		 */
		internal fun build(scene: Scene<out ElementView>, anim: Animation): javafx.animation.Animation =
			when (anim) {
				is MovementAnimation<*> -> buildMovementAnimation(scene, anim)
				is RotationAnimation<*> -> addRotateAnimation(scene, anim)
				is FlipAnimation<*> -> addFlipAnimation(scene, anim)
				is DelayAnimation -> addDelayAnimation(scene, anim)
				is DiceAnimation<*> -> addDiceAnimation(scene, anim)
				is RandomizeAnimation<*> -> addRandomizeAnimation(scene, anim)
			}
		
		/**
		 * Builds [MovementAnimation].
		 */
		private fun buildMovementAnimation(
			scene: Scene<out ElementView>,
			anim: MovementAnimation<*>
		): javafx.animation.Animation {
			val node = scene.elementsMap[anim.element]!!
			
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
				scene.animations.remove(anim)
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return animation
		}
		
		/**
		 * Builds [RotationAnimation].
		 */
		private fun addRotateAnimation(
			scene: Scene<out ElementView>,
			anim: RotationAnimation<*>
		): javafx.animation.Animation {
			val node = scene.elementsMap[anim.element]!!
			
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
				scene.animations.remove(anim)
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return animation
		}
		
		/**
		 * Builds [FlipAnimation].
		 */
		private fun addFlipAnimation(
			scene: Scene<out ElementView>,
			anim: FlipAnimation<*>
		): javafx.animation.Animation {
			val node = scene.elementsMap[anim.element]!!
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
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return animation1
		}
		
		/**
		 * Builds [DelayAnimation].
		 */
		private fun addDelayAnimation(scene: Scene<out ElementView>, anim: DelayAnimation): javafx.animation.Animation {
			val animation = PauseTransition(Duration.millis(anim.duration.toDouble()))
			
			//set on finished
			animation.setOnFinished {
				scene.animations.remove(anim)
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return animation
		}
		
		/**
		 * Builds [DiceAnimation].
		 */
		private fun addDiceAnimation(
			scene: Scene<out ElementView>,
			anim: DiceAnimation<*>
		): javafx.animation.Animation {
			val seq = SequentialTransition()
			
			repeat(anim.speed) {
				seq.children += PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
					setOnFinished {
						anim.element.currentSide = Random.nextInt(anim.element.visuals.size)
					}
				}
			}
			
			seq.setOnFinished {
				anim.element.currentSide = anim.toSide
				scene.animations.remove(anim)
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return seq
		}
		
		/**
		 * Builds [RandomizeAnimation].
		 */
		private fun addRandomizeAnimation(
			scene: Scene<out ElementView>,
			anim: RandomizeAnimation<*>
		): javafx.animation.Animation {
			val seq = SequentialTransition()
			
			repeat(anim.speed) {
				seq.children += PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
					setOnFinished {
						anim.element.visual = anim.visuals[Random.nextInt(anim.visuals.size)]
					}
				}
			}
			
			seq.setOnFinished {
				anim.element.visual = anim.toVisual
				scene.animations.remove(anim)
				anim.onFinished?.invoke(AnimationFinishedEvent())
			}
			
			return seq
		}
	}
}