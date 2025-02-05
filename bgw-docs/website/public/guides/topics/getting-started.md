[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[DeclaringScenes]: /guides/declaring-scenes

# Getting Started

Creating a simple board game application begins with the creation of a [BoardGameApplication][BoardGameApplicationKDoc] object. Typically, you would use a singleton that extends [BoardGameApplication][BoardGameApplicationKDoc] to serve as the main controller for your game. This way you can easily access the application object from anywhere in your code.

```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
)
```

The application can be started by calling the `show()` method inside the `init` block of the `MauMauApplication` object. Alternatively, you can call `MauMauApplication.show()` directly from your main function. The `show()` function is a blocking call that will display the a new window and keep the application running until this window is closed.

```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
) {
    init {
        show()
    }
}
```

> Because no scenes have been declared yet, the application will only display a black screen. To learn more about scenes and how to use them, continue with the [Declaring Scenes][DeclaringScenes] section.
> {style="warning"}
