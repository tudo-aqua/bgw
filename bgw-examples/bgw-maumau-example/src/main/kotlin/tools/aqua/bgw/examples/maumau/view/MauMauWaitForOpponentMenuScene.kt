package tools.aqua.bgw.examples.maumau.view


import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

/**
 * ViewController for the wait for opponent menu scene.
 */
class MauMauWaitForOpponentMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    private var dots = 1
    private val timer = Timer()
    
    private val menuLabel: Label = Label(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        posX = (300- MENU_ITEM_WIDTH) / 2,
        posY = (500 - MENU_ITEM_HEIGHT) / 2,
        text = "Waiting for opponent.",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    init {
        addComponents(menuLabel)
    }
    
    /**
     * Starts the dot animation.
     */
    fun startAnimation() {
        timer.scheduleAtFixedRate(
            delay = 0,
            period = 500
        ) {
            BoardGameApplication.runOnGUIThread {
                menuLabel.text = "Waiting for opponent" + ".".repeat(dots)
                dots %= 3
                dots++
            }
        }
    }
    
    /**
     * Stops the dot animation.
     */
    fun stopAnimation() {
        timer.cancel()
    }
}

