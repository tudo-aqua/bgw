---
layout: default title: Board Game Work nav_order: 1 has_children: false
---

[BGADoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html

[BGA#showGameSceneDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-game-scene.html

[BGA#showMenuSceneDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-menu-scene.html

[BGA#showDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show.html

[BGSDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-scene/index.html

[MSDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-menu-scene/index.html

[Scene#addComponents]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-scene/add-components.html

[StaticComponentViewDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-static-component-view/index.html

[LabelDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-label/index.html

[ButtonDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-button/index.html

[GameComponentDoc]:https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html

[ContainerDoc]:https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-game-component-container/index.html

[CardStackDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-card-stack/index.html

[LinearLayoutDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-linear-layout/index.html

[visualsTutorial]: https://tudo-aqua.github.io/bgw/concepts/visual/visual.html

# Board Game Work

This tutorial introduces the BoardGameWork framework. The upcoming section discusses the core features and how to set up
the first game scene. The running example implements the [MauMau](https://en.wikipedia.org/wiki/Mau-Mau_(card_game)) 
card game.

## Setup

Each application starts with a [BoardGameApplication][BGADoc] instance. By default, the view controller class should
inherit from [BoardGameApplication][BGADoc].

````kotlin
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau")
````

This creates a window in which the game can take place. For the game itself a [BoardGameScene][BGSDoc] and
a [MenuScene][MSDoc] is declared.

````kotlin
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {
    val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
    val mauMauGameScene: MauMauGameScene = MauMauGameScene()
}
````

````kotlin
class MauMauGameScene : BoardGameScene(background = ImageVisual("bg.jpg"))
````

````kotlin
class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE))
````

The menu scene gets a height of 500px and width of 300px while the game scene gets the default size of
FullHD. The game scene gets an image as background and the menu scene solid white. To read more about
visuals click [here][visualsTutorial].

## BoardGameScene and MenuScene

The MauMau example declares a game scene and a menu scene. In menu scenes draggable
components are not usable, only [LayoutViews](https://tudo-aqua.github.io/bgw/components/layout/layout.html) and 
[UIComponents](https://tudo-aqua.github.io/bgw/components/uicomponents/uicomponents.html): In other words components 
that extend [StaticComponentView][StaticComponentViewDoc].

A [BoardGameApplication][BGADoc] can display one [BoardGameScene][BGSDoc] and one [MenuScene][MSDoc] at the same time.
While the menu scene is visible, the game scene gets blurred out.

The scenes can be shown by calling [showGameScene()][BGA#showGameSceneDoc] and [showMenuScene()][BGA#showMenuSceneDoc]. 
<br>
The window gets shown by calling [show()][BGA#showDoc].

````kotlin
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {
    val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
    val mauMauGameScene: MauMauGameScene = MauMauGameScene()

    init {
        showGameScene(mauMauGameScene)
        showMenuScene(mauMauMenuScene)
        show()
    }
}
````

## Declaring a MenuScene

As shown above, the desired size of the scene can be passed as an argument to the super constructor. As with all
coordinates these are declared in a virtual coordinate space that will be transformed while rendering. Primary
constraint while choosing the scene size is the ratio of height and width as this is not changed by scaling. If the
ratio does not match the windows size, black bars will appear which can be styled using visuals as well.

All components declared in this scene will relate its position and size to the declared coordinate space.

In this example a [Label][LabelDoc] should display *"Main menu"* and three [Buttons][ButtonDoc]
display *"Continue"*, *"New Game"* and *"Exit"*. These components get declared and added to the scene by
calling [addComponents()][Scene#addComponents] in the initializer block.

````kotlin
class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

    val continueGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 110,
        text = "Continue",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
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

    private val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        text = "Main menu",
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
````

## Declaring a BoardGameScene

[BoardGameScenes][BGSDoc] are the main components of the game. BoardGameScenes behave just like menu scenes but can
additionally contain [GameComponentViews][GameComponentDoc] and [GameContainerViews][ContainerDoc].

For the MauMau example two [CardStacks][CardStackDoc] and two player hands as [LinearLayouts][LinearlayoutDoc] are 
necessary.

````kotlin
class MauMauGameScene : BoardGameScene(background = ImageVisual("bg.jpg")) {

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

    var otherPlayerHand: LinearLayout<CardView> = LinearLayout<CardView>(
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
````

## Event handler

When the application is started, both menu scene and game scene are shown. The game scene is blurred in the background.

![](assets/menu.png)
To start a new game and close the menu scene, event handlers have to be added. As these button's
actions change the scene, the handlers get set in the view controller.

````kotlin
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau") {
    val mauMauMenuScene: MauMauMenuScene = MauMauMenuScene()
    val mauMauGameScene: MauMauGameScene = MauMauGameScene()

    init {
        registerMenuEvents()

        showGameScene(mauMauGameScene)
        showMenuScene(mauMauMenuScene)
        show()
    }

    private fun registerMenuEvents() {
        mauMauMenuScene.continueGameButton.onMouseClicked = {
            hideMenuScene()
        }

        mauMauMenuScene.newGameButton.onMouseClicked = {
            //Start new game here
            hideMenuScene()
        }

        mauMauMenuScene.exitButton.onMouseClicked = {
            exit()
        }
    }
}
````

Now after pressing the *"New Game"* button the menu scene is hidden and the game scene is active.

![](assets/game.png)
