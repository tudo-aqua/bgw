package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView

/**
 * SceneBuilder.
 * Factory for BGW scenes.
 */
internal class SceneBuilder {
	companion object {
		internal fun buildMenu(scene: MenuScene): Pane = buildPane(scene)
		
		internal fun buildGame(scene: BoardGameScene): Pane {
			val pane = buildPane(scene)
			
			//register animations
			scene.animations.guiListener = {
				scene.animations.list.stream().filter { t -> !t.running }.forEach { anim ->
					run {
						when (anim) {
							is MovementAnimation<*> -> AnimationBuilder.addTranslateAnimation(scene, anim)
							is RotationAnimation<*> -> AnimationBuilder.addRotateAnimation(scene, anim)
							is FlipAnimation<*> -> AnimationBuilder.addFlipAnimation(scene, anim)
							is DelayAnimation -> AnimationBuilder.addDelayAnimation(scene, anim)
							is DiceAnimation<*> -> AnimationBuilder.addDiceAnimation(scene, anim)
						}
						
						anim.running = true
						
						//TODO: remove anim from list when finished
						//TODO: Add animation stop and endless mode
					}
				}
			}
			
			//register lock pane
			val lockPane = Pane().apply {
				prefHeightProperty().bind(pane.heightProperty())
				prefWidthProperty().bind(pane.widthProperty())
			}
			scene.lockedProperty.guiListener = {
				pane.children.remove(lockPane)
				if (it)
					pane.children.add(lockPane)
			}
			
			return pane
		}
		
		private fun buildPane(scene: Scene<out ElementView>): Pane {
			val pane = Pane().apply {
				prefHeight = scene.height
				prefWidth = scene.width
				rebuild(scene)
			}
			
			scene.rootElements.guiListener = { pane.rebuild(scene) }
			
			return pane
		}
		
		private fun Pane.rebuild(scene: Scene<out ElementView>) {
			children.clear()
			scene.elementsMap.clear()
			
			children.add(VisualBuilder.buildVisual(scene.background).apply { //TODO: Make observable
				prefWidthProperty().unbind() //TODO: Check
				prefWidthProperty().unbind()
				prefHeight = scene.height
				prefWidth = scene.width
				scene.opacityProperty.setGUIListenerAndInvoke(scene.opacity) { opacity = it }
			})
			
			for (element in scene.rootElements) {
				val node = NodeBuilder.build(scene, element)
				
				children.add(node)
				//scene.elementsMap[element] = node
			}
		}
	}
}