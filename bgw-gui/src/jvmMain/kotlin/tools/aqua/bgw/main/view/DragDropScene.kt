package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

class DragDropScene : BoardGameScene() {
    private val source = Pane<ComponentView>(width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY)
    private val target = Pane<ComponentView>(posX = 500, posY = 500, width = 500, height = 500, visual = ColorVisual.LIGHT_GRAY)

    private val label = Label(posX = 20, text="Hello", visual=ColorVisual.RED, width=100, height=100)

    init {
        source.add(label)
        addComponents(source, target)
    }
}