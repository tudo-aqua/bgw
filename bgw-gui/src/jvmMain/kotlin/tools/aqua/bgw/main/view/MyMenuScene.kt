package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

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

    private val testButton = Button(
        text = "Test",
        width = 200,
        height = 200,
        font = Font(size = 36),
        posX = this.width / 2 - 100,
        posY = this.height - 300,
        visual = ColorVisual.MAGENTA
    ).apply {
        onMouseClicked = {
            if(this.opacity == 1.0) {
                this.opacity = 0.2
            } else {
                this.opacity = 1.0
            }
        }
    }

    init {
        addComponents(label, testButton)
    }
}
