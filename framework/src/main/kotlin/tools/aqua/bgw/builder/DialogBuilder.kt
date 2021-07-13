package tools.aqua.bgw.builder

import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import tools.aqua.bgw.builder.FXConverters.Companion.toFXAlertType
import tools.aqua.bgw.builder.FXConverters.Companion.toFXButtonType
import tools.aqua.bgw.dialog.AlertType
import tools.aqua.bgw.dialog.Dialog

/**
 * DialogBuilder.
 * Factory for all BGW dialogs.
 */
class DialogBuilder {
	companion object {
		/**
		 * Builds dialogs.
		 */
		internal fun build(dialog: Dialog): Alert = Alert(dialog.alertType.toFXAlertType()).apply {
			title = dialog.title
			headerText = dialog.header
			contentText = dialog.message
			
			//If user specified buttons, clear defaults and set custom
			if (dialog.buttons.isNotEmpty()) {
				buttonTypes.clear()
				buttonTypes.addAll(dialog.buttons.map { it.toFXButtonType() })
			}
			
			//Add expandable content for exception stack trace in case of AlertType.EXCEPTION
			if (dialog.alertType == AlertType.EXCEPTION) {
				dialogPane.expandableContent = TextArea(dialog.exception!!.stackTraceToString()).apply {
					isEditable = false
					isWrapText = true
					maxWidth = Double.MAX_VALUE
					maxHeight = Double.MAX_VALUE
				}
			}
		}
	}
}