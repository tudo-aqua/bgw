/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.dialog

/**
 * A [FileDialog] such as a file chooser or save dialog, depending on [FileDialogMode].
 *
 * @constructor Creates a [FileDialog] with given [FileDialogMode].
 *
 * @property mode The [Dialog]'s mode.
 * @property title The [Dialog]'s title text.
 * @property initialFileName The initial file name in the file name text box.
 * @property initialDirectory The initial directory where to open the file chooser.
 * @property extensionFilters Extensions filters for this file chooser.
 *
 * @see FileDialogMode
 * @see ExtensionFilter
 */
class FileDialog(
    val mode: FileDialogMode,
    val title: String = "",
    val initialFileName: String = "",
    val initialDirectoryPath: String? = null,
    val extensionFilters: List<ExtensionFilter> = emptyList()
) {
  internal val id = IDGenerator.generateDialogID()

  /**
   * Gets invoked whenever this [FileDialog] is used to select file(s). It always returns a list of
   * selected file paths.
   *
   * If [mode] is [FileDialogMode.OPEN_FILE], [FileDialogMode.SAVE_FILE] or
   * [FileDialogMode.CHOOSE_DIRECTORY] it will always contain exactly one element.
   *
   * If [mode] is [FileDialogMode.OPEN_MULTIPLE_FILES] it will contain multiple elements.
   *
   * @see FileDialogMode
   *
   * @since 0.10
   */
  var onPathsSelected: ((List<String>) -> Unit)? = null

  /**
   * Gets invoked whenever this [FileDialog] is closed without selecting a path or the selection was
   * cancelled.
   *
   * @since 0.10
   */
  var onSelectionCancelled: (() -> Unit)? = null
}
