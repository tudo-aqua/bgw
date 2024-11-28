package tools.aqua.bgw.main.view

import VisualMapper
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.visual.*
import kotlin.random.Random

internal class HexGridGameScene : BoardGameScene() {
    private val hexGrid = HexagonGrid<HexagonView>(
        width = 500,
        height = 500,
        posX = 0,
        posY = 0,
        coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL,
        visual = Visual.EMPTY,
        orientation = HexOrientation.FLAT_TOP
    )

    private val satchel = Satchel<HexagonView>(
        posX = 800,
        posY = 800,
        width = 100,
        height = 100,
        visual = ImageVisual("https://static.vecteezy.com/system/resources/previews/010/256/326/non_2x/premium-flat-icon-of-game-bag-vector.jpg")
    ).apply {
        dropAcceptor = {
            it.draggedComponent is HexagonView
        }
        onDragDropped = {
            placeInSatchel(it.draggedComponent as HexagonView)
        }
    }

    private val hexPointy = HexagonView(posX = 800, posY = 200, visual = ColorVisual(Color(255, 0, 0)), size = 50, orientation = HexOrientation.POINTY_TOP)
    private val hexFlat = HexagonView(posX = 900, posY = 200, visual = ColorVisual(Color(0, 255, 0)), size = 50, orientation = HexOrientation.FLAT_TOP)

    fun placeOnHexGrid(hexagon: HexagonView) {
        satchel.remove(hexagon)
        hexGrid[Random.nextInt(-5, 5), Random.nextInt(-5, 5)] = hexagon.apply {
            isDraggable = true
        }
    }

    fun placeInSatchel(hexagon: HexagonView) {
        hexGrid.remove(hexagon)
        satchel.add(hexagon)
    }val hexMap = BidirectionalMap<Pair<Int, Int>, HexagonView>()

    fun buildHexGrid() {
        for (q in -5..5) {
            for (r in -5..5) {
                if (q + r >= -5 && q + r <= 5) {
                    if(hexMap.containsForward(Pair(q,r))) {
                        hexGrid[q,r] = hexMap[Pair(q,r)]!! as HexagonView
                        continue
                    }
                    val hexagon = HexagonView(
                        visual = ColorVisual.LIGHT_GRAY,
                        size = 40
                    ).apply {
                        dropAcceptor = {
                            it.draggedComponent is HexagonView
                        }

                        onDragDropped = {
                            hexGrid[q,r] = it.draggedComponent as HexagonView
                            satchel.remove(it.draggedComponent)
                        }

                        onDragGestureEntered = {
                            visual = ColorVisual.GRAY
                        }

                        onDragGestureExited = {
                            visual = ColorVisual.LIGHT_GRAY
                        }
                    }
                    hexGrid[q,r] = hexagon
                    hexMap[Pair(q,r)] = hexagon
                }
            }
        }
    }

    fun refreshHexGrid() {
        buildHexGrid()
    }

    init {
        buildHexGrid()

        addComponents(hexGrid, satchel)
        repeat(20) {
            val hexagon = HexagonView(posX = 800, posY = 800, visual = ImageVisual("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwc4YbxNBYXWRkgqzh9tbaSQh2Uy-f4e1Nl0teHHWFisub3gxv4rxn1eFjgVUUMASaNSg&usqp=CAU"), size = 40, orientation = HexOrientation.FLAT_TOP).apply {
                isDraggable = true
                onKeyPressed = {
                    if(it.keyCode == KeyCode.Q) {
                        rotation -= 60.0
                    } else if(it.keyCode == KeyCode.E) {
                        rotation += 60.0
                    }
                }
            }
            satchel.add(hexagon)
        }
    }
}
