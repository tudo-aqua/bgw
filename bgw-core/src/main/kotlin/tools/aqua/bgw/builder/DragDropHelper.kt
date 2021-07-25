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
import javafx.scene.layout.StackPane
import tools.aqua.bgw.builder.Frontend.Companion.mapToPane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.elements.layoutviews.ElementPane
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.util.Coordinate

/**
 * Drag and Drop helper methods.
 */
class DragDropHelper {
	companion object {
		/**
		 * Rotates coordinates to 0 degrees relative to scene.
		 *
		 * @param mouseEvent mouse event.
		 * @param draggedElementObject rotated element.
		 */
		internal fun transformCoordinatesToScene(
			mouseEvent: MouseEvent,
			draggedElementObject: DragElementObject
		): Coordinate {
			val rotated = Coordinate(
				xCoord = mouseEvent.sceneX / Frontend.sceneScale - draggedElementObject.mouseStartCoord.xCoord,
				yCoord = mouseEvent.sceneY / Frontend.sceneScale - draggedElementObject.mouseStartCoord.yCoord
			).rotated(-draggedElementObject.relativeParentRotation)
			
			return Coordinate(
				xCoord = draggedElementObject.posStartCoord.xCoord + rotated.xCoord,
				yCoord = draggedElementObject.posStartCoord.yCoord + rotated.yCoord
			)
		}
		
		/**
		 * Searches all elements below mouse position.
		 *
		 * Note: Invisible or disabled elements also get returned. Use findActiveElementsBelowMouse(...).
		 *
		 * @param mouseX mouse x coordinate.
		 * @param mouseY mouse y coordinate.
		 *
		 * @return [List] of elements.
		 */
		internal fun Scene<out ElementView>.findElementsBelowMouse(
			mouseX: Double,
			mouseY: Double
		): List<DragTargetObject> =
			mapToPane()!!.children
				.filterIsInstance<StackPane>()  //top level Elements
				.mapNotNull {
					//to DragTargetObject
					val element = elementsMap.entries.firstOrNull { t -> t.value == it }?.key
					val newMouseX = (mouseX - Frontend.sceneX) / Frontend.sceneScale
					val newMouseY = (mouseY - Frontend.sceneY) / Frontend.sceneScale
					
					if (element != null) DragTargetObject(element, it, newMouseX, newMouseY) else null
				}
				.filter {
					val rotatedTarget = it.rotated()
					rotatedTarget.mouseX in it.rangeX() && rotatedTarget.mouseY in it.rangeY()
				}.toList()
		
		/**
		 * Searches all visible and enabled elements below mouse position.
		 *
		 * Note: Invisible and disabled elements get returned by using findElementsBelowMouse(...).
		 *
		 * @param mouseX mouse x coordinate.
		 * @param mouseY mouse y coordinate.
		 *
		 * @return [List] of elements.
		 */
		internal fun Scene<out ElementView>.findActiveElementsBelowMouse(
			mouseX: Double,
			mouseY: Double
		): List<DragTargetObject> = findElementsBelowMouse(mouseX, mouseY).filter {
			it.dragTarget.isVisible && !it.dragTarget.isDisabled
		}
		
		/**
		 * Searches all elements below mouse position.
		 *
		 * @param mouseX mouse x coordinate.
		 * @param mouseY mouse y coordinate.
		 *
		 * @return `true` if a valid drop target was found and the drop was successful, `false` otherwise.
		 */
		internal fun Scene<out ElementView>.tryFindDropTarget(mouseX: Double, mouseY: Double): Boolean {
			val draggedElement = draggedElement!!
			
			val validTargets = mutableListOf<ElementView>()
			findElementsBelowMouse(mouseX, mouseY).forEach {
				val tryDropData = findAcceptingDropTargets(it)
				validTargets += tryDropData
				tryDropData.isNotEmpty()
			}
			
			val dropEvent = DropEvent(draggedElement, validTargets)
			val dragEvent = DragEvent(draggedElement)
			
			//Invoke drag drop handler in dragged element
			draggedElement.onDragGestureEnded?.invoke(dropEvent, validTargets.isNotEmpty())
			
			//Invoke drag drop handler on all accepting drag targets
			validTargets.forEach { it.onDragElementDropped?.invoke(dragEvent) }
			
			return validTargets.isNotEmpty()
		}
		
		/**
		 * Finds all drop targets that accept drop by invoking dropAcceptor.
		 *
		 * @param dragTargetObject drag target object.
		 *
		 * @return [List] of all accepting drop targets.
		 */
		private fun Scene<out ElementView>.findAcceptingDropTargets(
			dragTargetObject: DragTargetObject
		): List<ElementView> {
			val availableSubTargets = searchAvailableDropTargetsRecursively(
				DragTargetObject(
					dragTargetObject.dragTarget,
					dragTargetObject.dragTargetStackPane,
					dragTargetObject.mouseX,
					dragTargetObject.mouseY,
					dragTargetObject.dragTarget.posX -
							if (dragTargetObject.dragTarget.layoutFromCenter)
								dragTargetObject.dragTarget.width / 2
							else
								0.0,
					dragTargetObject.dragTarget.posY -
							if (dragTargetObject.dragTarget.layoutFromCenter)
								dragTargetObject.dragTarget.height / 2
							else
								0.0
				),
				mutableListOf()
			)
			
			return availableSubTargets.map { it.dragTarget }.filter {
				this != it
						&& it.dropAcceptor?.invoke(DragEvent(draggedElement!!)) ?: false
			}
		}
		
		/**
		 * Searches for drop targets recursively.
		 *
		 * @param parent current parent node.
		 * @param availableTargets [List] of all collected drop targets.
		 */
		private fun Scene<out ElementView>.searchAvailableDropTargetsRecursively(
			parent: DragTargetObject,
			availableTargets: MutableList<DragTargetObject>
		): MutableList<DragTargetObject> {
			when (parent.dragTarget) {
				is GameElementContainerView<*> -> parent.dragTarget.observableElements
				is GridLayoutView<*> -> parent.dragTarget.grid.mapNotNull { it.element }
				is ElementPane<*> -> parent.dragTarget.observableElements
				else -> listOf()
			}.map {
				Pair(it, parent.dragTarget.getChildPosition(it) ?: Coordinate(0.0, 0.0))
			}.filter {
				parent.mouseX - parent.offsetX in it.second.xCoord..it.second.xCoord + it.first.width
						&& parent.mouseY - parent.offsetY in it.second.yCoord..it.second.yCoord + it.first.height
				
			}.forEach {
				searchAvailableDropTargetsRecursively(
					DragTargetObject(
						it.first,
						elementsMap[it.first]!!,
						parent.mouseX,
						parent.mouseY,
						parent.offsetX + it.second.xCoord,
						parent.offsetY + it.second.yCoord
					),
					availableTargets
				)
			}
			
			return availableTargets.apply { add(parent) }
		}
	}
}