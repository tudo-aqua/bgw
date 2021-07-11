@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.builder.Frontend
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.visual.Visual
import java.io.File
import java.util.*

/**
 * Baseclass for all BGW Applications.
 * Extend from this class in order to create your own game application.
 * You may only instantiate one application.
 *
 * [Scene]s get shown by calling [showMenuScene] and [showGameScene].
 * Application starts by calling [show].
 *
 * @param windowTitle title for the application window. Gets displayed in the title bar.
 *      Default: "BoardGameWork Application".
 *
 * @see BoardGameScene
 * @see MenuScene
 */
open class BoardGameApplication(windowTitle: String = "BoardGameWork Application") {
    
    init {
        check(!instantiated) { "Unable to create second application." }
        instantiated = true
        Frontend.application = this
    }
    
    /**
     * Window title displayed in the title bar.
     */
    var title: String
        get() = Frontend.titleProperty.value
        set(value) {
            Frontend.titleProperty.value = value
        }
    
    /**
     * Background [Visual] for the [BoardGameApplication].
     * It is visible in the space that appears if the application window ratio does not fit the [Scene] ratio.
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
        title = windowTitle
    }
    
    /**
     * Shows a dialog and blocks further thread execution.
     *
     * @param dialog the [Dialog] to show
     *
     * @return chosen button or [Optional.empty] if canceled.
     */
    fun showDialog(dialog: Dialog): Optional<ButtonType> =
        Frontend.showDialog(dialog)
    
    /**
     * Shows the given [FileDialog].
     *
     * @param dialog the [FileDialog] to be shown.
     *
     * @return chosen file(s) or [Optional.empty] if canceled.
     */
    fun showFileDialog(dialog: FileDialog): Optional<List<File>> = Frontend.showFileDialog(dialog)
    
    /**
     * Shows given [MenuScene]. If [BoardGameScene] is currently displayed, it gets deactivated and blurred.
     *
     * @param scene menu scene to show.
     * @param fadeTime time to fade in, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
     */
    fun showMenuScene(scene: MenuScene, fadeTime: Number = DEFAULT_FADE_TIME) {
        Frontend.showMenuScene(scene, fadeTime.toDouble())
    }
    
    /**
     * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
     *
     * @param fadeTime time to fade out, specified in milliseconds. Default: [DEFAULT_FADE_TIME].
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
     * @param newAlignment new alignment to set.
     */
    fun setSceneAlignment(newAlignment: Alignment) {
        setHorizontalSceneAlignment(newAlignment.horizontalAlignment)
        setVerticalSceneAlignment(newAlignment.verticalAlignment)
    }

    /**
     * Sets [HorizontalAlignment] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newHorizontalAlignment new alignment to set.
     */
    fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
        Frontend.setHorizontalSceneAlignment(newHorizontalAlignment)
    }

    /**
     * Sets [VerticalAlignment] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newVerticalAlignment new alignment to set.
     */
    fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
        Frontend.setVerticalSceneAlignment(newVerticalAlignment)
    }

    /**
     * Sets [ScaleMode] of all [Scene]s in this [BoardGameApplication].
     *
     * @param newScaleMode new scale mode to set.
     */
    fun setScaleMode(newScaleMode: ScaleMode) {
        Frontend.setScaleMode(newScaleMode)
    }

//	/**
//	 * Sets this [BoardGameApplication] to fullscreen mode.
//	 */
//	fun setFullScreen(fullscreen: Boolean) {
//		Frontend.setFullScreen(fullscreen)
//	}

    /**
     * Manually refreshes currently displayed [Scene]s.
     */
    fun repaint() {
        Frontend.updateScene()
    }

    /**
     * Shows the [BoardGameApplication].
     */
    fun show() {
        Frontend.show()
    }
    
    /**
     * Returns the [show] function, thus closing the application window.
     */
    fun exit() {
        Frontend.exit()
    }
    
    companion object {
        /**
         * Static holder for instantiation of BoardGameApplication.
         */
        private var instantiated: Boolean = false
    
        /**
         * The default fade time for [MenuScene]s in [showMenuScene] or [hideMenuScene].
         */
        internal const val DEFAULT_FADE_TIME = 250
    }
}