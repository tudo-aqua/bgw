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

import ID
import IDAreaData
import IDCameraPaneData
import IDCardStackData
import IDComponentViewData
import IDData
import IDGameComponentContainerData
import IDGameComponentViewData
import IDGridElementData
import IDGridPaneData
import IDHexagonGridData
import IDLayoutViewData
import IDLinearLayoutData
import IDPaneData
import IDSatchelData
import IDSceneData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json as KJson
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.*

/**
 * Component mapper that creates a simplified version of ComponentView hierarchy containing only IDs
 * and structure information.
 */
internal object ComponentIdMapper {

  /** Maps a ComponentView to a simplified IDComponentViewData containing only ID and structure info */
  fun map(componentView: ComponentView): IDComponentViewData {
    println("Mapping component: ${componentView.id} of type ${componentView::class.simpleName}")
    return when (componentView) {
      // Layout Views (containers that can have child components)
      is Pane<*> -> {
        IDPaneData().apply {
          id = componentView.id
          components = componentView.components.map { map(it) }.toMutableList()
        }
      }
      is GridPane<*> -> {
        val grid =
            componentView.grid.clone().apply {
              removeEmptyColumns()
              removeEmptyRows()
            }

        IDGridPaneData().apply {
          id = componentView.id
          this.grid =
              grid
                  .mapNotNull { element ->
                    if (element.component != null) {
                      IDGridElementData(
                          column = element.columnIndex,
                          row = element.rowIndex,
                          component = map(element.component))
                    } else null
                  }
                  .toList()
        }
      }
      is CameraPane<*> -> {
        IDCameraPaneData().apply {
          id = componentView.id
          target =
              map(componentView.target) as IDLayoutViewData
        }
      }

      // Game Component Containers
      is Area<*> -> {
        IDAreaData().apply {
          id = componentView.id
          components = componentView.components.map { map(it) as IDGameComponentViewData }.toMutableList()
        }
      }
      is CardStack<*> -> {
        IDCardStackData().apply {
          id = componentView.id
          components = componentView.components.map { map(it) as IDGameComponentViewData }.toMutableList()
        }
      }
      is HexagonGrid<*> -> {
        IDHexagonGridData().apply {
          id = componentView.id
          map = componentView.map.mapKeys { entry ->
            "${entry.key.first}/${entry.key.second}"
          }.mapValues { (_, componentView) ->
            map(componentView) as IDGameComponentViewData
          }.toMutableMap()
        }
      }
      is LinearLayout<*> -> {
        IDLinearLayoutData().apply {
          id = componentView.id
          components = componentView.components.map { map(it) as IDGameComponentViewData }.toMutableList()
        }
      }
      is Satchel<*> -> {
        IDSatchelData().apply {
          id = componentView.id
          components = componentView.components.map { map(it) as IDGameComponentViewData }.toMutableList()
        }
      }

      // Regular components (leaves in the component tree)
      else -> IDGameComponentViewData().apply {
        id = componentView.id
        // For regular components, we don't need to map children since they are not containers
        // Just store the ID and type information
      }
    }
  }

  fun map(scene: Scene<*>): IDSceneData {
    return IDSceneData().apply {
      id = scene.id
      components = scene.components.map { map(it) }.toMutableList()
    }
  }

  /** Serializes a scene to a JSON string with proper type information */
  fun serializeScene(scene: Scene<*>): String {
    val idScene = map(scene)
    return idJson.encodeToString(idScene)
  }

  /** Serializes a component to a JSON string with proper type information */
  fun serializeComponent(component: ComponentView): String {
    val idComponent = map(component)
    return idJson.encodeToString(idComponent)
  }
}

/** Define serialization module for Id components to ensure proper polymorphic serialization */
private val idModule = SerializersModule {
  polymorphic(IDData::class) {
    subclass(IDSceneData::class)
    subclass(IDPaneData::class)
    subclass(IDGridPaneData::class)
    subclass(IDCameraPaneData::class)
    subclass(IDAreaData::class)
    subclass(IDCardStackData::class)
    subclass(IDHexagonGridData::class)
    subclass(IDLinearLayoutData::class)
    subclass(IDSatchelData::class)
  }
  polymorphic(IDComponentViewData::class) {
    subclass(IDPaneData::class)
    subclass(IDGridPaneData::class)
    subclass(IDCameraPaneData::class)
    subclass(IDAreaData::class)
    subclass(IDCardStackData::class)
    subclass(IDHexagonGridData::class)
    subclass(IDLinearLayoutData::class)
    subclass(IDSatchelData::class)
    subclass(IDGameComponentViewData::class)
  }
  polymorphic(IDLayoutViewData::class) {
    subclass(IDPaneData::class)
    subclass(IDGridPaneData::class)
  }
  polymorphic(IDGameComponentContainerData::class) {
    subclass(IDAreaData::class)
    subclass(IDCardStackData::class)
    subclass(IDHexagonGridData::class)
    subclass(IDLinearLayoutData::class)
    subclass(IDSatchelData::class)
  }
}

/** Json configuration for Id component serialization */
internal val idJson = KJson {
  serializersModule = idModule
  ignoreUnknownKeys = true
}
