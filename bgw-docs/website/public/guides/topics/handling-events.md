[MouseEventKDoc]: /docs/tools.aqua.bgw.event/-mouse-event/index.html
[hideMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/hide-menu-scene.html
[exitKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/exit.html
[DiceAnimationKDoc]: /docs/tools.aqua.bgw.animation/-dice-animation/index.html
[ParallelAnimationKDoc]: /docs/tools.aqua.bgw.animation/-parallel-animation/index.html
[DialogKDoc]: /docs/tools.aqua.bgw.dialog/-dialog/index.html

<!-- GH-Pages Doc -->

[UserInputDoc]: /guides/concepts/user-input
[AnimationDoc]: /guides/concepts/animations
[DialogDoc]: /guides/dialogs/dialog
[ComponentViewDoc]: /guides/components/componentview
[ApplicationDoc]: /guides/concepts/application
[DragDropDoc]: /guides/concepts/drag-and-drop

# Handling Events

<tldr>React to the player and bring Dice Duel to life</tldr>

## Event handlers

BGW tells you what the player does through **events**. Every interactive property of a component is a nullable function, you assign a lambda to it and BGW invokes it when the event occurs. Assigning `null` removes the handler again.

```kotlin
rollButton.onMouseClicked = { event ->
    println("Clicked with ${event.button}")
}
```

The lambda receives an event object that describes what happened, for example the [MouseEvent][MouseEventKDoc] with the pressed mouse button and the position of the cursor. If you do not need it, simply omit the parameter and use the implicit `it`.

> All available events are described in [ComponentView][ComponentViewDoc] and [User Input][UserInputDoc].
> {style="note"}

---

## Menu

The menu of Dice Duel has two buttons. _Start game_ starts a new game and hides the menu, _Quit_ closes the window. Scene switching and exiting are functions of the application, so the handlers are assigned in the `init` block of the `DiceDuelApplication`, **before** `show()` is called:

```kotlin
object DiceDuelApplication : BoardGameApplication(
    windowTitle = "Dice Duel",
    width = 890,
    height = 600
) {
    private val menuScene = DiceDuelMenuScene()
    private val gameScene = DiceDuelGameScene()

    init {
        menuScene.startButton.onMouseClicked = {
            gameScene.showNewGame()
            hideMenuScene()
        }
        menuScene.quitButton.onMouseClicked = { exit() }

        showGameScene(gameScene)
        showMenuScene(menuScene)
        show()
    }
}
```

[hideMenuScene()][hideMenuSceneKDoc] fades the menu out and unlocks the game scene, [exit()][exitKDoc] closes the window and thereby makes `show()` return.

---

## Rolling the dice

The scene offers a function `showRoll()` that displays the result of a round: the sides the dice land on and the new score. The _Roll_ button only has to ask for the next round. The rules of the game are not part of this guide, `rollDice()` stands for whatever decides the outcome of a round in your game:

```kotlin
gameScene.rollButton.onMouseClicked = { rollDice() }
```

The scene then shows the result:

```kotlin
class DiceDuelGameScene : BoardGameScene(
    width = 890,
    height = 600,
    background = ColorVisual(Color(0x161D29))
) {
    // components as declared before ...

    fun showNewGame() {
        scoreLabel.text = "0 : 0"
        playerDice.currentSide = 0
        opponentDice.currentSide = 0
    }

    fun showRoll(
        playerSide: Int,
        opponentSide: Int,
        playerPoints: Int,
        opponentPoints: Int,
        playerWon: Boolean?   // null as long as the game continues
    ) {
        rollButton.isDisabled = true

        // play the animation, see below
    }
}
```

First, `rollButton.isDisabled = true` prevents the player from rolling again while the dice are still rolling. A disabled component does not receive any user input.

The dice should visibly tumble before they show the result, which is what [DiceAnimation][DiceAnimationKDoc] is made for. It shuffles through the sides of a die and lands on `toSide`. Both dice roll at the same time, so the two animations are combined in a [ParallelAnimation][ParallelAnimationKDoc]:

<previews key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollScene" codepoints="tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollScene,tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollAnimation" fit>
// showRoll(playerSide = 5, opponentSide = 2, ...)
playAnimation(
    ParallelAnimation(
        DiceAnimation(
            dice = playerDice,
            toSide = playerSide,
            duration = 1500
        ),
        DiceAnimation(
            dice = opponentDice,
            toSide = opponentSide,
            duration = 1500
        )
    )
)
</previews>

Press **Animate** to see the result. In the preview `playerSide` is `5` and `opponentSide` is `2`.

### Reacting when the animation ends

Animations are played **asynchronously**: `playAnimation()` returns immediately and the code after it runs while the dice are still rolling. The new score should only appear once the dice have stopped, so the label is updated in the `onFinished` handler of the animation:

```kotlin
fun showRoll(
    playerSide: Int,
    opponentSide: Int,
    playerPoints: Int,
    opponentPoints: Int,
    playerWon: Boolean?
) {
    rollButton.isDisabled = true

    playAnimation(
        ParallelAnimation(
            DiceAnimation(dice = playerDice, toSide = playerSide, duration = 1500),
            DiceAnimation(dice = opponentDice, toSide = opponentSide, duration = 1500)
        ).apply {
            onFinished = {
                scoreLabel.text = "$playerPoints : $opponentPoints"
                rollButton.isDisabled = false
                playerWon?.let { onGameOver?.invoke(it) }
            }
        }
    )
}
```

The handler writes the values it received into the score label and enables the button again. Changing `scoreLabel.text` is all it takes to update the screen, BGW takes care of the rest. The scene only displays the values it receives.

> The dice keep their final side because animations on components persist their result by default. Read more in [Animations][AnimationDoc].
> {style="note"}

---

## Ending the game

When a player has won, the scene tells the application, because only the application can open dialogs. Like BGW components, your own classes can offer nullable function properties. The scene gets one, which is invoked in the handler above:

```kotlin
class DiceDuelGameScene : BoardGameScene(/* ... */) {
    /** Gets invoked with `true` if the player won the game and `false` if the opponent did. */
    var onGameOver: ((Boolean) -> Unit)? = null

    // ...
}
```

The application assigns the handler and opens a [Dialog][DialogKDoc] that announces the winner. As soon as the player confirms, the menu is shown again:

```kotlin
gameScene.onGameOver = { playerWon ->
    val dialog = Dialog(
        dialogType = DialogType.INFORMATION,
        title = "Game over",
        header = if (playerWon) "You win!" else "You lose!",
        message = "Do you want to play another round?"
    )
    dialog.onButtonClicked = { showMenuScene(menuScene) }
    showDialog(dialog)
}
```

> Dialogs are explained in detail in [Dialogs][DialogDoc].
> {style="note"}

---

## Putting it together

Run the application. The menu is displayed above the blurred game board. _Start game_ reveals the board, _Roll_ spins both dice and the score label shows the result of every round.

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.menuOverGameScene" fit nocode></preview>

> The full source code of the game is available in the [bgw-docs-examples](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-docs-examples/src/main/kotlin/examples/discover/DiceDuelExample.kt) module.
> {style="note"}

---

## What's next

You have now seen the life cycle of the GUI of a BGW application. From here you can dive deeper:

- [Animations][AnimationDoc] for movement, rotation, flipping and chaining animations.
- [Drag and Drop][DragDropDoc] to let the player move components with the mouse.
- [User Input][UserInputDoc] for keyboard and mouse wheel events.
- [Application][ApplicationDoc] for window modes, fonts and more.
