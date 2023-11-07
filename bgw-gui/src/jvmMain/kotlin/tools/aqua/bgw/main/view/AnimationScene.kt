package tools.aqua.bgw.main.view

import tools.aqua.bgw.animation.*
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.ComboBox
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class AnimationScene : BoardGameScene() {
    /*private val label = Label(
        text = "Test",
        posX = 40,
        posY = 120,
        width = 200,
        height = 200,
        font = Font(20.0, Color(0, 0, 0, 0.25), "Rubik", Font.FontWeight.SEMI_BOLD),
        visual = ColorVisual.MAGENTA
    )*/

    private val label = TokenView(
        posX = 40,
        posY = 40,
        width = 200,
        height = 200,
        visual = ColorVisual.MAGENTA
    )

    private val dice = DiceView(
        posX = 40,
        posY = 40,
        width = 200,
        height = 200,
        visuals = listOf(
            ColorVisual.RED,
            ColorVisual.GREEN,
            ColorVisual.BLUE,
            ColorVisual.YELLOW,
            ColorVisual.CYAN,
            ColorVisual.ORANGE,
            ColorVisual.PINK,
        )
    )



    init {
        addComponents(label, dice)

        /*
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
        } */

        /*
        label.onMouseClicked = {
            this.playAnimation(
                MovementAnimation (
                    componentView = label,
                    byX = 100,
                    byY = 200,
                    duration = 1000
                ).apply {
                    onFinished = {
                        println("Finished")
                    }
                }
            )
        } */

        /*
        label.onMouseClicked = {
            this.playAnimation(
                RotationAnimation (
                    componentView = label,
                    byAngle = 120.0,
                    duration = 1000
                ).apply {
                    onFinished = {
                        println("Finished")
                    }
                }
            )
        } */

        /* label.onMouseClicked = {
            this.playAnimation(
                SequentialAnimation(
                    ScaleAnimation (
                        componentView = label,
                        byScaleX = 2.0,
                        byScaleY = 0.5,
                        duration = 1000
                    ),
                    MovementAnimation (
                        componentView = label,
                        byX = 100,
                        byY = 200,
                        duration = 1000
                    )
                )
            )
        } */

        label.onMouseClicked = {
            /*this.playAnimation(
                ParallelAnimation(
                    ScaleAnimation (
                        componentView = label,
                        byScaleX = 2.0,
                        byScaleY = 0.5,
                        duration = 2000
                    ),
                    MovementAnimation (
                        componentView = label,
                        byX = 100,
                        byY = 200,
                        duration = 1000
                    )
                )
            )*/
            /*this.playAnimation(
                FlipAnimation(
                    componentView = label,
                    fromVisual = ColorVisual.BLUE,
                    toVisual = ColorVisual.RED,
                    duration = 2000
                )
            )*/
        }

        /*label.onMouseClicked = {
            this.playAnimation(
                RandomizeAnimation(
                    componentView = label,
                    visuals = listOf(
                        ColorVisual.GREEN,
                        ColorVisual.BLUE,
                        ColorVisual.YELLOW,
                        ColorVisual.CYAN,
                        ColorVisual.ORANGE,
                        ColorVisual.PINK,
                    ),
                    toVisual = ColorVisual.RED,
                    duration = 2000,
                    speed = 20
                )
            )
        }*/
    }
}