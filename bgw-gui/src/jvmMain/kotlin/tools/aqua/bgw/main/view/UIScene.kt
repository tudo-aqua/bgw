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

internal class UIScene : MenuScene() {
    val combo = ComboBox<TestObject>(
        width = 1920,
        height = 50,
        items = listOf(TestObject("Test1"), TestObject("Test2"), TestObject("Test3"), TestObject("Test4")),
        prompt = "Select an item",
        font = Font(20.0, java.awt.Color(0x551100), "Staatliches", Font.FontWeight.NORMAL),
        formatFunction = { it.name }
    ).apply {
    }

    private val toggleGroup = ToggleGroup().apply {
        onSelected = { button ->
            println("Selected: ${button.text}")
        }
        onDeselected = { button ->
            println("Deselected: ${button.text}")
        }
    }

    private val toggle = ToggleButton(
        posX = 1000,
        posY = 500,
        width = 300,
        height = 100,
        text = "Toggle",
        font = Font(20.0, Color.BLACK, "JetBrainsMono", Font.FontWeight.EXTRA_BOLD),
        visual = ColorVisual(Color.GREEN),
        toggleGroup = toggleGroup,
        alignment = Alignment.CENTER,
    ).apply {
        onSelected = { println("Selected 1") }
        onDeselected = { println("Deselected 1") }
    }

    private val toggle2 = ToggleButton(
        posX = 1000,
        posY = 600,
        width = 100,
        height = 100,
        text = "Indeterminate",
        font = Font(20.0, Color.BLACK, "JetBrainsMono", Font.FontWeight.EXTRA_BOLD),
        visual = ColorVisual(Color.ORANGE),
        toggleGroup = toggleGroup
    ).apply {
        onSelected = {
            println("Setting to indeterminate")
            checkBox.isIndeterminate = true
        }
        onDeselected = {
            println("Setting to not indeterminate")
            checkBox.isIndeterminate = false
        }
    }

    private val checkBox = CheckBox(
        posX = 1000,
        posY = 700,
        width = 100,
        height = 100,
        text = "Check",
        font = Font(20.0, Color.BLACK, "JetBrainsMono", Font.FontWeight.EXTRA_BOLD),
        visual = ColorVisual(Color.RED),
        allowIndeterminate = true
    ).apply {
        onChanged = { checked, indeterminate ->
            println("Checked: $checked, Indeterminate: $indeterminate")
        }
    }

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
        font = Font(20.0, Color.WHITE, "Rubik", Font.FontWeight.SEMI_BOLD),
        prompt = "Enter text here",
    ).apply {
        visual = ColorVisual(Color.RED)
    }

    private val passwordfield = PasswordField(
        posX = 80,
        posY = 250,
        width = 800,
        height = 50,
        text = "Passwortbox",
        font = Font(20.0, Color.BLACK, "JetBrainsMono", Font.FontWeight.EXTRA_BOLD),
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
        font = Font(20.0, Color.BLACK, "JetBrainsMono", Font.FontWeight.EXTRA_BOLD),
        prompt = "Enter text here",
    ).apply {
        visual = ColorVisual(Color.BLUE)
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
        addComponents(combo, progress, textfield, passwordfield, textarea, color, toggle, toggle2, checkBox)

        color.selectedColorProperty.addListener { _, newValue ->
            progress.barColor = newValue
        }


        combo.select(2)

        println("UIScene initialized")
    }

    inner class TestObject(val name : String) {}
}