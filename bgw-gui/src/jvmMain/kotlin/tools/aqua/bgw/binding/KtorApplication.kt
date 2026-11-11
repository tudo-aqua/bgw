/*
 * Copyright 2025-2026 The BoardGameWork Authors
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
import data.event.AnimationsStoppedEventData
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
import data.event.SliderChangedEventData
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
import kotlin.invoke
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.*
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.AnimationRegistry
import tools.aqua.bgw.animation.ComponentAnimation
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.RandomizeAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.animation.animationType
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.uicomponents.BinaryStateButton
import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.ColorPicker
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Slider
import tools.aqua.bgw.components.uicomponents.StructuredDataView
import tools.aqua.bgw.components.uicomponents.TextInputUIComponent
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.findComponent
import tools.aqua.bgw.core.getRootNode
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.event.AnimationFinishedEvent
import tools.aqua.bgw.event.DragEvent
import tools.aqua.bgw.event.DropEvent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.event.WheelEvent
import tools.aqua.bgw.mapper.DialogMapper
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.Logger

internal val componentChannel: Channel =
    Channel("/ws").apply {
      onClientConnected = {
        val json =
            try {
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
            } catch (e: Exception) {
              // Let the update scheduler deliver the state as soon as it can be mapped.
              UpdateScheduler.markDirty()
              throw e
            }
        UpdateScheduler.onClientConnected(json)
        it.send(json)
      }

      onClientMessage = { _, text ->
        val type = text.substringBefore('|')
        val content = text.substringAfter('|')
        // Changes made by handlers are sent as one update once the handler returned.
        UpdateScheduler.batch {
          try {
            when (type) {
              "bgwQuery" -> eventListener(content)
              "bgwAnimationQuery" -> handleAnimationFinished(content)
              "bgwAnimationStopQuery" -> handleAnimationsStopped(content)
              "bgwGlobalQuery" -> globalListener(content)
              "bgwRendered" -> UpdateScheduler.onRendered()
            }
          } catch (e: Exception) {
            Logger.error(e.stackTraceToString())
          }
        }
      }
    }

internal fun handleAnimationsStopped(text: String) {
  jsonMapper.decodeFromString<AnimationsStoppedEventData>(text)

  // For each running animation, clean up WITHOUT triggering onFinished or persist
  val running = AnimationRegistry.all().filter { it.isRunning }
  running.forEach { animation ->
    animation.isRunning = false
    AnimationRegistry.unregister(animation)
    Frontend.menuScene?.animations?.remove(animation)
    Frontend.boardGameScene?.animations?.remove(animation)

    if (animation is ComponentAnimation<*>) {
      val component = animation.componentView
      component.animationTypes.remove(animation.animationType)
      if (component.animationTypes.isEmpty()) {
        component.componentAnimating = false
        Frontend.animationCache.remove(component.id)
      }
    }
  }

  Logger.debug("Stopped ${running.size} animations without calling onFinished")

  Frontend.updateScene()
}

internal fun handleAnimationFinished(text: String) {
  val eventData = jsonMapper.decodeFromString<AnimationFinishedEventData>(text)

  val animation = AnimationRegistry[eventData.id ?: return] ?: return
  if (!animation.isRunning) return
  animation.isRunning = false
  AnimationRegistry.unregister(animation)
  Frontend.menuScene?.animations?.remove(animation)
  Frontend.boardGameScene?.animations?.remove(animation)

  // Apply final animation values to component properties if persist=true
  if (animation is ComponentAnimation<*> && animation.persist) persistAnimation(animation)

  // Remove animation type and cache for ComponentAnimations
  if (animation is ComponentAnimation<*>) {
    val component = animation.componentView
    val animationType = animation.animationType

    // The browser keeps showing the animated values until all animations of this type on the
    // component finished, so only then the type is reported as finished.
    if (!AnimationRegistry.isTypeRunningOn(component, animationType, except = animation)) {
      component.animationTypes.remove(animationType)
      UpdateScheduler.reportFinishedAnimation(component, animationType)
    }

    // Check if component has no more active animations
    if (!AnimationRegistry.isTypeRunningOn(component, null, except = animation)) {
      component.componentAnimating = false
      Frontend.animationCache.remove(component.id)
    }
  }

  try {
    animation.onFinished?.invoke(AnimationFinishedEvent())
  } catch (e: Exception) {
    reportHandlerException(e)
  } finally {
    // Without this, an exception in onFinished kept the parent animation running forever.
    checkIfParentFinished(animation)
  }
}

private fun persistAnimation(animation: ComponentAnimation<*>) {
  when (animation) {
    is MovementAnimation<*> -> {
      animation.componentView.posX += animation.toX - animation.fromX
      animation.componentView.posY += animation.toY - animation.fromY
    }
    is ScaleAnimation<*> -> {
      animation.componentView.scaleX =
          if (animation.fromScaleX == 0.0) animation.toScaleX
          else animation.componentView.scaleX * animation.toScaleX / animation.fromScaleX
      animation.componentView.scaleY =
          if (animation.fromScaleY == 0.0) animation.toScaleY
          else animation.componentView.scaleY * animation.toScaleY / animation.fromScaleY
    }
    is RotationAnimation<*> -> {
      animation.componentView.rotation += animation.toAngle - animation.fromAngle
    }
    is FadeAnimation<*> -> {
      animation.componentView.opacity = animation.toOpacity
    }
    is FlipAnimation<*> -> {
      val component = animation.componentView
      when (component) {
        is CardView ->
            when {
              animation.toVisual == component.backVisual -> component.showBack()
              animation.toVisual == component.frontVisual -> component.showFront()
              component.currentSide == CardView.CardSide.BACK -> {
                component.frontVisual = animation.toVisual
                component.showFront()
              }
              else -> {
                component.backVisual = animation.toVisual
                component.showBack()
              }
            }
        is DiceView -> component.setCurrentSideVisual(animation.toVisual)
        else -> component.visual = animation.toVisual
      }
    }
    is RandomizeAnimation<*> -> {
      val component = animation.componentView
      when (component) {
        is CardView ->
            if (component.currentSide == CardView.CardSide.FRONT) {
              component.frontVisual = animation.toVisual
            } else {
              component.backVisual = animation.toVisual
            }
        is DiceView -> component.setCurrentSideVisual(animation.toVisual)
        else -> component.visual = animation.toVisual
      }
    }
    is DiceAnimation<*> -> {
      animation.componentView.currentSide = animation.toSide
    }
  }
}

internal fun checkIfParentFinished(animation: Animation) {
  val parent = animation.parentAnimation ?: return
  if (!parent.isRunning) return

  val children =
      when (parent) {
        is SequentialAnimation -> parent.animations
        is ParallelAnimation -> parent.animations
        else -> emptyList()
      }
  if (children.all { !it.isRunning }) {
    parent.isRunning = false
    AnimationRegistry.unregister(parent)
    Frontend.menuScene?.animations?.remove(parent)
    Frontend.boardGameScene?.animations?.remove(parent)
    try {
      parent.onFinished?.invoke(AnimationFinishedEvent())
    } catch (e: Exception) {
      reportHandlerException(e)
    } finally {
      checkIfParentFinished(parent)
    }
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
  } catch (e: Exception) {
    // Exceptions thrown by the application's key handlers were silently swallowed before.
    reportHandlerException(e)
  }
}

/** Logs an exception thrown by an application event handler and shows it in an error dialog. */
internal fun reportHandlerException(e: Throwable) {
  Logger.error(e.stackTraceToString())
  Frontend.applicationEngine.frame?.openNewDialog(
      DialogMapper.map(
          Dialog(
              "Error",
              "Error",
              "An error occurred while handling an event: ${e.message}",
              exception = e)))
}

internal fun eventListener(text: String) {
  val eventData = jsonMapper.decodeFromString<EventData>(text)

  // Handle events for the application
  if (eventData is LoadEventData) {
    eventData.id?.indexOf("bgw-scene-")?.let {
      if (it >= 0) {
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

    if (Frontend.application.headless) {
      Frontend.loadCallback.invoke()
    } else {
      Frontend.applicationEngine.frame?.loadCallback?.invoke(Unit)
    }
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
          is SliderChangedEventData -> {
            if (component is Slider) component.value = eventData.value
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
              // The browser already shows this transform. It is only needed when the camera pane
              // is created again, so it does not cause an update on its own.
              component.zoomProperty.setSilent(eventData.zoomLevel)
              component.anchorPointProperty.setSilent(
                  Coordinate(eventData.anchor.first, eventData.anchor.second))
              component.reportedPanData =
                  InternalCameraPanData(
                      sequence = eventData.sequence,
                      panByTotalX = eventData.panByTotalX,
                      panByTotalY = eventData.panByTotalY)
              component.hasReportedTransform = true
            }
          }
          is DragGestureStartedEventData -> {
            if (component is DynamicComponentView) {
              val (posX, posY) =
                  Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
              component.onDragGestureStarted?.invoke(DragEvent(component, posX, posY))
              component.isDragged = true
            }
          }
          is DragGestureMovedEventData -> {
            if (component is DynamicComponentView) {
              val (posX, posY) =
                  Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
              component.onDragGestureMoved?.invoke(DragEvent(component, posX, posY))
            }
          }
          is DragGestureEnteredEventData -> {
            if (component is DynamicComponentView &&
                eventData.target.isNotBlank() &&
                component.parent != null) {
              val root = component.getRootNode()
              val target = root.findComponent(eventData.target)
              if (target?.dropAcceptor != null) {
                val (posX, posY) =
                    Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
                target.onDragGestureEntered?.invoke(DragEvent(component, posX, posY))
              }
            }
          }
          is DragGestureEndedEventData -> {
            if (component is DynamicComponentView && component.parent != null) {
              if (eventData.droppedOn != null) {
                val root = component.getRootNode()
                val target = root.findComponent(eventData.droppedOn!!)
                if (target?.dropAcceptor != null) {
                  val (posX, posY) =
                      Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
                  component.onDragGestureEnded?.invoke(
                      DropEvent(component, target),
                      target.dropAcceptor?.invoke(DragEvent(component, posX, posY)) == true)
                }
              } else {
                component.onDragGestureEnded?.invoke(DropEvent(component, null), false)
              }
              component.isDragged = false
            }
          }
          is DragGestureExitedEventData -> {
            if (component is DynamicComponentView &&
                eventData.target.isNotBlank() &&
                component.parent != null) {
              val root = component.getRootNode()
              val target = root.findComponent(eventData.target)
              if (target?.dropAcceptor != null) {
                val (posX, posY) =
                    Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
                target.onDragGestureExited?.invoke(DragEvent(component, posX, posY))
              }
            }
          }
          is DragDroppedEventData -> {
            val root = component.getRootNode()
            val target = root.findComponent(eventData.target)
            val (posX, posY) = Frontend.relativePositionsToAbsolute(eventData.posX, eventData.posY)
            val dropped = target?.dropAcceptor?.invoke(DragEvent(component, posX, posY))
            if (dropped == true) {
              target.onDragDropped?.invoke(DragEvent(component, posX, posY))
              (component as DynamicComponentView).isDragged = false
            }
          }
        }
      } catch (e: Exception) {
        reportHandlerException(e)
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

internal fun forceAnimationUpdate(animationData: AnimationData) {
  UpdateScheduler.sendStateWithAnimation(animationData)
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

@Suppress("UNUSED_PARAMETER")
internal fun markDirty(prop: ActionProp) {
  UpdateScheduler.markDirty()
}
