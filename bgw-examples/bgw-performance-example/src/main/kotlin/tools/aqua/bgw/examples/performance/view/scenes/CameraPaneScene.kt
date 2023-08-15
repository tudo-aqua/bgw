package tools.aqua.bgw.examples.performance.view.scenes

import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.performance.view.PerformanceViewController
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class CameraPaneScene(val root : PerformanceViewController): BoardGameScene(background = ColorVisual.WHITE) {
    val targetLayout = Pane<Label>(
        width = 5_000,
        height = 5_000
    )

    val cameraPane = CameraPane(width=1920, height = 1080, target = targetLayout).apply {
        interactive = false
        isHorizontalLocked = false
        zoom = 1.2
        isZoomLocked = true
    }
    val imageVisual = ImageVisual("mage.png")

    val count = 50

    init {
        println("Expected images to load: ${count * count}")
        for(x in 0 until count * 100 step 100) {
            for(y in 0 until count * 100 step 100) {
                targetLayout.add(
                    Label(posX = x, posY = y, width = 100, height = 100, visual = imageVisual)
                )
            }
        }

        addComponents(
            cameraPane
        )
    }
}