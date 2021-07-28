package examples.concepts.draganddrop

import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponents.GameToken
import tools.aqua.bgw.components.reposition
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

fun main() {
    DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
    val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
    
    val redGameToken: GameToken = GameToken(posX = 20, posY = 20, visual = ColorVisual.RED)
    val greenGameToken: GameToken = GameToken(posX = 20, posY = 200, visual = ColorVisual.GREEN)
    
    val redArea: Area<GameToken> =
        Area(
            height = 50,
            width = 50,
            posX = 200,
            posY = 20,
            visual = ColorVisual(255, 0, 0, 100)
        )
    
    val greenArea: Area<GameToken> =
        Area(
            height = 50,
            width = 50,
            posX = 200,
            posY = 200,
            visual = ColorVisual(0, 255, 0, 100)
        )
    
    init {
        redGameToken.isDraggable = true
        redGameToken.onDragGestureEnded = { _, success ->
            if (success) {
                redGameToken.isDraggable = false
            }
        }
        
        greenGameToken.isDraggable = true
        greenGameToken.onDragGestureEnded = { _, success ->
            if (success) {
                greenGameToken.isDraggable = false
            }
        }
        
        redArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                is GameToken -> dragEvent.draggedComponent == redGameToken
                else -> false
            }
        }
        redArea.onDragDropped = { dragEvent ->
            redArea.add((dragEvent.draggedComponent as GameToken).apply { reposition(0, 0) })
        }
        
        greenArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedComponent) {
                is GameToken -> dragEvent.draggedComponent == greenGameToken
                else -> false
            }
        }
        greenArea.onDragDropped = { dragEvent ->
            greenArea.add((dragEvent.draggedComponent as GameToken).apply { reposition(0, 0) })
        }
        
        gameScene.addComponents(redGameToken, greenGameToken, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}