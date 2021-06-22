package tools.aqua.bgw.builder

import javafx.scene.layout.StackPane
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.util.Coordinate

internal data class DragElementObject(
	val draggedElement: DynamicView,
	val draggedStackPane: StackPane,
	var mouseStartCoord: Coordinate,
	var posStartCoord: Coordinate,
	var relativeParentRotation: Double,
	var rollback: (() -> Unit) = {}
)