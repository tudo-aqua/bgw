import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.Board
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.examples.maumau.entity.CardSuit
import tools.aqua.bgw.examples.maumau.entity.CardValue
import tools.aqua.bgw.examples.maumau.main.CARDS_FILE
import tools.aqua.bgw.examples.maumau.main.IMG_HEIGHT
import tools.aqua.bgw.examples.maumau.main.IMG_WIDTH
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

val sevenOfSpadesImageVisual = ImageVisual(
    CARDS_FILE,
    IMG_WIDTH,
    IMG_HEIGHT,
    CardValue.SEVEN.ordinal * IMG_WIDTH,
    CardSuit.SPADES.ordinal * IMG_HEIGHT
)

fun main() {
    Application.show()
}

object Application : BoardGameApplication() {
    private val gameScene = object : BoardGameScene() {
        private val card = CardView(
            front = sevenOfSpadesImageVisual
        )
        private val board = Board<GameComponentView>(
            posX = width / 2 - DEFAULT_BOARD_WIDTH / 2,
            posY = height / 2 - DEFAULT_BOARD_HEIGHT / 2
        ).apply {
            visual = ColorVisual.RED.apply { transparency = 0.5 }
            for (y in 0 until (DEFAULT_BOARD_HEIGHT / DEFAULT_CARD_HEIGHT).toInt()) {
                for (x in 0 until (DEFAULT_BOARD_WIDTH / DEFAULT_CARD_WIDTH).toInt()) {
                    addAll(
                        CardView(
                            posX = x * DEFAULT_CARD_WIDTH,
                            posY = y * DEFAULT_CARD_HEIGHT,
                            front = sevenOfSpadesImageVisual
                        )
                    )
                }
            }
        }

        init {
            background = ColorVisual(108, 168, 59)
            addComponents(board)
        }
    }

    init {
        showGameScene(gameScene)
    }
}