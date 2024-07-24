/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

@file:Suppress("TooManyFunctions", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.core

import Action
import PropData
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import jsonMapper
import mapper.AnimationMapper
import mapper.DialogMapper
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.application.Application
import tools.aqua.bgw.application.Constants
import tools.aqua.bgw.application.JCEFApplication
import tools.aqua.bgw.binding.componentChannel
import tools.aqua.bgw.binding.messageQueue
import tools.aqua.bgw.binding.module
import tools.aqua.bgw.builder.*
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.RootComponent
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import webViewType
import java.io.File
import java.util.*

internal class Frontend {

  /** Starts the application. */
  internal fun start() {
    //println("Starting server...")

    embeddedServer(Netty, port = Constants.PORT, host = "localhost", module = io.ktor.server.application.Application::module).start(wait = false)
    applicationEngine.start {
      applicationEngine.clearAllEventListeners()
      boardGameScene?.let { SceneBuilder.build(it) }
      menuScene?.let { SceneBuilder.build(it) }
      renderedDOM.value = true

      // println("Main Thread: ${Thread.currentThread().id}")
    }
  }

  companion object {
    internal var applicationEngine: Application = JCEFApplication()

    /** Current scene scale. */
    internal var sceneScale: Double = 1.0

    internal var renderedDOM : BooleanProperty = BooleanProperty(false)

    /** [BoardGameApplication] instance. */
    internal lateinit var application: BoardGameApplication

    /** Initial aspect ratio passed to [BoardGameApplication]. */
    internal lateinit var initialAspectRatio: AspectRatio

    /** Initial window mode passed to [BoardGameApplication]. */
    internal var initialWindowMode: WindowMode? = null

    // region Properties
    /** Property for the window title. */
    internal val titleProperty: StringProperty = StringProperty()

    /** Property for the window icon. */
    internal val iconProperty: Property<ImageVisual?> = Property(null)

    /** Property for the fullscreen exit combination. */
    internal val fullscreenExitCombinationProperty: Property<KeyEvent?> = Property(null)

    /** Property for the fullscreen exit combination hint. */
    internal val fullscreenExitCombinationHintProperty: Property<String?> = Property(null)

    /** Property whether application is currently maximized. */
    internal val isMaximizedProperty = BooleanProperty(false)

    /** Property whether application is currently fullscreen. */
    internal val isFullScreenProperty = BooleanProperty(false)

    /** Property for the current application width. */
    internal val widthProperty =
        LimitedDoubleProperty(
            lowerBoundInclusive = 0,
            upperBoundInclusive = Double.POSITIVE_INFINITY,
            initialValue = 0)

    /** Property for the current application height. */
    internal val heightProperty =
        LimitedDoubleProperty(
            lowerBoundInclusive = 0,
            upperBoundInclusive = Double.POSITIVE_INFINITY,
            initialValue = 0)

    /** Property for the background [Visual]. */
    internal val backgroundProperty: Property<Visual> = Property(ColorVisual(Color.BLACK))
    // endregion

    // region Private attributes
    /** Current [BoardGameScene]. */
    internal var boardGameScene: BoardGameScene? = null

    /** Current [MenuScene]. */
    internal var menuScene: MenuScene? = null

    internal var loadedFonts = mutableListOf<Triple<String, String, Font.FontWeight>>()
    // endregion

    // region Internal functions
    /**
     * Shows given [MenuScene]. If [BoardGameScene] is currently displayed, it gets deactivated and
     * blurred.
     *
     * @param scene menu scene to show.
     * @param fadeTime time to fade in, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
     */
    internal fun showMenuScene(scene: MenuScene, fadeTime: Double) {
      menuScene = scene
      messageQueue.add(Action.SHOW_MENU_SCENE)
    }

    internal fun sendAnimation(animation: Animation) {
      val animationData = AnimationMapper.map(animation)
      val json = jsonMapper.encodeToString(PropData(animationData))
      //TODO: Add animation channel
      runBlocking { componentChannel.sendToAllClients(json) }

    }

    /**
     * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
     *
     * @param fadeTime time to fade out, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
     */
    internal fun hideMenuScene(fadeTime: Double) {
      menuScene = null
      messageQueue.add(Action.HIDE_MENU_SCENE)
    }

    /**
     * Shows given [BoardGameScene].
     *
     * @param scene [BoardGameScene] to show.
     */
    internal fun showGameScene(scene: BoardGameScene) {
      boardGameScene = scene
      messageQueue.add(Action.SHOW_GAME_SCENE)
    }

    /**
     * Sets [HorizontalAlignment] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newHorizontalAlignment new alignment to set.
     */
    internal fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
      TODO("Not yet implemented")
    }

    /**
     * Sets [VerticalAlignment] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newVerticalAlignment new alignment to set.
     */
    internal fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
      TODO("Not yet implemented")
    }

    /**
     * Sets [ScaleMode] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newScaleMode new scale mode to set.
     */
    internal fun setScaleMode(newScaleMode: ScaleMode) {
      TODO("Not yet implemented")
    }

    /** Manually refreshes currently displayed [Scene]s. */
    internal fun updateScene() {
      showGameScene(boardGameScene!!)
    }

    internal fun updateComponent(component: ComponentView) {
      //println("Sending update for component ${component.id}")
      messageQueue.add(Action.UPDATE_COMPONENT)
      //val json = jsonMapper.encodeToString(PropData(RecursiveMapper.map(component)))
      //runBlocking { sendToAllClients(json) }
    }

    internal fun updateVisual(visual: Visual) {
      messageQueue.add(Action.UPDATE_VISUAL)
      //val json = jsonMapper.encodeToString(PropData(VisualMapper.map(visual)))
      //runBlocking { sendToAllClients(json) }
    }

    /**
     * Shows a dialog without blocking further thread execution.
     *
     * @param dialog the [Dialog] to show
     */
    internal fun showDialogNonBlocking(dialog: Dialog): Unit = TODO("Not yet implemented")

    /**
     * Shows a dialog and blocks further thread execution.
     *
     * @param dialog the [Dialog] to show
     *
     * @return chosen button or [Optional.empty] if canceled.
     */
    internal fun showDialog(dialog: Dialog): Optional<ButtonType> {
      val dialogData = DialogMapper.map(dialog)
      applicationEngine.openNewDialog(dialogData)

      return Optional.empty()
    }

    /**
     * Shows the given [FileDialog].
     *
     * @param dialog the [FileDialog] to be shown.
     *
     * @return chosen file(s) or [Optional.empty] if canceled.
     */
    internal fun showFileDialog(dialog: FileDialog): Optional<List<File>> = TODO("Not yet implemented")

    /** Starts the application. */
    internal fun show() {
      Frontend().start()
    }

    /** Stops the application. */
    internal fun exit() {
      TODO("Not yet implemented")
    }

    fun loadFont(path : String, fontName : String, weight : Font.FontWeight): Boolean {
      if(loadedFonts.filter { it.first == path }.isEmpty()) {
        loadedFonts.add(Triple(path, fontName, weight))
        return true
      }
      return false
    }

    fun runLater(task: Runnable) {
      task.run()
    }
    // endregion
  }
}

fun Scene<*>.findComponent(id: String): ComponentView? {
  return this.components.map { it.findComponent(id) }.find { it != null }
}

fun ComponentView.getRootNode(): RootComponent<*> {
  return when(this) {
    is RootComponent<*> -> this
    else -> this.parent?.getRootNode() ?: throw IllegalStateException("Component has no root node.")
  }
}

fun ComponentView.findComponent(id: String): ComponentView? {
  if(id == this.id) return this
  return when(this) {
    is RootComponent<*> -> this.scene.components.map { it.findComponent(id) }.find { it != null }
    is CameraPane<*> ->  this.target.findComponent(id)
    is Pane<*> -> this.components.map { it.findComponent(id) }.find { it != null }
    is GridPane<*> -> this.grid.map { it.component?.findComponent(id) }.find { it != null }
    is GameComponentContainer<*> -> this.components.map { it.findComponent(id) }.find { it != null }
    else -> null
  }
}