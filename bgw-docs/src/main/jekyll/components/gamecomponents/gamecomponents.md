---
parent: Components
title: GameComponents
nav_order: 3
layout: default
---

<!-- KDoc -->
[GameComponentDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html
[ContainerDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-game-component-container/index.html
[TokenDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html
[CardDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[CardStackDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-card-stack/index.html
[LinearLayoutDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-linear-layout/index.html
[DiceDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-dice-view/index.html
[DiceAnimationDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-dice-animation/index.html

<!-- GH-Pages Doc -->
[ComponentViewDoc]: https://tudo-aqua.github.io/bgw/components/componentview/componentview.html
[DynamicView]: https://tudo-aqua.github.io/bgw/components/dynamiccomponentview/dynamiccomponentview.html
[DnDDoc]: https://tudo-aqua.github.io/bgw/concepts/drag-and-drop/DragAndDropExample.html
[AnimationDoc]: https://tudo-aqua.github.io/bgw/concepts/animations/Animations.html

<!-- Start Page -->
# GameComponentViews

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

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