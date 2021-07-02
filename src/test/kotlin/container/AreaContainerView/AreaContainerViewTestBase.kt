package container.AreaContainerView

import container.GameElementContainerViewTestBase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.elements.container.AreaContainerView
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.TokenView
import java.lang.IllegalArgumentException
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse

abstract class AreaContainerViewTestBase: GameElementContainerViewTestBase() {
    lateinit protected var tokenAreaContainer : AreaContainerView<TokenView>

    @BeforeEach
    fun areaContainerViewSetup() {
        tokenAreaContainer = AreaContainerView()
    }
}