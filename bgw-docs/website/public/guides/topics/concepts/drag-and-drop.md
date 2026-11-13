[DynamicComponentViewKDoc]: /docs/tools.aqua.bgw.components/-dynamic-component-view/index.html
[DragEventKDoc]: /docs/tools.aqua.bgw.event/-drag-event/index.html
[DropEventKDoc]: /docs/tools.aqua.bgw.event/-drop-event/index.html
[TokenViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html
[AreaKDoc]: /docs/tools.aqua.bgw.components.container/-area/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html

<!-- GH-Pages Doc -->

[DynamicComponentViewDoc]: /guides/components/dynamiccomponentview
[GameComponentsDoc]: /guides/components/gamecomponents
[ContainerDoc]: /guides/components/container
[ComponentViewDoc]: /guides/components/componentview
[UserInputDoc]: /guides/concepts/user-input

# Drag and Drop

<tldr>Let the player move components with the mouse</tldr>

## Introduction

Moving a card to a stack, a token to a field: drag and drop is how players move things in most board games. In BGW the feature is split into two roles:

- A **draggable component** is picked up with the mouse. Only [DynamicComponentViews][DynamicComponentViewDoc] can be draggable, which are all [game components][GameComponentsDoc] like tokens, cards, dice and hexagons.
- A **drop target** decides whether it accepts the dragged component. Every [ComponentView][ComponentViewDoc] can be a drop target, but typically it is a [container][ContainerDoc].

The framework takes care of the gesture itself: the component follows the mouse, the drop target is detected and an invalid drop makes the component snap back. Your code only answers two questions: _may this component be dragged?_ and _may it be dropped here?_.

Drag the tokens in the preview below. In the finished example, the red token fits into the red area and the green token into the green area.

<preview key="tools.aqua.bgw.main.examples.ExampleInteractionScene.dragAndDropExample" nocode></preview>

---

## Making a component draggable

A component is draggable if `isDraggable` is `true`. It is `false` by default.

<signature>
<code-block lang="kotlin" copy="false">
isDraggable: Boolean
isDragged: Boolean
</code-block>

`isDraggable` enables and disables the gesture. Set it to `false` for example when it is not the player's turn. `isDragged` is `true` while the component is being dragged and read-only.

</signature>

```kotlin
val redToken = TokenView(
    posX = 40,
    posY = 40,
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xEF4444)).apply {
        style.borderRadius = BorderRadius.FULL
    }
)

redToken.isDraggable = true
```

The drag gesture itself can be observed with three handlers, all receiving the mouse position in scene coordinates:

<signature>
<code-block lang="kotlin" copy="false">
onDragGestureStarted: ((DragEvent) -&gt; Unit)?
onDragGestureMoved: ((DragEvent) -&gt; Unit)?
onDragGestureEnded: ((DropEvent, Boolean) -&gt; Unit)?
</code-block>

Invoked when the component is picked up, while it is moved, and when it is released. The second parameter of `onDragGestureEnded` is `true` if the drop was accepted by the target. The [DropEvent][DropEventKDoc] contains the `draggedComponent` and the `dropTarget`, which is `null` if the component was released above nothing.

</signature>

A common use is a visual feedback while dragging, or locking a component after a successful move:

```kotlin
redToken.onDragGestureStarted = { redToken.scale(1.2) }
redToken.onDragGestureEnded = { _, success ->
    redToken.scale(1.0)
    if (success) {
        redToken.isDraggable = false
    }
}
```

---

## Creating a drop target

A drop target needs two things: a `dropAcceptor` that approves or rejects a component, and an `onDragDropped` handler that reacts to an accepted drop.

<signature>
<code-block lang="kotlin" copy="false">
dropAcceptor: ((DragEvent) -&gt; Boolean)?
</code-block>

Gets invoked with a [DragEvent][DragEventKDoc] while a component is dragged over the target. Return `true` if the `draggedComponent` may be dropped here. A component without a `dropAcceptor` is not a drop target. If the released component is not accepted, it snaps back to where it was.

<br>

<code-block lang="kotlin" copy="false">
onDragDropped: ((DragEvent) -&gt; Unit)?
</code-block>

Gets invoked after the component was released above the target **and** the `dropAcceptor` returned `true`. This is the place to change the scene.

<br>

<code-block lang="kotlin" copy="false">
onDragGestureEntered: ((DragEvent) -&gt; Unit)?
onDragGestureExited: ((DragEvent) -&gt; Unit)?
</code-block>

Gets invoked when a dragged component enters or leaves the target. They are ideal to highlight valid targets.

</signature>

In our example the red area only accepts the red token. The area is a container of type [Area][AreaKDoc], that holds game components without arranging them:

```kotlin
val redArea = Area<GameComponentView>(
    posX = 310,
    posY = 25,
    width = 100,
    height = 100,
    visual = ColorVisual(Color(0xEF4444)).apply { transparency = 0.3 }
)

redArea.dropAcceptor = { event ->
    event.draggedComponent == redToken
}
```

### Taking over the dragged component

When a drop was accepted, nothing moves automatically: after `onDragDropped` it is up to you where the dragged component belongs. In most games you add it to the target. As a component can only have one parent, it has to be removed from its old parent first:

```kotlin
redArea.onDragDropped = { event ->
    val token = event.draggedComponent as TokenView
    token.removeFromParent()
    token.reposition(15, 15)
    redArea.add(token)
}
```

An `Area` does not arrange its children, so the token is positioned manually relative to the area. Containers like `LinearLayout` or `CardStack` position their children themselves, so no `reposition` is needed for them.

> Do not change the scene in the `dropAcceptor`. It may be invoked several times per gesture, so it must not have side effects. Do all modifications in `onDragDropped`.
> {style="warning"}

---

## Complete example

The following scene combines everything. A helper function connects a token with its area, so red and green are set up with the same code:

```kotlin
class DragAndDropScene : BoardGameScene(
    width = 445,
    height = 300,
    background = ColorVisual(Color(0x0F141F))
) {
    private val redToken = TokenView(
        posX = 40, posY = 40, width = 70, height = 70,
        visual = ColorVisual(Color(0xEF4444)).apply { style.borderRadius = BorderRadius.FULL }
    )

    private val greenToken = TokenView(
        posX = 40, posY = 190, width = 70, height = 70,
        visual = ColorVisual(Color(0xC6FF6E)).apply { style.borderRadius = BorderRadius.FULL }
    )

    private val redArea = Area<GameComponentView>(
        posX = 310, posY = 25, width = 100, height = 100,
        visual = ColorVisual(Color(0xEF4444)).apply { transparency = 0.3 }
    )

    private val greenArea = Area<GameComponentView>(
        posX = 310, posY = 175, width = 100, height = 100,
        visual = ColorVisual(Color(0xC6FF6E)).apply { transparency = 0.3 }
    )

    init {
        connect(redToken, redArea)
        connect(greenToken, greenArea)
        addComponents(redToken, greenToken, redArea, greenArea)
    }

    private fun connect(token: TokenView, area: Area<GameComponentView>) {
        token.isDraggable = true
        token.onDragGestureStarted = { token.scale(1.2) }
        token.onDragGestureEnded = { _, success ->
            token.scale(1.0)
            if (success) {
                token.isDraggable = false
            }
        }

        area.dropAcceptor = { event -> event.draggedComponent == token }
        area.onDragDropped = {
            token.removeFromParent()
            token.reposition(15, 15)
            area.add(token)
        }
    }
}
```

Once a token has been dropped into its area, it can not be dragged anymore. Dropping it into the wrong area is rejected and the token returns.

> The runnable version of this example is part of the [documentation examples](https://github.com/tudo-aqua/bgw/tree/main/bgw-docs/examples/src/main/kotlin/examples/concepts/draganddrop/DragAndDropExample.kt) module.
> {style="note"}

---

## Order of events

During a gesture, BGW invokes the handlers in the following order:

<table orientation="horizontal">
    <tr>
        <td width="8%">1</td>
        <td width="32%"><code>onDragGestureStarted</code></td>
        <td>On the dragged component, once</td>
    </tr>
    <tr>
        <td>2</td>
        <td><code>onDragGestureMoved</code></td>
        <td>On the dragged component, with every mouse movement</td>
    </tr>
    <tr>
        <td>3</td>
        <td><code>onDragGestureEntered</code> / <code>onDragGestureExited</code></td>
        <td>On the drop targets below the mouse, whenever the mouse crosses a target</td>
    </tr>
    <tr>
        <td>4</td>
        <td><code>dropAcceptor</code></td>
        <td>On the drop target when the component is released, to check the drop</td>
    </tr>
    <tr>
        <td>5</td>
        <td><code>onDragDropped</code></td>
        <td>On the drop target, only if the <code>dropAcceptor</code> returned <code>true</code></td>
    </tr>
    <tr>
        <td>6</td>
        <td><code>onDragGestureEnded</code></td>
        <td>On the dragged component, with <code>success</code> telling whether the drop was accepted</td>
    </tr>
</table>

### Good to know

- A dragged component is drawn above everything else, so it is never hidden by other components during the gesture.
- If the component is released above a component that has **no** `dropAcceptor`, or above nothing, it snaps back to its previous position.
- If several drop targets are stacked above each other and accept the component, `onDragDropped` is invoked on each of them. There is no guarantee for the order. Make sure that only one target accepts a component.
- Disabled components (`isDisabled`) do not take part in drag and drop.
- Positions in the events are absolute positions in the scene. They can be negative or larger than the scene, if the mouse leaves it.
- If you add the dragged component to a container, its position is controlled by that container. If you do not add it anywhere after an accepted drop, it is not contained in the scene anymore.
