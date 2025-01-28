[GameComponentDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html
[ContainerDoc]: /docs/tools.aqua.bgw.components.container/-game-component-container/index.html
[TokenDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html
[CardDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[CardStackDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[LinearLayoutDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[DiceDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-dice-view/index.html
[DiceAnimationDoc]: /docs/tools.aqua.bgw.animation/-dice-animation/index.html
[HexagonDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-hexagon-view/index.html
[ComponentViewDoc]: /guides/components/componentview
[DynamicView]: /guides/components/dynamiccomponentview
[DnDDoc]: /guides/concepts/drag-and-drop
[AnimationDoc]: /guides/concepts/animations

# GameComponentViews

<tldr>
    <p><format style="bold">Components for displaying game elements</format></p>
    <p>→ &nbsp; <a href="http://">DynamicComponentView</a></p>
</tldr>

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

## Prior knowledge

All GameComponentViews inherit from [ComponentView][ComponentViewDoc] and [DynamicView][DynamicView].
It is therefore helpful to read those documentations first as the features from those superclasses don't get repeated here.

## Introduction

[GameComponentViews][GameComponentDoc] are used to model interactive game elements like cards, meeples or any other
form of game tokens.
GameComponentViews can be added to [GameComponentContainers][ContainerDoc] and be made draggable as explained in
[this][DnDDoc] tutorial.
Helpful animations can be found [here][AnimationDoc]

## Generic Token

For a generic token use [TokenView][TokenDoc]. It takes its position and dimensions as well as one visual as
a constructor parameter.

## Cards

For cards use [CardView][CardDoc]. It by default takes a front and back visual and offers additional functions to handle
the card.
As cards are a common concept, there exists a [CardStack][CardStackDoc] especially for CardViews.
For card hands, a [LinearLayout][LinearLayoutDoc] may become handy.

The CardView can also be used for other two-sided tokens.

## Dice

The [DiceView][DiceDoc] class takes a list of visuals for its sides.
It is not limited to a D6 and has the exact amount of sides as it has visuals, where the visual at index i is the i-1th
side e.g. the visual at index 0 is side 1.
If the list of visuals gets altered the amount of sides changes too.

For a dice roll there exists a dedicated [DiceAnimation][DiceAnimationDoc].

## Hexagons

The [HexagonView][HexagonDoc] component can also be utilized for hexagonal tokens,
providing a versatile solution for displaying various hexagonal-shaped elements.

Hexagons only have one visual.
