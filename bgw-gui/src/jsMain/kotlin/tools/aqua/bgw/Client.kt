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

package tools.aqua.bgw

import ActionProp
import AnimationData
import AppData
import AreaData
import CameraPaneData
import CardStackData
import ComponentViewData
import Data
import DialogData
import FileDialogData
import GameComponentContainerData
import GameComponentViewData
import GridPaneData
import HexagonGridData
import HexagonViewData
import ID
import JsonData
import LayoutViewData
import LinearLayoutData
import PaneData
import PropData
import SatchelData
import SceneData
import data.event.AnimationFinishedEventData
import data.event.FilesPickedEventData
import data.event.LoadEventData
import jsonMapper
import kotlin.math.floor
import kotlin.random.Random
import kotlinx.browser.document
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonEncoder
import org.w3c.dom.CustomEvent
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.NodeList
import org.w3c.dom.WebSocket
import react.*
import react.dom.client.Root
import react.dom.client.createRoot
import react.dom.render
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.elements.Dialog
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.dom.Element
import web.fs.FileSystemFileHandle
import web.timers.setTimeout

internal var internalSocket: WebSocket? = null
internal var webSocket: WebSocket? = null
internal var handlers: MutableMap<ID, (Data) -> Unit> = mutableMapOf()

internal var lastAppData: AppData? = null

internal lateinit var container: HTMLElement
internal lateinit var dialogContainer: HTMLElement
internal lateinit var root: Root
internal lateinit var dialogRoot: Root
internal val dialogMap = mutableMapOf<ID, DialogData>()

internal fun main() {
  if (Config.USE_SOCKETS) {
    webSocket = WebSocket("ws://${document.location?.host}/ws")
    webSocket?.onopen = {}
    webSocket?.onmessage = { event ->
      val cont = document.getElementById("bgw-root")
      val dialog = document.getElementById("bgw-dialogs")

      if (cont != null) {
        dialogContainer = dialog as HTMLElement
      }

      if (cont != null) {
        container = cont as HTMLElement
        val receivedData = jsonMapper.decodeFromString<PropData>(event.data.toString()).data
        if (receivedData == null) {
          handleSingleUpdates(event.data.toString())
        } else {
          handleReceivedData(receivedData!!)
        }
      }
    }
  } else {
    document.addEventListener(
        "BGW_MSG",
        {
          val event = it as CustomEvent
          val data = event.detail
          val jsonData = jsonMapper.decodeFromString<JsonData>(data.toString())
          val receivedData = jsonData.props.data
          val containerId = jsonData.container

          val cont = document.getElementById(containerId)
          if (cont != null) {
            container = cont as HTMLElement
            handleReceivedData(receivedData!!)
          }
        })
  }
}

internal fun resetAnimations() {
  val styles = document.body?.querySelectorAll("style") as NodeList
  for (i in 0 until styles.length) {
    val style = styles.item(i) as HTMLStyleElement
    document.body?.removeChild(style)
  }
}

internal fun stopAnimations() {
  Animator.clearAllTimeoutsAndIntervals()
  resetAnimations()
}

internal fun handleSingleUpdates(data: String) {
  val jsonData: MutableMap<String, Triple<ActionProp, String, String>> = Json.decodeFromString(data)

  jsonData.forEach { (id, triple) ->
    val action = triple.first
    val parentId = triple.second
    val data = triple.third

    println("Handling single update for ID: $id (Parent: ${parentId}), Action: $action, Data: $data")

    // Use the new recursive function for all component actions
    if (action in listOf(ActionProp.REMOVE_COMPONENT, ActionProp.UPDATE_COMPONENT, ActionProp.ADD_COMPONENT)) {
      val component = if (action != ActionProp.REMOVE_COMPONENT) {
        jsonMapper.decodeFromString<ComponentViewData>(data)
      } else {
        null
      }

      // If we're adding a component, we need to consider the parentId
      val updated = if (action == ActionProp.ADD_COMPONENT && parentId.isNotEmpty()) {
        addComponentToParent(lastAppData, parentId, component as ComponentViewData)
      } else {
        updateAppDataGeneral(lastAppData, id, action, component)
      }

      if (!updated) {
        if (action == ActionProp.ADD_COMPONENT && parentId.isNotEmpty()) {
          console.warn("Parent with ID $parentId not found for adding component $id")
        } else {
          console.warn("Component with ID $id not found for $action operation.")
        }
      }
    } else {
      console.warn("Unknown action: $action for ID $id")
    }
  }

  if (lastAppData != null) {
    if (!Config.USE_SOCKETS) {
      renderApp(lastAppData!!)
    } else {
      renderAppFast(lastAppData!!)
    }
  } else {
    console.warn("No lastAppData available to render after single updates.")
  }
}

internal fun handleReceivedData(receivedData: Data) {
  when (receivedData) {
    is AppData -> {
      lastAppData = receivedData
      if (receivedData.action == ActionProp.HIDE_MENU_SCENE) {
        console.log("[SCENE] Hiding Menu Scene")
        val element = document.querySelector("#menuScene") as HTMLElement
        element.classList.toggle("scene--visible", false)
        setTimeout(
            {
              if (!Config.USE_SOCKETS) {
                renderApp(receivedData)
              } else {
                renderAppFast(receivedData)
              }
            },
            300)
      } else if (receivedData.action == ActionProp.SHOW_MENU_SCENE) {
        if (!Config.USE_SOCKETS) {
          renderApp(receivedData)
        } else {
          renderAppFast(receivedData)
        }
        val element = document.querySelector("#menuScene") as HTMLElement
        setTimeout({ element.classList.toggle("scene--visible", true) }, 50)
      } else {
        // Cancel all pending animation timeouts before new render to prevent incorrect resets
        Animator.cancelCleanupTimeouts()
        if (!Config.USE_SOCKETS) {
          renderApp(receivedData)
        } else {
          renderAppFast(receivedData)
        }
      }
      stopAnimations()
    }
    is AnimationData -> {
      if (receivedData.isStop) {
        stopAnimations()
        return
      }
      Animator.startAnimation(receivedData) {
        JCEFEventDispatcher.dispatchEvent(AnimationFinishedEventData().apply { id = it })
      }
    }
    is DialogData -> {
      dialogMap[receivedData.id] = receivedData
      renderDialogs()
    }
    /* is FileDialogData -> {
      when (receivedData.mode) {
        "open_file" -> {
          window.asDynamic().showOpenFilePicker(
            jso {
              id = receivedData.id
              multiple = false
            }
          ).then { handle -> sendFile(handle[0] as FileSystemFileHandle, receivedData)
          }.catch {
            JCEFEventDispatcher.dispatchEvent(FilesPickedEventData(emptyList()).apply { id = receivedData.id })
          }
        }
        "open_multiple_files" -> {
        }
        "save_file" -> {
        }
        "choose_directory" -> {
        }
        else -> {}
      }
    } */
    else -> {
      // Handle other data types if necessary
      println("Received unknown data type: ${receivedData::class.simpleName}")
    }
  }
}

internal fun updateAppDataGeneral(
  appData: AppData?,
  id: String,
  action: ActionProp,
  component: ComponentViewData? = null
): Boolean {
  if (appData == null) return false

  // First check if the component is in the game scene
  if (updateSceneData(appData.gameScene, id, action, component)) {
    return true
  }

  // Then check if the component is in the menu scene
  if (updateSceneData(appData.menuScene, id, action, component)) {
    return true
  }

  return false
}

internal fun updateSceneData(
  scene: SceneData?,
  id: String,
  action: ActionProp,
  component: ComponentViewData? = null
): Boolean {
  if (scene == null) return false

  // First check at the top level components list
  when (action) {
    ActionProp.REMOVE_COMPONENT -> {
      val removed = scene.components.removeAll { it.id == id }
      if (removed) return true
    }

    ActionProp.UPDATE_COMPONENT -> {
      val compIndex = scene.components.indexOfFirst { it.id == id }
      if (compIndex >= 0 && component != null) {
        scene.components[compIndex] = component
        return true
      }
    }

    ActionProp.ADD_COMPONENT -> {
      if (component != null) {
        scene.components.add(component)
        return true
      }
    }

    else -> { /* Other actions not handled yet */ }
  }

  // If not found at top level, recursively search through all components
  println("Searching for component with ID $id in scene ${scene.id} for action $action")
  return searchAndUpdateInComponents(scene.components, id, action, component)
}

internal fun searchAndUpdateInComponents(
  components: List<ComponentViewData>,
  id: String,
  action: ActionProp,
  newComponent: ComponentViewData? = null
): Boolean {
  // Check each component in the list
  for (component in components) {
    // First check if this is the target component
    if (component.id == id) {
      when (action) {
        ActionProp.UPDATE_COMPONENT -> {
          if (newComponent != null && components is MutableList<ComponentViewData>) {
            val index = components.indexOf(component)
            components[index] = newComponent
            return true
          }
        }
        // For REMOVE_COMPONENT, we need to handle it at the parent level
        // For ADD_COMPONENT, we don't add at this level since we found a match
        else -> { /* Other actions not applicable here */ }
      }
    }

    println("Checking component with ID ${component.id} for action $action")
    // Recursively search in containers and layouts
    when (component) {
      // Game component containers
      is AreaData -> {
        if (updateInGameComponentList(component.components, id, action, newComponent)) {
          return true
        }
      }
      is CardStackData -> {
        println("Checking CardStackData with ID ${component.id} for action $action")
        if (updateInGameComponentList(component.components, id, action, newComponent)) {
          return true
        }
      }
      is HexagonGridData -> {
        if (updateInGameComponentList(component.components, id, action, newComponent)) {
          return true
        }
        // Also check in the hex map
        component.map.values.forEach { hexagon ->
          if (hexagon.id == id && action == ActionProp.UPDATE_COMPONENT && newComponent is HexagonViewData) {
            component.map[id] = newComponent
            return true
          }
        }
      }
      is LinearLayoutData -> {
        if (updateInGameComponentList(component.components, id, action, newComponent)) {
          return true
        }
      }
      is SatchelData -> {
        if (updateInGameComponentList(component.components, id, action, newComponent)) {
          return true
        }
      }

      // Layout views
      is PaneData -> {
        if (searchAndUpdateInComponents(component.components, id, action, newComponent)) {
          return true
        }
      }
      is GridPaneData -> {
        for (gridElement in component.grid) {
          if (gridElement.component?.id == id) {
            when (action) {
              ActionProp.REMOVE_COMPONENT -> {
                gridElement.component = null
                return true
              }
              ActionProp.UPDATE_COMPONENT -> {
                if (newComponent != null) {
                  gridElement.component = newComponent
                  return true
                }
              }
              else -> { /* Other actions not applicable here */ }
            }
          }

          // Also recursively check the component if it's a container
          if (gridElement.component != null &&
              searchAndUpdateInComponents(listOf(gridElement.component!!), id, action, newComponent)) {
            return true
          }
        }
      }
      is CameraPaneData -> {
        if (component.target != null &&
            searchAndUpdateInComponents(listOf(component.target!!), id, action, newComponent)) {
          return true
        }
      }
    }
  }

  return false
}

internal fun updateInGameComponentList(
  components: List<GameComponentViewData>,
  id: String,
  action: ActionProp,
  newComponent: ComponentViewData? = null
): Boolean {
  println("Updating in game component list for ID: $id, Action: $action")
  // For removal
  if (action == ActionProp.REMOVE_COMPONENT && components is MutableList<GameComponentViewData>) {
    val removed = components.removeAll { it.id == id }
    if (removed) return true
  }

  // For update
  if (action == ActionProp.UPDATE_COMPONENT && newComponent is GameComponentViewData) {
    val index = components.indexOfFirst { it.id == id }
    if (index >= 0 && components is MutableList<GameComponentViewData>) {
      components[index] = newComponent
      return true
    }
  }

  // For addition
  if (action == ActionProp.ADD_COMPONENT && newComponent is GameComponentViewData && components is MutableList<GameComponentViewData>) {
    println("Adding new component with ID ${newComponent.id} to game component list")
    components.add(newComponent)
    return true
  }

  return false
}

internal fun sendFile(handle: FileSystemFileHandle, dialog: FileDialogData) {
  handle.getFileAsync().then { file ->
    println(file.webkitRelativePath)
    JCEFEventDispatcher.dispatchEvent(
        FilesPickedEventData(listOf(file.name)).apply { id = dialog.id })
    println("File picked: ${file.name}")
  }
}

/** Renders the app with React 17 syntax to provide fallback for BGW Playground web app. */
internal fun renderApp(appData: AppData) {
  render(
      App.create { data = appData },
      container as Element,
      callback = { JCEFEventDispatcher.dispatchEvent(LoadEventData()) })
}

/** Renders the app with React 18 syntax. */
internal fun renderAppFast(appData: AppData) {
  if (!::root.isInitialized) {
    root = createRoot(container as Element)
  }
  root.render(App.create { data = appData })
  JCEFEventDispatcher.dispatchEvent(LoadEventData())
}

internal fun renderDialogs() {
  println("Rendering Dialogs $dialogMap")
  if (!::dialogRoot.isInitialized) {
    dialogRoot = createRoot(dialogContainer as Element)
  }
  dialogRoot.render(Dialog.create { data = dialogMap.values.toList() })
}

internal fun List<ReactElement<*>>.toFC() = FC<Props> { appendChildren(this@toFC) }

internal fun ChildrenBuilder.appendChildren(components: List<ReactElement<*>>) =
    components.forEach { +it }

internal fun randomHexColor(): String {
  val chars = "0123456789ABCDEF"
  var color = "#"
  repeat(6) { color += chars[floor(Random.nextDouble() * 16).toInt()] }
  return color
}

/**
 * Adds a component to a specific parent by ID
 *
 * @param appData The app data structure
 * @param parentId The ID of the parent component to add to
 * @param component The component to add
 * @return Boolean indicating success/failure
 */
internal fun addComponentToParent(
  appData: AppData?,
  parentId: String,
  component: ComponentViewData
): Boolean {
  if (appData == null) return false

  // Check if parent is a scene
  if (appData.gameScene?.id == parentId) {
    appData.gameScene?.components?.add(component)
    println("Added component ${component.id} to game scene")
    return true
  }

  if (appData.menuScene?.id == parentId) {
    appData.menuScene?.components?.add(component)
    println("Added component ${component.id} to menu scene")
    return true
  }

  // Search for parent in game scene
  if (findAndAddToParent(appData.gameScene?.components ?: emptyList(), parentId, component)) {
    return true
  }

  // Search for parent in menu scene
  if (findAndAddToParent(appData.menuScene?.components ?: emptyList(), parentId, component)) {
    return true
  }

  return false
}

/**
 * Recursively searches for a parent component by ID and adds the new component to it
 */
internal fun findAndAddToParent(
  components: List<ComponentViewData>,
  parentId: String,
  newComponent: ComponentViewData
): Boolean {
  for (component in components) {
    // Check if this component is the parent we're looking for
    if (component.id == parentId) {
      when (component) {
        // Handle all the different container types
        is AreaData -> {
          if (newComponent is GameComponentViewData) {
            component.components.add(newComponent)
            println("Added component ${newComponent.id} to AreaData parent ${component.id}")
            return true
          }
        }
        is CardStackData -> {
          if (newComponent is GameComponentViewData) {
            component.components.add(newComponent)
            println("Added component ${newComponent.id} to CardStackData parent ${component.id}")
            return true
          }
        }
        is LinearLayoutData -> {
          if (newComponent is GameComponentViewData) {
            component.components.add(newComponent)
            println("Added component ${newComponent.id} to LinearLayoutData parent ${component.id}")
            return true
          }
        }
        is HexagonGridData -> {
          if (newComponent is GameComponentViewData) {
            component.components.add(newComponent)
            println("Added component ${newComponent.id} to HexagonGridData parent ${component.id}")
            return true
          } else if (newComponent is HexagonViewData) {
            // Add to hex map if the new component is a hex
            component.map[newComponent.id] = newComponent
            println("Added hexagon ${newComponent.id} to HexagonGridData map")
            return true
          }
        }
        is SatchelData -> {
          if (newComponent is GameComponentViewData) {
            component.components.add(newComponent)
            println("Added component ${newComponent.id} to SatchelData parent ${component.id}")
            return true
          }
        }
        is PaneData -> {
          component.components.add(newComponent)
          println("Added component ${newComponent.id} to PaneData parent ${component.id}")
          return true
        }
        is GridPaneData -> {
          // For GridPane, we need to find an empty cell or use specified cell coordinates
          // This is a simplification - in a real implementation, you might need logic
          // to determine the proper cell to place the component
          for (gridElement in component.grid) {
            if (gridElement.component == null) {
              gridElement.component = newComponent
              println("Added component ${newComponent.id} to GridPaneData cell (${gridElement.column},${gridElement.row})")
              return true
            }
          }
          println("No empty cell found in GridPaneData ${component.id}")
        }
        is CameraPaneData -> {
          if (component.target == null && newComponent is LayoutViewData) {
            component.target = newComponent
            println("Set target of CameraPaneData ${component.id} to ${newComponent.id}")
            return true
          }
        }
      }
    }

    // Recursively search in child components
    val found = when (component) {
      is AreaData,
      is CardStackData,
      is LinearLayoutData,
      is HexagonGridData,
      is SatchelData -> {
        findAndAddToParent((component as GameComponentContainerData).components, parentId, newComponent)
      }
      is PaneData -> {
        findAndAddToParent(component.components, parentId, newComponent)
      }
      is GridPaneData -> {
        var found = false
        component.grid.forEach { gridElement ->
          if (gridElement.component != null) {
            found = found || findAndAddToParent(listOf(gridElement.component!!), parentId, newComponent)
          }
        }
        found
      }
      is CameraPaneData -> {
        if (component.target != null) {
          findAndAddToParent(listOf(component.target!!), parentId, newComponent)
        } else false
      }
      else -> false
    }

    if (found) return true
  }

  return false
}
