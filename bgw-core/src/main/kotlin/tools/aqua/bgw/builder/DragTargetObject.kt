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

import javafx.scene.layout.StackPane
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.util.Coordinate

/**
 * [DragTargetObject] class.
 *
 * @param dragTarget currently dragged [ComponentView].
 * @param dragTargetStackPane stackPane of currently dragged [DynamicComponentView].
 * @param mouseX mouse position x.
 * @param mouseY mouse position y.
 * @param offsetX mouse offset to [dragTarget] x.
 * @param offsetY mouse offset to [dragTarget] y.
 */
internal class DragTargetObject(
	val dragTarget: ComponentView,
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
		if (dragTarget is GridPane<*>)
			dragTarget.posX - dragTarget.width / 2..dragTarget.posX + dragTarget.width / 2
		else
			dragTarget.posX.rangeTo(dragTarget.posX + dragTarget.width)
	
	/**
	 * Returns a [ClosedFloatingPointRange] between yPos and yPos + height.
	 */
	internal fun rangeY() =
		if (dragTarget is GridPane<*>)
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