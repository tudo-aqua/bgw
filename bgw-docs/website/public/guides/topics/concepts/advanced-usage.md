[UIComponentDoc]: /guides/components/uicomponents
[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[FontKDoc]: /docs/tools.aqua.bgw.util/-font/index.html

> This guide is currently work-in-progress. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

## Loading custom fonts

Most [UIComponents][UIComponentDoc] have a `font` parameter which can be passed to the constructor. In order to load a custom font use the `init`-block in your derived [BoardGameApplication][BoardGameApplicationKDoc] object.

```kotlin
// Load Roboto-Regular.ttf from root of resource folder
init {
    loadFont("Roboto-Regular.ttf", "Roboto", Font.FontWeight.NORMAL)
}
```

Now the font is registered and ready to use in your components. Make sure to use the specified font name and weight when creating a [Font][FontKDoc] object.

```kotlin
val label = Label(
    text = "I am a Label.",
    font = Font(family = "Roboto", size = 20, weight = Font.FontWeight.NORMAL)
)
```
