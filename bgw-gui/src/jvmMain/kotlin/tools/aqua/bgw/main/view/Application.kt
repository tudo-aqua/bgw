package tools.aqua.bgw.main.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

object Application : BoardGameApplication() {
    private val grid = GridGameScene()
    private val animation = AnimationScene()
    val menuScene = MyMenuScene()
    private val uiScene = UIScene()
    private val dragDropScene = DragDropScene()
    private val visualScene = VisualScene()

    init {
        loadFont("Rubik.ttf", "Rubik", Font.FontWeight.SEMI_BOLD)
        //showGameScene(animation)
        showGameScene(grid)
        //showGameScene(dragDropScene)
        //showGameScene(visualScene)
        // showMenuScene(uiScene)
        //showGameScene(visualScene)
    }
}