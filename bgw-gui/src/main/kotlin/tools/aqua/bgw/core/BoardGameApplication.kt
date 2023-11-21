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

@file:Suppress("MemberVisibilityCanBePrivate", "unused", "TooManyFunctions")

package tools.aqua.bgw.core

import java.io.File
import java.io.InputStream
import java.util.*
import javafx.application.Platform
import javafx.scene.text.Font as JFXFont
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.builder.Frontend
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all BGW Applications. Extend from this class in order to create your own game
 * application. You may only instantiate one application.
 *
 * [Scene]s get shown by calling [showMenuScene] and [showGameScene]. Application starts by calling
 * [show].
 *
 * @constructor Creates the BoardGameApplication with optional title and aspect ratio. May only be
 * called once per execution.
 *
 * @param windowTitle Title for the application window. Gets displayed in the title bar. Default:
 * [DEFAULT_WINDOW_TITLE].
 *
 * @param aspectRatio Initial aspect ratio of application window. Default: empty [AspectRatio]
 * constructor.
 *
 * @param windowMode Initial window mode. Overrides [isMaximized] and [isFullScreen] if passed.
 * Refer to [WindowMode] docs for further information about the effects.
 *
 * @see BoardGameScene
 * @see MenuScene
 */
@Suppress("LeakingThis")
open class BoardGameApplication(
    windowTitle: String = DEFAULT_WINDOW_TITLE,
    aspectRatio: AspectRatio = AspectRatio(),
    windowMode: WindowMode? = null,
) {

  /** Window title displayed in the title bar. */
  var title: String
    get() = Frontend.titleProperty.value
    set(value) {
      Frontend.titleProperty.value = value
    }

  /** Window icon displayed in the title and task bar. */
  var icon: ImageVisual?
    get() = Frontend.iconProperty.value
    set(value) {
      Frontend.iconProperty.value = value
    }

  /**
   * Specifies the KeyCombination that will allow the user to exit full screen mode. A value of
   * 'null' will not match any KeyEvent and will make it so the user is not able to escape from
   * fullscreen mode.
   *
   * The 'character' field in the [KeyEvent] is unused.
   *
   * @see isFullScreen
   * @see fullscreenExitCombinationHint
   */
  var fullscreenExitCombination: KeyEvent?
    get() = Frontend.fullscreenExitCombinationProperty.value
    set(value) {
      Frontend.fullscreenExitCombinationProperty.value = value
    }

  /**
   * Specifies the KeyCombination hint that will be shown upon entering fullscreen mode. Hint will
   * only be shown if an exit combination other than 'null' has been set.
   *
   * A value of 'null' will result in the default text being shown. An empty string will result in
   * no text being shown despite [fullscreenExitCombination] being set.
   *
   * @see isFullScreen
   * @see fullscreenExitCombination
   */
  var fullscreenExitCombinationHint: String?
    get() = Frontend.fullscreenExitCombinationHintProperty.value
    set(value) {
      Frontend.fullscreenExitCombinationHintProperty.value = value
    }

  /**
   * Sets this [BoardGameApplication]'s preferred width. Only affects non-maximized, non-fullscreen
   * windows.
   */
  var windowWidth: Number
    get() = Frontend.widthProperty.value
    set(value) {
      Frontend.widthProperty.value = value.toDouble()
    }

  /**
   * Sets this [BoardGameApplication]'s preferred height. Only affects non-maximized, non-fullscreen
   * windows.
   */
  var windowHeight: Number
    get() = Frontend.heightProperty.value
    set(value) {
      Frontend.heightProperty.value = value.toDouble()
    }

  /**
   * Sets this [BoardGameApplication]'s maximized mode.
   *
   * `true` for maximized mode, `false` for default window size.
   */
  var isMaximized: Boolean
    get() = Frontend.isMaximizedProperty.value
    set(value) {
      Frontend.isMaximizedProperty.value = value
    }

  /**
   * Sets this [BoardGameApplication]'s fullscreen mode. `true` for fullscreen mode, `false` for
   * default window.
   */
  var isFullScreen: Boolean
    get() = Frontend.isFullScreenProperty.value
    set(value) {
      Frontend.isFullScreenProperty.value = value
    }

  /**
   * Background [Visual] for the [BoardGameApplication].
   *
   * It is visible in the space that appears if the application window ratio does not fit the
   * [Scene] ratio.
   *
   * Do not mix up this [Property] with the [Scene] background [Visual] [Scene.background].
   */
  var background: Visual
    get() = Frontend.backgroundProperty.value
    set(value) {
      Frontend.backgroundProperty.value = value
    }

  /**
   * Gets invoked when the application was started and the window was shown.
   *
   * @see onWindowClosed
   */
  var onWindowShown: (() -> Unit)? = null

  /**
   * Gets invoked after the application window was closed.
   *
   * @see onWindowShown
   */
  var onWindowClosed: (() -> Unit)? = null

  init {
    synchronized(this) {
      check(!isInstantiated) { "Unable to create second application." }
      isInstantiated = true
    }

    title = windowTitle

    Frontend.application = this
    Frontend.initialAspectRatio = aspectRatio
    Frontend.initialWindowMode = windowMode
  }

  /**
   * Creates the BoardGameApplication with optional title and dimension. May only be called once per
   * execution.
   *
   * @param windowTitle Title for the application window. Gets displayed in the title bar. Default:
   * [DEFAULT_WINDOW_TITLE].
   *
   * @param width Initial window width. Default: [DEFAULT_WINDOW_WIDTH].
   *
   * @param height Initial window height. Default: [DEFAULT_WINDOW_HEIGHT].
   *
   * @param windowMode Initial window mode. Overrides [isMaximized] and [isFullScreen] if passed.
   * Refer to [WindowMode] docs for further information about the effects.
   */
  constructor(
      windowTitle: String = DEFAULT_WINDOW_TITLE,
      width: Number = DEFAULT_WINDOW_WIDTH,
      height: Number = DEFAULT_WINDOW_HEIGHT,
      windowMode: WindowMode? = null,
  ) : this(
      windowTitle = windowTitle,
      aspectRatio = AspectRatio.of(width = width, height = height),
      windowMode = windowMode) {
    windowHeight = height
    windowWidth = width
  }

  /**
   * Shows a dialog without blocking further thread execution.
   *
   * @param dialog The [Dialog] to show.
   */
  fun showDialogNonBlocking(dialog: Dialog): Unit = Frontend.showDialogNonBlocking(dialog)

  /**
   * Shows a dialog and blocks further thread execution.
   *
   * @param dialog The [Dialog] to show.
   *
   * @return Chosen button or [Optional.empty] if canceled.
   */
  fun showDialog(dialog: Dialog): Optional<ButtonType> = Frontend.showDialog(dialog)

  /**
   * Shows the given [FileDialog].
   *
   * @param dialog The [FileDialog] to be shown.
   *
   * @return Chosen file(s) or [Optional.empty] if canceled.
   */
  fun showFileDialog(dialog: FileDialog): Optional<List<File>> = Frontend.showFileDialog(dialog)

  /**
   * Shows given [MenuScene]. If [BoardGameScene] is currently displayed, it gets deactivated and
   * blurred.
   *
   * @param scene [MenuScene] to show.
   * @param fadeTime Time to fade in, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
   */
  fun showMenuScene(scene: MenuScene, fadeTime: Number = DEFAULT_FADE_TIME) {
    Frontend.showMenuScene(scene, fadeTime.toDouble())
  }

  /**
   * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
   *
   * @param fadeTime Time to fade out in milliseconds. Default: [DEFAULT_FADE_TIME].
   */
  fun hideMenuScene(fadeTime: Number = DEFAULT_FADE_TIME) {
    Frontend.hideMenuScene(fadeTime.toDouble())
  }

  /**
   * Shows given [BoardGameScene].
   *
   * @param scene [BoardGameScene] to show.
   */
  fun showGameScene(scene: BoardGameScene) {
    Frontend.showGameScene(scene)
  }

  /**
   * Sets [Alignment] of all [Scene]s in this [BoardGameApplication].
   *
   * @param newAlignment New alignment to set.
   */
  fun setSceneAlignment(newAlignment: Alignment) {
    setHorizontalSceneAlignment(newAlignment.horizontalAlignment)
    setVerticalSceneAlignment(newAlignment.verticalAlignment)
  }

  /**
   * Sets [HorizontalAlignment] of all [Scene]s in this [BoardGameApplication].
   *
   * @param newHorizontalAlignment New alignment to set.
   */
  fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
    Frontend.setHorizontalSceneAlignment(newHorizontalAlignment)
  }

  /**
   * Sets [VerticalAlignment] of all [Scene]s in this [BoardGameApplication].
   *
   * @param newVerticalAlignment New alignment to set.
   */
  fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
    Frontend.setVerticalSceneAlignment(newVerticalAlignment)
  }

  /**
   * Sets [ScaleMode] of all [Scene]s in this [BoardGameApplication].
   *
   * @param newScaleMode New scale mode to set.
   */
  fun setScaleMode(newScaleMode: ScaleMode) {
    Frontend.setScaleMode(newScaleMode)
  }

  /** Manually refreshes currently displayed [Scene]s. */
  fun repaint() {
    Frontend.updateScene()
  }

  /** Shows the [BoardGameApplication]. */
  fun show() {
    Frontend.show()
  }

  /** Returns the [show] function, thus closing the application window. */
  fun exit() {
    Frontend.exit()
  }

  companion object {
    /** Static holder for instantiation of [BoardGameApplication]. */
    private var isInstantiated: Boolean = false

    /**
     * Executes given [task] on the UI thread. Use this method to update properties of
     * [ComponentView]s from asynchronous environments like [Animation.onFinished] events. If no
     * Application has yet been started, the [task] is executed on the calling Thread. This function
     * is Thread safe.
     */
    fun runOnGUIThread(task: Runnable) {
      val toolkitInitialized: Boolean =
          try {
            Platform.runLater {}
            true
          } catch (e: IllegalStateException) {
            false
          }

      if (toolkitInitialized) {
        Platform.runLater(task)
      } else {
        synchronized(this) { task.run() }
      }
    }

    /**
     * Loads a font file and registers it in the JFX graphics system.
     * @param font The font file off type .ttf which is to be loaded
     * @throws NoSuchFileException if the file doesn't exist
     * @throws AccessDeniedException if the file can't be read
     * @return A boolean weather the file could be loaded or not
     */
    fun loadFont(font: File): Boolean {
      if (!font.exists()) throw NoSuchFileException(font)
      if (!font.canRead()) throw AccessDeniedException(font)
      val jfxFont = JFXFont.loadFont(font.inputStream(), DEFAULT_FONT_SIZE) ?: return false
      return JFXFont.getFamilies().contains(jfxFont.family)
    }

    /**
     * Loads a font file input stream and registers it in the JFX graphics system.
     * @param inputStream The font inputStream which is to be loaded
     * @throws NoSuchFileException if the file doesn't exist
     * @throws AccessDeniedException if the file can't be read
     * @return A boolean weather the file could be loaded or not
     */
    fun loadFont(inputStream: InputStream): Boolean {
      val jfxFont = JFXFont.loadFont(inputStream, DEFAULT_FONT_SIZE) ?: return false
      return JFXFont.getFamilies().contains(jfxFont.family)
    }

    /**
     * Loads a font file and registers it in the JFX graphics system.
     * @param path The font file path in the resources folder which is to be loaded
     * @throws NoSuchFileException if the file doesn't exist
     * @throws AccessDeniedException if the file can't be read
     * @return A boolean weather the file could be loaded or not
     */
    fun loadFont(path: String): Boolean {
      this::class.java.classLoader.getResourceAsStream(path)?.let {
        return loadFont(it)
      }
          ?: throw NoSuchFileException(File(path))
    }
  }
}
