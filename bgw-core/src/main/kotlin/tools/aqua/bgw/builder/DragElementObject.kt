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