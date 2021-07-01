@file:Suppress("unused")

package tools.aqua.bgw.builder

import javafx.animation.*
import javafx.util.Duration
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.AnimationFinishedEvent
import kotlin.random.Random

internal class AnimationBuilder {
	companion object {
		internal fun addTranslateAnimation(scene: BoardGameScene, anim: MovementAnimation<*>) {
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
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			animation.play()
		}
		
		internal fun addRotateAnimation(scene: BoardGameScene, anim: RotationAnimation<*>) {
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
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			animation.play()
		}
		
		internal fun addFlipAnimation(scene: BoardGameScene, anim: FlipAnimation<*>) {
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
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			animation1.play()
		}
		
		internal fun addDelayAnimation(scene: BoardGameScene, anim: DelayAnimation) {
			val animation = PauseTransition(Duration.millis(anim.duration.toDouble()))
			
			//set on finished
			animation.setOnFinished {
				scene.animations.remove(anim)
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			animation.play()
		}
		
		internal fun addRandomizeAnimation(scene: BoardGameScene, anim: RandomizeAnimation<*>) {
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
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			seq.play()
		}
		
		internal fun addDiceAnimation(scene: BoardGameScene, anim: DiceAnimation<*>) {
			val seq = SequentialTransition()
			
			repeat(anim.speed) {
				seq.children += PauseTransition(Duration.millis(anim.duration / anim.speed.toDouble())).apply {
					setOnFinished {
						anim.element.currentSide = Random.nextInt(anim.element.visuals.size())
					}
				}
			}
			
			seq.setOnFinished {
				anim.element.currentSide = anim.toSide
				scene.animations.remove(anim)
				anim.onFinished?.handle(AnimationFinishedEvent())
			}
			
			seq.play()
		}
	}
}