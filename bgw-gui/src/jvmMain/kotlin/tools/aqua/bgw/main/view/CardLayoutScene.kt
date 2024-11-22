package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.visual.ColorVisual
import kotlin.random.Random

class CardLayoutScene : BoardGameScene() {

    val button = Button(
        width = 200,
        height = 200,
        posX = 700,
        posY = 700,
        text = "Add Card",
        visual = ColorVisual(Color(255, 0, 0))
    ).apply {
        dropAcceptor = {
            it.draggedComponent is CardView && it.draggedComponent != this
        }

        onDragDropped = { event ->
            println("Dropped ${event.draggedComponent} on $this")
        }
    }

    val layout = LinearLayout<CardView>(
        width = 150,
        height = 800,
        posX = 100,
        posY = 100,
        orientation = Orientation.VERTICAL,
        alignment = Alignment.BOTTOM_CENTER,
        visual = ColorVisual.LIGHT_GRAY,
        spacing = 15
    ).apply {
        onMouseClicked = { event ->
            if (event.button == MouseButtonType.LEFT_BUTTON) {
                this.add(
                    CardView(
                        posX = 0,
                        posY = 0,
                        width = 100,
                        height = 200,
                        front = ColorVisual(Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
                    ).apply {
                        isDraggable = true

                        dropAcceptor = {
                            it.draggedComponent is CardView && it.draggedComponent != this
                        }

                        onDragDropped = { event ->
                            println("Dropped ${event.draggedComponent} on $this")
                        }
                    }
                )
            } else if (event.button == MouseButtonType.RIGHT_BUTTON) {
                this.remove(this.components.last())
            }
        }
    }

    init {
        addComponents(layout, button)
        repeat(5) {
            val card = CardView(
                posX = 0,
                posY = 0,
                width = 100,
                height = 200,
                front = ColorVisual(Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
            ).apply {
                isDraggable = true

                dropAcceptor = {
                    it.draggedComponent is CardView && it.draggedComponent != this
                }

                onDragDropped = { event ->
                    println("Dropped ${event.draggedComponent} on $this")
                }
            }
            layout.add(card)
        }
    }
}
