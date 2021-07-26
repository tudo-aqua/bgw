package examples.maumau.view


import examples.main.BUTTON_BG_FILE
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.elements.uielements.Button
import tools.aqua.bgw.elements.uielements.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

@Suppress("SpellCheckingInspection")
class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    val continueGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 110,
        label = "Weiter",
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    val newGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        label = "Neues Spiel",
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    val exitButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        label = "Verlassen",
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
    ).apply {
        visual = ImageVisual(BUTTON_BG_FILE)
    }
    
    private val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        label = "Hauptmenü",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    init {
        addElements(
            menuLabel,
            continueGameButton,
            newGameButton,
            exitButton,
        )
    }
}

