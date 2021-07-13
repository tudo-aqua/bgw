package container

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.TokenView
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

abstract class GameElementContainerViewTestBase {
    lateinit protected var redToken : TokenView
    lateinit protected var blueToken : TokenView
    lateinit protected var greenToken : TokenView

    @BeforeEach
    fun setup() {
        redToken = TokenView(50, 50, 0 ,0, ColorVisual(Color.RED))
        blueToken = TokenView(50, 50, 0 ,0, ColorVisual(Color.BLUE))
        greenToken = TokenView(50, 50, 0 ,0, ColorVisual(Color.GREEN))
    }
}