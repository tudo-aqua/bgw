package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual
import kotlin.random.Random

class HexGridGameScene : BoardGameScene() {
    private val hexGrid = HexagonGrid<HexagonView>(
        width = 500,
        height = 500,
        posX = 0,
        posY = 0,
        coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL,
        visual = ColorVisual.LIGHT_GRAY
    ).apply {
        dropAcceptor = {
            it.draggedComponent is HexagonView
        }
        onDragDropped = {
            placeOnHexGrid(it.draggedComponent as HexagonView)
        }
    }

    private val satchel = Satchel<HexagonView>(
        posX = 800,
        posY = 800,
        width = 100,
        height = 100,
        visual = ColorVisual.DARK_GRAY
    ).apply {
        dropAcceptor = {
            it.draggedComponent is HexagonView
        }
        onDragDropped = {
            placeInSatchel(it.draggedComponent as HexagonView)
        }
    }

    fun placeOnHexGrid(hexagon: HexagonView) {
        satchel.remove(hexagon)
        hexGrid[Random.nextInt(-5, 5), Random.nextInt(-5, 5)] = hexagon.apply {
            isDraggable = true
        }
    }

    fun placeInSatchel(hexagon: HexagonView) {
        hexGrid.remove(hexagon)
        satchel.add(hexagon)
    }

    init {
        addComponents(satchel, hexGrid)
        repeat(20) {
            val hexagon = HexagonView(posX = 800, posY = 800, visual = ColorVisual(Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))), size = 50).apply {
                isDraggable = true
            }
            satchel.add(hexagon)
        }
    }
}
