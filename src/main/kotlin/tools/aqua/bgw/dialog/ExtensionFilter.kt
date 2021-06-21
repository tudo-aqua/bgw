package tools.aqua.bgw.dialog

/**
 * Extension filters for FileChoosers.
 * Maps a file type description to it's extensions.
 *
 * @param description File type description
 * @param extensions File extensions
 */
class ExtensionFilter(val description: String, vararg extensions: String) {
	
	/**
	 * File extensions.
	 */
	internal val extensions = extensions.toList()
}