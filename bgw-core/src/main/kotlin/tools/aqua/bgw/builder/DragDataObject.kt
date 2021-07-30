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
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.util.Coordinate

/**
 * [DragDataObject] data class.
 *
 * @param draggedComponent currently dragged [DynamicComponentView].
 * @param draggedStackPane stackPane of currently dragged [DynamicComponentView].
 * @param mouseStartCoord mouse coordinate relative to [draggedComponent] at drag start.
 * @param posStartCoord coordinate of [draggedComponent] at drag start.
 * @param relativeParentRotation rotation of [draggedComponent] relative to parent.
 * @param rollback rollback function to roll back changes made during drag.
 */
internal data class DragDataObject(
	val draggedComponent: DynamicComponentView,
	val draggedStackPane: StackPane,
	var mouseStartCoord: Coordinate,
	var posStartCoord: Coordinate,
	var relativeParentRotation: Double,
	var rollback: (() -> Unit) = {}
)