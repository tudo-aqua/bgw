[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[SceneKDoc]: /docs/tools.aqua.bgw.core/-scene/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[MenuSceneKDoc]: /docs/tools.aqua.bgw.core/-menu-scene/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[StaticComponentViewKDoc]: /docs/tools.aqua.bgw.components/-static-component-view/index.html
[showGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-game-scene.html
[showMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-menu-scene.html
[hideMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/hide-menu-scene.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/-scene/add-components.html
[ColorVisualKDoc]: /docs/tools.aqua.bgw.visual/-color-visual/index.html
[ImageVisualKDoc]: /docs/tools.aqua.bgw.visual/-image-visual/index.html
[lockKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/lock.html

<!-- GH-Pages Doc -->

[GettingStarted]: /guides/getting-started
[UsingComponents]: /guides/using-components
[VisualDoc]: /guides/concepts/visual
[ApplicationDoc]: /guides/concepts/application
[GameComponentsDoc]: /guides/components/gamecomponents
[UIComponentsDoc]: /guides/components/uicomponents
[LayoutDoc]: /guides/components/layout
[ContainerDoc]: /guides/components/container

# Declaring Scenes

<tldr>Scenes are the canvases your game is drawn on</tldr>

## Introduction

Everything a player sees in a BGW application lives inside a [Scene][SceneKDoc]. A scene owns a coordinate space, a background and a tree of components. The [BoardGameApplication][BoardGameApplicationKDoc] you created in [Getting Started][GettingStarted] shows up to **two scenes at the same time**: one [BoardGameScene][BoardGameSceneKDoc] and one [MenuScene][MenuSceneKDoc] on top of it.

If both are visible, the board game scene is blurred and locked for user input until the menu scene is hidden again.

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.menuOverGameScene" fit>
val menuScene = DiceDuelMenuScene()
val gameScene = DiceDuelGameScene()
&nbsp;
showGameScene(gameScene)
showMenuScene(menuScene)
</preview>

> The scenes in this preview belong to the _Dice Duel_ game that we are going to build throughout the next guides. By the end of this page, you will have created both of them.
> {style="note"}

---

## Choosing a scene type

Both scene types inherit from the sealed [Scene][SceneKDoc] class. They share the same constructor parameters and the same API for managing components, but they differ in what they are meant for and which components they accept.

| Property | BoardGameScene | MenuScene |
| --- | --- | --- |
| Purpose | The board, cards, dice and everything else that belongs to the game itself. | Menus, settings, pause screens and other overlays. |
| Accepted components | Every `ComponentView`: game components, containers, UI components and layouts. | Only `StaticComponentViews`, i.e. UI components and layouts. |
| Default size | 1920 × 1080 | 1920 × 1080 |
| Default background | Opaque white | White with a `backgroundOpacity` of 0.75, so the scene below shines through |
| Extras | `lock()`, `unlock()` and `onLockChanged` to block user input | `blurRadius` of the scene underneath |

> Drag and drop is only available in a `BoardGameScene`, because only [GameComponentViews][GameComponentsDoc] can be dragged.
> {style="note"}

---

## Creating scenes

A scene is declared by extending one of the two base classes. This keeps all components of a scene in one place and lets you reuse the scene wherever you need it.

##### BoardGameScene

<signature>
<code-block lang="kotlin" copy="false">
BoardGameScene(
    width: Number = 1920,
    height: Number = 1080,
    background: Visual = ColorVisual.WHITE
)
</code-block>

The first scene of Dice Duel is the table the game is played on. It is `890 × 600` units large and uses a dark [ColorVisual][ColorVisualKDoc] as background.

</signature>

```kotlin
class DiceDuelGameScene : BoardGameScene(
    width = 890,
    height = 600,
    background = ColorVisual(Color(0x161D29))
)
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.emptyGameScene" fit nocode></preview>

The scene is still empty, but it is already a fully functional canvas. We are adding components to it in [Using Components][UsingComponents].

##### MenuScene

<signature>
<code-block lang="kotlin" copy="false">
MenuScene(
    width: Number = 1920,
    height: Number = 1080,
    background: Visual = ColorVisual.WHITE,
    blurRadius: Double = 40.0
)
</code-block>

A menu rarely needs to fill the entire screen. Giving the menu scene a smaller `width` and `height` turns it into a panel that floats centered above the game.

</signature>

```kotlin
class DiceDuelMenuScene : MenuScene(
    width = 400,
    height = 280,
    background = ColorVisual(Color(0x0F141F))
)
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.emptyMenuScene" fit nocode></preview>

> A MenuScene applies a `backgroundOpacity` of `0.75` to its background by default. Set `backgroundOpacity = 1.0` if the game below should not be visible through it.
> {style="note"}

---

## Properties

All scene properties are shared by both scene types. For a complete list refer to the [Scene][SceneKDoc] API reference.

<signature>
<code-block lang="kotlin" copy="false">
width: Double
height: Double
</code-block>

The size of the scene in **virtual coordinates**. These values are fixed after the scene is created. Everything you place on the scene, positions as well as sizes, is expressed in this coordinate space and scaled to the real window by BGW.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
background: Visual
</code-block>

Any [Visual][VisualDoc] can be used as background of a scene: a plain color, an [ImageVisual][ImageVisualKDoc] or a stack of both. It is always rendered behind all components.

<br>

<code-block lang="kotlin" copy="false">
backgroundOpacity: Double
</code-block>

The opacity of the background between `0.0` and `1.0`. Components on the scene are not affected.

> The former `opacity` property is deprecated in favor of `backgroundOpacity`.
> {style="warning"}

</signature>

<signature>
<code-block lang="kotlin" copy="false">
components: List&lt;ComponentView&gt;
rootNode: RootComponent
</code-block>

All components that were added directly to the scene. Their `parent` is the scene's `rootNode`, which you can use to check whether a component was added directly to a scene.

</signature>

---

## Managing components

Components are managed with three functions. They are explained in more detail in [Using Components][UsingComponents].

<signature>
<code-block lang="kotlin" copy="false">
addComponents(vararg components: ComponentView)
addComponents(elements: Collection&lt;ComponentView&gt;)
</code-block>

Adds the components to the scene. The order matters: later components are drawn on top of earlier ones.

<br>

<code-block lang="kotlin" copy="false">
removeComponents(vararg components: ComponentView)
clearComponents()
</code-block>

Removes the given components, or all components, from the scene.

> A component can only have one parent. Adding a component that is already contained somewhere else, or adding the same component twice, throws an `IllegalStateException` and leaves the scene unchanged.
> {style="warning"}

</signature>

---

## Listeners

Scenes inform you about their lifecycle and about key presses that happen anywhere in the scene. Read more about key events in [User Input](/guides/concepts/user-input).

<signature>
<code-block lang="kotlin" copy="false">
onSceneShown: (() -&gt; Unit)?
onSceneHidden: (() -&gt; Unit)?
</code-block>

Gets invoked after the scene was rendered for the first time, or after it was hidden again. Use them to start background music, to focus a component or to kick off an intro animation.

<br>

<code-block lang="kotlin" copy="false">
onSceneRescaled: (() -&gt; Unit)?
</code-block>

Gets invoked whenever the scene is rescaled because the window was resized.

<br>

<code-block lang="kotlin" copy="false">
onKeyPressed: ((KeyEvent) -&gt; Unit)?
onKeyReleased: ((KeyEvent) -&gt; Unit)?
</code-block>

Global key listeners that fire independent of the focused component.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
onLockChanged: ((Boolean) -&gt; Unit)?
</code-block>

Only available in a `BoardGameScene`. Gets invoked whenever the scene is [locked][lockKDoc] or unlocked.

</signature>

---

## Showing scenes

Declaring a scene does not display it. Scenes are shown by the application, typically in its `init` block right before `show()` is called.

<signature>
<code-block lang="kotlin" copy="false">
showGameScene(scene: BoardGameScene)
</code-block>

Shows the given [BoardGameScene][showGameSceneKDoc]. Calling it again replaces the scene that is currently shown.

<br>

<code-block lang="kotlin" copy="false">
showMenuScene(scene: MenuScene, fadeTime: Number = 250)
hideMenuScene(fadeTime: Number = 250)
</code-block>

[Shows][showMenuSceneKDoc] or [hides][hideMenuSceneKDoc] a menu scene with a fade of `fadeTime` milliseconds. While a menu scene is visible, the board game scene is blurred and cannot receive input. After the menu scene is hidden, the game scene becomes active again.

</signature>

Our Dice Duel application declares both scenes once and shows them together. The game starts with the menu on top:

```kotlin
object DiceDuelApplication : BoardGameApplication(
    windowTitle = "Dice Duel",
    width = 890,
    height = 600
) {
    private val menuScene = DiceDuelMenuScene()
    private val gameScene = DiceDuelGameScene()

    init {
        showGameScene(gameScene)
        showMenuScene(menuScene)
        show()
    }
}
```

> Show the scenes **before** calling `show()`: the function blocks until the window is closed, so everything after it only runs after the application has ended.
> {style="warning"}

---

## Size and scaling

The `width` and `height` of a scene do not describe pixels, they describe a coordinate space. BGW works with a **virtual screen** that is defined by the `width` and `height` of the [BoardGameApplication][BoardGameApplicationKDoc] (Full&nbsp;HD by default, `890 × 600` for Dice Duel). This virtual screen is scaled uniformly to the real window, and the scenes are placed inside of it.

- A scene of the same size as the application fills the whole screen. This is the case for the `DiceDuelGameScene`, which is why we also pass `width` and `height` to the `DiceDuelApplication` above.
- A smaller scene, like the `DiceDuelMenuScene`, is centered on the virtual screen. Use `setSceneAlignment()` to align all scenes differently.
- Resizing the window scales everything, you never need to react to the window size to keep your layout intact.
- If the window has a different aspect ratio than the application, empty bars appear next to the virtual screen. They show the `background` of the application, which is independent of the scene backgrounds. See [Application][ApplicationDoc] to change it.
