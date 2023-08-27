package tools.aqua.bgw.main

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.style.BlurFilter
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.style.Flip
import tools.aqua.bgw.style.SaturationFilter
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual
import kotlin.random.Random


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


fun getRandomImageVisual(): ImageVisual {
    return ImageVisual("https://picsum.photos/200?id=${Random.nextInt()}")
}

fun main() {
    val application = object : BoardGameApplication() {
        val scene = object : BoardGameScene(1920.0, 1080.0, ColorVisual.GREEN) {
            val label = Label(posX = 0, posY = 0, visual = CompoundVisual(
                ImageVisual("https://cdn2.thecatapi.com/images/9qh.jpg").apply {
                    flipped = Flip.HORIZONTAL
                },
                ColorVisual(Color.CYAN).apply {
                    style.borderRadius = BorderRadius.FULL
                    filters.blur = BlurFilter(4.0)
                    filters.saturation = SaturationFilter.BLACK_WHITE
                },
                //ColorVisual(Color.GREEN).apply { style.borderRadius = BorderRadius(10,0,5,0) },
                TextVisual("Hello World", font = Font(
                    family = "Times New Roman",
                    size = 20.0,
                    color = Color.BLUE
                )
                )
            ), width = 200, height = 200, text = "")
            val label2 = Label(posX = 200, posY = 200, visual = getRandomImageVisual(), width = 200, height = 200, text = "Hello, SoPra!")


            val pane = Pane<ComponentView>(visual = ColorVisual.CYAN, width = 2000, height = 2000)
            val cameraPane = CameraPane(posX = 400, posY = 0, visual = ColorVisual.GRAY, width = 1000, height = 1000, target = pane)

            val button = Button(posX = 50, posY = 50, visual = getRandomImageVisual(), width = 200, height = 200, text = "Click")
            val button2 = Button(posX = 50, posY = 250, visual = ColorVisual.ORANGE, width = 200, height = 200, text = "Click 2")
            init {
                pane.addAll(button, button2)
                addComponents(label, label2, cameraPane)
                // addComponents(hexPane)
            }
        }

        val scene2 = object : BoardGameScene(1920.0, 1080.0, ColorVisual.YELLOW) {
           val label2 = Label(posX = 500, posY = 500, visual = getRandomImageVisual(), width = 300, height = 200, text = "Wirklich")
            val pane = Pane<ComponentView>(posX = 400, posY = 0, visual = ColorVisual.MAGENTA, width = 300, height = 500)
            val button = Button(posX = 50, posY = 50, visual = ColorVisual.BLUE, width = 200, height = 200, text = "Dont Click or else")
            val button2 = Button(posX = 50, posY = 250, visual = getRandomImageVisual(), width = 200, height = 200, text = "Click 2")
            init {
                pane.addAll(button, button2)
                addComponents(label2, pane)
                // addComponents(hexPane)
            }
        }
        var counter = 1
        init {
            scene.button.onMouseClicked = {
                    //println("Scene1 Clicked Button 1 ${scene.button.id}!")
                    showGameScene(scene2)
            }
            scene.button2.onKeyPressed = {
                //println("Typed ${it.keyCode} on ${it.id}!")
            }
            scene.button2.onMouseClicked = {
                //println("Scene1 Clicked Button 2 ${scene.button.id}!")
                scene.button.visual = ImageVisual("assets/$counter.jpg")
                counter++
                if (counter > 5) counter = 1
            }
            scene2.button2.onMouseClicked = {
                //println("Scene2 Clicked Button 2 ${scene.button.id}!")
                showGameScene(scene)
            }
            scene2.button.onMouseClicked = {
                //println("Scene2 Clicked Button 1 ${scene.button.id}!")
                scene2.button2.visual = getRandomImageVisual()
            }

            showGameScene(scene)
        }
    }
    application.show()
}

