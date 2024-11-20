package tools.aqua.bgw.main.view

import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.visual.ColorVisual
import kotlin.random.Random

class CardLayoutScene : BoardGameScene() {

    val layout = LinearLayout<CardView>(
        width = 500,
        height = 200,
        posX = 100,
        posY = 100,
        orientation = Orientation.HORIZONTAL,
        alignment = Alignment.CENTER,
        visual = ColorVisual.LIGHT_GRAY,
        spacing = 15
    ).apply {
        onMouseClicked = { event ->
            if(event.button == MouseButtonType.LEFT_BUTTON) {
                this.add(
                    CardView(
                        posX = 0,
                        posY = 0,
                        width = 100,
                        height = 200,
                        front = ColorVisual(Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
                    ).apply {
                        opacity = 0.5
                        onMouseEntered = {
                            posY -= 25
                        }
                        onMouseExited = {
                            posY += 25
                        }
                    }
                )
            } else if(event.button == MouseButtonType.RIGHT_BUTTON) {
                this.remove(this.components.last())
            }
        }
    }

    init {
        addComponents(layout)
        repeat(5) {
            val card = CardView(
                posX = 0,
                posY = 0,
                width = 100,
                height = 200,
                front = ColorVisual(Color(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255)))
            ).apply {
                opacity = 0.5

                onMouseEntered = {
                    posY -= 25
                }

                onMouseExited = {
                    posY += 25
                }
            }
            layout.add(card)
        }
    }
}
