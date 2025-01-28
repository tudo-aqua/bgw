[MauMauRules]: https://en.wikipedia.org/wiki/Mau_Mau_(card_game)
[BGW]: https://github.com/tudo-aqua/bgw
[JavaFX 17]: https://openjfx.io/openjfx-docs/
[AzulZuluOpenJDK]: https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk
[Mac M1]: https://www.azul.com/downloads/?version=java-11-lts&os=macos&architecture=arm-64-bit&package=jdk-fx#download-openjdk
[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[MenuSceneKDoc]: /docs/tools.aqua.bgw.core/-menu-scene/index.html
[GameComponentKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html
[StaticComponentViewKDoc]: /docs/tools.aqua.bgw.components/-static-component-view/index.html
[LabelKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-label/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[ContainerKDoc]: /docs/tools.aqua.bgw.components.container/-game-component-container/index.html
[CardStackKDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[CardViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[showGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-game-scene.html
[showMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-menu-scene.html
[showKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/-scene/add-components.html
[UIComponentDoc]: /guides/components/uicomponents
[LayoutViewDoc]: /guides/components/layout
[VisualsDoc]: /guides/concepts/visual
[DeclaringScenes]: /guides/declaring-scenes
[AdvancedComponents]: /guides/advanced-components
[HandlingEvents]: /guides/handling-events

# Using Components

Components are the building blocks of a scene. They are the visual elements that are rendered to the screen.

## Coordinates and Scaling

All coordinates, including those for size and position, are defined in a virtual coordinate space that undergoes transformation during rendering. The primary constraint when determining the scene size is the aspect ratio, as it remains unchanged during scaling. If the aspect ratio doesn't match the window size, black bars may appear, which can be customized using visuals.

> All components added to a scene will have their position and size relative to the declared coordinate space (or layout element) they are added to.
> {style="note"}

## Declaring Components

Components are declared by instantiating them with the desired parameters. The following example shows how to declare a [Label][LabelKDoc] that displays _"Main menu"_.

```kotlin
    val menuLabel = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        text = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
```

> Components may inherit default values that can be overridden by passing the desired values as parameters.

Furthermore, we declare three [Buttons][ButtonKDoc] that display _"Continue"_, _"New Game"_ and _"Exit"_ for our `MauMauMenuScene`.

```kotlin
val continueGameButton = Button(
    height = 80,
    width = 200,
    posX = 50,
    posY = 110,
    text = "Continue",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)

val newGameButton = Button(
    height = 80,
    width = 200,
    posX = 50,
    posY = 220,
    text = "New Game",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)

val exitButton = Button(
    height = 80,
    width = 200,
    posX = 50,
    posY = 330,
    text = "Exit",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)
```

> The structure of all buttons is almost identical. For outsourcing them into a reusable component, please visit the [Advanced Components][AdvancedComponents] section.
> { style="note" }

## Adding Components

Components can be added to a scene using the `addComponents` method. The order in which components are added to a scene determines the order in which they are rendered. Components can be removed from a scene using the `removeComponents` method.

In this example the components get declared as properties of the scene and added to the scene by
calling [addComponents()][addComponentsKDoc] in the initializer block.

```kotlin
class MauMauMenuScene : MenuScene(
    width = 300,
    height = 500,
    background = ColorVisual(Color.WHITE)
) {
    val menuLabel = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        text = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )

    val continueGameButton = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 110,
        text = "Continue",
        font = Font(color = Color.WHITE),
        visual = ColorVisual.GREY
    )

    val newGameButton = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        text = "New Game",
        font = Font(color = Color.WHITE),
        visual = ColorVisual.GREY
    )

    val exitButton = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        text = "Exit",
        font = Font(color = Color.WHITE),
        visual = ColorVisual.GREY
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
```

To complete the example, we add the components to our `MauMauGameScene` as well. We create two [CardStack][CardStackKDoc] for the draw stack and the game stack, as well as two [LinearLayouts][LinearLayoutKDoc] for the player's hands. All four components get the generic type `CardView` to indicate that they will contain [CardViews][CardViewKDoc].

```kotlin
class MauMauGameScene : BoardGameScene(
    background = ImageVisual("bg.jpg")
) {
    val drawStack: CardStack<CardView> = CardStack(
        height = 200,
        width = 130,
        posX = 750,
        posY = 360,
        visual = ColorVisual(255, 255, 255, 50)
    )

    val gameStack: CardStack<CardView> = CardStack(
        height = 200,
        width = 130,
        posX = 1040,
        posY = 360,
        visual = ColorVisual(255, 255, 255, 50)
    )

    var currentPlayerHand: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 800,
        posX = 560,
        posY = 750,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    )

    var otherPlayerHand: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 800,
        posX = 560,
        posY = 50,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    ).apply {
        rotation = 180.0
    }

    init {
        addComponents(
            drawStack,
            gameStack,
            currentPlayerHand,
            otherPlayerHand
        )
    }
}
```

> We should finally see the components rendered to the screen, although the game has no interactions yet.
>
> To learn more about handling user interaction, continue with the [Handling Events][HandlingEvents] section.
> {style="warning"}
