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
    val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
    
    val redTokenView: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
    val greenTokenView: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)
    
    val redArea: Area<TokenView> =
        Area(
            height = 50,
            width = 50,
            posX = 200,
            posY = 20,
            visual = ColorVisual(255, 0, 0, 100)
        )
    
    val greenArea: Area<TokenView> =
        Area(
            height = 50,
            width = 50,
            posX = 200,
            posY = 200,
            visual = ColorVisual(0, 255, 0, 100)
        )
    
    init {
        redTokenView.isDraggable = true
        redTokenView.onDragGestureEnded = { _, success ->
            if (success) {
                redTokenView.isDraggable = false
            }
        }
    
        greenTokenView.isDraggable = true
        greenTokenView.onDragGestureEnded = { _, success ->
            if (success) {
                greenTokenView.isDraggable = false
            }
        }
    
        redArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                is TokenView -> dragEvent.draggedComponent == redTokenView
                else -> false
            }
        }
        redArea.onDragDropped = { dragEvent ->
            redArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
        }
        
        greenArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                is TokenView -> dragEvent.draggedComponent == greenTokenView
                else -> false
            }
        }
        greenArea.onDragDropped = { dragEvent ->
            greenArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
        }
    
        gameScene.addComponents(redTokenView, greenTokenView, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}