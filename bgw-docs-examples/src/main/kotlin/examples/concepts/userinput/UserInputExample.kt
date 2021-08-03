package examples.concepts.userinput

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.reposition
import tools.aqua.bgw.components.resize
import tools.aqua.bgw.components.rotate
import tools.aqua.bgw.components.scale
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
	UserInputExample()
}

class UserInputExample: BoardGameApplication("User input example") {

	val button : Button = Button(height = 150, width = 300, posX = 30, posY = 30).apply {
		visual = ColorVisual.GREEN
	}

	val token : TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)

	val gameScene : BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

	init {
		button.onMouseClicked = {
			button.label = "someone clicked on me!"
		}
		button.onMousePressed = { mouseEvent ->
			button.label = "pressed ${mouseEvent.button}"
		}
		button.onMouseReleased = { mouseEvent ->
			button.label = "released ${mouseEvent.button}"
		}
		button.onMouseEntered = {
			button.visual = ColorVisual.MAGENTA
		}
		button.onMouseExited = {
			button.visual = ColorVisual.GREEN
		}
		button.onKeyPressed = { keyEvent ->
			button.label = "pressed key: ${keyEvent.keyCode}"
		}
		button.onKeyReleased = { keyEvent ->
			button.label = "released key: ${keyEvent.keyCode}"
		}
		button.onKeyTyped = { keyEvent ->
			button.label = "typed key: ${keyEvent.character}"
		}
		button.dropAcceptor = { true }
		button.onDragDropped = {
			it.draggedComponent.reposition(500,30)
			it.draggedComponent.rotation = 0.0
			gameScene.addComponents(token)
		}
		button.onDragGestureEntered = { dragEvent ->
			button.visual = dragEvent.draggedComponent.visual
		}
		button.onDragGestureExited = {
			button.visual = ColorVisual.GREEN
		}


		token.isDraggable = true

		token.onDragGestureMoved = { token.rotate(5) }
		token.onDragGestureStarted = { token.scale(1.2) }
		token.onDragGestureEnded = { dropEvent, success ->
			if (success) token.resize(50,50)
		}

		showGameScene(gameScene.apply { addComponents(button, token) })
		show()
	}
}