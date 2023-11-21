/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import javafx.scene.input.MouseEvent
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.FXConverters.toKeyEvent
import tools.aqua.bgw.builder.FXConverters.toMouseEvent
import tools.aqua.bgw.builder.Frontend.Companion.mapToPane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.util.Coordinate

/** SceneBuilder. Factory for BGW scenes. */
object SceneBuilder {
  /** Builds [MenuScene]. */
  internal fun buildMenu(scene: MenuScene): Pane = buildPane(scene)

  /** Builds [BoardGameScene]. */
  internal fun buildGame(scene: BoardGameScene): Pane {
    val pane = buildPane(scene)

    scene.draggedComponentProperty.setGUIListenerAndInvoke(scene.draggedComponentProperty.value) {
        oV,
        nV ->
      scene.refreshDraggedComponent(oV?.draggedStackPane, nV?.draggedStackPane)
    }

    pane.setOnMouseDragged { scene.onMouseDragged(it) }
    pane.setOnMouseReleased { scene.onMouseReleased(it) }

    // register lock pane
    @Suppress("DuplicatedCode", "DuplicatedCode")
    val lockPane =
        Pane().apply {
          prefHeightProperty().bind(pane.heightProperty())
          prefWidthProperty().bind(pane.widthProperty())
        }
    scene.lockedProperty.guiListener = { _, nV ->
      pane.children.remove(lockPane)
      if (nV) pane.children.add(lockPane)
    }

    // register lock pane for menu
    @Suppress("DuplicatedCode")
    val internalLockPane =
        Pane().apply {
          prefHeightProperty().bind(pane.heightProperty())
          prefWidthProperty().bind(pane.widthProperty())
        }
    scene.internalLockedProperty.guiListener = { _, nV ->
      pane.children.remove(internalLockPane)
      if (nV) pane.children.add(internalLockPane)
    }

    return pane
  }

  /** Builds a [Scene] pane. */
  private fun buildPane(scene: Scene<*>): Pane {
    // register animations
    scene.animations.guiListener = { _, _ ->
      scene.animations
          .filter { t -> !t.isRunning }
          .forEach { anim ->
            AnimationBuilder.build(scene, anim).play()
            anim.isRunning = true
          }
    }
    val pane =
        Pane().apply {
          prefHeight = scene.height
          prefWidth = scene.width

          setOnKeyTyped {
            if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
                scene.onKeyTyped?.invoke(it.toKeyEvent())
            it.consume()
          }
          setOnKeyPressed {
            if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
                scene.onKeyPressed?.invoke(it.toKeyEvent())
            it.consume()
          }
          setOnKeyReleased {
            if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
                scene.onKeyReleased?.invoke(it.toKeyEvent())
            it.consume()
          }
        }

    scene.rootComponents.setGUIListenerAndInvoke(emptyList()) { oV, _ -> pane.rebuild(scene, oV) }

    return pane
  }

  /**
   * Rotates coordinates to 0 degrees relative to scene.
   *
   * @param mouseEvent mouse event.
   * @param draggedDataObject rotated component.
   */
  internal fun transformCoordinatesToScene(
      mouseEvent: MouseEvent,
      draggedDataObject: DragDataObject
  ): Coordinate =
      draggedDataObject.posStartCoord +
          Coordinate(
              xCoord =
                  mouseEvent.sceneX / Frontend.sceneScale -
                      draggedDataObject.mouseStartCoord.xCoord,
              yCoord =
                  mouseEvent.sceneY / Frontend.sceneScale -
                      draggedDataObject.mouseStartCoord.yCoord)

  /** Event handler for onMouseDragged. */
  internal fun BoardGameScene.onMouseDragged(e: MouseEvent) {
    val draggedDataObject = draggedComponentProperty.value ?: return
    val draggedComponent = draggedDataObject.draggedComponent

    // Move dragged component to mouse position
    val newCoords = transformCoordinatesToScene(e, draggedDataObject)
    draggedComponent.posX = newCoords.xCoord
    draggedComponent.posY = newCoords.yCoord

    // Invoke onDragMoved on dragged component
    draggedComponent.onDragGestureMoved?.invoke(DragEvent(draggedComponent))
  }

  /** Event handler for onMouseReleased. */
  internal fun BoardGameScene.onMouseReleased(e: MouseEvent) {
    val dragDataObject = draggedComponentProperty.value
    val draggedComponent = dragDataObject?.draggedComponent ?: return

    draggedComponent.isDragged = false
    draggedComponentProperty.value = null

    // Invoke onMouseReleased
    draggedComponent.onMouseReleased?.invoke(e.toMouseEvent())

    // Create Drag Event
    val dragEvent = DragEvent(draggedComponent)

    // Find valid targets by invoking drop acceptor
    val validTargets =
        dragTargetsBelowMouse.reversed().filter {
          it.dropAcceptor?.invoke(DragEvent(draggedComponent)) ?: false
        }

    if (validTargets.isEmpty()) {
      draggedComponent.posX = dragDataObject.initialPosX
      draggedComponent.posY = dragDataObject.initialPosY
      draggedComponent.rotation = dragDataObject.initialRotation
      dragDataObject.rollback()
    }

    // Create drop event containing all accepting drop targets
    val dropEvent = DropEvent(draggedComponent, validTargets)

    // Invoke drag drop handler in dragged component
    draggedComponent.onDragGestureEnded?.invoke(dropEvent, validTargets.isNotEmpty())

    // Invoke drag drop handler on all accepting drag targets
    validTargets.forEach { it.onDragDropped?.invoke(dragEvent) }

    // If dragged element was not added to a container, add it to the scene
    if (draggedComponent.parent == dragGestureRootNode) {
      draggedComponent.parent = null
      addComponents(draggedComponent)
    }
  }

  /** Rebuilds pane on components changed. */
  private fun Pane.rebuild(scene: Scene<out ComponentView>, cachedComponents: List<ComponentView>) {
    children.clear()

    scene.backgroundCache = null
    scene.backgroundProperty.setGUIListenerAndInvoke(scene.background) { oldValue, newValue ->
      if (oldValue != newValue || scene.backgroundCache == null) {
        val newBackground =
            VisualBuilder.buildVisual(newValue).apply {
              prefHeightProperty().unbind()
              prefWidthProperty().unbind()
              prefHeight = scene.height
              prefWidth = scene.width
              scene.opacityProperty.setGUIListenerAndInvoke(scene.opacity) { _, nV -> opacity = nV }
            }

        if (scene.backgroundCache != null) {
          children.remove(scene.backgroundCache)
        }

        children.add(0, newBackground)
        scene.backgroundCache = newBackground
      }
    }

    (cachedComponents - scene.rootComponents).forEach { scene.componentsMap.remove(it) }

    children.addAll(
        scene.rootComponents.map {
          if (cachedComponents.contains(it)) {
            scene.componentsMap[it]
          } else {
            NodeBuilder.build(scene, it)
          }
        })
  }

  /** Refreshes [Scene] after drag and drop. */
  private fun Scene<*>.refreshDraggedComponent(oV: StackPane?, nV: StackPane?) {
    when {
      nV == null && oV != null -> {
        // Remove dragged component from pane
        removeDraggedComponent(oV)
        return
      }
      nV != null && oV == null -> {
        // Add dragged component to pane
        addDraggedComponent(nV)
        return
      }
      nV != null && oV != null && nV != oV -> {
        // Remove old and add new dragged component to pane
        removeDraggedComponent(oV)
        addDraggedComponent(nV)
        return
      }
    }
  }

  /** Removes dragged component from [Scene]. */
  private fun Scene<*>.removeDraggedComponent(node: StackPane) {
    mapToPane().children.remove(node)
  }

  /** Adds dragged component to [Scene]. */
  private fun Scene<*>.addDraggedComponent(node: StackPane) {
    mapToPane().children.add(node)
  }
}
