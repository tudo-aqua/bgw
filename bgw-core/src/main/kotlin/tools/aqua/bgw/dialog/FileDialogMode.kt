package tools.aqua.bgw.dialog

/**
 * Enum for possible [FileDialog] modes.
 */
enum class FileDialogMode {
	/**
	 * Mode to open one file.
	 */
	OPEN_FILE,
	
	/**
	 * Mode to open multiple files.
	 */
	OPEN_MULTIPLE_FILES,
	
	/**
	 * Mode to save a file.
	 */
	SAVE_FILE,
	
	/**
	 * Mode so select a directory.
	 */
	CHOOSE_DIRECTORY
}