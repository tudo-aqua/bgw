/*
 * Copyright 2021-2024 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.builder

import javafx.scene.layout.StackPane
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.util.Coordinate

/**
 * [DragDataObject] data class.
 *
 * @property draggedComponent Currently dragged [DynamicComponentView].
 * @property draggedStackPane StackPane of currently dragged [DynamicComponentView].
 * @property mouseStartCoord Mouse coordinate relative to [draggedComponent] at drag start.
 * @property posStartCoord Coordinate of [draggedComponent] at drag start.
 * @property relativeRotation Rotation of [draggedComponent] relative to scene.
 * @property initialPosX The initial posX of the [draggedComponent].
 * @property initialPosY The initial posY of the [draggedComponent].
 * @property initialRotation The initial rotation of the [draggedComponent].
 * @property rollback Rollback function to roll back changes made during drag.
 */
internal data class DragDataObject(
    val draggedComponent: DynamicComponentView,
    val draggedStackPane: StackPane,
    val mouseStartCoord: Coordinate,
    val posStartCoord: Coordinate,
    val relativeRotation: Double,
    val initialPosX: Double,
    val initialPosY: Double,
    val initialRotation: Double,
    val rollback: (() -> Unit) = {}
)
