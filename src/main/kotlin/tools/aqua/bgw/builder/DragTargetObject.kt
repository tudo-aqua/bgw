package tools.aqua.bgw.builder

import javafx.scene.layout.StackPane
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.util.Coordinate

internal class DragTargetObject(
	val dragTarget: ElementView,
	val dragTargetView: StackPane,
	val mouseX: Double,
	val mouseY: Double,
	val offsetX: Double = 0.0,
	val offsetY: Double = 0.0,
	//val rotationOffset: Double = 0.0
) {
	internal fun rangeX() =
		if (dragTarget is GridLayoutView<*>)
			dragTarget.posX - dragTarget.width / 2..dragTarget.posX + dragTarget.width / 2
		else
			dragTarget.posX.rangeTo(dragTarget.posX + dragTarget.width)
	
	internal fun rangeY() =
		if (dragTarget is GridLayoutView<*>)
			dragTarget.posY - dragTarget.height / 2..dragTarget.posY + dragTarget.height / 2
		else
			dragTarget.posY.rangeTo(dragTarget.posY + dragTarget.height)
	
	internal fun rotated(): DragTargetObject {
		//get parent center
		val parentCenter = Coordinate(
			offsetX + dragTarget.posX + dragTarget.width / 2,
			offsetY + dragTarget.posY + dragTarget.height / 2
		)
		
		val rotated = Coordinate(mouseX, mouseY).rotated(-dragTarget.rotation, parentCenter)
		
		return DragTargetObject(
			dragTarget = dragTarget,
			dragTargetView = dragTargetView,
			mouseX = rotated.xCoord,
			mouseY = rotated.yCoord,
			offsetX = offsetX,
			offsetY = offsetY
		)
	}
}