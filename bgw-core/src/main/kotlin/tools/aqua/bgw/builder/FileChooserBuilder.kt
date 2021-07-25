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

import javafx.stage.DirectoryChooser
import javafx.stage.FileChooser
import tools.aqua.bgw.dialog.FileDialog

/**
 * FileChooserBuilder.
 * Factory for all BGW file choosers.
 */
internal class FileChooserBuilder {
	companion object {
		/**
		 * Builds FileChoosers.
		 */
		internal fun buildFileChooser(chooser: FileDialog): FileChooser =
			FileChooser().apply {
				if (chooser.title.isNotBlank())
					title = chooser.title
				
				if (chooser.initialFileName.isNotBlank())
					initialFileName = chooser.initialFileName
				
				if (chooser.initialDirectory != null)
					initialDirectory = chooser.initialDirectory
				
				if (chooser.extensionFilters.isNotEmpty()) {
					extensionFilters.addAll(chooser.extensionFilters.map {
						FileChooser.ExtensionFilter(
							it.description,
							it.extensions
						)
					})
				}
			}
		
		/**
		 * Builds DirectoryChoosers.
		 */
		internal fun buildDirectoryChooser(chooser: FileDialog): DirectoryChooser =
			DirectoryChooser().apply {
				if (chooser.title.isNotBlank())
					title = chooser.title
				
				if (chooser.initialDirectory != null)
					initialDirectory = chooser.initialDirectory
			}
	}
}