package examples.dialog

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.visual.ColorVisual

fun main() {
	DialogExample()
}

class DialogExample : BoardGameApplication("Dialog example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)
	
	private val buttonInformation: Button = Button(posX = 500, posY = 500, text = "INFORMATION").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					dialogType = DialogType.INFORMATION,
					title = "Information Dialog",
					header = "Information",
					message = "An information Dialog."
				)
			)
		}
	}
	private val buttonWarning: Button = Button(posX = 700, posY = 500, text = "WARNING").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					dialogType = DialogType.WARNING,
					title = "Warning",
					header = "Empty player name",
					message = "Player name must not be empty!"
				)
			)
		}
	}
	private val buttonError: Button = Button(posX = 900, posY = 500, text = "ERROR").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					dialogType = DialogType.ERROR,
					title = "Error Dialog",
					header = "Error",
					message = "An error Dialog."
				)
			)
		}
	}
	private val buttonConfirmation: Button = Button(posX = 1100, posY = 500, text = "CONFIRMATION").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					dialogType = DialogType.CONFIRMATION,
					title = "Confirmation Dialog",
					header = "Confirmation",
					message = "A confirmation Dialog."
				)
			)
		}
	}
	private val buttonNone: Button = Button(posX = 1300, posY = 500, text = "NONE").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					dialogType = DialogType.NONE,
					title = "Dialog",
					header = "Dialog",
					message = "A dialog."
				)
			)
		}
	}
	private val buttonException: Button = Button(posX = 900, posY = 600, text = "EXCEPTION").apply {
		visual = ColorVisual.WHITE
		onMouseClicked = {
			showDialog(
				Dialog(
					title = "Exception Dialog",
					header = "Exception",
					message = "An exception Dialog.",
					exception = IllegalArgumentException("IllegalArgument passed.")
				)
			)
		}
	}
	
	init {
		gameScene.addComponents(
			buttonInformation, buttonWarning, buttonError,
			buttonConfirmation, buttonNone, buttonException
		)
		showGameScene(gameScene)
		show()
	}
}