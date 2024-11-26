package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

/* object Application : BoardGameApplication() {
    private val grid = GridGameScene()
    private val hexGrid = HexGridGameScene()
    private val animation = AnimationScene()
    val menuScene = MyMenuScene()
    private val uiScene = UIScene()
    private val dragDropScene = DragDropScene()
    private val visualScene = VisualScene()
    private val cardLayoutScene = CardLayoutScene()

    init {
        loadFont("Rubik.ttf", "Rubik", Font.FontWeight.SEMI_BOLD)
        showGameScene(cardLayoutScene)
        // showGameScene(hexGrid)
        // showGameScene(animation)
        // showGameScene(grid)
        // showGameScene(dragDropScene)
        // showMenuScene(uiScene)
        // showGameScene(visualScene)
    }
} */

object Application : BoardGameApplication("User input example") {

    val button : Button = Button(height = 150, width = 300, posX = 30, posY = 30).apply {
        visual = ColorVisual.GREEN
    }

    val token : TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)

    val gameScene : BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

    init {
        // handling user input on ComponentView
        button.onMouseClicked = this::handleMouseClicked

        button.onMousePressed = { mouseEvent -> button.text = "pressed ${mouseEvent.button}" }
        button.onMouseReleased = { mouseEvent -> button.text = "released ${mouseEvent.button}" }
        button.onMouseEntered = { button.visual = ColorVisual.MAGENTA }
        button.onMouseExited = { button.visual = ColorVisual.GREEN }
        button.onKeyPressed = { keyEvent -> button.text = "pressed key: ${keyEvent.keyCode}" }
        button.onKeyReleased = { keyEvent -> button.text = "released key: ${keyEvent.keyCode}" }
        button.onKeyTyped = { keyEvent -> button.text = "typed key: ${keyEvent.character}" }
        button.dropAcceptor = { true }
        button.onDragDropped = {
            it.draggedComponent.reposition(500, 30)
            it.draggedComponent.rotation = 0.0
        }
        button.onDragGestureEntered = { dragEvent -> button.visual = dragEvent.draggedComponent.visual }
        button.onDragGestureExited = { button.visual = ColorVisual.GREEN }

        // Additional function references available only to DynamicComponentViews
        token.isDraggable = true

        token.rotation = 45.0

        // token.onDragGestureMoved = { token.rotate(5) }
        token.onDragGestureStarted = { token.visual = ColorVisual.BLUE }
        token.onDragGestureEnded = { _, success -> if (success) token.resize(50, 50) }

        // Global input listener
        gameScene.onKeyPressed = { event ->
            if (event.keyCode == KeyCode.ESCAPE)
                exit()
        }

        showGameScene(gameScene.apply { addComponents(button, token) })
        show()
    }

    private fun handleMouseClicked(@Suppress("UNUSED_PARAMETER") mouseEvent: MouseEvent) {
        button.text = "someone clicked on me!"
    }
}