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