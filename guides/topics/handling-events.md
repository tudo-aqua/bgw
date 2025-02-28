[Documentation]: /

# Handling Events

To make the game playable, we need to make our components interactive. This is achieved by incorporating event handlers into the components.

## Assigning Event Handlers

Upon launching the application, both the menu scene and the game scene are displayed, with the game scene appearing blurred in the background.

![](menu.png)

To initiate a new game and dismiss the menu scene, we need to assign event handlers. Since the actions of these buttons alter the scene, we set the handlers within the application.

```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
) {
    val mauMauMenuScene = MauMauMenuScene()
    val mauMauGameScene = MauMauGameScene()

    init {
        registerMenuEvents()

        showGameScene(mauMauGameScene)
        showMenuScene(mauMauMenuScene)
        show()
    }

    private fun registerMenuEvents() {
        mauMauMenuScene.continueGameButton.apply {
            onMouseClicked = {
                hideMenuScene()
            }
        }

        mauMauMenuScene.newGameButton.apply {
            onMouseClicked = {
                // Start new game logic here
                hideMenuScene()
            }
        }

        mauMauMenuScene.exitButton.apply {
            onMouseClicked = {
                exit()
            }
        }
    }
}
```

---

## Conclusion

Upon clicking the _"New Game"_ button, the menu scene is concealed, and the game scene becomes active. To render the game functional, it's necessary to assign handlers to the cards and implement the game logic.

The full implementation and other - more complex examples - can be found [here](https://github.com/tudo-aqua/bgw/tree/master/bgw-examples).

![](game.png)

> This concludes the basic example of creating your first BoardGameWork application. There are still many great features to discover, so feel free to explore the [Documentation][Documentation].
