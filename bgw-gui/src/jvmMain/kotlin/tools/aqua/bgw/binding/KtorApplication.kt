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

package tools.aqua.bgw.binding

import ActionProp
import AnimationData
import AppData
import PropData
import SceneMapper
import data.event.AnimationFinishedEventData
import data.event.AnimationCleanedEventData
import data.event.CheckBoxChangedEventData
import data.event.ColorInputChangedEventData
import data.event.DragDroppedEventData
import data.event.DragGestureEndedEventData
import data.event.DragGestureEnteredEventData
import data.event.DragGestureExitedEventData
import data.event.DragGestureMovedEventData
import data.event.DragGestureStartedEventData
import data.event.EventData
import data.event.FilesPickedEventData
import data.event.InternalCameraPanData
import data.event.KeyEventAction
import data.event.KeyEventData
import data.event.LoadEventData
import data.event.MouseEnteredEventData
import data.event.MouseEventData
import data.event.MouseExitedEventData
import data.event.MousePressedEventData
import data.event.MouseReleasedEventData
import data.event.RadioChangedEventData
import data.event.ScrollEventData
import data.event.SelectionChangedEventData
import data.event.StructuredDataSelectEventData
import data.event.TextInputChangedEventData
import data.event.TransformChangedEventData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.io.ByteArrayOutputStream
import java.time.Duration
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import jsonMapper
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.uicomponents.BinaryStateButton
import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.StructuredDataView
import tools.aqua.bgw.components.uicomponents.TextInputUIComponent
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.findComponent
import tools.aqua.bgw.core.getRootNode
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.event.AnimationFinishedEvent
import tools.aqua.bgw.event.AnimationCleanedEvent
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.event.WheelEvent
import tools.aqua.bgw.mapper.DialogMapper
import tools.aqua.bgw.util.Coordinate

internal val componentChannel: Channel =
    Channel("/ws").apply {
      onClientConnected = {
        Frontend.application
        val json =
            jsonMapper.encodeToString(
                PropData(
                    SceneMapper.map(
                            menuScene = Frontend.menuScene, gameScene = Frontend.boardGameScene)
                        .apply {
                          fonts =
                              Frontend.loadedFonts.map { (path, fontName, weight) ->
                                Triple(path, fontName, weight.toInt())
                              }
                        }))
        it.send(json)
        // if (!uiJob.isActive) uiJob.start()
      }

      onClientMessage = { session, text ->
        val type = text.substringBefore('|')
        val content = text.substringAfter('|')
        when (type) {
          "bgwQuery" -> {
            try {
              eventListener(content)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
          "bgwAnimationFinishQuery" -> {
            try {
              animationFinishedListener(content)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
          "bgwAnimationCleanedQuery" -> {
            try {
              animationCleanedListener(content)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
          "bgwGlobalQuery" -> {
            try {
              globalListener(content)
            } catch (e: Exception) {
              e.printStackTrace()
            }
          }
        }
      }
    }

internal fun animationFinishedListener(text: String) {
  val eventData = jsonMapper.decodeFromString<AnimationFinishedEventData>(text)
  if (eventData is AnimationFinishedEventData) {
    val menuSceneAnimations = Frontend.menuScene?.animations?.toList() ?: listOf()
    val boardGameSceneAnimations = Frontend.boardGameScene?.animations?.toList() ?: listOf()
    val animations = menuSceneAnimations + boardGameSceneAnimations
    val animation = animations.find { it.id == eventData.id }
    animation?.isRunning = false
    animation?.onFinished?.invoke(AnimationFinishedEvent())
  }
}

internal fun animationCleanedListener(text: String) {
  val eventData = jsonMapper.decodeFromString<AnimationCleanedEventData>(text)
  if(eventData is AnimationCleanedEventData) {
    val menuSceneAnimations = Frontend.menuScene?.animations?.toList() ?: listOf()
    val boardGameSceneAnimations = Frontend.boardGameScene?.animations?.toList() ?: listOf()
    val animations = menuSceneAnimations + boardGameSceneAnimations
    val animation = animations.find { it.id == eventData.id }
    animation?.onCleaned?.invoke(AnimationCleanedEvent())
    animation?.isRunning = false
  }
}

internal fun globalListener(text: String) {
  val eventData = jsonMapper.decodeFromString<KeyEventData>(text)

  try {
    val keyEvent =
        KeyEvent(
            eventData.keyCode,
            eventData.character,
            eventData.isControlDown,
            eventData.isShiftDown,
            eventData.isAltDown)
    val menuScene = Frontend.menuScene
    val boardGameScene = Frontend.boardGameScene

    when (eventData.action) {
      KeyEventAction.PRESS -> {
        menuScene?.onKeyPressed?.invoke(keyEvent)
        boardGameScene?.onKeyPressed?.invoke(keyEvent)
      }
      KeyEventAction.RELEASE -> {
        menuScene?.onKeyReleased?.invoke(keyEvent)
        boardGameScene?.onKeyReleased?.invoke(keyEvent)
      }
      KeyEventAction.TYPE -> {
        menuScene?.onKeyTyped?.invoke(keyEvent)
        boardGameScene?.onKeyTyped?.invoke(keyEvent)
      }
    }
  } catch (e: Exception) {}
}

internal fun eventListener(text: String) {
  val eventData = jsonMapper.decodeFromString<EventData>(text)

  // Handle events for the application
  if (eventData is LoadEventData) {
    eventData.id?.indexOf("bgw-scene-")?.let {
      if (it >= -1) {
        val sceneId = eventData.id ?: return
        if (Frontend.boardGameScene?.id == sceneId && Frontend.boardGameScene?.isVisible != true) {
          Frontend.boardGameScene?.isVisible = true
          Frontend.boardGameScene?.onSceneShown?.invoke()
        }
        if (Frontend.menuScene?.id == sceneId && Frontend.menuScene?.isVisible != true) {
          Frontend.menuScene?.isVisible = true
          Frontend.menuScene?.onSceneShown?.invoke()
        }
      }
    }
    Frontend.applicationEngine.frame?.loadCallback?.invoke(Unit)
  }

  val id = eventData.id
  if (id != null && id.isNotBlank()) {
    val component = Frontend.getComponentById(id)
    if (component != null) {
      try {
        when (eventData) {
          // Handle events for components
          is FilesPickedEventData -> {
            if (Frontend.openedFileDialog != null &&
                eventData.id == Frontend.openedFileDialog?.id) {
              if (eventData.paths.isNotEmpty()) {
                Frontend.openedFileDialog?.onPathsSelected?.invoke(eventData.paths)
              } else {
                Frontend.openedFileDialog?.onSelectionCancelled?.invoke()
              }
              Frontend.openedFileDialog = null
            }
          }
          is MouseEnteredEventData -> {
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            component.onMouseEntered?.invoke(MouseEvent(MouseButtonType.UNSPECIFIED, posX, posY))
          }
          is MouseExitedEventData -> {
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            component.onMouseExited?.invoke(MouseEvent(MouseButtonType.UNSPECIFIED, posX, posY))
          }
          is MousePressedEventData -> {
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            component.onMousePressed?.invoke(MouseEvent(eventData.button, posX, posY))
          }
          is ScrollEventData -> {
            component.onMouseWheel?.invoke(
                WheelEvent(
                    eventData.direction,
                    eventData.ctrl,
                    eventData.shift,
                    eventData.alt,
                    eventData.delta))
          }
          is MouseReleasedEventData -> {
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            component.onMouseReleased?.invoke(MouseEvent(eventData.button, posX, posY))
          }
          is MouseEventData -> {
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            component.onMouseClicked?.invoke(MouseEvent(eventData.button, posX, posY))
          }
          is KeyEventData -> {
            val keyEvent =
                KeyEvent(
                    eventData.keyCode,
                    eventData.character,
                    eventData.isControlDown,
                    eventData.isShiftDown,
                    eventData.isAltDown)
            when (eventData.action) {
              KeyEventAction.PRESS -> component.onKeyPressed?.invoke(keyEvent)
              KeyEventAction.RELEASE -> component.onKeyReleased?.invoke(keyEvent)
              KeyEventAction.TYPE -> component.onKeyTyped?.invoke(keyEvent)
            }
          }
          is SelectionChangedEventData -> {
            // println("Selection changed")
            if (component is ComboBox<*>) component.select(eventData.selectedItem)
          }
          is StructuredDataSelectEventData -> {
            if (component is StructuredDataView<*>) component.selectIndex(eventData.index)
          }
          is TextInputChangedEventData -> {
            if (component is TextInputUIComponent) {
              component.textProperty.setSilent(eventData.value)
              component.onTextChanged?.invoke(eventData.value)
            }
          }
          is ColorInputChangedEventData -> {
            // println("Text changed")
            if (component is ColorPicker) component.selectedColor = Color(eventData.value)
          }
          is CheckBoxChangedEventData -> {
            if (component is CheckBox) {
              if (component.isIndeterminateAllowedProperty.value) {
                if (eventData.value &&
                    !component.isCheckedProperty.value &&
                    !component.isIndeterminateProperty.value) {
                  component.isIndeterminate = true
                } else if (eventData.value &&
                    component.isIndeterminateProperty.value &&
                    !component.isCheckedProperty.value) {
                  component.isIndeterminate = false
                  component.isChecked = true
                } else if (!eventData.value &&
                    !component.isIndeterminateProperty.value &&
                    component.isCheckedProperty.value) {
                  component.isChecked = false
                }
              } else {
                component.isChecked = eventData.value
                if (component.isIndeterminateProperty.value) {
                  component.isIndeterminate = false
                }
              }
            }
          }
          is RadioChangedEventData -> {
            if (component is BinaryStateButton) component.isSelected = eventData.value
          }
          is TransformChangedEventData -> {
            if (component is CameraPane<*>) {
              if (component.zoomProperty.value != eventData.zoomLevel) {
                component.onZoomed?.invoke(eventData.zoomLevel)
              }
              component.zoomProperty.setInternal(eventData.zoomLevel)
              component.panDataProperty.setInternal(InternalCameraPanData())
              component.anchorPointProperty.setInternal(
                  Coordinate(eventData.anchor.first, eventData.anchor.second))
            }
          }
          is DragGestureStartedEventData -> {
            if (component is DynamicComponentView) {
              component.onDragGestureStarted?.invoke(DragEvent(component))
              component.isDragged = true
            }
          }
          is DragGestureMovedEventData -> {
            if (component is DynamicComponentView) {
              component.onDragGestureMoved?.invoke(DragEvent(component))
            }
          }
          is DragGestureEnteredEventData -> {
            if (component is DynamicComponentView &&
                eventData.target.isNotBlank() &&
                component.parent != null) {
              val root = component.getRootNode()
              val target = root.findComponent(eventData.target)
              if (target?.dropAcceptor != null) {
                target.onDragGestureEntered?.invoke(DragEvent(component))
              }
            }
          }
          is DragGestureEndedEventData -> {
            if (component is DynamicComponentView && component.parent != null) {
              if (eventData.droppedOn != null) {
                val root = component.getRootNode()
                val target = root.findComponent(eventData.droppedOn!!)
                if (target?.dropAcceptor != null) {
                  component.onDragGestureEnded?.invoke(
                      DropEvent(component, target),
                      target.dropAcceptor?.invoke(DragEvent(component)) == true)
                }
              } else {
                component.onDragGestureEnded?.invoke(DropEvent(component, null), false)
              }
            }
          }
          is DragGestureExitedEventData -> {
            if (component is DynamicComponentView &&
                eventData.target.isNotBlank() &&
                component.parent != null) {
              val root = component.getRootNode()
              val target = root.findComponent(eventData.target)
              if (target?.dropAcceptor != null) {
                target.onDragGestureExited?.invoke(DragEvent(component))
              }
            }
          }
          is DragDroppedEventData -> {
            val root = component.getRootNode()
            val target = root.findComponent(eventData.target)
            val dropped = target?.dropAcceptor?.invoke(DragEvent(component))
            if (dropped == true) {
              target.onDragDropped?.invoke(DragEvent(component))
              (component as DynamicComponentView).isDragged = false
            }
          }
        }
      } catch (e: Exception) {
        val mainFrame = Frontend.applicationEngine.frame
        mainFrame?.openNewDialog(
            DialogMapper.map(
                Dialog(
                    "Error",
                    "Error",
                    "An error occurred while handling an event: ${e.message}",
                    exception = e)))
        e.printStackTrace()
      }
    }
  }
}

internal val internalChannel: Channel =
    Channel("/internal").apply { onClientMessage = { session, text -> } }

internal fun HTML.index() {
  body {
    style = "width: 100%; height: 100%; overflow: hidden; overscroll-behavior: none;"
    div {
      classes = setOf("bgw-root-container")
      div {
        classes = setOf("bgw-dialogs")
        id = "bgw-dialogs"
      }
      div {
        classes = setOf("bgw-root")
        id = "bgw-root"
      }
    }
    script(src = "/static/bgw-gui.js") {}
  }
}

internal fun Application.module() {
  configureRouting()
  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }
  componentChannel.install(this)
  internalChannel.install(this)
}

internal fun Application.configureRouting() {
  routing {
    get("/") { call.respondHtml(HttpStatusCode.OK, HTML::index) }
    static("/static") { resources() }
  }
}

internal fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: (suspend () -> Unit)) =
    this.async {
      if (repeatMillis > 0) {
        while (isActive) {
          action()
          delay(repeatMillis)
        }
      } else {
        action()
      }
    }

internal fun gzip(content: String): String {
  val bos = ByteArrayOutputStream()
  GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(content) }
  return Base64.getEncoder().encodeToString(bos.toByteArray())
}

internal fun ungzip(content: ByteArray): String =
    GZIPInputStream(content.inputStream()).bufferedReader(UTF_8).use { it.readText() }

private val updateStack = Collections.synchronizedList(Stack<AppData>())
private val debounceTimeMillis = 5L // Adjust this value as needed
private val debounceMutex = Mutex()
private var debounceJob: Job? = null

private var refreshJob: Job? = null

private var finalUpdate: String = ""

internal fun enqueueUpdate(data: AppData) {
  updateStack.add(data)
  refreshJob?.cancel()
  debounceJob?.cancel()
  debounceJob =
      CoroutineScope(Dispatchers.IO).launch {
        delay(debounceTimeMillis)
        debounceMutex.withLock { processLastUpdate() }
      }
}

internal suspend fun forceAnimationUpdate(animationData: AnimationData) {
  debounceJob?.cancel()
  updateStack.clear()
  val json =
      jsonMapper.encodeToString(PropData(collectAppData().apply { forcedByAnimation = true }))
  componentChannel.sendToAllClients(json)
  val animationJson = jsonMapper.encodeToString(PropData(animationData))
  componentChannel.sendToAllClients(animationJson)
}

private suspend fun processLastUpdate() {
  val lastUpdate = updateStack.lastOrNull()
  updateStack.clear()
  lastUpdate?.let {
    try {
      val json = jsonMapper.encodeToString(PropData(lastUpdate))
      finalUpdate = json
      // println("Processing update: $json")
      componentChannel.sendToAllClients(json)
      refreshJob =
          CoroutineScope(Dispatchers.IO).launch {
            delay(50)
            checkForMissingState()
          }
    } catch (e: Exception) {
      println("Error sending update: $e")
    }
  }
}

internal fun checkForMissingState() {
  val appData = collectAppData()
  val json = jsonMapper.encodeToString(PropData(appData))
  if (json != finalUpdate) {
    // println("Missing state detected. Sending update...")
    enqueueUpdate(appData)
  }
  finalUpdate = ""

  refreshJob?.cancel()
}

internal fun collectAppData(): AppData {
  val appData =
      SceneMapper.map(menuScene = Frontend.menuScene, gameScene = Frontend.boardGameScene).apply {
        fonts =
            Frontend.loadedFonts.map { (path, fontName, weight) ->
              Triple(path, fontName, weight.toInt())
            }
      }
  appData.action = ActionProp.UPDATE_COMPONENT
  return appData
}

internal fun markDirty(prop: ActionProp) {
  runCatching {
    val appData = collectAppData()
    // println("Collecting updates... Size: " + updateStack.size)
    enqueueUpdate(appData)
  }
}
