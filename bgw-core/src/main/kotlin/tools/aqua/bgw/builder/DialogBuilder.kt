/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.aqua.bgw.builder

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import tools.aqua.bgw.builder.FXConverters.Companion.toFXAlertType
import tools.aqua.bgw.builder.FXConverters.Companion.toFXButtonType
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType

/**
 * DialogBuilder.
 * Factory for all BGW dialogs.
 */
internal class DialogBuilder {
	companion object {
		/**
		 * Builds dialogs.
		 */
		internal fun build(dialog: Dialog): Alert = Alert(dialog.dialogType.toFXAlertType()).apply {
			title = dialog.title
			headerText = dialog.header
			contentText = dialog.message
			
			//If user specified buttons, clear defaults and set custom
			if (dialog.buttons.isNotEmpty()) {
				buttonTypes.clear()
				buttonTypes.addAll(dialog.buttons.map { it.toFXButtonType() })
				buttonTypes.clear()
			} else if (dialog.dialogType == DialogType.CONFIRMATION) {
				buttonTypes.clear()
				buttonTypes.addAll(ButtonType.YES, ButtonType.NO)
			} else if (dialog.dialogType == DialogType.NONE) {
				buttonTypes.add(ButtonType.OK)
			}
			
			//Add expandable content for exception stack trace in case of AlertType.EXCEPTION
			if (dialog.dialogType == DialogType.EXCEPTION) {
				dialogPane.expandableContent = TextArea(dialog.exception.stackTraceToString()).apply {
					isEditable = false
					isWrapText = true
					maxWidth = Double.MAX_VALUE
					maxHeight = Double.MAX_VALUE
				}
				buttonTypes.add(ButtonType.OK)
			}
		}
	}
}