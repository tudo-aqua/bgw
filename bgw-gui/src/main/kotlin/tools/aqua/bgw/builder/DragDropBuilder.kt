/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.util.Coordinate
import kotlin.math.min

/** DragDropBuilder. Applies drag drop functions to [DynamicComponentView]s. */
object DragDropBuilder {
  /** Registers drag drop events to [DynamicComponentView]. */
  internal fun DynamicComponentView.registerDragEvents(
    stackPane: StackPane,
    scene: Scene<out ComponentView>
  ) {
    val isMouseInitiallyTransparent = stackPane.isMouseTransparent

    stackPane.onDragDetected =
      EventHandler {
        if (isDraggable && scene.draggedComponentProperty.value == null) {
          onDragDetected(scene as BoardGameScene, it)
          stackPane.isMouseTransparent = true
          stackPane.startFullDrag()
        }
      }

    stackPane.onDragDropped =
      EventHandler {
        scene.draggedComponentProperty.value = null
        stackPane.isMouseTransparent = isMouseInitiallyTransparent
      }
  }

  /** Adds onDragDetected to [DynamicComponentView]. */
  private fun DynamicComponentView.onDragDetected(scene: BoardGameScene, e: MouseEvent) {
    val mouseStartCoord =
      Coordinate(xCoord = e.sceneX / Frontend.sceneScale, yCoord = e.sceneY / Frontend.sceneScale)

    val pathToChild = scene.findPathToChild(this)

    var posStartCoord =
      Coordinate(
        xCoord = pathToChild.sumOf { t -> t.posX }, yCoord = pathToChild.sumOf { t -> t.posY })

    val rollback: (() -> Unit) =
      when (val parent = parent) {
        is GameComponentContainer<*> -> {
          parent.findRollback(this as GameComponentView)
        }
        is GridPane<*> -> {
          // calculate position in grid
          posStartCoord += parent.getActualChildPosition(this) ?: Coordinate()

          parent.findRollback(this)
        }
        is Pane<*> -> {
          parent.findRollback(this)
        }
        scene.rootNode -> {
          findRollbackOnRoot(scene)
        }
        else -> {
          {}
        }
      }

    val dragDataObject =
      DragDataObject(
        this,
        checkNotNull(scene.componentsMap[this]),
        mouseStartCoord,
        posStartCoord,
        rollback)

    val newCoords = SceneBuilder.transformCoordinatesToScene(e, dragDataObject)

    removeFromParent()

    posX = newCoords.xCoord
    posY = newCoords.yCoord
    scene.draggedComponentProperty.value = dragDataObject
    scene.dragTargetsBelowMouse.clear()

    isDragged = true
    onDragGestureStarted?.invoke(DragEvent(this))
  }

  //region Find rollback
  /** Calculates rollback for [GameComponentContainer]s. */
  private fun GameComponentContainer<*>.findRollback(component: GameComponentView): (() -> Unit) {
    val index = observableComponents.indexOf(component)
    val initialX = component.posX
    val initialY = component.posY

    return {
      component.posX = initialX
      component.posY = initialY
      @Suppress("UNCHECKED_CAST")
      (this as GameComponentContainer<GameComponentView>).add(
        component, min(observableComponents.size, index))
    }
  }

  /** Calculates rollback for [GridPane]s. */
  private fun GridPane<*>.findRollback(component: ComponentView): (() -> Unit) {
    val element =
      grid.find { iteratorElement -> iteratorElement.component == component } ?: return {}

    if (element.component == null)
      throw ConcurrentModificationException(
        "Grid was modified while calculating drag drop rollback.")

    val initialX = element.component.posX
    val initialY = element.component.posY
    val initialColumnIndex = element.columnIndex
    val initialRowIndex = element.rowIndex

    return {
      component.posX = initialX
      component.posY = initialY
      @Suppress("UNCHECKED_CAST")
      (this as GridPane<ComponentView>)[initialColumnIndex, initialRowIndex] = component
    }
  }

  /** Calculates rollback for [Pane]s. */
  private fun Pane<*>.findRollback(component: ComponentView): (() -> Unit) {
    val index = observableComponents.indexOf(component)
    val initialX = component.posX
    val initialY = component.posY

    return {
      component.posX = initialX
      component.posY = initialY
      @Suppress("UNCHECKED_CAST")
      (this as Pane<ComponentView>).add(
        this as ComponentView, min(observableComponents.size, index))
    }
  }

  private fun DynamicComponentView.findRollbackOnRoot(scene: BoardGameScene): (() -> Unit) {
    val initialX = posX
    val initialY = posY

    return {
      posX = initialX
      posY = initialY
      scene.addComponents(this)
    }
  }
  //endregion
}
