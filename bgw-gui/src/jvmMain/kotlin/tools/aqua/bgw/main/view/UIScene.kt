package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import kotlin.random.Random

class UIScene : MenuScene() {
    val combo = ComboBox<TestObject>(
        width = 1920,
        height = 50,
        items = listOf(TestObject("Test1"), TestObject("Test2"), TestObject("Test3"), TestObject("Test4")),
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

    private val textfield = TextField(
        posX = 80,
        posY = 180,
        width = 800,
        height = 50,
        text = "Testbox",
        font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
        prompt = "Enter text here",
    ).apply {
        visual = ColorVisual(Color.LIGHT_GRAY)
    }

    private val passwordfield = PasswordField(
        posX = 80,
        posY = 250,
        width = 800,
        height = 50,
        text = "Passwortbox",
        font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
        prompt = "Enter password here",
    ).apply {
        visual = ColorVisual(Color.LIGHT_GRAY)
    }

    private val textarea = TextArea(
        posX = 80,
        posY = 320,
        width = 800,
        height = 600,
        text = "Test\nArea",
        font = Font(20.0, Color.BLACK, "Rubik", Font.FontWeight.SEMI_BOLD),
        prompt = "Enter text here",
    ).apply {
        visual = ColorVisual(Color.LIGHT_GRAY)
    }

    private val color = ColorPicker(
        posX = 500,
        posY = 50,
        width = 100,
        height = 50,
        initialColor = Color(255, 0, 0),
    ).apply {
        visual = ColorVisual(Color.LIGHT_GRAY)
    }

    init {
        addComponents(combo, progress, textfield, passwordfield, textarea, color)


        combo.select(2)
    }

    inner class TestObject(val name : String) {}
}