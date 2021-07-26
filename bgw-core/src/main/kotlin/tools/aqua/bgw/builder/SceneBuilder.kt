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

package tools.aqua.bgw.builder

import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.DragDropHelper.Companion.findActiveElementsBelowMouse
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
			
			pane.setOnMouseDragged { scene.onMouseDragged(it) }
			pane.setOnMouseReleased { scene.onMouseReleased(it) }
			
			return pane
		}
		
		/**
		 * Event handler for onMouseDragged.
		 */
		private fun Scene<*>.onMouseDragged(e: MouseEvent) {
			val draggedElementObject = draggedElementObjectProperty.value ?: return
			val draggedElement = draggedElementObject.draggedElement
			
			//Move dragged element to mouse position
			val newCoords = DragDropHelper.transformCoordinatesToScene(e, draggedElementObject)
			draggedElement.posX = newCoords.xCoord
			draggedElement.posY = newCoords.yCoord
			
			//Invoke onDragMoved on dragged element
			draggedElement.onDragGestureMoved?.invoke(DragEvent(draggedElement))
			
			//Inspect elements below mouse
			val newElementsBelow = findActiveElementsBelowMouse(e.sceneX, e.sceneY)
			val oldElementsBelow = dragTargetsBelowMouse
			
			//Find delta for entered and exited elements
			val entered = newElementsBelow.filter { oldElementsBelow.none { t -> t.dragTarget == it.dragTarget } }
			val exited = oldElementsBelow.filter { newElementsBelow.none { t -> t.dragTarget == it.dragTarget } }
			
			//Invoke onDragGestureEntered and onDragGestureExited
			entered.forEach { t -> t.dragTarget.onDragGestureEntered?.invoke(DragEvent(draggedElement)) }
			exited.forEach { t -> t.dragTarget.onDragGestureExited?.invoke(DragEvent(draggedElement)) }
			
			//Keep house holding of elements currently below mouse
			dragTargetsBelowMouse.clear()
			dragTargetsBelowMouse.addAll(newElementsBelow)
		}
		
		/**
		 * Event handler for onMouseReleased.
		 */
		private fun Scene<*>.onMouseReleased(e: MouseEvent) {
			val draggedElementObject = draggedElementObjectProperty.value
			val draggedElement = draggedElementObject?.draggedElement ?: return
			
			draggedElement.onMouseReleased?.invoke(e.toMouseEvent())
			
			if (!tryFindDropTarget(e.sceneX, e.sceneY)) {
				draggedElementObject.rollback()
			}
			
			draggedElement.isDragged = false
			draggedElementObjectProperty.value = null
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
