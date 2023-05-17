import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.main.CARDS_FILE
import tools.aqua.bgw.examples.maumau.main.IMG_HEIGHT
import tools.aqua.bgw.examples.maumau.main.IMG_WIDTH
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

fun main() {
    Application.show()
}

object Application : BoardGameApplication() {
    private val gameScene = object : BoardGameScene() {

        private val pane = Pane<CardView>(
            width = 5000,
            height = 5000,
            posX = width / 2 - DEFAULT_BOARD_WIDTH / 2,
            posY = height / 2 - DEFAULT_BOARD_HEIGHT / 2,
        ).apply {
            for (y in 0 until (DEFAULT_BOARD_HEIGHT / DEFAULT_CARD_HEIGHT).toInt()) {
                for (x in 0 until (DEFAULT_BOARD_WIDTH / DEFAULT_CARD_WIDTH).toInt()) {
                    addAll(
                        CardView(
                            posX = x * DEFAULT_CARD_WIDTH,
                            posY = y * DEFAULT_CARD_HEIGHT,
                            front = ImageVisual(
                                CARDS_FILE,
                                IMG_WIDTH,
                                IMG_HEIGHT,
                                (x % CardValue.values().size) * IMG_WIDTH,
                                (y % CardSuit.values().size) * IMG_HEIGHT
                            )
                        )
                    )
                }
            }
        }

        private val camera = CameraPane<Pane<*>>(
            posX = width / 2 - DEFAULT_BOARD_WIDTH / 2,
            posY = height / 2 - DEFAULT_BOARD_HEIGHT / 2,
            target = pane
        )

        private val scrollLeft = Button(
            text = "←",
            posX = camera.posX - DEFAULT_BUTTON_WIDTH,
            posY = height / 2 - DEFAULT_BUTTON_HEIGHT / 2
        ).apply {
            onMouseClicked = {
                camera.scrollTo(camera.scroll.xCoord - 130.0, camera.scroll.yCoord)
            }
        }

        private val scrollRight = Button(
            text = "→",
            posX = camera.width + camera.posX,
            posY = height / 2 - DEFAULT_BUTTON_HEIGHT / 2
        ).apply {
            onMouseClicked = {
                camera.scrollTo(camera.scroll.xCoord + 130.0, camera.scroll.yCoord)
            }
        }

        private val scrollUp = Button(
            text = "↑",
            posX = width / 2 - DEFAULT_BUTTON_WIDTH / 2,
            posY = camera.posY - DEFAULT_BUTTON_HEIGHT
        ).apply {
            onMouseClicked = {
                camera.scrollTo(camera.scroll.xCoord, camera.scroll.yCoord - 200.0)
            }
        }

        private val scrollDown = Button(
            text = "↓",
            posX = width / 2 - DEFAULT_BUTTON_WIDTH / 2,
            posY = camera.height + camera.posY
        ).apply {
            onMouseClicked = {
                camera.scrollTo(camera.scroll.xCoord, camera.scroll.yCoord + 200.0)
            }
        }

        private val zoomIn = Button(
            text = "+",
            posX = camera.posX + camera.width - DEFAULT_BUTTON_WIDTH,
            posY = camera.height + camera.posY
        ).apply {
            onMouseClicked = {
                camera.zoom += 0.1
            }
        }

        private val zoomOut = Button(
            text = "-",
            posX = camera.posX + camera.width - 2*DEFAULT_BUTTON_WIDTH,
            posY = camera.height + camera.posY
        ).apply {
            onMouseClicked = {
                camera.zoom -= 0.1
            }
        }

        private val xCord = TextField(
            posX = camera.posX,
            posY = camera.height + camera.posY
        )

        private val yCord = TextField(
            posX = camera.posX + DEFAULT_TEXT_FIELD_WIDTH,
            posY = camera.height + camera.posY
        )

        private val moveTo = Button(
            text = "Move",
            posX = camera.posX + 2 * DEFAULT_TEXT_FIELD_WIDTH,
            posY = camera.height + camera.posY
        ).apply {
            onMouseClicked = {
                camera.scrollTo(xCord.text.toDouble(), yCord.text.toDouble())
            }
        }

        init {
            background = ColorVisual(108, 168, 59)
            addComponents(
                camera,
                scrollLeft, scrollRight, scrollUp, scrollDown,
                zoomIn, zoomOut,
                xCord, yCord, moveTo
            )
        }
    }

    init {
        showGameScene(gameScene)
    }
}