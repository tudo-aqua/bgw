package examples.concepts.draganddrop

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.reposition
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
    DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
    private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
    
    private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
    private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)
    
    private val redArea: AreaContainerView<TokenView> = AreaContainerView(
        height = 50,
        width = 50,
        posX = 200,
        posY = 20,
        visual = ColorVisual(255, 0, 0, 100)
    )
    
    private val greenArea: AreaContainerView<TokenView> =
        AreaContainerView(
            height = 50,
            width = 50,
            posX = 200,
            posY = 200,
            visual = ColorVisual(0, 255, 0, 100)
        )
    
    init {
        //initialize areas for drag and drop
        redArea.dropAcceptor = {
            when (it.draggedElement) {
                is TokenView -> it.draggedElement == redToken
                else -> false
            }
        }
        redArea.onDragElementDropped = {
            redArea.add((it.draggedElement as TokenView).apply { reposition(0, 0) })
        }
        
        greenArea.dropAcceptor = {
            when (it.draggedElement) {
                is TokenView -> it.draggedElement == greenToken
                else -> false
            }
        }
        greenArea.onDragElementDropped = {
            greenArea.add((it.draggedElement as TokenView).apply { reposition(0, 0) })
        }

        //initialize Tokens for drag and drop
        redToken.isDraggable = true
        redToken.onDragGestureEnded = { _, success -> redToken.isDraggable = !success }
        
        greenToken.isDraggable = true
        greenToken.onDragGestureEnded = { _, success -> greenToken.isDraggable = !success }

        //add elements to scene and show scene
        gameScene.addElements(redToken, greenToken, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}