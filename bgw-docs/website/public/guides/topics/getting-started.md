[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[SceneKDoc]: /docs/tools.aqua.bgw.core/-scene/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[showKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show.html

<!-- GH-Pages Doc -->

[Installation]: /guides/installation
[DeclaringScenes]: /guides/declaring-scenes
[UsingComponents]: /guides/using-components
[HandlingEvents]: /guides/handling-events
[ApplicationDoc]: /guides/concepts/application
[ComponentDoc]: /guides/components/componentview

# Getting Started

<tldr>Open your first BGW window and learn how the pieces fit together</tldr>

## Big picture

Every BGW game is built from the same three layers. Understanding them makes everything else in the framework easy to place.

| Layer | Role |
| --- | --- |
| Application | The window. A single `BoardGameApplication` decides which scenes are shown and provides global features like dialogs and fonts. |
| Scenes | The canvases. A `BoardGameScene` holds the game itself, a `MenuScene` is shown on top for menus. See Declaring Scenes. |
| Components | Everything the player sees and touches: tokens, cards, dice, buttons, labels and containers. See Using Components. |

You never draw anything yourself. You create objects, put them into scenes and change their properties. BGW observes these changes and keeps the window up to date. User input flows the opposite way: when the player clicks a component, BGW invokes the event handler you assigned to it.

---

## Application

An application is created by extending [BoardGameApplication][BoardGameApplicationKDoc]. Because only one application can exist at a time, a Kotlin `object` is the natural fit: it is created once and reachable from anywhere in your code.

```kotlin
object DiceDuelApplication : BoardGameApplication(
    windowTitle = "Dice Duel"
)
```

Calling [show()][showKDoc] opens the window. The function **blocks until the window is closed**, so it is usually the last statement of the `init` block.

```kotlin
object DiceDuelApplication : BoardGameApplication(
    windowTitle = "Dice Duel"
) {
    init {
        show()
    }
}
```

An object is only initialized when it is first accessed, so the application still has to be touched from your `main` function:

```kotlin
fun main() {
    DiceDuelApplication
}
```

Start the application with `./gradlew run` and a window titled _Dice Duel_ opens. Without a scene to show, it stays empty.

> The `run` task works out of the box because the BGW Gradle plugin passes the JVM arguments that BGW needs. If you run `main` from your IDE instead, add these arguments to the run configuration as described in [Installation][Installation].
> {style="note"}

> The constructor takes more parameters like `width`, `height` and `windowMode`. They are covered in [Application][ApplicationDoc].
> {style="note"}

---

## What we are building

Over the next guides we build Dice Duel step by step. This is the destination, rendered by the very code we write:

<previews key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollScene" codepoints="tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollScene,tools.aqua.bgw.main.examples.ExampleDiscoverScene.rollAnimation" fit>
// The game board with both dice
val gameScene = DiceDuelGameScene()
&nbsp;
// What happens when the player presses "Roll"
playAnimation(
    ParallelAnimation(
        DiceAnimation(
            playerDice,
            toSide = 5,
            duration = 1500
        ),
        DiceAnimation(
            opponentDice,
            toSide = 2,
            duration = 1500
        )
    )
)
</previews>

Press **Animate** to roll the dice. Each player rolls one die per round, the higher number scores a point, and whoever reaches three points first wins.

The game is built in three steps:

1. **[Declaring Scenes][DeclaringScenes]**: create the menu and the game board.
2. **[Using Components][UsingComponents]**: fill the scenes with labels, buttons and dice.
3. **[Handling Events][HandlingEvents]**: make the buttons do something and play the game.

> The complete source code of Dice Duel is available in the [documentation examples](https://github.com/tudo-aqua/bgw/tree/main/bgw-docs/examples/src/main/kotlin/examples/discover/DiceDuelExample.kt) module.
> {style="note"}
