package tools.aqua.bgw.examples.maumau.view.scenes


import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.LabeledUIComponent
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.examples.maumau.view.custom_components.MenuButton
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

/**
 * ViewController for the main menu scene.
 */
class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    private val menuLabel: Label = Label(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        text = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    val continueGameButton: Button = MenuButton("Continue")
    val newLocalGameButton: Button = MenuButton("New Local Game")
    val hostGameButton: Button = MenuButton("Host Game")
    val joinGameButton: Button = MenuButton("Join Game")
    val exitButton: Button = MenuButton("Exit")
    
    init {
        addComponents(
            GridPane<LabeledUIComponent>(columns = 1, rows = 6, spacing = 15, layoutFromCenter = false).apply {
                this[0,0] = menuLabel
                this[0,1] = continueGameButton
                this[0,2] = newLocalGameButton
                this[0,3] = hostGameButton
                this[0,4] = joinGameButton
                this[0,5] = exitButton
    
                setColumnWidth(0, 300)
                setCenterMode(Alignment.CENTER)
            }
        )
    }
}
