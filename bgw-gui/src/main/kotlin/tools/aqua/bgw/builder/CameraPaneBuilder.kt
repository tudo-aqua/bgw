/*
 * Copyright 2023 The BoardGameWork Authors
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

import javafx.geometry.Point2D
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.builder.FXConverters.toFXMouseButton
import tools.aqua.bgw.builder.SceneBuilder.onMouseReleased
import tools.aqua.bgw.builder.components.ZoomableScrollPane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.DragEvent

/**
 * The [CameraPaneBuilder] object provides a method for building a camera pane in a specified scene
 * and container.
 */
object CameraPaneBuilder {
  /**
   * Builds a camera pane within the specified scene and container.
   * @param scene The scene in which the camera pane will be built.
   * @param container The container that will hold the camera pane.
   * @return The built camera pane as a Region.
   */
  internal fun buildCameraPane(
      scene: Scene<out ComponentView>,
      container: CameraPane<out LayoutView<*>>
  ): Region {
    val node =
        ZoomableScrollPane(
            (NodeBuilder.build(scene, container.target) as Pane).apply {
              minWidth = container.target.width
              minHeight = container.target.height
            },
            container)

    if (scene is BoardGameScene) {
      node.content.setOnMouseDragged {
        if (it.button == node.panMouseButton && node.interactive) node.isPannable = true
        with(scene) {
          val draggedDataObject = draggedComponentProperty.value ?: return@setOnMouseDragged
          val draggedComponent = draggedDataObject.draggedComponent
          // Move dragged component to mouse position
          val newCoords = SceneBuilder.transformCoordinatesToScene(it, draggedDataObject)
          draggedComponent.posX = newCoords.xCoord - node.anchorPoint.x
          draggedComponent.posY = newCoords.yCoord - node.anchorPoint.y
          // Invoke onDragMoved on dragged component
          draggedComponent.onDragGestureMoved?.invoke(DragEvent(draggedComponent))
        }
      }
      node.content.setOnMouseReleased {
        if (it.button == node.panMouseButton && node.interactive) node.isPannable = false
        scene.onMouseReleased(it)
      }
    }

    container.interactiveProperty.setGUIListenerAndInvoke(container.interactive) { _, nV ->
      node.interactive = nV
    }

    container.isHorizontalLockedProperty.setGUIListenerAndInvoke(container.isHorizontalLocked) {
        _,
        nV ->
      node.hPanLocked = nV
      node.hValueLocked = node.hvalue
    }

    container.isVerticalLockedProperty.setGUIListenerAndInvoke(container.isVerticalLocked) { _, nV
      ->
      node.vPanLocked = nV
      node.vValueLocked = node.vvalue
    }

    container.panMouseButtonProperty.setGUIListenerAndInvoke(container.panMouseButton) { _, nV ->
      node.panMouseButton = nV.toFXMouseButton()
    }

    container.zoomProperty.setGUIListenerAndInvoke(container.zoom) { _, nV -> node.scaleValue = nV }
    container.smoothScrollingProperty.setGUIListenerAndInvoke(container.smoothScrolling) { _, nV ->
      node.smooth = nV
    }

    container.anchorPointProperty.setGUIListenerAndInvoke(container.anchorPoint) { _, nV ->
      if (node.smooth) node.smoothScrollTo(Point2D(nV.xCoord, nV.yCoord))
      else node.scrollTo(Point2D(nV.xCoord, nV.yCoord))
    }

    return node.apply {
      minWidth = container.width
      minHeight = container.height
    }
  }
}
