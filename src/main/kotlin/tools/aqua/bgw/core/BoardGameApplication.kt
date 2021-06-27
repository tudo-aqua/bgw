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
 *
 * You may extend from this class in order to create your own application.
 * Scenes get shown by calling showMenuScene and showGameScene.
 * Application starts by calling show.
 */
open class BoardGameApplication {
	
	private val frontend: Frontend = Frontend()
	
	/**
	 * Shows the given FileDialog.
	 *
	 * @param dialog The FileDialog to be shown
	 *
	 * @return Chosen file(s) or Optional.empty if canceled
	 */
	fun showFileDialog(dialog: FileDialog): Optional<List<File>> = Frontend.showFileDialog(dialog)
	
	/**
	 * Shows a dialog containing the given message and buttons.
	 *
	 * @param alertType The type of the alert. Affects the displayed icon
	 * @param msg Message to be shown
	 * @param buttons Buttons to be shown
	 *
	 * @return Chosen button or Optional.empty if canceled
	 */
	fun showAlertDialog(alertType: AlertType, msg: String, vararg buttons: ButtonType): Optional<ButtonType> =
		Alert(
			alertType.toAlertType(),
			msg,
			*buttons.map { it.toButtonType() }.toTypedArray()
		).showAndWait().map { ButtonType.fromButtonType(it) }
	
	/**
	 * Shows given menu scene. If BoardGameScene is currently displayed, it gets deactivated and blurred.
	 *
	 * @param scene MenuScene to show
	 * @param fadeTime Time to fade in. Default: Frontend.DEFAULT_FADE_TIME
	 */
	fun showMenuScene(scene: MenuScene, fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
		Frontend.showMenuScene(scene, fadeTime.toDouble())
	}//TODO: Check what happens if menuScene is already shown (same or other)
	
	/**
	 * Hides currently shown menuScene. Activates BoardGameScene if present.
	 *
	 * @param fadeTime Time to fade in. Default: Frontend.DEFAULT_FADE_TIME
	 */
	fun hideMenuScene(fadeTime: Number = Frontend.DEFAULT_FADE_TIME) {
		Frontend.hideMenuScene(fadeTime.toDouble())
	}
	
	/**
	 * Shows given BoardGameScene.
	 *
	 * @param scene BoardGameScene to show
	 */
	fun showGameScene(scene: BoardGameScene) {
		Frontend.showGameScene(scene)
	}//TODO: Check what happens if menuScene is shown (new boardGameScene inactive?)
	
	/**
	 * Sets alignment of all scenes in this BoardGameApplication.
	 */
	fun setSceneAlignment(newAlignment: Alignment) {
		setHorizontalSceneAlignment(newAlignment.horizontalAlignment)
		setVerticalSceneAlignment(newAlignment.verticalAlignment)
	}
	
	/**
	 * Sets horizontal alignment of all scenes in this BoardGameApplication.
	 */
	fun setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment) {
		Frontend.setHorizontalSceneAlignment(newHorizontalAlignment)
	}
	
	/**
	 * Sets vertical alignment of all scenes in this BoardGameApplication.
	 */
	fun setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment) {
		Frontend.setVerticalSceneAlignment(newVerticalAlignment)
	}
	
	/**
	 * Sets scale mode of all scenes in this BoardGameApplication.
	 */
	fun setScaleMode(newScaleMode: ScaleMode) {
		Frontend.setScaleMode(newScaleMode)
	}
	
	/**
	 * Sets this BoardGameApplication to fullscreen mode.
	 */
	fun setFullScreen(fullscreen: Boolean) {
		Frontend.setFullScreen(fullscreen)
	}
	
	/**
	 * Manually refreshes currently displayed scenes.
	 */
	fun repaint() {
		Frontend.updateScene()
	}
	
	/**
	 * Shows the BoardGameApplication.
	 */
	fun show() {
		frontend.show()
	}
}