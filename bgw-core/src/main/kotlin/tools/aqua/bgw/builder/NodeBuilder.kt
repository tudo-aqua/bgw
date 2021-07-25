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
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.DragDropHelper.Companion.findElementsBelowMouse
import tools.aqua.bgw.builder.DragDropHelper.Companion.transformCoordinatesToScene
import tools.aqua.bgw.builder.FXConverters.Companion.toKeyEvent
import tools.aqua.bgw.builder.FXConverters.Companion.toMouseEvent
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.elements.layoutviews.ElementPane
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.layoutviews.LayoutElement
import tools.aqua.bgw.elements.uielements.UIElementView
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
		 * Switches between top level element types.
		 */
		internal fun build(scene: Scene<out ElementView>, elementView: ElementView): Region {
			val node = when (elementView) {
				is GameElementContainerView<out GameElementView> ->
					ContainerNodeBuilder.buildContainer(scene, elementView)
				is GameElementView ->
					ElementNodeBuilder.buildGameElement(elementView)
				is LayoutElement<out ElementView> ->
					LayoutNodeBuilder.buildLayoutElement(scene, elementView)
				is UIElementView ->
					UINodeBuilder.buildUIElement(elementView)
				is StaticView<*> ->
					throw IllegalInheritanceException(elementView, StaticView::class.java)
				is DynamicView ->
					throw IllegalInheritanceException(elementView, DynamicView::class.java)
				else ->
					throw IllegalInheritanceException(elementView, ElementView::class.java)
			}
			val background = VisualBuilder.build(elementView)
			val stackPane = StackPane(background, node).apply { isPickOnBounds = false }
			
			//JavaFX -> Framework
			elementView.registerEvents(stackPane, node, scene)
			
			//Framework -> JavaFX
			elementView.registerObservers(stackPane, node, background)
			
			//Register in elementsMap
			scene.elementsMap[elementView] = stackPane
			
			return stackPane
		}
		
		/**
		 * Registers events.
		 */
		private fun ElementView.registerEvents(stackPane: StackPane, node: Region, scene: Scene<out ElementView>) {
			stackPane.onDragDetected = EventHandler {
				if (this is DynamicView && isDraggable) {
					val mouseStartCoord = Coordinate(
						xCoord = it.sceneX / Frontend.sceneScale,
						yCoord = it.sceneY / Frontend.sceneScale
					)
					
					val pathToChild = scene.findPathToChild(this)
					
					var posStartCoord = Coordinate(
						xCoord = pathToChild.sumOf { t -> t.posX },
						yCoord = pathToChild.sumOf { t -> t.posY }
					)
					
					var rollback: (() -> Unit) = {}
					when (val parent = pathToChild[1]) {
						is GameElementContainerView<*> -> {
							val index = parent.observableElements.indexOf(this)
							val initialX = posX
							val initialY = posY
							rollback = {
								posX = initialX
								posY = initialY
								@Suppress("UNCHECKED_CAST")
								(parent as GameElementContainerView<GameElementView>)
									.add(this as GameElementView, min(parent.observableElements.size, index))
							}
						}
						is GridLayoutView<*> -> {
							parent.grid.find { iteratorElement ->
								iteratorElement.element == this
							}?.apply {
								val initialX = posX
								val initialY = posY
								
								//calculate position in grid
								posStartCoord += parent.getChildPosition(element!!)!!
								
								//add layout from center bias
								if (parent.layoutFromCenter) {
									posStartCoord -= Coordinate(parent.width / 2, parent.height / 2)
								}
								
								rollback = {
									posX = initialX
									posY = initialY
									@Suppress("UNCHECKED_CAST")
									(parent as GridLayoutView<ElementView>)[columnIndex, rowIndex] =
										this@registerEvents as ElementView
								}
							}
							
						}
						is ElementPane<*> -> {
							val index = parent.observableElements.indexOf(this)
							val initialX = posX
							val initialY = posY
							rollback = {
								posX = initialX
								posY = initialY
								@Suppress("UNCHECKED_CAST")
								(parent as ElementPane<ElementView>)
									.add(this as ElementView, min(parent.observableElements.size, index))
							}
						}
						scene.rootNode -> {
							val initialX = posX
							val initialY = posY
							rollback = { when (scene) {
								is BoardGameScene -> {
									this.posX = initialX
									this.posY = initialY
									scene.addElements(this)
								}
								is MenuScene -> {
									throw RuntimeException("DynamicView $this should not be contained in a MenuScene.")
								}
							} }


						}
						else -> {}
					}
					
					val relativeParentRotation = if (pathToChild.size > 1)
						pathToChild.drop(1).sumOf { t -> t.rotation }
					else
						0.0
					
					val dragElementObject = DragElementObject(
						this,
						scene.elementsMap[this]!!,
						mouseStartCoord,
						posStartCoord,
						relativeParentRotation,
						rollback
					)
					val newCoords = transformCoordinatesToScene(it, dragElementObject)
					
					removeFromParent()
					posX = newCoords.xCoord
					posY = newCoords.yCoord
					scene.draggedElementObjectProperty.value = dragElementObject
					
					scene.dragTargetsBelowMouse.clear()
					scene.dragTargetsBelowMouse.addAll(scene.findElementsBelowMouse(it.sceneX, it.sceneY))
					
					println("--- STARTING LIST ---")
					scene.dragTargetsBelowMouse.forEach { targetObject -> println(targetObject) }
					println("---------------------")
					
					isDragged = true
					onDragGestureStarted?.invoke(DragEvent(this))
				}
			}
			
			node.setOnMouseClicked { onMouseClicked?.invoke(it.toMouseEvent()) }
			node.setOnMousePressed { onMousePressed?.invoke(it.toMouseEvent()) }
			node.setOnMouseEntered { onMouseEntered?.invoke(it.toMouseEvent()) }
			node.setOnMouseExited { onMouseExited?.invoke(it.toMouseEvent()) }
			
			node.setOnKeyPressed { onKeyPressed?.invoke(it.toKeyEvent()) }
			node.setOnKeyReleased { onKeyReleased?.invoke(it.toKeyEvent()) }
			node.setOnKeyTyped { onKeyTyped?.invoke(it.toKeyEvent()) }
		}

		/**
		 * Registers observers.
		 */
		@Suppress("DuplicatedCode")
		private fun ElementView.registerObservers(stackPane: StackPane, node: Region, background: Region) {
			posXProperty.setGUIListenerAndInvoke(posX) { _, nV ->
				stackPane.layoutX = nV - if (layoutFromCenter) width / 2 else 0.0
			}
			posYProperty.setGUIListenerAndInvoke(posY) { _, nV ->
				stackPane.layoutY = nV - if (layoutFromCenter) height / 2 else 0.0
			}
			scaleXProperty.setGUIListenerAndInvoke(scaleX) { _, nV ->
				stackPane.scaleX = nV
			}
			scaleYProperty.setGUIListenerAndInvoke(scaleY) { _, nV ->
				stackPane.scaleY = nV
			}
			
			rotationProperty.setGUIListenerAndInvoke(rotation) { _, nV -> stackPane.rotate = nV }
			opacityProperty.setGUIListenerAndInvoke(opacity) { _, nV -> stackPane.opacity = nV }
			
			heightProperty.setGUIListenerAndInvoke(height) { _, nV ->
				node.prefHeight = nV
				background.prefHeight = nV
			}
			widthProperty.setGUIListenerAndInvoke(width) { _, nV ->
				node.prefWidth = nV
				background.prefWidth = nV
			}
			
			isVisibleProperty.setGUIListenerAndInvoke(isVisible) { _, nV ->
				node.isVisible = nV
				background.isVisible = nV
			}
			isDisabledProperty.setGUIListenerAndInvoke(isDisabled) { _, nV ->
				node.isDisable = nV
				background.isDisable = nV
			}
			
			if (this is UIElementView) {
				backgroundStyleProperty.setGUIListenerAndInvoke(backgroundStyle) { _, nV ->
					if (nV.isNotEmpty())
						background.style = nV
				}
				componentStyleProperty.setGUIListenerAndInvoke(componentStyle) { _, nV ->
					if (nV.isNotEmpty())
						node.style = nV
				}
			}
		}
	}
}