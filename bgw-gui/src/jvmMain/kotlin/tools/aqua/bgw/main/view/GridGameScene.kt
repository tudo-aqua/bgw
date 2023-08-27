package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual


class GridGameScene : BoardGameScene() {
    private val gridPane = GridPane<ComponentView>(columns = 5, rows = 5, visual = toColorVisual("#403F4CFF"))

    init {
        repeat(gridPane.columns) { x ->
            repeat(gridPane.rows) { y ->
                val index = x + y * gridPane.columns
                gridPane[x, y] = Label(
                    width = 100,
                    height = 100,
                    text = "${index + 1}",
                    visual = randomColorVisual()
                ).apply {
                    onMouseClicked = {
                        gridPane.removeRow(gridPane.rows - 1)
                    }
                }
            }
        }
        addComponents(gridPane)
    }

    private fun randomColorVisual(): ColorVisual {
        return ColorVisual((0..255).random(), (0..255).random(), (0..255).random(), 1)
    }

    private fun toColorVisual(hexColor: String): ColorVisual {
        val color = hexColor.removePrefix("#")
        val a = color.substring(0, 2).toInt(16)
        val r = color.substring(2, 4).toInt(16)
        val g = color.substring(4, 6).toInt(16)
        val b = color.substring(6, 8).toInt(16)
        return ColorVisual(r, g, b, a)
    }
}