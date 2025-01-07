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

import tools.aqua.bgw.io.File

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
data class FileDialog(
    val mode: FileDialogMode,
    val title: String = "",
    val initialFileName: String = "",
    val initialDirectoryPath: File? = null,
    val extensionFilters: List<ExtensionFilter> = emptyList()
)
