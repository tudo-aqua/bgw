package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual


internal class GridGameScene : BoardGameScene() {
    private val targetPane = Pane<ComponentView>(
        posX = 0,
        posY = 0,
        width = 700,
        height = 500,
        visual = ImageVisual("assets/3.jpg")
    )

    private val cameraPane = CameraPane(
        posX = 150,
        posY = 200,
        width = 600,
        height = 500,
        target = targetPane,
        visual = ColorVisual.BLUE
    ).apply {
        interactive = true
    }

    init {
        addComponents(cameraPane)
    }
}