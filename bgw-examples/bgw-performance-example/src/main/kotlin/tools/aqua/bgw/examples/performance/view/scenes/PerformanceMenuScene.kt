package tools.aqua.bgw.examples.performance.view.scenes

import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.LabeledUIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.performance.view.PerformanceViewController
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class PerformanceMenuScene(val root : PerformanceViewController): MenuScene(background = ColorVisual(Color(29,24,39))) {
    val buttonCameraPaneTest: Button =
        Button(
            height = 100,
            width = 200,
            posX = 0,
            posY = 0,
            visual = ColorVisual.WHITE,
            text = "Camera Pane"
        ).apply {
            onMouseClicked = {
                root.hideMenuScene()
                root.showGameScene(root.cameraPaneScene)
            }
        }

    init {
        addComponents(
            buttonCameraPaneTest
        )
    }
}