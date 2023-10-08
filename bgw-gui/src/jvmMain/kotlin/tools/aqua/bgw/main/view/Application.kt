package tools.aqua.bgw.main.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

object Application : BoardGameApplication() {
    private val grid = GridGameScene()
    private val ui = UIScene()
    val menuScene = MyMenuScene()

    init {
        loadFont("Rubik.ttf", "Rubik", Font.FontWeight.SEMI_BOLD)
        showGameScene(ui)
        //showGameScene(scene)
        showMenuScene(menuScene)
    }
}