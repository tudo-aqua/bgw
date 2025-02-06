[LabelKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-label/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[CardStackKDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[CardViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/-scene/add-components.html
[AdvancedComponents]: /guides/concepts/advanced-components
[HandlingEvents]: /guides/handling-events

# Using Components

Components are the building blocks of a scene. They are the visual elements that are rendered to the screen.

## Coordinates and Scaling

All coordinates, including those for size and position, are defined in a virtual coordinate space that undergoes transformation during rendering. The primary constraint when determining the scene size is the aspect ratio, as it remains unchanged during scaling. If the aspect ratio doesn't match the window size, black bars may appear, which can be customized using visuals.

> All components added to a scene will have their position and size relative to the declared coordinate space (or layout element) they are added to.
> {style="note"}

---

## Declaring Components

Components are declared by instantiating them with the desired parameters. The following example shows how to declare a [Label][LabelKDoc] that displays _"Main menu"_.

```kotlin
val menuLabel = Label(
    posX = 50,
    posY = 0,
    height = 100,
    width = 200,
    text = "Main menu",
    font = Font(fontWeight = Font.FontWeight.BOLD)
)
```

> Components may inherit default values that can be overridden by passing the desired values to the constructor or by setting them after component instantiation.

Furthermore, we declare three [Buttons][ButtonKDoc] that display _"Continue"_, _"New Game"_ and _"Exit"_ for our `MauMauMenuScene`.

```kotlin
val continueGameButton = Button(
    posX = 50,
    posY = 110,
    height = 80,
    width = 200,
    text = "Continue",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)

val newGameButton = Button(
    posX = 50,
    posY = 220,
    height = 80,
    width = 200,
    text = "New Game",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)

val exitButton = Button(
    posX = 50,
    posY = 330,
    height = 80,
    width = 200,
    text = "Exit",
    font = Font(color = Color.WHITE),
    visual = ColorVisual.GREY
)
```

> The structure of all buttons is almost identical. For outsourcing them into a reusable component, please visit the [Advanced Components][AdvancedComponents] section.
> { style="note" }

---

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
        posX = 50,
        posY = 0,
        height = 100,
        width = 200,
        text = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )

    val continueGameButton = Button(
        posX = 50,
        posY = 110,
        height = 80,
        width = 200,
        text = "Continue",
        font = Font(color = Color.WHITE),
        visual = ColorVisual.GREY
    )

    val newGameButton = Button(
        posX = 50,
        posY = 220,
        height = 80,
        width = 200,
        text = "New Game",
        font = Font(color = Color.WHITE),
        visual = ColorVisual.GREY
    )

    val exitButton = Button(
        posX = 50,
        posY = 330,
        height = 80,
        width = 200,
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

To complete the example, we add the components to our `MauMauGameScene` as well. We create two [CardStacks][CardStackKDoc] for the draw stack and the game stack, as well as two [LinearLayouts][LinearLayoutKDoc] for the player's hands. All four components get the generic type `CardView` to indicate that they will contain [CardViews][CardViewKDoc].

> Notice how `otherPlayerHand` is rotated by 180 degrees to display the cards in the opposite direction. Because `rotation` is not a constructor parameter, it must be set after the component is instantiated by using the `apply` scope function.

```kotlin
class MauMauGameScene : BoardGameScene(
    background = ImageVisual("bg.jpg")
) {
    val drawStack: CardStack<CardView> = CardStack(
        posX = 750,
        posY = 360
        height = 200,
        width = 130,,
        visual = ColorVisual(255, 255, 255, 50)
    )

    val gameStack: CardStack<CardView> = CardStack(
        posX = 1040,
        posY = 360,
        height = 200,
        width = 130,
        visual = ColorVisual(255, 255, 255, 50)
    )

    var currentPlayerHand: LinearLayout<CardView> = LinearLayout(
        posX = 560,
        posY = 750,
        height = 220,
        width = 800,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    )

    var otherPlayerHand: LinearLayout<CardView> = LinearLayout(
        posX = 560,
        posY = 50,
        height = 220,
        width = 800,
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
