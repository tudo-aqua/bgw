[LabelKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-label/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[DiceViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-dice-view/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/-scene/add-components.html
[FontKDoc]: /docs/tools.aqua.bgw.util/-font/index.html
[ColorKDoc]: /docs/tools.aqua.bgw.core/-color/index.html
[CompoundVisualKDoc]: /docs/tools.aqua.bgw.visual/-compound-visual/index.html
[TextVisualKDoc]: /docs/tools.aqua.bgw.visual/-text-visual/index.html

<!-- GH-Pages Doc -->

[DeclaringScenes]: /guides/declaring-scenes
[HandlingEvents]: /guides/handling-events
[GameComponentsDoc]: /guides/components/gamecomponents
[VisualDoc]: /guides/concepts/visual
[AdvancedUsage]: /guides/concepts/advanced-usage

# Using Components

<tldr>Declare components and add them to your scenes</tldr>

## Introduction

Components are the building blocks of a scene. Everything that is visible in a BGW application is a [ComponentView][ComponentViewKDoc]: a dice, a card, a button or a text label. This guide shows how components are declared, positioned and added to a scene by completing the Dice Duel scenes.

---

## Coordinates

Every component has a position and a size. Both are given in the **virtual coordinate space** of the scene it is placed in, see [Declaring Scenes][DeclaringScenes].

<signature>
<code-block lang="kotlin" copy="false">
posX: Double
posY: Double
width: Double
height: Double
</code-block>

The position of the **top left corner** of a component, relative to the top left corner of its parent. The origin `(0, 0)` is in the top left, `x` grows to the right and `y` grows downwards. `width` and `height` define the size.

</signature>

> A component that is added to a scene is positioned relative to the scene. A component that is added to a container or layout is positioned relative to that container or layout. Rotation and scaling of a parent also apply to all of its children.
> {style="note"}

The Dice Duel board is a scene of `890 × 600` units. Its center is at `(445, 300)`, so everything is arranged symmetrically around this point.

---

## Declaring components

Components are declared by calling their constructor. All constructor parameters are optional and have sensible defaults, so you only pass what you want to change. Named arguments keep declarations readable.

The score of Dice Duel is displayed in a [Label][LabelKDoc]:

```kotlin
val scoreLabel = Label(
    posX = 245,
    posY = 100,
    width = 400,
    height = 70,
    text = "0 : 0",
    font = Font(size = 56, color = Color(0xFFC656), fontWeight = Font.FontWeight.BOLD)
)
```

The text style is described by a [Font][FontKDoc] and its [Color][ColorKDoc]. A color can be created from a hex value like `Color(0xFFC656)`, from `Color(red, green, blue)` or by using one of the presets such as `Color.WHITE`.

Two more labels name the players. Their `font` only differs in color:

```kotlin
val playerLabel = Label(
    posX = 245,
    posY = 190,
    width = 160,
    height = 40,
    text = "You",
    font = Font(size = 24, color = Color(0x6DBEFF))
)

val opponentLabel = Label(
    posX = 485,
    posY = 190,
    width = 160,
    height = 40,
    text = "Opponent",
    font = Font(size = 24, color = Color(0xFA6C56))
)
```

> Most constructor parameters are also available as properties. Properties like `visual`, `text` or `isVisible` can be changed at any time after the component was created, even when it is already shown.
> {style="note"}

---

## Adding components

A declared component is not visible yet. It has to be added to a scene with [addComponents()][addComponentsKDoc]. Doing so in the `init` block of the scene keeps declaration and setup together:

```kotlin
class DiceDuelGameScene : BoardGameScene(
    width = 890,
    height = 600,
    background = ColorVisual(Color(0x161D29))
) {
    val scoreLabel = Label(/* ... */)
    val playerLabel = Label(/* ... */)
    val opponentLabel = Label(/* ... */)

    init {
        addComponents(scoreLabel, playerLabel, opponentLabel)
    }
}
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.gameSceneLabels" fit nocode></preview>

The **order matters**: components that are added later are drawn on top of earlier ones. To change the order afterwards use `toFront()`, `toBack()` or set the `zIndex` of a component. A component that was added to a scene has the scene's `rootNode` as its `parent`.

> A component can only be part of one scene or container at a time. Adding it to a second one throws an exception. Remove it first with `removeFromParent()`.
> {style="warning"}

---

## Game components

The labels only display text. The dice are [GameComponentViews][GameComponentsDoc]. A [DiceView][DiceViewKDoc] receives a list of [Visuals][VisualDoc], one for each side, and shows the one at the index `currentSide` (zero-based).

Each die face of Dice Duel is a [CompoundVisual][CompoundVisualKDoc] that stacks a white rounded square and the number as [TextVisual][TextVisualKDoc]. A small helper function creates all six faces:

```kotlin
private fun face(value: Int) = CompoundVisual(
    ColorVisual(Color.WHITE).apply { style.borderRadius = BorderRadius.LARGE },
    TextVisual(
        text = value.toString(),
        font = Font(size = 84, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD)
    )
)

val playerDice = DiceView(
    posX = 245,
    posY = 240,
    width = 160,
    height = 160,
    visuals = List(6) { face(it + 1) }
)

val opponentDice = DiceView(
    posX = 485,
    posY = 240,
    width = 160,
    height = 160,
    visuals = List(6) { face(it + 1) }
)
```

> Read more about visuals, rounded corners and stacking in the [Visual][VisualDoc] guide.
> {style="note"}

```kotlin
init {
    addComponents(
        scoreLabel, playerLabel, opponentLabel,
        playerDice, opponentDice
    )
}
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.gameSceneWithoutButton" fit nocode></preview>

---

## UI components

Finally the player needs a way to roll the dice. A [Button][ButtonKDoc] is a UI component with a text, a font and a `visual` that defines its background. The rounded corners are applied through the `style` of the visual.

```kotlin
val rollButton = Button(
    posX = 345,
    posY = 440,
    width = 200,
    height = 60,
    text = "Roll",
    font = Font(size = 22, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
    visual = ColorVisual(Color(0xBB6DFF)).apply {
        style.borderRadius = BorderRadius.MEDIUM
    }
)
```

With this button added, the game scene is complete:

```kotlin
init {
    addComponents(
        scoreLabel, playerLabel, opponentLabel,
        playerDice, opponentDice,
        rollButton
    )
}
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.gameScene" fit nocode></preview>

---

## Menu

A MenuScene accepts UI components and layouts only. The menu of Dice Duel consists of a title and two buttons. They follow the same pattern as above, the buttons only differ in text, color and position. For bigger projects, [reusing them][AdvancedUsage] in a custom button class is a good idea.

```kotlin
class DiceDuelMenuScene : MenuScene(
    width = 400,
    height = 280,
    background = ColorVisual(Color(0x0F141F))
) {
    val titleLabel = Label(
        posX = 0,
        posY = 30,
        width = 400,
        height = 60,
        text = "Dice Duel",
        font = Font(size = 36, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD)
    )

    val startButton = Button(
        posX = 80,
        posY = 110,
        width = 240,
        height = 56,
        text = "Start game",
        font = Font(size = 20, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
        visual = ColorVisual(Color(0xC6FF6E)).apply {
            style.borderRadius = BorderRadius.MEDIUM
        }
    )

    val quitButton = Button(
        posX = 80,
        posY = 190,
        width = 240,
        height = 56,
        text = "Quit",
        font = Font(size = 20, color = Color(0x0F141F), fontWeight = Font.FontWeight.BOLD),
        visual = ColorVisual(Color(0xFA6C56)).apply {
            style.borderRadius = BorderRadius.MEDIUM
        }
    )

    init {
        addComponents(titleLabel, startButton, quitButton)
    }
}
```

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.menuScene" fit nocode></preview>

> The positions in the menu are relative to the **menu scene**, not to the screen. A button at `posX = 150` in a menu that is 600 units wide is always centered, no matter where the menu is displayed.
> {style="note"}
