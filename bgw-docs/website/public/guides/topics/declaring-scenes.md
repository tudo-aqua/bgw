<!-- TODO -->

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
[GameComonentDoc]: /guides/components/gamecomponentviews
[UIComponentDoc]: /guides/components/uicomponents
[LayoutViewDoc]: /guides/components/layout
[VisualsDoc]: /guides/concepts/visual
[UsingComponents]: /guides/using-components

# Declaring Scenes

In a board game application, scenes serve as the fundamental layout elements. A single instance of [BoardGameApplication][BoardGameApplicationKDoc] can concurrently display one [BoardGameScene][BoardGameSceneKDoc] and one [MenuScene][MenuSceneKDoc].

> If a menu scene is visible, the active board game scene gets blurred out in the background.

## BoardGameScene

A [BoardGameScene][BoardGameSceneKDoc] is the primary scene for your game. In most cases, a single BoardGameScene is used throughout a project. It is responsible for showcasing the game board and all associated components. It can incorporate any kind of visual element, with a particular emphasis on [GameComponentViews][GameComponentKDoc] and [Containers][ContainerKDoc].

<chapter title="GameComponentViews" collapsible="true" default-state="expanded">
    <table style="header-column">
    <tr>
        <td width="20%">CardView</td>
        <td>Component for displaying a playing card with two visuals</td>
    </tr>
    <tr>
        <td>DiceView</td>
        <td>Component for displaying a dice with n visuals</td>
    </tr>
    <tr>
        <td id="hexagon-view-def">HexagonView</td>
        <td>Component for displaying a hexagon with one visual</td>
    </tr>
    <tr>
        <td>TokenView</td>
        <td>Component for displaying a token with one visual</td>
    </tr>
    </table>
</chapter>

<chapter title="Containers" collapsible="true" default-state="expanded">
    <table style="header-column">
    <tr>
        <td width="20%">Area</td>
        <td>Container for displaying a simple rectangular area</td>
    </tr>
    <tr>
        <td>CardStack</td>
        <td>Container for displaying a stack of n CardViews</td>
    </tr>
    <tr>
        <td>HexagonGrid</td>
        <td>Container for displaying a hexagonal grid of <a href="#hexagon-view-def">HexagonViews</a> with <a href="https://www.redblobgames.com/grids/hexagons/#coordinates">offset or axial coordinates</a></td>
    </tr>
    <tr>
        <td>LinearLayout</td>
        <td>Container for displaying a dynamically sized linear arrangement of n GameComponentViews</td>
    </tr>
    <tr>
        <td>Satchel</td>
        <td>Container for displaying a stack of n tokens</td>
    </tr>
    </table>
</chapter>

## MenuScene

While it's commonly sufficient to use a single BoardGameScene, the game might need multiple MenuScenes. Each MenuScene can be designed to serve a specific purpose. It can be customized with different components, however, it may only use [UIComponents][UIComponentDoc] and [LayoutViews][LayoutViewDoc] to create a unique user interface.

> You can switch between different MenuScenes based on user interactions.

<chapter title="UIComponents" collapsible="true" default-state="expanded">
    <table style="header-column">
        <tr>
            <td style="padding-left: 6rem;">Button</td>
            <td>Component for displaying a styled interactive button</td>
        </tr>
        <tr>
            <td>Label</td>
            <td>Component for displaying a styled text label</td>
        </tr>
        <tr>
            <td>TextField</td>
            <td>Component for displaying a styled text input field</td>
        </tr>
        <tr>
            <td>...</td>
            <td>
            <blockquote>
                A full list of all available UIComponents can be found <a href="components/uicomponents/uicomponents">here</a> {style="more"}.
            </blockquote></td>
        </tr>
    </table>
</chapter>

<chapter title="LayoutViews" collapsible="true" default-state="expanded">
    <table style="header-column">
    <tr>
        <td width="20%">CameraPane</td>
        <td width="100%">Layout container to use zoom and pan functionality in an infinite coordinate space</td>
    </tr>
    <tr>
        <td>GridPane</td>
        <td>Layout container to define a flexible grid with a dynamic size of columns and rows</td>
    </tr>
    <tr>
        <td>Pane</td>
        <td>Layout container to define a new coordinate space for absolute positioning</td>
    </tr>
    </table>
</chapter>

## Scene Creation

The first step of setting up scenes for our MauMau game involves the creation of two classes: `MauMauGameScene` and `MauMauMenuScene`, which inherit from the respective base classes.

```kotlin
class MauMauGameScene : BoardGameScene(
    background = ImageVisual("bg.jpg")
)
```

The `MauMauGameScene` is using a background image, which is added to the scene by passing it as a parameter to the constructor. The background visual is always rendered behind all other components.

```kotlin
class MauMauMenuScene : MenuScene(
    width = 300,
    height = 500,
    background = ColorVisual(Color.WHITE)
)
```

The `MauMauMenuScene` is configured with a `height` of 500<tooltip term="Pixels">px</tooltip> and a `width` of 300<tooltip term="Pixels">px</tooltip>, while the `MauMauGameScene` adopts the default <tooltip term="FullHD">FullHD</tooltip> size. The `MauMauGameScene` uses its background image, while a solid white backdrop will be displayed for the `MauMauMenuScene`.

> For further information on visuals, please visit the [Visual][VisualsDoc] section.
> {style="note"}

## Showing Scenes

The declared scenes can then be shown by calling [showGameScene()][showGameSceneKDoc] and [showMenuScene()][showMenuSceneKDoc] in the `init` block of the `MauMauApplication` object.

```kotlin
object MauMauApplication : BoardGameApplication(
    windowTitle = "MauMau"
) {
    val mauMauMenuScene = MauMauMenuScene()
    val mauMauGameScene = MauMauGameScene()

    init {
        showGameScene(mauMauGameScene)
        showMenuScene(mauMauMenuScene)
        show()
    }
}
```

> Because no components have been added to the scenes yet, the application will only display empty scenes. To learn more about adding components to your scene, continue with the [Using Components][UsingComponents] section.
> {style="warning"}
