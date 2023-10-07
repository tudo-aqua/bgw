package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font

class MyMenuScene : MenuScene() {
    private val label = Button(
        text = "Hello World!",
        width = 200,
        height = 200,
        font = Font(size = 36),
        posX = this.width / 2 - 100,
        posY = this.height / 2 - 100
    ).apply {
        onMouseClicked = {
            println("Clicked ME!")
            Application.hideMenuScene()
        }
    }

    init {
        addComponents(label)
    }
}
