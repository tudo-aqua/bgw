package tools.aqua.bgw.main.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.util.Font

class Application : BoardGameApplication() {
    private val scene = GridGameScene()

    init {
        loadFont("Rubik.ttf", "Rubik", Font.FontWeight.SEMI_BOLD)
        showGameScene(scene)
    }
}