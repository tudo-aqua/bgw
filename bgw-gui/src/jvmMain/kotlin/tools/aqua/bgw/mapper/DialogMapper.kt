/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.mapper

import ButtonTypeData
import DialogData
import FileDialogData
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.FileDialog

internal object DialogMapper {
  fun map(dialog: Dialog): DialogData {
    return DialogData().apply {
      id = dialog.id
      dialogType = dialog.dialogType
      title = dialog.title
      header = dialog.header
      message = dialog.message
      exception = dialog.exception.stackTraceToString()
      buttons =
          dialog.buttonTypes.map {
            ButtonTypeData().apply {
              text = it.text
              backgroundColor = it.bg
              foregroundColor = it.fg
            }
          }
    }
  }

  fun map(fileDialog: FileDialog): FileDialogData {
    return FileDialogData().apply {
      id = fileDialog.id
      mode = fileDialog.mode.toString().lowercase()
      title = fileDialog.title
      initialFileName = fileDialog.initialFileName
      initialDirectoryPath = fileDialog.initialDirectoryPath
      extensionFilters = fileDialog.extensionFilters.map { it.description to it.extensions }
    }
  }
}
