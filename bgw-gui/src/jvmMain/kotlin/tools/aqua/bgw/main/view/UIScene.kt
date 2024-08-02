package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.uicomponents.CheckBox
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.ProgressBar
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import kotlin.random.Random

class UIScene : MenuScene() {
    private val combo = ComboBox<TestObject>(
        width = 1920,
        height = 50,
        items = listOf(TestObject("Test1"), TestObject("Test2"), TestObject("Test3")),
        prompt = "Select an item",
        font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
        formatFunction = { it.name }
    )

    private val progress = ProgressBar(
        posX = 80,
        posY = 80,
        width = 800,
        height = 50,
        progress = 0.5,
        barColor = Color.BLUE
    ).apply {
        onMouseClicked = { this.progress = Random.nextDouble(0.0,1.0) }
        scaleY = 2.0
    }

    private val checkbox = CheckBox(
        posX = 80,
        posY = 150,
        width = 800,
        height = 50,
        text = "Testbox",
        font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
        isChecked = true,
        visual = ColorVisual.RED
    )

    private val label = Label(
        posX = 80,
        posY = 220,
        height = 400,
        width = 260,
        text = "Testtext",
        font= Font(20.0, Color.BLACK, "04b_19", Font.FontWeight.NORMAL),
        visual = ColorVisual.YELLOW
    )

    init {
        addComponents(combo, progress, checkbox, label)


        combo.select(2)
        combo.selectedItemProperty.addListener { _, newValue ->
            println(newValue?.name)
        }
    }

    inner class TestObject(val name : String) {}
}