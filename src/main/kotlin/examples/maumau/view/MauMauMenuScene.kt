package examples.maumau.view

import examples.main.BUTTON_BG_FILE
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.elements.uielements.Button
import tools.aqua.bgw.elements.uielements.Label
import tools.aqua.bgw.elements.uielements.TextField
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

@Suppress("SpellCheckingInspection")
class MauMauMenuScene : MenuScene(width = 300, height = 500) {
    
    val continueGameButton: Button = Button(height = 80, width = 200, posX = 50, posY = 110, label = "Weiter")
    val newGameButton: Button = Button(height = 80, width = 200, posX = 50, posY = 220, label = "Neues Spiel")
    val exitButton: Button = Button(height = 80, width = 200, posX = 50, posY = 330, label = "Verlassen")
    
    private val menuLabel: Label = Label(height = 100, width = 200, posX = 50, posY = 0, label = "Hauptmenü")
    
    init {
        background = ColorVisual(Color.WHITE)
        opacity = 0.7
    
        val visual = ImageVisual(BUTTON_BG_FILE)
        continueGameButton.visual = visual
        newGameButton.visual = visual
        exitButton.visual = visual
    
        addElements(
            menuLabel,
            continueGameButton,
            newGameButton,
            exitButton,
        )
    }
    
}

