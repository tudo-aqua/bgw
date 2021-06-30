package tools.aqua.bgw.builder

import javafx.scene.input.MouseEvent
import javafx.scene.layout.StackPane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.util.Coordinate

/**
 * Drag and Drop helper methods.
 */
class DragDropHelper {
	companion object {
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
		
		internal fun Scene<out ElementView>.tryFindDropTarget(
			mouseX: Double,
			mouseY: Double
		): Boolean {
			val draggedElement = draggedElement!!
			val validTargets = mutableListOf<ElementView>()
			val acceptingDropTargets = Frontend.mapScene(this)!!.children
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
					//try Drop allowed?
					if (rotatedTarget.mouseX in it.rangeX() && rotatedTarget.mouseY in it.rangeY()) {
						val tryDropData = findAcceptingDropTargets(rotatedTarget)
						validTargets += tryDropData
						return@filter tryDropData.isNotEmpty()
					}
					false
				}
			
			val isNodesNotEmpty = acceptingDropTargets.isNotEmpty()
			val dropEvent = DropEvent(draggedElement, validTargets)
			val dragEvent = DragEvent(draggedElement)
			
			//Invoke drag drop handler in dragged element
			draggedElement.onDragGestureEnded?.invoke(dropEvent, isNodesNotEmpty)
			
			//Invoke drag drop handler on all accepting drag targets
			validTargets.forEach { it.onDragElementDropped?.invoke(dragEvent) }
			
			return isNodesNotEmpty
		}
		
		private fun Scene<out ElementView>.findAcceptingDropTargets(
			dragTargetObject: DragTargetObject
		): List<ElementView> {
			
			val availableSubTargets = searchAvailableDropTargetsRecursively(
				DragTargetObject(
					dragTargetObject.dragTarget,
					dragTargetObject.dragTargetView,
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
						&& it.dropAcceptor?.invoke(DropEvent(draggedElement!!)) ?: false
			}
		}
		
		private fun Scene<out ElementView>.searchAvailableDropTargetsRecursively(
			parent: DragTargetObject,
			availableTargets: MutableList<DragTargetObject>
		): MutableList<DragTargetObject> {
			when (parent.dragTarget) {
				is GameElementContainerView<*> -> parent.dragTarget.observableElements
				is GridLayoutView<*> -> parent.dragTarget.grid.mapNotNull { it.element }
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