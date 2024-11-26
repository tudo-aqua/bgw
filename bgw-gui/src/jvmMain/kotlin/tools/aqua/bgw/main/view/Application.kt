package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
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

    val token : TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.BLACK)

    val label : Label = Label().apply {
        text = "Hello, World!"
        posX = 30.0
        posY = 200.0
    }

    val gameScene : BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

    init {
        // handling user input on ComponentView
        button.onMouseClicked  = { event -> button.text = "clicked ${event.button}" }  // Done

        button.onMousePressed = { mouseEvent -> button.text = "pressed ${mouseEvent.button}" }   // Done
        button.onMouseReleased = { mouseEvent -> button.text = "released ${mouseEvent.button}" }    // Done
        button.onMouseEntered = { button.visual = ColorVisual.MAGENTA }  // Done
        button.onMouseExited = { button.visual = ColorVisual.GREEN }  // Done

        button.onKeyPressed = { keyEvent -> button.text = "pressed key: ${keyEvent.keyCode}" }  // Done
        button.onKeyReleased = { keyEvent -> button.text = "released key: ${keyEvent.keyCode}" }  // Done
        button.onKeyTyped = { keyEvent -> button.text = "typed key: ${keyEvent.character}" }  // Done

        button.dropAcceptor = { true }  // Done
        button.onDragDropped = {
            it.draggedComponent.reposition(500, 30)
            it.draggedComponent.rotation = 0.0
        }  // Done
        button.onDragGestureEntered = { dragEvent -> button.visual = dragEvent.draggedComponent.visual }  // Done
        button.onDragGestureExited = { button.visual = ColorVisual.GREEN }  // Done

        // Additional function references available only to DynamicComponentViews
        token.isDraggable = true

        token.onDragGestureMoved = { token.rotate(5) }  // Done
        token.onDragGestureStarted = { token.visual = ColorVisual.YELLOW }  // Done
        token.onDragGestureEnded = { _, success -> if (success) token.resize(50, 50) }  // Done

        token.onMouseClicked = { event -> token.visual = ColorVisual.GREEN }  // Done
        token.onMousePressed = { event -> token.visual = ColorVisual.RED }  // Done
        token.onMouseReleased = { event -> token.visual = ColorVisual.BLUE }  // Done

        // Global input listener
        gameScene.onKeyPressed = { event ->
            if (event.keyCode == KeyCode.ESCAPE)
                exit()
        } // TODO

        showGameScene(gameScene.apply { addComponents(button, token, label) })
        show()
    }

    private fun handleMouseClicked(@Suppress("UNUSED_PARAMETER") mouseEvent: MouseEvent) {
        button.text = "someone clicked on me!"
    }
}