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
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[showGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-game-scene.html
[showMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-menu-scene.html
[showKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/-scene/add-components.html
[UIComponentDoc]: /guides/components/uicomponents
[LayoutViewDoc]: /guides/components/layout
[VisualsDoc]: /guides/concepts/visual
[DeclaringScenes]: /guides/declaring-scenes

# Getting Started

Creating a simple board game application begins with the instantiation of a [BoardGameApplication][BoardGameApplicationKDoc] object. Typically, you would create a singleton that extends [BoardGameApplication][BoardGameApplicationKDoc] to serve as the main controller for your game.

```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
)
```

The application can be started by calling the `show()` method inside the `init` block of the `MauMauApplication` object.

<!-- ```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
) {
    init {
        show()
    }
}
``` -->

> Because no scenes have been declared yet, the application will only display a black screen. To learn more about scenes and how to use them, continue with the [Declaring Scenes][DeclaringScenes] section.
> {style="warning"}
