package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.builder.DragDropHelper.Companion.transformCoordinatesToScene
import tools.aqua.bgw.builder.DragDropHelper.Companion.tryFindDropTarget
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.EventConverter.Companion.toMouseEvent

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
			scene.lockedProperty.guiListener = { _, nV ->
				pane.children.remove(lockPane)
				if (nV)
					pane.children.add(lockPane)
			}
			
			return pane
		}
		
		private fun buildPane(scene: Scene<out ElementView>): Pane {
			val pane = Pane().apply {
				prefHeight = scene.height
				prefWidth = scene.width
			}
			
			scene.rootElements.setGUIListenerAndInvoke { pane.rebuild(scene) }
			scene.draggedElementObjectProperty.setGUIListenerAndInvoke(
				scene.draggedElementObjectProperty.value
			) { oV, nV ->
				scene.refreshDraggedElement(oV?.draggedStackPane, nV?.draggedStackPane)
			}
			
			pane.setOnMouseDragged {
				val draggedElementObject = scene.draggedElementObjectProperty.value
				if (draggedElementObject != null) {
					val draggedElement = draggedElementObject.draggedElement
					val newCoords = transformCoordinatesToScene(it, draggedElementObject)
					
					draggedElement.posX = newCoords.xCoord
					draggedElement.posY = newCoords.yCoord
					
					draggedElement.onDragGestureMoved?.invoke(DragEvent(draggedElement))
				}
			}
			pane.setOnMouseReleased {
				val draggedElementObject = scene.draggedElementObjectProperty.value
				val draggedElement = draggedElementObject?.draggedElement
				
				if (draggedElement != null) {
					draggedElement.onMouseReleased?.invoke(it.toMouseEvent())
					
					if (!scene.tryFindDropTarget(it.sceneX, it.sceneY)) {
						draggedElementObject.rollback()
					}
					draggedElement.isDragged = false
					scene.draggedElementObjectProperty.value = null
				}
			}
			
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
				scene.opacityProperty.setGUIListenerAndInvoke(scene.opacity) { _, nV -> opacity = nV }
			})
			
			for (element in scene.rootElements) {
				val node = NodeBuilder.build(scene, element)
				
				children.add(node)
				//scene.elementsMap[element] = node
			}
		}
		
		private fun Scene<*>.refreshDraggedElement(oV: StackPane?, nV: StackPane?) {
			println("refresh")
			when {
				nV == null && oV != null -> {
					//Remove dragged element from pane
					removeDraggedElement(oV)
					return
				}
				
				nV != null && oV == null -> {
					//Add dragged element to pane
					addDraggedElement(nV)
					return
				}
				
				nV != null && oV != null && nV != oV -> {
					//Remove old and add new dragged element to pane
					removeDraggedElement(oV)
					addDraggedElement(nV)
					return
				}
			}
		}
		
		private fun Scene<*>.removeDraggedElement(node: StackPane) {
			Frontend.mapScene(this)!!.children.remove(node)
		}
		
		private fun Scene<*>.addDraggedElement(node: StackPane) {
			Frontend.mapScene(this)!!.children.add(node)
		}
	}
}
