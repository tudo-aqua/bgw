[UIComponentDoc]: /guides/components/uicomponents
[GettingStarted]: /guides/getting-started
[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application
[FontKDoc]: /docs/tools.aqua.bgw.util/-font
[ButtonDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button
[GridPaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout
[ContainerKDoc]: /docs/tools.aqua.bgw.components.container/-game-component-container
[ContainerDoc]: /guides/components/container
[GameComponentViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view
[GameComponentDoc]: /guides/components/gamecomponents
[TokenViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view
[TokenView]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view
[TextVisual]: /docs/tools.aqua.bgw.visual/-text-visual
[Enum]: https://kotlinlang.org/docs/enum-classes.html

# Advanced Usage

<tldr>Learn advanced concepts and best practices</tldr>

## Introduction

This guide covers advanced usage of BGW and its components, including common practices. It is assumed that you are already familiar with the basics of BGW and have read the [Getting Started][GettingStarted] guide.

---

## Reusing components

All components in BGW are designed to be inherited and extended. This allows you to create custom components with ease and reuse them in your application, eliminating the need to write the same code multiple times. This following example demonstrates how to create a custom button by extending the [Button][ButtonDoc] class.

The `ExampleButton` class extends the `Button` class and sets default values for the `posX`, `posY`, `width`, `height`, `text`, `font` parameters. In addition the `visual` parameter is set to a `ColorVisual` with a green color, while a border radius of 10 pixels is applied.

```kotlin
class ExampleButton(
    posX : Int = 0, posY : Int = 0, text : String = "Button"
) : Button(
    posX = posX,
    posY = posY,
    width = 300,
    height = 85,
    text = text,
    font = Font(24.0, Color(0x0F141F), "Arial", Font.FontWeight.BOLD),
    visual = ColorVisual(Color(0xBB6DFF)).apply {
        style.borderRadius = BorderRadius(10.0)
    }
)
```

This allows you to create buttons with the same style throughout your application. Because the constructor passes the values for `posX`, `posY`, and `text` directly to the [Button][ButtonDoc] class, you can create an `ExampleButton` instance without specifying any other parameters.

<preview key="tools.aqua.bgw.main.examples.AdvancedSceneComponents.exampleButton">
val exampleButton = ExampleButton(
    posX = 180,
    posY = 310,
    text = "Primary"
)
</preview>

For use in a [GridPane][GridPaneKDoc], even the `posX` and `posY` values can be omitted, because the `GridPane` will automatically position components in its layout.

<preview key="tools.aqua.bgw.main.examples.AdvancedSceneComponents.grid">
val grid = GridPane&lt;ComponentView&gt;(
    posX = 180,
    posY = 200,
    layoutFromCenter = false,
    rows = 3,
    columns = 1,
    spacing = 20
)
&#13;
val exampleButton1 = ExampleButton(text = "Primary")
val exampleButton2 = ExampleButton(text = "Secondary")
val exampleButton3 = ExampleButton(text = "Tertiary")
&#13;
grid[0,0] = exampleButton1
grid[0,1] = exampleButton2
grid[0,2] = exampleButton3
</preview>

The same is true for [LinearLayouts][LinearLayoutKDoc], but because [Container][ContainerKDoc] can only contain [GameComponentViews][GameComponentDoc], the `ExampleButton` cannot be added to a `LinearLayout`. Instead, we can define an `ExampleToken` class that extends [TokenView][TokenViewKDoc] and add it to a `LinearLayout`.

```kotlin
class ExampleToken(
    posX : Int = 0, posY : Int = 0, width : Int = 300, text : String = "Token"
) : TokenView(
    posX = posX,
    posY = posY,
    width = width,
    height = 85,
    visual = CompoundVisual(
        ColorVisual(Color(0x6DBEFF)).apply {
            style.borderRadius = BorderRadius(10)
        },
        TextVisual(
            text = text,
            font = Font(24.0, Color(0x0F141F), "Arial", Font.FontWeight.BOLD),
            alignment = Alignment.CENTER
        )
    )
)
```

Our `ExampleToken` class extends the [TokenView][TokenViewKDoc] class and, similarly to the `ExampleButton`, it sets default values for the `posX`, `posY`, `width`, `height`, `text`, and `visual` parameters. Its `visual` parameter is set to a `CompoundVisual` with a grayish color and again gets a border radius of 10 pixels.

A `TextVisual` is added to the `CompoundVisual` to display the text in the center of the token, as the `TokenView` class does not have a dedicated `text` parameter.

<preview key="tools.aqua.bgw.main.examples.AdvancedSceneComponents.linearLayout">
val linearLayout = LinearLayout&lt;GameComponentView&gt;(
    posX = 180,
    posY = 200,
    width = 300,
    height = 295,
    orientation = Orientation.VERTICAL,
    alignment = Alignment.CENTER,
    spacing = 20
)
&#13;
val exampleToken1 = ExampleToken(text = "Token 1", width = 300)
val exampleToken2 = ExampleToken(text = "Token 2", width = 250)
val exampleToken3 = ExampleToken(text = "Token 3", width = 200)
&#13;
linearLayout.addAll(
    exampleToken1,
    exampleToken2,
    exampleToken3
)
</preview>

> `CompoundVisual`s combine multiple visuals into one by layering them on top of each other.
> {style="note"}

---

## Defining color schemes

Color schemes are a great way to define a set of colors that can be reused throughout your application. This allows you to maintain a consistent look and feel across all components. The following example demonstrates a common way of defining a color scheme in BGW.

A good way to keep track of your colors throughout your application is to define constants for each color in a `Constants.kt`. You can then easily change the color of a component by changing the constant value. Additional constants can be defined for common properties, such as the width and height of buttons, or the width and height of the scene. This makes it easy to maintain a consistent look and feel across all components.

```kotlin
const val PRIMARY = 0xEF4444
const val SECONDARY = 0xFA6C56
const val TERTIARY = 0xFFC656
const val GRAY = 0x0F141F

val PRIMARY_COLOR = Color(PRIMARY)
val SECONDARY_COLOR = Color(SECONDARY)
val TERTIARY_COLOR = Color(TERTIARY)
val TEXT_COLOR = Color(GRAY)

// Further constants for common properties
val DEFAULT_BUTTON_WIDTH = 300
val DEFAULT_BUTTON_HEIGHT = 85

val SCENE_WIDTH = 660
val SCENE_HEIGHT = 710
```

Next we can make use of a [Kotlin Enum][Enum] to define different button themes. Each theme consists of a background and a foreground color, allowing you to easily switch between different themes by calling the `getBackground()` and `getForeground()` functions to retrieve the respective colors.

```kotlin
enum class Theme(
    private val background: Color,
    private val foreground: Color
) {
    PRIMARY(PRIMARY_COLOR, TEXT_COLOR),
    SECONDARY(SECONDARY_COLOR, TEXT_COLOR),
    TERTIARY(TERTIARY_COLOR, TEXT_COLOR);

    fun getBackground() = background
    fun getForeground() = foreground
}
```

Reusing the `ExampleButton` class from the previous example, we can now pass a `theme` parameter to the constructor to set the background and foreground colors of the button. The `theme` parameter defaults to `Theme.PRIMARY`, so you can create a button with the primary theme by simply calling `ExampleButton()`. All colors for the `font` and `visual` are automatically retrieved from the `Theme` enum.

```kotlin
class ExampleButton(
    text : String = "Button", theme : Theme = Theme.PRIMARY
) : Button(
    posX = 0,
    posY = 0,
    width = 300,
    height = 85,
    text = text,
    font = Font(24.0, theme.getForeground(), "Arial", Font.FontWeight.BOLD),
    visual = ColorVisual(theme.getBackground()).apply {
        style.borderRadius = BorderRadius(10.0)
    }
)
```

This allows you to easily switch between different themes by passing the respective `Theme` to the `ExampleButton` constructor.

<preview key="tools.aqua.bgw.main.examples.AdvancedSceneComponents.colorGrid">
val exampleButton1 = ExampleButton(
    text = "Primary",
    theme = Theme.PRIMARY
)
&#13;
val exampleButton2 = ExampleButton(
    text = "Secondary",
    theme = Theme.SECONDARY
)
&#13;
val exampleButton3 = ExampleButton(
    text = "Tertiary",
    theme = Theme.TERTIARY
)
</preview>

---

## Loading custom fonts

Most [UIComponents][UIComponentDoc] have a `font` parameter which can be passed to the constructor. In order to load a custom font use the `init`-block in your derived [BoardGameApplication][BoardGameApplicationKDoc] object.

```kotlin
// Load JetBrainsMono-Regular.ttf from root of resource folder
init {
    loadFont("JetBrainsMono-Regular.ttf", "JetBrains Mono", Font.FontWeight.NORMAL)
}
```

> The font file must be located in the resource folder to be loaded correctly and bundled with the application.
> {style="warning"}

Now the font is registered and ready to use in your components. Make sure to use the specified font name and weight when creating a [Font][FontKDoc] object.

<preview key="tools.aqua.bgw.main.examples.AdvancedSceneComponents.label">
val label = Label(
    text = "The quick brown fox jumps over the lazy dog.",
    font = Font(
        size = 20.0,
        color = Color(0xFFFFFF),
        family = "JetBrains Mono",
        fontWeight = Font.FontWeight.NORMAL
    )
)
</preview>

> The `font` parameter is also available on [TextVisuals][TextVisual] and can be used to apply text to any component that supports visuals.
