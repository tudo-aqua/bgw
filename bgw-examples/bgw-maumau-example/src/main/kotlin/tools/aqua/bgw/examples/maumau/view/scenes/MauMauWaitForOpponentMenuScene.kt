package tools.aqua.bgw.examples.maumau.view.scenes


import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.examples.maumau.view.custom_components.MenuButton
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
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    val startGameButton: Button = MenuButton("Start Game").apply {
        posX = (300- MENU_ITEM_WIDTH) / 2.0
        posY = 400.0
        isVisible = false
    }
    
    init {
        addComponents(menuLabel, startGameButton)
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
    
    fun onOpponentConnected(name : String) {
        stopAnimation()
        menuLabel.text = "$name connected."
        startGameButton.isVisible = true
    }
    
    /**
     * Stops the dot animation.
     */
    fun stopAnimation() {
        timer.cancel()
    }
}

