package tools.aqua.bgw.builder

import javafx.scene.layout.StackPane
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.util.Coordinate

/**
 * DragElementObject data class.
 *
 * @param draggedElement currently dragged [DynamicView].
 * @param draggedStackPane stackPane of currently dragged [DynamicView].
 * @param mouseStartCoord mouse coordinate relative to [draggedElement] at drag start.
 * @param posStartCoord coordinate of [draggedElement] at drag start.
 * @param relativeParentRotation rotation of [draggedElement] relative to parent.
 * @param rollback rollback function to roll back changes made during drag.
 */
internal data class DragElementObject(
	val draggedElement: DynamicView,
	val draggedStackPane: StackPane,
	var mouseStartCoord: Coordinate,
	var posStartCoord: Coordinate,
	var relativeParentRotation: Double,
	var rollback: (() -> Unit) = {}
)