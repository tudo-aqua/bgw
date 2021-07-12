package tools.aqua.bgw.builder

import javafx.scene.layout.StackPane
import tools.aqua.bgw.elements.DynamicView
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.util.Coordinate

/**
 * DragElementObject class.
 *
 * @param dragTarget currently dragged [ElementView].
 * @param dragTargetStackPane stackPane of currently dragged [DynamicView].
 * @param mouseX mouse position x.
 * @param mouseY mouse position y.
 * @param offsetX mouse offset to [dragTarget] x.
 * @param offsetY mouse offset to [dragTarget] y.
 */
internal class DragTargetObject(
	val dragTarget: ElementView,
	val dragTargetStackPane: StackPane,
	val mouseX: Double,
	val mouseY: Double,
	val offsetX: Double = 0.0,
	val offsetY: Double = 0.0,
) {
	/**
	 * Returns a [ClosedFloatingPointRange] between xPos and xPos + width.
	 */
	internal fun rangeX(): ClosedFloatingPointRange<Double> =
		if (dragTarget is GridLayoutView<*>)
			dragTarget.posX - dragTarget.width / 2..dragTarget.posX + dragTarget.width / 2
		else
			dragTarget.posX.rangeTo(dragTarget.posX + dragTarget.width)
	
	/**
	 * Returns a [ClosedFloatingPointRange] between yPos and yPos + height.
	 */
	internal fun rangeY() =
		if (dragTarget is GridLayoutView<*>)
			dragTarget.posY - dragTarget.height / 2..dragTarget.posY + dragTarget.height / 2
		else
			dragTarget.posY.rangeTo(dragTarget.posY + dragTarget.height)
	
	/**
	 * Returns object rotated to parent.
	 */
	internal fun rotated(): DragTargetObject {
		val parentCenter = Coordinate(
			offsetX + dragTarget.posX + dragTarget.width / 2,
			offsetY + dragTarget.posY + dragTarget.height / 2
		)
		
		val rotated = Coordinate(mouseX, mouseY).rotated(-dragTarget.rotation, parentCenter)
		
		return DragTargetObject(
			dragTarget = dragTarget,
			dragTargetStackPane = dragTargetStackPane,
			mouseX = rotated.xCoord,
			mouseY = rotated.yCoord,
			offsetX = offsetX,
			offsetY = offsetY
		)
	}
}