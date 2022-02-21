package tools.aqua.bgw.examples.maumau.view.scenes


import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.entity.MauMauPlayer
import tools.aqua.bgw.examples.maumau.main.BUTTON_BG_FILE
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.util.Font.FontStyle
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * ViewController for the main menu scene.
 */
class MauMauPlayerWonScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {
    
    var playerWon : MauMauPlayer = MauMauPlayer("Alice")
        set(value) {
            field = value
            playerWonLabel.text = "${value.name} won the game."
        }
    
    private val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        text = "Game over!",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    private val playerWonLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 100,
        text = "",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
    
    val newGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        text = "New Game",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
    )
    
    val exitButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        text = "Exit",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
    )
    
    init {
        addComponents(
            menuLabel,
            playerWonLabel,
            newGameButton,
            exitButton,
        )
    }
}

