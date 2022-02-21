package tools.aqua.bgw.examples.maumau.view.scenes


import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.components.uicomponents.UIComponent
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
class MauMauHostGameMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    private val menuLabel: Label = Label(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        text = "Host Game",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    val addressText: TextField = TextField(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        text = "127.0.0.1:8080",
        prompt = "Server address: 127.0.0.1:8080"
    )
    
    val nameText: TextField = TextField(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        prompt = "Your Name"
    )
    
    val sessionIDText: TextField = TextField(
        height = MENU_ITEM_HEIGHT,
        width = MENU_ITEM_WIDTH,
        prompt = "sessionID"
    )

    val joinGameButton: Button = MenuButton("Host Game")
    
    val backButton: Button = MenuButton("← Back")
    
    init {
        addComponents(
            GridPane<UIComponent>(columns = 1, rows = 6, spacing = 15, layoutFromCenter = false).apply {
                this[0,0] = menuLabel
                this[0,1] = addressText
                this[0,2] = nameText
                this[0,3] = sessionIDText
                this[0,4] = joinGameButton
                this[0,5] = backButton
                
                setColumnWidth(0, this@MauMauHostGameMenuScene.width)
                setCenterMode(Alignment.CENTER)
            }
        )
    }
}

