package examples.concepts.userinput

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.reposition
import tools.aqua.bgw.components.resize
import tools.aqua.bgw.components.rotate
import tools.aqua.bgw.components.scale
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.MouseEvent
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

	private fun handleMouseClicked(mouseEvent: MouseEvent) {
		button.text = "someone clicked on me!"
	}

	init {
		//handling user input on ComponentView

		button.onMouseClicked = this::handleMouseClicked

		button.onMousePressed = { mouseEvent ->
			button.text = "pressed ${mouseEvent.button}"
		}
		button.onMouseReleased = { mouseEvent ->
			button.text = "released ${mouseEvent.button}"
		}
		button.onMouseEntered = {
			button.visual = ColorVisual.MAGENTA
		}
		button.onMouseExited = {
			button.visual = ColorVisual.GREEN
		}
		button.onKeyPressed = { keyEvent ->
			button.text = "pressed key: ${keyEvent.keyCode}"
		}
		button.onKeyReleased = { keyEvent ->
			button.text = "released key: ${keyEvent.keyCode}"
		}
		button.onKeyTyped = { keyEvent ->
			button.text = "typed key: ${keyEvent.character}"
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

		//Additional function references available only to DynamicComponentViews

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