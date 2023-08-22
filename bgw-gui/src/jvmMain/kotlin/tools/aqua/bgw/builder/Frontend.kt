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

package tools.aqua.bgw.builder

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.awt.Color
import java.awt.Toolkit
import java.io.File
import java.util.*
import javafx.animation.*
import javafx.application.Application
import javafx.application.Platform
import javafx.beans.property.SimpleDoubleProperty
import javafx.event.EventHandler
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.effect.GaussianBlur
import javafx.scene.layout.Pane
import javafx.scene.layout.StackPane
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import kotlin.math.min
import tools.aqua.bgw.builder.FXConverters.toButtonType
import tools.aqua.bgw.builder.FXConverters.toFXImage
import tools.aqua.bgw.builder.FXConverters.toFXKeyCodeCombination
import tools.aqua.bgw.builder.SceneBuilder.buildGame
import tools.aqua.bgw.builder.SceneBuilder.buildMenu
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.*
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialogMode.*
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.main.PORT
import tools.aqua.bgw.main.module
import tools.aqua.bgw.observable.properties.BooleanProperty
import tools.aqua.bgw.observable.properties.LimitedDoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.observable.properties.StringProperty
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

internal class Frontend {

  /** Starts the application. */
  internal fun start() {
    embeddedServer(Netty, port = PORT, host = "localhost", module = io.ktor.server.application.Application::module).start(wait = false)
    tools.aqua.bgw.main.Application().show()
  }

  companion object {
    /** Current scene scale. */
    internal var sceneScale: Double = 1.0

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
    private var boardGameScene: BoardGameScene? = null

    /** Current [MenuScene]. */
    private var menuScene: MenuScene? = null

    /** The game's root pane. */
    internal var gamePane: Pane? = null

    /** The menu's root pane. */
    internal var menuPane: Pane? = null
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
      TODO("Not yet implemented")
    }

    /**
     * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
     *
     * @param fadeTime time to fade out, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
     */
    internal fun hideMenuScene(fadeTime: Double) {
      TODO("Not yet implemented")
    }

    /**
     * Shows given [BoardGameScene].
     *
     * @param scene [BoardGameScene] to show.
     */
    internal fun showGameScene(scene: BoardGameScene) {
      TODO("Not yet implemented")
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
      TODO("Not yet implemented")
    }

    /**
     * Returns pane associated to scene.
     *
     * @return [gamePane] for [boardGameScene], [menuPane] for [menuScene] and `null` for other
     * parameters.
     */
    internal fun tools.aqua.bgw.core.Scene<*>.mapToPane(): Pane =
        checkNotNull(
            when (this) {
              boardGameScene -> gamePane
              menuScene -> menuPane
              else -> null
            })

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
    internal fun showDialog(dialog: Dialog): Optional<ButtonType> = TODO("Not yet implemented")

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

    fun loadFont(font: File): Boolean {
      TODO("Not yet implemented")
    }

    fun runLater(task: Runnable) {
      TODO("Not yet implemented")
    }
    // endregion
  }
}

internal val ComponentView.renderedComponent: StackPane
  get() {
    fun Parent.findStackPaneById(id: String): StackPane? {
      val stackPane = childrenUnmodifiable.find { it.id == id } as? StackPane
      if (stackPane != null) return stackPane
      childrenUnmodifiable.forEach {
        if (it is Parent) {
          val parent = it.findStackPaneById(id)
          if (parent != null) return parent
        }
      }
      return null
    }
    return Frontend.gamePane?.findStackPaneById(id)
      ?: Frontend.menuPane?.findStackPaneById(id)
      ?: throw IllegalStateException("ComponentView $this is not rendered.")
  }
