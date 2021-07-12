package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.DragDropHelper.Companion.transformCoordinatesToScene
import tools.aqua.bgw.builder.DragDropHelper.Companion.tryFindDropTarget
import tools.aqua.bgw.builder.FXConverters.Companion.toMouseEvent
import tools.aqua.bgw.builder.Frontend.Companion.mapToPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.event.DragEvent

/**
 * SceneBuilder.
 * Factory for BGW scenes.
 */
internal class SceneBuilder {
	companion object {
		/**
		 * Builds [MenuScene].
		 */
		internal fun buildMenu(scene: MenuScene): Pane = buildPane(scene)
		
		/**
		 * Builds [BoardGameScene].
		 */
		internal fun buildGame(scene: BoardGameScene): Pane {
			val pane = buildPane(scene)
			
			//register animations
			scene.animations.guiListener = {
				scene.animations.list.stream().filter { t -> !t.running }.forEach { anim ->
					AnimationBuilder.build(scene, anim).play()
					anim.running = true
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
		
		/**
		 * Builds a [Scene] pane.
		 */
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
		
		/**
		 * Rebuilds pane on elements changed.
		 */
		private fun Pane.rebuild(scene: Scene<out ElementView>) {
			children.clear()
			scene.elementsMap.clear()
			
			scene.backgroundProperty.setGUIListenerAndInvoke(scene.background) { _, nV ->
				children.add(0, VisualBuilder.buildVisual(nV).apply {
					prefWidthProperty().unbind()
					prefWidthProperty().unbind()
					prefHeight = scene.height
					prefWidth = scene.width
					scene.opacityProperty.setGUIListenerAndInvoke(scene.opacity) { _, nV -> opacity = nV }
				})
			}
			
			for (element in scene.rootElements) {
				val node = NodeBuilder.build(scene, element)
				
				children.add(node)
				//scene.elementsMap[element] = node
			}
		}
		
		/**
		 * Refreshes [Scene] after drag and drop.
		 */
		private fun Scene<*>.refreshDraggedElement(oV: StackPane?, nV: StackPane?) {
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
		
		/**
		 * Removes dragged element from [Scene].
		 */
		private fun Scene<*>.removeDraggedElement(node: StackPane) {
			mapToPane()!!.children.remove(node)
		}
		
		/**
		 * Adds dragged element to [Scene].
		 */
		private fun Scene<*>.addDraggedElement(node: StackPane) {
			mapToPane()!!.children.add(node)
		}
	}
}
