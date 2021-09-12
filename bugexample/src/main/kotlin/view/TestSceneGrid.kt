package view

import entity.CardImageLoader
import entity.CardSuit
import entity.CardValue
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class TestSceneGrid : BoardGameScene() {
    private val cardImageLoader = CardImageLoader()

    private val gridPane1 = GridPane<Area<CardView>>(
        posX = 100,
        posY = 100,
        columns = 2,
        rows = 1,
        spacing = 25,
        layoutFromCenter = false,
        visual = ColorVisual.ORANGE,
    )

    private val sourceArea = Area<CardView>(
        width = 130,
        height = 200,
        visual = ColorVisual.GREEN,
    )

    private val targetArea = Area<CardView>(
        width = 130,
        height = 200,
        visual = ColorVisual.RED,
    )

    private val cardView = CardView(
        width = 130,
        height = 200,
        front = ImageVisual(cardImageLoader.frontImageFor(CardSuit.HEARTS, CardValue.ACE)),
        back = ImageVisual(cardImageLoader.backImage),
    ).apply {
        onMouseClicked = {
            testAnimation()
        }
    }

    init {
        sourceArea.add(cardView)

        gridPane1[0, 0] = sourceArea

        gridPane1[1, 0] = targetArea

        addComponents(gridPane1)
    }

    private fun testAnimation() {
        playAnimation(
            MovementAnimation.toComponentView(
                componentView = cardView,
                toComponentViewPosition = targetArea,
                scene = this,
                duration = 1000,
            ).apply {
                onFinished = {
                    cardView.removeFromParent()
                    targetArea.add(cardView)
                }
            }
        )
    }
}