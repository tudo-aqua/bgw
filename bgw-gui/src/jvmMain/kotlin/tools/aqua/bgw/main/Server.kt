package tools.aqua.bgw.main

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.visual.ColorVisual


/* val hexPane = HexagonGrid<HexagonView>(posX=400, posY=0, visual=ColorVisual.WHITE, width = 300, height = 500, coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET)
val hex1 = HexagonView(posX=900, posY=0, visual=ColorVisual.MAGENTA, size = 100.0)
val hex2 = HexagonView(posX=900, posY=200, visual=ColorVisual.RED, size = 100.0)
val hex3 = HexagonView(posX=900, posY=400, visual=ColorVisual.BLUE, size = 100.0)
val hex4 = HexagonView(posX=900, posY=600, visual=ColorVisual.CYAN, size = 100.0)
val hexNew = HexagonView(posX=900, posY=600, visual= ImageVisual("https://cdn2.thecatapi.com/images/9qh.jpg"), size = 100.0)

hexPane[-1, 0] = hex1
hexPane[1, 0] = hex2
hexPane[0, 1] = hex3
hexPane[1, 1] = hex4
hexPane[0, -2] = hexNew
*/

val label = Label(visual = ColorVisual.RED, width = 200, height = 200, text = "Hello, SoPra!")
val label2 = Label(posX = 200, posY = 200, visual = ColorVisual.BLUE, width = 200, height = 200, text = "Hello, SoPra!")

val pane = Pane<ComponentView>(posX = 400, posY = 0, visual = ColorVisual.MAGENTA, width = 300, height = 500)
val button = Button(posX = 50, posY = 50, visual = ColorVisual.ORANGE, width = 200, height = 200, text = "Click").apply {
    onMouseClicked = { println("Clicked Button 1!") }
}
val button2 = Button(posX = 50, posY = 250, visual = ColorVisual.ORANGE, width = 200, height = 200, text = "Click 2").apply {
    onMouseClicked = { println("Clicked Button 2!") }
}

val scene = object : BoardGameScene(1920.0, 1080.0, ColorVisual.GREEN) {
    init {
        pane.addAll(button, button2)
        addComponents(label, label2, pane)
        // addComponents(hexPane)
    }
}

fun main() {
    val application = BoardGameApplication()
    application.showGameScene(scene)
    application.show()
}

