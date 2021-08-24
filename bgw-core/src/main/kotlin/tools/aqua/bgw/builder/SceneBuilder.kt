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
import tools.aqua.bgw.builder.DragDropHelper.Companion.findActiveComponentsBelowMouse
import tools.aqua.bgw.builder.DragDropHelper.Companion.tryFindDropTarget
import tools.aqua.bgw.builder.FXConverters.Companion.toMouseEvent
import tools.aqua.bgw.builder.Frontend.Companion.mapToPane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
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
			scene.animations.guiListener = { _, _ -> //TODO performance
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
		private fun buildPane(scene: Scene<out ComponentView>): Pane {
			val pane = Pane().apply {
				prefHeight = scene.height
				prefWidth = scene.width
			}
			
			scene.rootComponents.setGUIListenerAndInvoke (listOf()) { _, _ -> //TODO performance
				pane.rebuild(scene)
			}
			scene.draggedDataProperty.setGUIListenerAndInvoke(
				scene.draggedDataProperty.value
			) { oV, nV ->
				scene.refreshDraggedComponent(oV?.draggedStackPane, nV?.draggedStackPane)
			}
			
			pane.setOnMouseDragged { scene.onMouseDragged(it) }
			pane.setOnMouseReleased { scene.onMouseReleased(it) }
			
			return pane
		}
		
		/**
		 * Event handler for onMouseDragged.
		 */
		private fun Scene<*>.onMouseDragged(e: MouseEvent) {
			val draggedDataObject = draggedDataProperty.value ?: return
			val draggedComponent = draggedDataObject.draggedComponent
			
			//Move dragged component to mouse position
			val newCoords = DragDropHelper.transformCoordinatesToScene(e, draggedDataObject)
			draggedComponent.posX = newCoords.xCoord
			draggedComponent.posY = newCoords.yCoord
			
			//Invoke onDragMoved on dragged component
			draggedComponent.onDragGestureMoved?.invoke(DragEvent(draggedComponent))
			
			//Inspect components below mouse
			val newComponentsBelow = findActiveComponentsBelowMouse(e.sceneX, e.sceneY)
			val oldComponentsBelow = dragTargetsBelowMouse
			
			//Find delta for entered and exited components
			val entered = newComponentsBelow.filter { oldComponentsBelow.none { t -> t.dragTarget == it.dragTarget } }
			val exited = oldComponentsBelow.filter { newComponentsBelow.none { t -> t.dragTarget == it.dragTarget } }
			
			//Invoke onDragGestureEntered and onDragGestureExited
			entered.forEach { t -> t.dragTarget.onDragGestureEntered?.invoke(DragEvent(draggedComponent)) }
			exited.forEach { t -> t.dragTarget.onDragGestureExited?.invoke(DragEvent(draggedComponent)) }
			
			//Keep house holding of components currently below mouse
			dragTargetsBelowMouse.clear()
			dragTargetsBelowMouse.addAll(newComponentsBelow)
		}
		
		/**
		 * Event handler for onMouseReleased.
		 */
		private fun Scene<*>.onMouseReleased(e: MouseEvent) {
			val dragDataObject = draggedDataProperty.value
			val draggedComponent = dragDataObject?.draggedComponent ?: return
			
			draggedComponent.onMouseReleased?.invoke(e.toMouseEvent())
			
			if (!tryFindDropTarget(e.sceneX, e.sceneY)) {
				dragDataObject.rollback()
			}
			
			draggedComponent.isDragged = false
			draggedDataProperty.value = null
		}
		
		/**
		 * Rebuilds pane on components changed.
		 */
		private fun Pane.rebuild(scene: Scene<out ComponentView>) {
			children.clear()
			scene.componentsMap.clear()
			
			scene.backgroundProperty.setGUIListenerAndInvoke(scene.background) { _, nV ->
				children.add(0, VisualBuilder.buildVisual(nV).apply {
					prefWidthProperty().unbind()
					prefWidthProperty().unbind()
					prefHeight = scene.height
					prefWidth = scene.width
					scene.opacityProperty.setGUIListenerAndInvoke(scene.opacity) { _, nV -> opacity = nV }
				})
			}
			
			scene.rootComponents.forEach { children.add(NodeBuilder.build(scene, it)) }
		}
		
		/**
		 * Refreshes [Scene] after drag and drop.
		 */
		private fun Scene<*>.refreshDraggedComponent(oV: StackPane?, nV: StackPane?) {
			when {
				nV == null && oV != null -> {
					//Remove dragged component from pane
					removeDraggedComponent(oV)
					return
				}
				
				nV != null && oV == null -> {
					//Add dragged component to pane
					addDraggedComponent(nV)
					return
				}
				
				nV != null && oV != null && nV != oV -> {
					//Remove old and add new dragged component to pane
					removeDraggedComponent(oV)
					addDraggedComponent(nV)
					return
				}
			}
		}
		
		/**
		 * Removes dragged component from [Scene].
		 */
		private fun Scene<*>.removeDraggedComponent(node: StackPane) {
			mapToPane()!!.children.remove(node)
		}
		
		/**
		 * Adds dragged component to [Scene].
		 */
		private fun Scene<*>.addDraggedComponent(node: StackPane) {
			mapToPane()!!.children.add(node)
		}
	}
}
