package tools.aqua.bgw.main.view

import tools.aqua.bgw.core.BoardGameApplication

class Application : BoardGameApplication() {
    private val scene = GridGameScene()

    init {
        showGameScene(scene)
    }
}