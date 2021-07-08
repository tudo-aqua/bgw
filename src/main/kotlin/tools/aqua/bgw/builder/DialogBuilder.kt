package tools.aqua.bgw.builder

import javafx.scene.control.Alert
import javafx.scene.control.TextArea
import tools.aqua.bgw.dialog.AlertType
import tools.aqua.bgw.dialog.Dialog


class DialogBuilder {
	/*
	 * Alert(
	alertType.toAlertType(),
	message,
	 *buttons.map { it.toFXButtonType() }.toTypedArray()
	).showAndWait().map { it.toButtonType() }
	 */
	companion object {
		internal fun build(dialog: Dialog): Alert {
			val alert = Alert(dialog.alertType.toFXAlertType())
			alert.title = dialog.title
			alert.headerText = dialog.header
			alert.contentText = dialog.message
			
			if (dialog.buttons.isNotEmpty()) {
				alert.buttonTypes.clear()
				alert.buttonTypes.addAll(dialog.buttons.map { it.toFXButtonType() })
			}
			
			if (dialog.alertType == AlertType.EXCEPTION) {
				alert.dialogPane.expandableContent = TextArea(dialog.exception!!.stackTraceToString()).apply {
					isEditable = false
					isWrapText = true
					maxWidth = Double.MAX_VALUE
					maxHeight = Double.MAX_VALUE
				}
			}
			
			return alert
		}
	}
}