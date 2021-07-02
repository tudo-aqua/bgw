@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package tools.aqua.bgw.core

import javafx.scene.control.Alert
import tools.aqua.bgw.builder.Frontend
import tools.aqua.bgw.dialog.AlertType
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.visual.Visual
import java.io.File
import java.util.*

/**
 * Baseclass for all BGW Applications.
 * Extends from this class in order to create your own game application.
 *
 * [Scene]s get shown by calling [showMenuScene] and [showGameScene].
 * Application starts by calling [show].
 *
 * @param windowTitle Window title displayed in the title bar.
 *
 * @see BoardGameScene
 * @see MenuScene
 */
open class BoardGameApplication(windowTitle: String = "BoardGameWork Application") {
	
	/**
	 * [Frontend] instance.
	 */
	private val frontend: Frontend = Frontend()
	
	/**
	 * Window title displayed in the title bar.
	 */
	var title: String
		get() = Frontend.titleProperty.value
		set(value) {
			Frontend.titleProperty.value = value
		}
	
	/**
	 * Background [Visual] for the [BoardGameApplication]. [Visual] appears as bars window does not match [Scene] ratio.
	 * Do not mix up this [Property] with the [Scene] background [Visual] [Scene.background].
	 */
	var background: Visual
		get() = Frontend.backgroundProperty.value
		set(value) {
			Frontend.backgroundProperty.value = value
		}
	
	init {
		title = windowTitle
	}
	
	/**
	 * Shows the given [FileDialog].
	 *
	 * @param dialog the [FileDialog] to be shown.
	 *
	 * @return chosen file(s) or [Optional.empty] if canceled.
	 */
	fun showFileDialog(dialog: FileDialog): Optional<List<File>> = Frontend.showFileDialog(dialog)
	
	/**
	 * Shows a dialog containing the given [message] and [buttons].
	 *
	 * @param alertType the [AlertType] of the alert. Affects the displayed icon.
	 * @param message message to be shown.
	 * @param buttons buttons to be shown.
	 *
	 * @return chosen button or [Optional.empty] if canceled.
	 */
	fun showAlertDialog(alertType: AlertType, message: String, vararg buttons: ButtonType): Optional<ButtonType> =
		Alert(
			alertType.toAlertType(),
			message,
			*buttons.map { it.toButtonType() }.toTypedArray()
		).showAndWait().map { ButtonType.fromButtonType(it) }
	
	/**
	 * Shows given [MenuScene]. If [BoardGameScene] is currently displayed, it gets deactivated and blurred.
	 *
	 * @param scene menu scene to show.
	 * @param fadeTime time to fade in, specified in seconds. Default: [Frontend.DEFAULT_FADE_TIME].
	 */
	fun showMenuScene(scene: MenuScene, fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
		Frontend.showMenuScene(scene, fadeTime.toDouble())
	}
	
	/**
	 * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
	 *
	 * @param fadeTime time to fade out, specified in seconds. Default: [Frontend.DEFAULT_FADE_TIME].
	 */
	fun hideMenuScene(fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
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
	 */
	fun setSceneAlignment(newAlignment: Alignment) {
		setHorizontalSceneAlignment(newAlignment.horizontalAlignment)
		setVerticalSceneAlignment(newAlignment.verticalAlignment)
	}
	
	/**
	 * Sets [HorizontalAlignment] of all [Scene]s in this [BoardGameApplication].
	 */
	fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
		Frontend.setHorizontalSceneAlignment(newHorizontalAlignment)
	}
	
	/**
	 * Sets [VerticalAlignment] of all [Scene]s in this [BoardGameApplication].
	 */
	fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
		Frontend.setVerticalSceneAlignment(newVerticalAlignment)
	}
	
	/**
	 * Sets [ScaleMode] of all [Scene]s in this [BoardGameApplication].
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
		frontend.show()
	}
}