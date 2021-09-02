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

import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.FXConverters.Companion.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.Companion.toKeyEvent
import tools.aqua.bgw.builder.FXConverters.Companion.toMouseEvent
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.StaticComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.exception.IllegalInheritanceException
import tools.aqua.bgw.util.Coordinate
import kotlin.math.min

/**
 * NodeBuilder.
 * Factory for all BGW nodes.
 */
internal class NodeBuilder {
	companion object {
		/**
		 * Switches between top level component types.
		 */
		internal fun build(scene: Scene<out ComponentView>, componentView: ComponentView): Region {
			val node = when (componentView) {
				is GameComponentContainer<out GameComponentView> ->
					ContainerNodeBuilder.buildContainer(scene, componentView)
				is GameComponentView ->
					ComponentNodeBuilder.buildGameComponent(componentView)
				is LayoutView<out ComponentView> ->
					LayoutNodeBuilder.buildLayoutView(scene, componentView)
				is UIComponent ->
					UINodeBuilder.buildUIComponent(componentView)
				is StaticComponentView<*> ->
					throw IllegalInheritanceException(componentView, StaticComponentView::class.java)
				is DynamicComponentView ->
					throw IllegalInheritanceException(componentView, DynamicComponentView::class.java)
				else ->
					throw IllegalInheritanceException(componentView, ComponentView::class.java)
			}
			val background = VisualBuilder.build(componentView)
			val stackPane = StackPane(background, node).apply { isPickOnBounds = false }

			//JavaFX -> Framework
			componentView.registerEvents(stackPane, node, scene)
			
			//Framework -> JavaFX
			componentView.registerObservers(stackPane, node, background)
			
			//Register in componentsMap
			scene.componentsMap[componentView] = stackPane
			
			return stackPane
		}
		
		/**
		 * Registers events.
		 */
		private fun ComponentView.registerEvents(stackPane: StackPane, node: Region, scene: Scene<out ComponentView>) {
			if(this is DynamicComponentView) {
				registerDragEvents(stackPane, scene)
			}
			
			stackPane.onMouseDragEntered = EventHandler{
				val dragTarget = scene.draggedComponent?:return@EventHandler
				scene.dragTargetsBelowMouse.add(this)
				onDragGestureEntered?.invoke(DragEvent(dragTarget))
			}
			
			stackPane.onMouseDragExited = EventHandler{
				val dragTarget = scene.draggedComponent?:return@EventHandler
				scene.dragTargetsBelowMouse.remove(this)
				onDragGestureExited?.invoke(DragEvent(dragTarget))
			}
			
			node.setOnMouseClicked { onMouseClicked?.invoke(it.toMouseEvent()) }
			node.setOnMousePressed { onMousePressed?.invoke(it.toMouseEvent()) }
			node.setOnMouseEntered { onMouseEntered?.invoke(it.toMouseEvent()) }
			node.setOnMouseExited { onMouseExited?.invoke(it.toMouseEvent()) }
			
			node.setOnKeyPressed { onKeyPressed?.invoke(it.toKeyEvent()) }
			node.setOnKeyReleased { onKeyReleased?.invoke(it.toKeyEvent()) }
			node.setOnKeyTyped { onKeyTyped?.invoke(it.toKeyEvent()) }
		}
		
		private fun DynamicComponentView.registerDragEvents(stackPane: StackPane, scene: Scene<out ComponentView>) {
			val initialMouseTransparency = stackPane.isMouseTransparent
			
			stackPane.onDragDetected = EventHandler {
				if (isDraggable && scene.draggedComponentProperty.value == null) {
					onDragDetected(scene as BoardGameScene, it)
					stackPane.isMouseTransparent = true
					stackPane.startFullDrag()
				}
			}
			
			stackPane.onDragDropped = EventHandler{
				scene.draggedComponentProperty.value = null
				stackPane.isMouseTransparent = initialMouseTransparency
			}
		}
		
		
		private fun DynamicComponentView.onDragDetected(scene: BoardGameScene, e: MouseEvent) {
			val mouseStartCoord = Coordinate(
				xCoord = e.sceneX / Frontend.sceneScale,
				yCoord = e.sceneY / Frontend.sceneScale
			)

			val pathToChild = scene.findPathToChild(this)
			
			var posStartCoord = Coordinate(
				xCoord = pathToChild.sumOf { t -> t.posX },
				yCoord = pathToChild.sumOf { t -> t.posY }
			)
			
			val rollback: (() -> Unit) = when (val parent = pathToChild[1]) {
				is GameComponentContainer<*> -> {
					parent.findRollback(this as GameComponentView)
				}
				is GridPane<*> -> {
					//calculate position in grid
					posStartCoord += parent.getChildPosition(this)!!
					
					//add layout from center bias
					if (parent.layoutFromCenter) {
						posStartCoord -= Coordinate(parent.width / 2, parent.height / 2)
					}
					
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
			
			val relativeParentRotation = if (pathToChild.size > 1)
				pathToChild.drop(1).sumOf { t -> t.rotation }
			else
				0.0
			
			val dragDataObject = DragDataObject(
				this,
				scene.componentsMap[this]!!,
				mouseStartCoord,
				posStartCoord,
				relativeParentRotation,
				rollback
			)
			
			val newCoords = SceneBuilder.transformCoordinatesToScene(e, dragDataObject)
			
			removeFromParent()
			
			posX = newCoords.xCoord
			posY = newCoords.yCoord
			scene.draggedComponentProperty.value = dragDataObject
			scene.dragTargetsBelowMouse.clear()
			
			isDragged = true
			onDragGestureStarted?.invoke(DragEvent(this))
		}
		
		/**
		 * Calculates rollback for [GameComponentContainer]s.
		 */
		private fun GameComponentContainer<*>.findRollback(component: GameComponentView): (() -> Unit) {
			val index = observableComponents.indexOf(component)
			val initialX = posX
			val initialY = posY
			
			return {
				posX = initialX
				posY = initialY
				@Suppress("UNCHECKED_CAST")
				(this as GameComponentContainer<GameComponentView>).add(
					component,
					min(observableComponents.size, index)
				)
			}
		}
		
		/**
		 * Calculates rollback for [GridPane]s.
		 */
		private fun GridPane<*>.findRollback(component: ComponentView): (() -> Unit) {
			val element = grid.find { iteratorElement ->
				iteratorElement.component == component
			} ?: return {}
			
			val initialX = element.component!!.posX
			val initialY = element.component.posY
			val initialColumnIndex = element.columnIndex
			val initialRowIndex = element.rowIndex
			
			return {
				posX = initialX
				posY = initialY
				@Suppress("UNCHECKED_CAST")
				(parent as GridPane<ComponentView>)[initialColumnIndex, initialRowIndex] =
					this@findRollback as ComponentView
			}
		}
		
		/**
		 * Calculates rollback for [Pane]s.
		 */
		private fun Pane<*>.findRollback(component: ComponentView): (() -> Unit) {
			val index = observableComponents.indexOf(component)
			val initialX = posX
			val initialY = posY
			
			return {
				posX = initialX
				posY = initialY
				@Suppress("UNCHECKED_CAST")
				(parent as Pane<ComponentView>)
					.add(this as ComponentView, min(observableComponents.size, index))
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
		
		/**
		 * Registers observers.
		 */
		@Suppress("DuplicatedCode")
		private fun ComponentView.registerObservers(stackPane: StackPane, node: Region, background: Region) {
			posXProperty.setGUIListenerAndInvoke(posX) { _, nV ->
				stackPane.layoutX = nV - if (layoutFromCenter) actualWidth / 2 else 0.0
			}
			posYProperty.setGUIListenerAndInvoke(posY) { _, nV ->
				stackPane.layoutY = nV - if (layoutFromCenter) actualHeight / 2 else 0.0
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
					if (nV.isNotEmpty())
						background.style = nV
				}
				
				fontProperty.guiListener = { _,_ -> updateStyle(node) }
				internalCSSProperty.guiListener = { _,_ -> updateStyle(node) }
				componentStyleProperty.setGUIListenerAndInvoke(componentStyle) { _,_ -> updateStyle(node) }
			}
		}
		
		/**
		 * Updates nodes style property.
		 */
		private fun UIComponent.updateStyle(node: Region) {
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
			(cached - components).forEach { scene.componentsMap.remove(it) }
			components.forEach {
				if (it in cached) {
					children.add(scene.componentsMap[it])
				} else {
					children.add(build(scene, it))
				}
			}
		}
	}
}