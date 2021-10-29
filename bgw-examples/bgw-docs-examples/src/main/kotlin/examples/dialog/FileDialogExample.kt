package examples.dialog

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.dialog.FileDialog
import tools.aqua.bgw.dialog.FileDialogMode
import tools.aqua.bgw.visual.ColorVisual

fun main() {
	FileDialogExample()
}

class FileDialogExample : BoardGameApplication("FileDialog example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)
	
	private val buttonOpenFile: Button = Button(posX = 700, posY = 500, text = "OpenFile").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showFileDialog(
				FileDialog(
					mode = FileDialogMode.OPEN_FILE,
					title = "Open file",
				)
			)
		}
	}
	private val buttonOpenFiles: Button = Button(posX = 900, posY = 500, text = "OpenFiles").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showFileDialog(
				FileDialog(
					mode = FileDialogMode.OPEN_MULTIPLE_FILES,
					title = "Open files",
				)
			)
		}
	}
	private val buttonSaveFile: Button = Button(posX = 1100, posY = 500, text = "SaveFile").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showFileDialog(
				FileDialog(
					mode = FileDialogMode.SAVE_FILE,
					title = "Save file",
				)
			)
		}
	}
	private val buttonChooseDirectory: Button = Button(posX = 900, posY = 600, text = "ChooseDir").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showFileDialog(
				FileDialog(
					mode = FileDialogMode.CHOOSE_DIRECTORY,
					title = "Choose directory",
				)
			).ifPresent { println("Chosen Directory:"); it.forEach { t -> println(t) } }
		}
	}
	
	init {
		gameScene.addComponents(buttonOpenFile, buttonOpenFiles, buttonSaveFile, buttonChooseDirectory)
		showGameScene(gameScene)
		show()
	}
}