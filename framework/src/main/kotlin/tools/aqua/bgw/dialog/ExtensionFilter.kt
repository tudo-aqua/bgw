@file:Suppress("unused")

package tools.aqua.bgw.dialog

/**
 * Extension filters for [FileDialog]s.
 * Maps a file type description to it's extensions.
 * To generate e.g. "Image Files (*.png, *.jpg)" set [description] = "Image files" and [extensions] = ("png", "jpg").
 *
 * @param description file type description.
 * @param extensions file extensions.
 */
class ExtensionFilter(val description: String, vararg extensions: String) {
	
	/**
	 * File extensions.
	 */
	internal val extensions = extensions.toList()
}