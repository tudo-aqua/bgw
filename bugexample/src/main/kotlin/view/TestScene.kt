package view

import entity.CardImageLoader
import entity.CardSuit
import entity.CardValue
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

class TestScene : BoardGameScene() {
    private val cardImageLoader = CardImageLoader()

    private val sourceArea = Area<CardView>(
        posX = 100,
        posY = 100,
        width = 130,
        height = 200,
        visual = ColorVisual.GREEN,
    )

    private val targetArea = Area<CardView>(
        posX = 1000,
        posY = 500,
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

        addComponents(sourceArea, targetArea)
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