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

import javafx.scene.Node
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.DragDropBuilder.registerDragEvents
import tools.aqua.bgw.builder.FXConverters.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.toKeyEvent
import tools.aqua.bgw.builder.FXConverters.toMouseEvent
import tools.aqua.bgw.builder.FXConverters.toScrollEvent
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.exception.IllegalInheritanceException

/** NodeBuilder. Factory for all BGW nodes. */
object NodeBuilder {
  /** Switches between top level component types. */
  internal fun build(scene: Scene<out ComponentView>, componentView: ComponentView): Region {
    val node: Region =
        when (componentView) {
          is GameComponentContainer<out GameComponentView> ->
              ContainerNodeBuilder.buildContainer(scene, componentView)
          is GameComponentView -> ComponentNodeBuilder.buildGameComponent(componentView)
          is LayoutView<out ComponentView> ->
              LayoutNodeBuilder.buildLayoutView(scene, componentView)
          is UIComponent -> UINodeBuilder.buildUIComponent(componentView)
          is StaticComponentView<*> ->
              throw IllegalInheritanceException(componentView, StaticComponentView::class.java)
          is DynamicComponentView ->
              throw IllegalInheritanceException(componentView, DynamicComponentView::class.java)
          else -> throw IllegalInheritanceException(componentView, ComponentView::class.java)
        }
    val background = VisualBuilder.build(componentView)
    var stackPane = StackPane(background, node).apply { isPickOnBounds = false }
    if (componentView is HexagonView) {
      stackPane = StackPane(node).apply { isPickOnBounds = false }
    }

    // JavaFX -> Framework
    componentView.registerEvents(stackPane, node, scene)

    // Framework -> JavaFX
    componentView.registerObservers(stackPane, node, background)

    // Register in componentsMap
    scene.componentsMap[componentView] = stackPane

    return stackPane
  }

  /** Registers events. */
  private fun ComponentView.registerEvents(
      stackPane: StackPane,
      node: Region,
      scene: Scene<out ComponentView>
  ) {
    if (this is DynamicComponentView) {
      registerDragEvents(stackPane, scene)
    }

    stackPane.setOnMouseDragEntered {
      val dragTarget = scene.draggedComponent ?: return@setOnMouseDragEntered
      scene.dragTargetsBelowMouse.add(this)
      onDragGestureEntered?.invoke(DragEvent(dragTarget))
    }

    stackPane.setOnMouseDragExited {
      val dragTarget = scene.draggedComponent ?: return@setOnMouseDragExited
      scene.dragTargetsBelowMouse.remove(this)
      onDragGestureExited?.invoke(DragEvent(dragTarget))
    }

    node.setOnMouseClicked { onMouseClicked?.invoke(it.toMouseEvent()) }
    node.setOnMousePressed { onMousePressed?.invoke(it.toMouseEvent()) }
    node.setOnMouseReleased { onMouseReleased?.invoke(it.toMouseEvent()) }
    node.setOnMouseEntered { onMouseEntered?.invoke(it.toMouseEvent()) }
    node.setOnMouseExited { onMouseExited?.invoke(it.toMouseEvent()) }
    node.setOnScroll { onScroll?.invoke(it.toScrollEvent()) }

    node.setOnKeyPressed {
      if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
          onKeyPressed?.invoke(it.toKeyEvent())
    }
    node.setOnKeyReleased {
      if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
          onKeyReleased?.invoke(it.toKeyEvent())
    }
    node.setOnKeyTyped {
      if (scene !is BoardGameScene || !scene.internalLockedProperty.value)
          onKeyTyped?.invoke(it.toKeyEvent())
    }
  }

  /** Registers observers. */
  @Suppress("DuplicatedCode")
  private fun ComponentView.registerObservers(
      stackPane: StackPane,
      node: Region,
      background: Region
  ) {
    posXProperty.setGUIListenerAndInvoke(posX) { _, nV ->
      stackPane.layoutX = nV - if (isLayoutFromCenter) actualWidth / 2 else 0.0
    }
    posYProperty.setGUIListenerAndInvoke(posY) { _, nV ->
      stackPane.layoutY = nV - if (isLayoutFromCenter) actualHeight / 2 else 0.0
    }
    scaleXProperty.setGUIListenerAndInvoke(scaleX) { _, nV ->
      stackPane.scaleX = nV
      posXProperty.notifyUnchanged()
    }
    scaleYProperty.setGUIListenerAndInvoke(scaleY) { _, nV ->
      stackPane.scaleY = nV
      posYProperty.notifyUnchanged()
    }

    rotationProperty.setGUIListenerAndInvoke(rotation) { _, nV -> stackPane.rotate = nV }
    opacityProperty.setGUIListenerAndInvoke(opacity) { _, nV -> stackPane.opacity = nV }

    heightProperty.setGUIListenerAndInvoke(height) { _, nV ->
      node.prefHeight = nV
      background.prefHeight = nV
      posYProperty.notifyUnchanged()
    }
    widthProperty.setGUIListenerAndInvoke(width) { _, nV ->
      node.prefWidth = nV
      background.prefWidth = nV
      posXProperty.notifyUnchanged()
    }

    isVisibleProperty.setGUIListenerAndInvoke(isVisible) { _, nV ->
      node.isVisible = nV
      background.isVisible = nV
    }
    isDisabledProperty.setGUIListenerAndInvoke(isDisabled) { _, nV ->
      node.isDisable = nV
      background.isDisable = nV
    }

    if (this is UIComponent) {
      backgroundStyleProperty.setGUIListenerAndInvoke(backgroundStyle) { _, nV ->
        if (nV.isNotEmpty()) background.style = nV
      }

      fontProperty.guiListener = { _, _ -> updateStyle(node) }
      internalCSSProperty.guiListener = { _, _ -> updateStyle(node) }
      componentStyleProperty.setGUIListenerAndInvoke(componentStyle) { _, _ -> updateStyle(node) }
    }
  }

  /** Updates nodes style property. */
  private fun UIComponent.updateStyle(node: Node) {
    node.style = this.internalCSS + this.font.toFXFontCSS() + componentStyle
  }

  /**
   * This function is used in various places to increase the performance of rebuilding a [Pane].
   *
   * @param scene the scene that is responsible for the building of this [Pane].
   * @param components the [ComponentView]s that should make up this [Pane]s children.
   * @param cached the [ComponentView]s that currently make up this [Pane]s children.
   */
  internal fun javafx.scene.layout.Pane.buildChildren(
      scene: Scene<out ComponentView>,
      components: Iterable<ComponentView>,
      cached: Set<ComponentView>
  ) {
    children.clear()
    (cached - components.toSet()).forEach { scene.componentsMap.remove(it) }
    components.forEach {
      if (it in cached) {
        children.add(scene.componentsMap[it])
      } else {
        children.add(build(scene, it))
      }
    }
  }
}
