[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[GameComponentDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html
[TokenDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html
[CardDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[CardStackDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[LinearLayoutDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[DiceDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-dice-view/index.html
[DiceAnimationDoc]: /docs/tools.aqua.bgw.animation/-dice-animation/index.html
[HexagonDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-hexagon-view/index.html
[GameComponentKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/index.html
[ComponentView]: /guides/components/componentview
[DynamicComponentView]: /guides/components/dynamiccomponentview
[GameComponentsDoc]: /guides/components/gamecomponents
[LayoutViewDoc]: /guides/components/layout
[UIComponentsDoc]: /guides/components/uicomponents
[ContainerDoc]: /guides/components/container
[DnDDoc]: /guides/concepts/drag-and-drop
[AnimationDoc]: /guides/concepts/animations

# GameComponentViews

<tldr>Components for displaying game elements</tldr>

## Introduction

[GameComponentViews][GameComponentDoc] are used to model interactive game elements like cards, meeples or any other
form of game tokens.
GameComponentViews can be added to [GameComponentContainers][ContainerDoc] and be made draggable as explained in
[this][DnDDoc] tutorial.
Helpful animations can be found [here][AnimationDoc].

All [GameComponentViews][GameComponentKDoc] inherit from [ComponentView][ComponentView] and [DynamicComponentView][DynamicComponentView]. Because they are dynamic components, they can only be used in [BoardGameScenes][BoardGameSceneKDoc].
It is therefore helpful to read those documentations first as the features from those superclasses don't get repeated here.

Please also take a look at corresponding [Containers][ContainerDoc], [Layouts][LayoutViewDoc] and [UIComponents][UIComponentsDoc].

---

> The following examples are visually accurate representations of BGW components based on the provided code snippets.
> All example listeners however are purely illustrative and do not execute any code when interacting with the components in this guide.
> {style="warning"}

<br>

## TokenView

<tldr>Component for displaying simple game tokens</tldr>

The [TokenView][TokenDoc] is a versatile component for creating generic game tokens. It only requires position coordinates, dimensions, and a single visual element. Use TokenView when you need a simple, single-sided game piece.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.tokenExample">
val tokenRect = TokenView(
    posX = 0, 
    posY = 0,
    width = 100,
    height = 100,
    visual = ColorVisual(Color(0xc6ff6e))
)
&nbsp;
val tokenCircle = TokenView(
    posX = 180, 
    posY = 0,
    width = 100,
    height = 100,
    visual = ColorVisual(Color(0xffc656)).apply {
        style.borderRadius = BorderRadius.FULL 
    }
)
</preview>

---

## CardView

<tldr>Component for displaying playing cards</tldr>

[CardViews][CardDoc] are designed for implementing playing cards or any two-sided game component. They require front and back visuals and provide methods for card manipulation like flipping.

If you want to manage multiple cards, use [CardStack][CardStackDoc] for deck functionality. The [LinearLayout][LinearLayoutDoc] is ideal for displaying cards in a hand.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.cardExample">
val cardImage = CardView(
    posX = 0,
    posY = 0,
    width = 120,
    height = 200,
    front = ImageVisual("fortify.png")
)
&nbsp;
val cardBack = CardView(
    posX = 200,
    posY = 0,
    width = 120,
    height = 200,
    front = ImageVisual("fortify.png"),
    back = ImageVisual("back.png")
)
&nbsp;
/* Show the back side of the card */
cardBack.showBack()
</preview>

---

## DiceView

<tldr>Component for displaying customizable dice</tldr>

A [DiceView][DiceDoc] creates customizable two-dimensional dice with any number of sides. Each side corresponds to a visual in the provided list, where index 0 represents side 1.
If the list of visuals gets altered the amount of sides changes too and the dice will update accordingly.

There is also a dedicated [DiceAnimation][DiceAnimationDoc] for rolling effects.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.diceExample">
val diceDefaultSide = DiceView(
    posX = 0,
    posY = 0,
    width = 100,
    height = 100,
    visuals = listOf(ColorVisual(Color(0xef4444)))
)
&nbsp;
val diceCustomSide = DiceView(
    posX = 180,
    posY = 0,
    width = 100,
    height = 100,
    visuals = listOf(
        ColorVisual(Color(0xc6ff6e)),
        ColorVisual(Color(0xffc656)),
        ColorVisual(
            /* Orange color */
            Color(0xfa6c56)
        ),
        ColorVisual(Color(0xef4444)),
        ColorVisual(Color(0xbb6dff)),
        ColorVisual(Color(0x6dbeff))
    )
)
&nbsp;
/* Set the dice to show the orange side */
diceCustomSide.currentSide = 2
</preview>

---

## HexagonView

<tldr>Component for displaying hexagonal game pieces</tldr>

The [HexagonView][HexagonDoc] creates hexagonal game pieces with a single visual thus providing a versatile solution for displaying various hexagonal-shaped elements.

Every Hexagon by default has its tips pointing up and down defined by `HexOrientation.POINTY_TOP`. It can however also have its tips pointing left and right by passing `HexOrientation.FLAT_TOP` to the constructor.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.hexagonExample">
val hexagonPointy = HexagonView(
    posX = 0,
    posY = 0,
    size = 60,
    visual = ColorVisual(Color(0xbb6dff))
)
&#13;
val hexagonFlat = HexagonView(
    posX = 180,
    posY = 0,
    size = 60,
    visual = ColorVisual(Color(0x6dbeff)),
    orientation = HexOrientation.FLAT_TOP
)
</preview>
