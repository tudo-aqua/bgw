package tools.aqua.bgw.main.view

import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class UIScene : BoardGameScene() {
    private val combo = ComboBox<TestObject>(
        width = 1920,
        height = 50,
        items = listOf(TestObject("Test1"), TestObject("Test2"), TestObject("Test3")),
        prompt = "Select an item",
        font = Font(20.0, Color(0, 0, 0, 0.25), "Rubik", Font.FontWeight.SEMI_BOLD),
        formatFunction = { it.name }
    )

    private val label = Label(
        text = "Test",
        posX = 40,
        posY = 120,
        width = 200,
        height = 200,
        font = Font(20.0, Color(0, 0, 0, 0.25), "Rubik", Font.FontWeight.SEMI_BOLD),
        visual = ColorVisual.MAGENTA
    )

    init {
        addComponents(combo, label)
        combo.select(2)
        label.onMouseClicked = {
            this.playAnimation(
                FadeAnimation(
                    componentView = label,
                    fromOpacity = 1.0,
                    toOpacity = 0.2,
                    duration = 1000
                ).apply {
                    onFinished = {
                        println("Finished")
                    }
                }
            )
        }

        combo.selectedItemProperty.addListener { _, newValue ->
            println(newValue?.name)
        }
    }

    inner class TestObject(val name : String) {}
}