package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.AspectRatio
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

internal object Application : BoardGameApplication(aspectRatio = AspectRatio.of(1080, 700)) {
    private val grid = GridGameScene()
    private val hexGrid = HexGridGameScene()
    private val animation = AnimationScene()
    val menuScene = MyMenuScene()
    private val uiScene = UIScene()
    private val dragDropScene = DragDropScene()
    private val visualScene = VisualScene()
    private val cardLayoutScene = CardLayoutScene()

    init {
        loadFont("Rubik.ttf")
        // showGameScene(cardLayoutScene)
        showGameScene(hexGrid)
        // showGameScene(animation)
        // showGameScene(grid)
        // showGameScene(dragDropScene)
        // showMenuScene(uiScene)
        // showGameScene(visualScene)
    }
}