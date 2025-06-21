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

package tools.aqua.bgw.mapper

import ComponentIdData
import GridPosition
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json as KJson
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.*

internal val idJson = KJson { encodeDefaults = false }
/**
 * Component mapper that creates a simplified version of ComponentView hierarchy containing only IDs
 * and minimal structure information.
 */
internal object ComponentIdMapper {
  /** Configure JSON serialization to exclude null values */

  /** Maps a ComponentView to a simplified ComponentIdData containing only ID and structure info */
  fun map(componentView: ComponentView): ComponentIdData {
    return when (componentView) {
      // Container components with a list of children
      is Pane<*>,
      is Area<*>,
      is CardStack<*>,
      is LinearLayout<*>,
      is Satchel<*> -> {
        val children =
            when (componentView) {
              is Pane<*> -> componentView.components.map { map(it) }
              is Area<*> -> componentView.components.map { map(it) }
              is CardStack<*> -> componentView.components.map { map(it) }
              is LinearLayout<*> -> componentView.components.map { map(it) }
              is Satchel<*> -> componentView.components.map { map(it) }
              else -> emptyList() // Will never happen due to when condition
            }

        ComponentIdData(id = componentView.id, components = children)
      }

      // Grid components with positioned children
      is GridPane<*> -> {
        val grid =
            componentView.grid.clone().apply {
              removeEmptyColumns()
              removeEmptyRows()
            }

        ComponentIdData(
            id = componentView.id,
            grid =
                grid.mapNotNull { element ->
                  if (element.component != null) {
                    GridPosition(
                        column = element.columnIndex,
                        row = element.rowIndex,
                        component = map(element.component))
                  } else null
                })
      }

      // Hexagon grid with a map of children
      is HexagonGrid<*> -> {
        ComponentIdData(
            id = componentView.id,
            map =
                componentView.map
                    .mapKeys { "${it.key.first}/${it.key.second}" }
                    .mapValues { map(it.value) })
      }

      // Camera pane with a target
      is CameraPane<*> -> {
        ComponentIdData(
            id = componentView.id,
            target = map(componentView.target)
        )
      }

      // Regular components (leaves in the component tree)
      else -> ComponentIdData(id = componentView.id)
    }
  }

  fun map(scene: Scene<*>): ComponentIdData {
    return ComponentIdData(id = scene.id, components = scene.components.map { map(it) })
  }

  /** Serializes a scene to a JSON string */
  fun serializeScene(scene: Scene<*>): String {
    val idScene = map(scene)
    return idJson.encodeToString(idScene)
  }

  /** Serializes a component to a JSON string */
  fun serializeComponent(component: ComponentView): String {
    val idComponent = map(component)
    return idJson.encodeToString(idComponent)
  }
}
