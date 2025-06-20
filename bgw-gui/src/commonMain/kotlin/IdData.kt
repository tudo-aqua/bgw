/*
 * Copyright 2025 The BoardGameWork Authors
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

@file:Suppress("unused")

import kotlinx.serialization.Serializable

/**
 * Minimal data structure to represent a component's ID and its hierarchy. Only includes fields that
 * are actually used by each component type.
 */
@Serializable
internal data class ComponentIdData(
    val id: ID,
    val components: List<ComponentIdData>? = null,
    val grid: List<GridPosition>? = null,
    val componentsMap: Map<String, ComponentIdData>? = null,
    val target: ComponentIdData? = null
)

/** Represents a component position in a grid */
@Serializable
internal data class GridPosition(val column: Int, val row: Int, val component: ComponentIdData)
