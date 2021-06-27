@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package tools.aqua.bgw.core

import javafx.scene.control.Alert
import tools.aqua.bgw.builder.Frontend
import tools.aqua.bgw.dialog.AlertType
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.FileDialog
import java.io.File
import java.util.*

/**
 * Superclass for all BGW Applications.
 * Extends from this class in order to create your own game application.
 *
 * [Scene]s get shown by calling [showMenuScene] and [showGameScene].
 * Application starts by calling [show].
 *
 * @see BoardGameScene
 * @see MenuScene
 */
open class BoardGameApplication {
	
	/**
	 * Frontend instance.
	 */
	private val frontend: Frontend = Frontend()
	
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
	 * @param fadeTime time to fade in. Default: [Frontend.DEFAULT_FADE_TIME].
	 */
	fun showMenuScene(scene: MenuScene, fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
		Frontend.showMenuScene(scene, fadeTime.toDouble())
	}
	
	/**
	 * Hides currently shown [MenuScene]. Activates [BoardGameScene] if present.
	 *
	 * @param fadeTime time to fade in. Default: [Frontend.DEFAULT_FADE_TIME].
	 */
	fun hideMenuScene(fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
		Frontend.hideMenuScene(fadeTime.toDouble())
	}
	
	/**
	 * Shows given [BoardGameScene].
	 *
	 * @param scene game scene to show.
	 */
	fun showGameScene(scene: BoardGameScene) {
		Frontend.showGameScene(scene)
	}
	
	/**
	 * Sets alignment of all [Scene]s in this [BoardGameApplication].
	 */
	fun setSceneAlignment(newAlignment: Alignment) {
		setHorizontalSceneAlignment(newAlignment.horizontalAlignment)
		setVerticalSceneAlignment(newAlignment.verticalAlignment)
	}
	
	/**
	 * Sets horizontal alignment of all [Scene]s in this [BoardGameApplication].
	 */
	fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
		Frontend.setHorizontalSceneAlignment(newHorizontalAlignment)
	}
	
	/**
	 * Sets vertical alignment of all [Scene]s in this [BoardGameApplication].
	 */
	fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
		Frontend.setVerticalSceneAlignment(newVerticalAlignment)
	}
	
	/**
	 * Sets scale mode of all [Scene]s in this [BoardGameApplication].
	 */
	fun setScaleMode(newScaleMode: ScaleMode) {
		Frontend.setScaleMode(newScaleMode)
	}
	
	/**
	 * Sets this [BoardGameApplication] to fullscreen mode.
	 */
	fun setFullScreen(fullscreen: Boolean) {
		Frontend.setFullScreen(fullscreen)
	}
	
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