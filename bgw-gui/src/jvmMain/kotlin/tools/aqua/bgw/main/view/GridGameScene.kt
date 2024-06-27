package tools.aqua.bgw.main.view

import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual


class GridGameScene : BoardGameScene() {
    private val gridPane = GridPane<ComponentView>(columns = 5, rows = 5, visual = toColorVisual("#403F4CFF")).apply {
        spacing = 10.0
    }

    private val gridEntries = mutableListOf<ComponentView>()

    private val cameraPane = CameraPane(width = 1920, height = 1080, target = gridPane, visual = randomColorVisual())
    private val button = Button(
        width = 200,
        height = 200,
        text = "Hello World!",
        font = Font(40.0, Color(0, 0, 0, 0.25), "Rubik", Font.FontWeight.SEMI_BOLD),
        visual = ColorVisual.GRAY
    ).apply {
        onMouseClicked = {
            gridEntries.forEachIndexed { index, componentView ->
                playAnimation(
                    FlipAnimation(
                        componentView = componentView,
                        fromVisual = componentView.visual,
                        toVisual = randomColorVisual(),
                        duration = 250 + index * 50
                    ).apply {
                        onFinished = {
                            println("Finished")
                        }
                    }
                )

                playAnimation(
                    FadeAnimation(
                        componentView = componentView,
                        fromOpacity = 0,
                        toOpacity = 1,
                        duration = 250 + index * 50
                    ).apply {
                        onFinished = {
                            println("Finished")
                        }
                    }
                )
            }
        }
    }

    init {
        repeat(gridPane.columns) { x ->
            repeat(gridPane.rows) { y ->
                val index = x + y * gridPane.columns
                val entry = Label(
                    width = 100,
                    height = 100,
                    text = "($x, $y)",
                    visual = ColorVisual.GRAY,
                    font = Font(40.0, Color(0, 0, 0, 0.25), "Rubik", Font.FontWeight.SEMI_BOLD)
                ).apply {
                    opacity = 0.0
                }

                gridPane[x, y] = entry
                gridEntries.add(entry)
            }
        }
        addComponents(cameraPane, button)
    }

    private fun randomColorVisual(): ColorVisual {
        return ColorVisual((0..255).random(), (0..255).random(), (0..255).random(), 1.0)
    }

    private fun toColorVisual(hexColor: String): ColorVisual {
        val color = hexColor.removePrefix("#")
        val a = color.substring(0, 2).toInt(16)
        val r = color.substring(2, 4).toInt(16)
        val g = color.substring(4, 6).toInt(16)
        val b = color.substring(6, 8).toInt(16)
        return ColorVisual(Color(r, g, b, a))
    }
}