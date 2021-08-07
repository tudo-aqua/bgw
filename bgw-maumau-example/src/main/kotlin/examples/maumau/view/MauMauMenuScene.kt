package examples.maumau.view


import examples.main.BUTTON_BG_FILE
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    val continueGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 110,
        label = "Continue",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    
    val newGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        label = "New Game",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC)
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    val exitButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        label = "Exit",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC)
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    
    private val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        label = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    init {
        addComponents(
            menuLabel,
            continueGameButton,
            newGameButton,
            exitButton,
        )
    }
}

