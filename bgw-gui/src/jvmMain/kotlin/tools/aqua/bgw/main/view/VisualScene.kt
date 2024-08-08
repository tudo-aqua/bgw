package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual

class VisualScene : BoardGameScene() {
    val card = CardView(
        width = 150,
        height = 150,
        posX = 50,
        posY = 0,
        front = CompoundVisual(
            ColorVisual.YELLOW,
            ImageVisual(
                "locked.png",
                offsetX = 40
            )
        )
    )

    val card2 = CardView(
        width = 150,
        height = 150,
        posX = 50,
        posY = 0,
        front = CompoundVisual(
            ColorVisual.RED,
            ImageVisual(
                "locked.png",
                offsetX = 40
            )
        )
    )

    /* val list = ListView<GameComponentView>(
        width = 700,
        height = 250,
        posX = 0,
        posY = 250,
        visual = ColorVisual.GREEN,
        formatFunction = { it::class.simpleName.toString() }
    ).apply {
        items.add(card)
        items.add(card2)
    } */


    val group1 = ToggleGroup()

    val toggle1 = ToggleButton(
        posX = 0,
        posY = 0,
        width = 100,
        height = 50,
        text = "Toggle 1",
        visual = ColorVisual.RED,
        toggleGroup = group1
    )

    val toggle2 = ToggleButton(
        posX = 0,
        posY = 50,
        width = 100,
        height = 50,
        text = "Toggle 2",
        visual = ColorVisual.GREEN,
        toggleGroup = group1,
        alignment = Alignment.BOTTOM_RIGHT
    )

    val toggle3 = ToggleButton(
        posX = 0,
        posY = 100,
        width = 100,
        height = 50,
        text = "Toggle 3",
        visual = ColorVisual.BLUE,
        alignment = Alignment.BOTTOM_RIGHT
    )


    val group2 = ToggleGroup()

    val radio1 = RadioButton(
        posX = 300,
        posY = 0,
        width = 100,
        height = 50,
        text = "Radio 1",
        visual = ColorVisual.RED,
        toggleGroup = group2
    )

    val radio2 = RadioButton(
        posX = 300,
        posY = 50,
        width = 100,
        height = 50,
        text = "Radio 2",
        visual = ColorVisual.GREEN,
        toggleGroup = group2,
        alignment = Alignment.BOTTOM_RIGHT
    )

    val radio3 = RadioButton(
        posX = 300,
        posY = 100,
        width = 100,
        height = 50,
        text = "Radio 3",
        visual = ColorVisual.BLUE,
        alignment = Alignment.BOTTOM_RIGHT
    )


    val checkbox = CheckBox(
        posX = 600,
        posY = 0,
        width = 100,
        height = 50,
        text = "Check 1",
        visual = ColorVisual.RED
    )

    init {
        addComponents(radio1, radio2, radio3, toggle1, toggle2, toggle3, checkbox)
    }
}