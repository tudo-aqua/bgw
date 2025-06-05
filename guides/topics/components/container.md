[BGADocs]: /docs/tools.aqua.bgw.core/-board-game-application/
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[PaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-pane/index.html
[GameComponentViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/
[ContainerKDoc]: /guides/bpwdgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.container/-game-component-container/index.html
[AreaKDoc]: /docs/tools.aqua.bgw.components.container/-area/index.html
[TokenKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html
[CardViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html
[CardStackKDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[SatchelKDoc]: /docs/tools.aqua.bgw.components.container/-satchel/index.html
[CameraPaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-camera-pane/index.html
[HexagonGridKDoc]: /docs/tools.aqua.bgw.components.container/-hexagon-grid/index.html
[HexagonKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-hexagon-view/index.html
[LayoutViewDoc]: /guides/components/layout
[ComponentViewDoc]: /guides/components/componentview
[GameComponentsDoc]: /guides/components/gamecomponents
[DynamicView]: /guides/components/dynamiccomponentview
[UIComponentsDoc]: /guides/components/uicomponents
[ContainerExample]: /guides/components/container
[IterableDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/
[HexDoc]: https://www.redblobgames.com/grids/hexagons/

# Container

<tldr>Components especially for laying out game elements</tldr>

## Introduction

Containers are specifically designed to be used to group and lay out [GameComponentViews][GameComponentsDoc].

[GameComponentContainer][ContainerKDoc] is the abstract baseclass for containers. Different implementations support different styles of layouting for the contained components.

All [GameComponentContainers][ContainerKDoc] inherit from [ComponentView][ComponentViewDoc] and [DynamicComponentView][DynamicView]. Because they are dynamic components, they can only be used in [BoardGameScenes][BoardGameSceneKDoc].
It is therefore helpful to read those documentations first as the features from those superclasses don't get repeated here.

Please also take a look at corresponding [GameComponents][GameComponentsDoc], [Layouts][LayoutViewDoc] and [UIComponents][UIComponentsDoc].

##### Components

For easy access to the components contained in a container, the `components` property is provided.

<signature>
<code-block lang="kotlin" copy="false">
components : List&lt;T&gt;
</code-block>

This property holds all components that are currently contained in the container.

</signature>

To add components to a container, the `add(...)` functions are used. To remove components, the `remove(...)` functions are used. Additionally, there are functions to add and remove multiple components at once.

<signature>
<code-block lang="kotlin" copy="false">
fun add(component: T, index: Int = components.size)
</code-block>

This function adds a component to the container at the specified index. If no index is provided, the component is added at the end of the components list.

<br>

<code-block lang="kotlin" copy="false">
fun addAll(collection: Collection&lt;T&gt;)
</code-block>

This function adds all components from the collection to the container.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
fun remove(component: T) : Boolean
</code-block>

This function removes the specified component from the container. It returns `true` if the component was removed successfully, `false` otherwise.

<br>

<code-block lang="kotlin" copy="false">
fun removeAll(collection: Collection&lt;T&gt;) : Boolean
</code-block>

This function removes all components from the collection from the container. It returns `true` if all components were removed successfully, `false` otherwise.

</signature>

To simply remove all components from a container, the `clear()` function can be used.

<signature>
<code-block lang="kotlin" copy="false">
fun clear() : List&lt;T&gt;
</code-block>

This function removes all components from the container without the need to provide a predicate and returns a list of the removed components.

</signature>

##### Listeners

Every [GameComponentContainer][ContainerKDoc] allows to track when components are added to or removed from itself by adding the `onAdd` and `onRemove` listeners.

<signature>
<code-block lang="kotlin" copy="false">
onAdd: (T.() -> Unit)? = null
</code-block>

This listener gets invoked when a component is added to the container. The added component is accessible through `this`.

<br>

<code-block lang="kotlin" copy="false">
onRemove: (T.() -> Unit)? = null
</code-block>

This listener gets invoked when a component is removed from the container. The removed component is accessible through `this`.

</signature>

---

> The following examples are visually accurate representations of BGW components based on the provided code snippets.
> All example listeners however are purely illustrative and do not execute any code when interacting with the components in this guide.
> {style="warning"}

<br>

## Area

The [Area][AreaKDoc] is the simplest form of a container. Its contained components are positioned relative to the top-left corner of the Area. No further layouting is provided thus working similar to a [Pane][PaneKDoc] but designed for [GameComponentViews][GameComponentViewKDoc]. Like every other [GameComponentContainer][ContainerKDoc] it can be made draggable.

The example shows two tokens with the first positioned relative to the scene, while the second is positioned relative to an area using the exact same coordinates. The area itself is shifted to the right thus the second token is also shifted to the right.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.areaExample">
val absoluteToken = TokenView(
    posX = 75,
    posY = 75,
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0x6dbeff)).apply {
        style.borderRadius = BorderRadius.FULL
    }
)
&#13;
val areaToken = TokenView(
    posX = 75,
    posY = 75,
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xbb6dff))
)
&#13;
val area = Area&lt;GameComponentView&gt;(
    posX = 225,
    posY = 0,
    width = 225,
    height = 225,
    visual = ColorVisual(Color(0x0f141f))
)
&#13;
area.add(areaToken)
</preview>

---

## CardStack

[CardStack][CardStackKDoc] is a special form of container. It can only contain [CardView][CardViewKDoc] and should be used to visualize card stacks. It provides automatic layouting and alignment features.

The example shows three cards stacked on top of each other. Each card is rotated slightly to give the stack a more realistic look. Despite the rotation, the cards are still aligned in the center of the stack.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.cardStackExample">
val redCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0xef4444))
)
&#13;
val orangeCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0xfa6c56))
)
&#13;
val yellowCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0xffc656))
)
&#13;
redCard.rotation = -6.0
orangeCard.rotation = 8.0
yellowCard.rotation = -1.0
&#13;
val cardStack = CardStack&lt;CardView&gt;(
    posX = 0,
    posY = 0,
    width = 100,
    height = 160,
    alignment = Alignment.CENTER
)
&#13;
cardStack.addAll(listOf(redCard, orangeCard, yellowCard))
</preview>

##### Deck Functionality

Because the [CardStack][CardStackKDoc] should behave like a real stack of cards and to streamline the process of adding and removing cards, the following functions are provided:

<signature>
<code-block lang="kotlin" copy="false">
fun push(cardView: T)
</code-block>

This function adds a component to the top of the stack.

<br>

<code-block lang="kotlin" copy="false">
fun pop() : T
</code-block>

This function removes and returns the component at the top of the stack.

<br>

<code-block lang="kotlin" copy="false">
fun peek() : T
</code-block>

This function returns the component at the top of the stack without removing it.

</signature>

---

## LinearLayout

A [LinearLayout][LinearLayoutKDoc] spaces its components dynamically based on its dimensions, the components dimensions and the user defined spacing. Additionally, an orientation and alignment may be specified.

In this example a LinearLayout is used to visualize a hand of cards aligned horizontally and centered. The spacing between the cards is set to `20`, but because the `width` of the LinearLayout is only `200`, the cards start to overlap and ignore the `spacing`. Otherwise the cards would be spaced evenly taking the `spacing` into account.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.linearLayoutExample">
val greenCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0xc6ff6e))
)
&#13;
val blueCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0x6dbeff))
)
&#13;
val purpleCard = CardView(
    width = 100,
    height = 160,
    front = ColorVisual(Color(0xbb6dff))
)
&#13;
val linearLayout = LinearLayout&lt;GameComponentView&gt;(
    posX = 0,
    posY = 0,
    width = 200,
    height = 160,
    orientation = Orientation.HORIZONTAL,
    alignment = Alignment.CENTER,
    spacing = 20
)
&#13;
linearLayout.addAll(listOf(greenCard, blueCard, purpleCard))
</preview>

---

## Satchel

A [Satchel][SatchelKDoc] hides its components and reveals them, when they are removed. This container can be used to visualize an
entity, where the user should not know what might get drawn next or what is in the container. All conatined components are automatically hidden and made draggable.

In this example a token is added to a satchel. The token is not visible until you drag it out of the satchel.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.satchelExample">
val hiddenToken = TokenView(
    width = 50,
    height = 50,
    visual = ColorVisual(Color(0x6dbeff)).apply { 
        style.borderRadius = BorderRadius.FULL 
    }
)
&#13;
val satchel = Satchel&lt;TokenView&gt;(
    posX = 0,
    posY = 0,
    width = 100,
    height = 100,
    visual = ColorVisual(Color(0x0f141f))
)
&#13;
satchel.add(hiddenToken)
</preview>

---

## HexagonGrid

As the name suggests, [HexagonGrids][HexagonGridKDoc] are grids of [HexagonViews][HexagonKDoc] in a coordinate system. Each hexagon can be accessed and manipulated using column and row coordinates.

> Both examples use a [CameraPane][CameraPaneKDoc] to allow for panning and zooming in order for you to better see the coordinates. The camera pane is neither part of the [HexagonGrid][HexagonGridKDoc] itself nor provided in the code snippets.
> {style="warning"}

##### Offset Coordinate System

A grid system where hexagons are laid out in a rectangular pattern with alternating offsets. Each hexagon's position is defined by column and row indices, similar to a traditional 2D grid. This makes it intuitive for grid-based game boards where hexagons need to align with a rectangular boundary.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.offsetHexagonGridExample">
val offsetHexagonGrid = HexagonGrid&lt;HexagonView&gt;(
    posX = 0,
    posY = 0,
    coordinateSystem = HexagonGrid.CoordinateSystem.OFFSET
)
&#13;
/* Create a 4x5 grid of hexagons */
for (x in 0..3) {
    for (y in 0..4) {
        val hexagon = HexagonView(
            visual = CompoundVisual(
                ColorVisual(Color(0xfa6c56)),
                TextVisual(
                    text = "$x, $y",
                    font = Font(10.0, Color(0x0f141f))
                )
            ), 
            size = 30
        )
        &#13;
        offsetHexagonGrid[x, y] = hexagon
    }
}
</preview>

##### Axial Coordinate System

A more natural coordinate system for hexagonal grids that uses two axes `(q, r)` instead of traditional `(x, y)`. The q-axis runs from top-left to bottom-right, while the r-axis runs vertically. This system eliminates the need for offset calculations and simplifies many hexagonal grid algorithms. Additionally, it is more intuitive for hexagonal game boards that expand in all directions.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.axialHexagonGridExample">
val axialHexagonGrid = HexagonGrid&lt;HexagonView&gt;(
    posX = 0,
    posY = 0,
    coordinateSystem = HexagonGrid.CoordinateSystem.AXIAL
)
&#13;
/* Create three rings of hexagons */
for (row in -2..2) {
    for (col in -2..2) {
        /* Only add hexagons that would fit in a circle */
        if(row + col in -2..2) {
            val hexagon = HexagonView(
                visual = CompoundVisual(
                    ColorVisual(Color(0xc6ff6e)),
                    TextVisual(
                        text = "$col, $row",
                        font = Font(10.0, Color(0x0f141f))
                    )
                ), 
                size = 30
            )
            &#13;
            axialHexagonGrid[col, row] = hexagon
        }
    }
}
</preview>

> For a more detailed explanation of hexagonal grids, please refer to this [documentation][HexDoc].

##### Components

To access and manipulate the hexagons in a [HexagonGrid][HexagonGridKDoc], the `get` and `set` operators are used. This allows for either accessing hexagons using the array access syntax as done in the examples or directly using the `get()` and `set()` functions.

<signature>
<code-block lang="kotlin" copy="false">
operator fun get(columnIndex: Int, rowIndex: Int) : T?
</code-block>

This function returns the hexagon at the specified column and row indices. If no hexagon exists at the specified indices, `null` is returned.

<br>

<code-block lang="kotlin" copy="false">
operator fun set(columnIndex: Int, rowIndex: Int, component: T)
</code-block>

This function sets the hexagon at the specified column and row indices. If a hexagon already exists at the specified indices, it is replaced.

</signature>

Because the [GameComponentContainer's][ContainerKDoc] default method of getting all components is not entirely suitable for a grid (because it would be missing the coordinates), the `getCoordinateMap()` function is provided:

<signature>
<code-block lang="kotlin" copy="false">
fun getCoordinateMap() : Map&lt;HexCoordinate, T&gt;
</code-block>

This function returns a map of all hexagons in the grid with their respective column and row indices using `HexCoordinate`, which is a simple typealias for `Pair<Int, Int>`.

</signature>
