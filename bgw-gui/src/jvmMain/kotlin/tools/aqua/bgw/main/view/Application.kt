package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

internal object Application : BoardGameApplication() {
    private val grid = GridGameScene()
    private val hexGrid = HexGridGameScene()
    private val animation = AnimationScene()
    val menuScene = MyMenuScene()
    private val uiScene = UIScene()
    private val dragDropScene = DragDropScene()
    private val visualScene = VisualScene()
    private val cardLayoutScene = CardLayoutScene()

    init {
        loadFont("Rubik.ttf", "Rubik", Font.FontWeight.EXTRA_BOLD)
        // showGameScene(cardLayoutScene)
        // showGameScene(hexGrid)
        showGameScene(animation)
        // showGameScene(grid)
        // showGameScene(dragDropScene)
        // showMenuScene(uiScene)
        // showGameScene(visualScene)
    }
}