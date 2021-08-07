---
layout: default
title: Board Game Work
nav_order: 1
has_children: false
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

[visualsTutorial]: https://tudo-aqua.github.io/bgw/concepts/visual/visual.html

# Board Game Work

In this tutorial we want to introduce you to BoardGameWork.
In this section we will discuss the core features and how to set up your first game scene.
Running example will be the first steps in implementing the MauMau card game.

## Setup

Each application starts with a [BoardGameApplication][BGADoc] instance. 
By default, your view controller class should inherit from BoardGameApplication.

````kotlin
class MauMauViewController : BoardGameApplication(windowTitle = "MauMau")
````

This creates a window in which the game can take place.
For the game itself we declare a [BoardGameScene][GBSDoc] and a [MenuScene][MSDoc].

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

As you can see the menu scene gets a height of 500px and width of 300px while the game scene gets the default size of FullHD.
The game scene gets an image as background and the menu scene solid white. Read more about visuals [here][visualsTutorial].

## BoardGameScene and MenuScene

In the MauMau example we have declared a game scene and a menu scene. 
In menu scenes you cannot use draggable components, only layouts and ui elements: In other words components that extend [StaticComponentView][StaticComponentViewDoc].

A [BoardGameApplication][BGADoc] can display one [BoardGameScene][BGSDoc] and one [MenuScene][MSDoc] at the same time. 
While the menu scene is shown, the game scene gets blurred out.

The scenes can be shown by calling [showGameScene][BGA#showGameSceneDoc] and [showMenuScene][BGA#showMenuSceneDoc]. <br>
The window gets shown by calling [show][BGA#showDoc].

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

As we have seen, we cann pass the desired size of the scene as an argument to the super constructor.
As with all coordinates these are declared in a virtual coordinate space that will be transformed while rendering.
Primary constraint while choosing the scene size is the ratio of height and width as this is not changed by scaling.
If the ratio does not match the windows size, black bars will appear which can be styled using visuals as well.

All components declared in this scene will relate its position and size to the declared coordinate space.

In this example we want to have a [Label][LabelDoc] displaying *"Main menu"* and three [Buttons][ButtonDoc] displaying *"Continue"*, *"New Game"* and *"Exit"*.
These components get declared and added to the scene by calling [addComponents][Scene#addComponents]

````kotlin
class MauMauMenuScene : MenuScene(width = 300, height = 500, background = ColorVisual(Color.WHITE)) {

    val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        label = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )
	
    val continueGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 110,
        label = "Continue",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
    )
    
    val newGameButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        label = "New Game",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
    )
    
    val exitButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        label = "Exit",
        font = Font(color = Color.WHITE, fontStyle = FontStyle.ITALIC),
        visual = ImageVisual(BUTTON_BG_FILE)
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