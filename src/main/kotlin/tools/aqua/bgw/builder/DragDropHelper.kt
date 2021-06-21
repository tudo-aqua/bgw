package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.DynamicView
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
		internal fun DynamicView.tryFindDropTarget(
			draggedElement: StackPane,
			scene: Scene<out ElementView>,
			mouseX: Double,
			mouseY: Double
		): Boolean {
			val validTargets = mutableListOf<ElementView>()
			val nodes = (Frontend.scenePane.children.last() as Pane).children
				.filterIsInstance<StackPane>()  //top level Elements
				.mapNotNull {
					//to DragTargetObject
					val element = scene.elementsMap.entries.firstOrNull { t -> t.value == it }?.key
					val newMouseX = (mouseX - Frontend.sceneX) / Frontend.sceneScale
					val newMouseY = (mouseY - Frontend.sceneY) / Frontend.sceneScale
					
					if (element != null) DragTargetObject(element, it, newMouseX, newMouseY) else null
				}
				.filter {
					val rotatedTarget = it.rotated()
					//try Drop allowed?
					if (rotatedTarget.mouseX in it.rangeX() && rotatedTarget.mouseY in it.rangeY()) {
						val tryDropData = tryDrop(scene, draggedElement, rotatedTarget)
						validTargets += tryDropData.second
						return@filter tryDropData.first
					}
					false
				}
			
			val isNodesNotEmpty = nodes.isNotEmpty()
			val dragEvent = DragEvent(this, validTargets)
			val dropEvent = DropEvent(this)
			
			//Invoke drag drop handler in dragged element
			preDragGestureEnded?.invoke(dragEvent, isNodesNotEmpty)
			onDragGestureEnded?.invoke(dragEvent, isNodesNotEmpty)
			postDragGestureEnded?.invoke(dragEvent, isNodesNotEmpty)
			
			//Invoke drag drop handler on all accepting drag targets
			validTargets.forEach { it.onDragElementDropped?.invoke(dropEvent) }
			
			println(isNodesNotEmpty)
			return isNodesNotEmpty
		}
		
		private fun ElementView.tryDrop(
			scene: Scene<out ElementView>,
			draggedElement: StackPane,
			dragTargetObject: DragTargetObject
		): Pair<Boolean, List<ElementView>> {
			
			val availableSubTargets = searchAvailableDropTargetsRecursively(
				scene,
				DragTargetObject(
					dragTargetObject.dragTarget,
					dragTargetObject.dragTargetView,
					dragTargetObject.mouseX,
					dragTargetObject.mouseY,
					dragTargetObject.dragTarget.posX -
							if (dragTargetObject.dragTarget.layoutFromCenter) dragTargetObject.dragTarget.width / 2 else 0.0,
					dragTargetObject.dragTarget.posY -
							if (dragTargetObject.dragTarget.layoutFromCenter) dragTargetObject.dragTarget.height / 2 else 0.0
				),
				mutableListOf()
			)
			
			val snapBackX = posX
			val snapBackY = posY
			val validTargets = availableSubTargets.map { it.dragTarget }.filter {
				this != it
						&& it.dropAcceptor?.invoke(DropEvent(this)) ?: false
			}
			
			return if (validTargets.isNotEmpty()) {
				posX = draggedElement.layoutX
				posY = draggedElement.layoutY
				Pair(true, validTargets)
			} else {
				posX = snapBackX
				posY = snapBackY
				Pair(false, listOf())
			}
		}
		
		private fun ElementView.searchAvailableDropTargetsRecursively(
			scene: Scene<out ElementView>,
			parent: DragTargetObject,
			availableTargets: MutableList<DragTargetObject>
		): MutableList<DragTargetObject> {
			when (parent.dragTarget) {
				is GameElementContainerView<*> -> parent.dragTarget.observableElements
				is GridLayoutView<*> -> parent.dragTarget.grid.mapNotNull { it.third }
				else -> listOf()
			}.map {
				Pair(it, parent.dragTarget.getChildPosition(it) ?: Coordinate(0.0, 0.0))
			}.filter {
				parent.mouseX - parent.offsetX in it.second.xCoord..it.second.xCoord + it.first.width
						&& parent.mouseY - parent.offsetY in it.second.yCoord..it.second.yCoord + it.first.height
				
			}.forEach {
				searchAvailableDropTargetsRecursively(
					scene,
					DragTargetObject(
						it.first,
						scene.elementsMap[it.first]!!,
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