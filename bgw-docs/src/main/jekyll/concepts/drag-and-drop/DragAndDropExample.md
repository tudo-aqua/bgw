---
parent: Concepts 
title: Drag and Drop 
has_toc: true 
nav_order: 3
layout: default
---

<!-- KDoc -->
[DragEventKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-drag-event/
[TokenKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-token-view/
[AreaKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.examples.components.container/-area/

<!-- GH-Pages Doc -->
[BoardGameApplicationDoc]: https://tudo-aqua.github.io/bgw/
[DynamicComponentViewDoc]: https://tudo-aqua.github.io/bgw/components/dynamiccomponent/dynamiccomponentview.html
[TokenDoc]: https://tudo-aqua.github.io/bgw/components/gamecomponents/gamecomponents.html#generic-token
[AreaDoc]: https://tudo-aqua.github.io/bgw/components/container/container.html#area

[DragDropExample]: https://tudo-aqua.github.io/bgw/concepts/drag-and-drop/DragAndDropExample.html#complete-source-code-for-the-example

<!-- Start Page -->
# Drag and Drop


{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

In this section we are going to create a fully functional step-by-step example for the drag and drop feature of the BGW
framework.

The goal of this example is to create a scene with a red and a green token, and a red and a green area. It should only
be possible to drag tokens into areas of the same color. Once the tokens have been dragged into a correct area, they
should become non-draggable.

The complete source code for this example can be
found [here][DragDropExample].

## Prior knowledge

Knowledge about the following components and concepts is necessary for this tutorial.

- [DynamicComponentView][DynamicComponentViewDoc]
- [BoardGameApplication][BoardGameApplicationDoc]
- [Token][TokenDoc]
- [Area][AreaDoc]

## Component declaration

To create a running example, the described components are wrapped in a
[BoardGameApplication][BoardGameApplicationDoc].

````kotlin
class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
  private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

  private val redArea: Area<TokenView> = Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 20,
      visual = ColorVisual(255, 0, 0, 100)
    )

  private val greenArea: Area<TokenView> = Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 200,
      visual = ColorVisual(0, 255, 0, 100)
    )

  init {
    //Drag and Drop code goes here

    gameScene.addComponents(redToken, greenToken, redArea, greenArea)
    showGameScene(gameScene)
    show()
  }
}
````

## Make a component draggable

To start of with this drag and drop example, a draggable component has to be declared. In this case the ``redToken``
should be draggable. This can be achieved by setting the following property:

````kotlin
redToken.isDraggable = true
````

After performing the desired drag and drop gesture on the ``redToken`` it should then be non-draggable. In order to
achieve that, the ``onDragGestureEnded`` event listener has to be adapted. After checking for a successful gesture,
the ``isDraggable`` is set to ``false``.

````kotlin
redToken.onDragGestureEnded = { _, success ->
    if (success) {
        redToken.isDraggable = false
    }
}
````

The ``greenToken`` gets initialized accordingly:

````kotlin
greenToken.isDraggable = true
greenToken.onDragGestureEnded = { _, success ->
    if (success) {
        greenToken.isDraggable = false
    }
}
````

## Make a component act as a target for a drag and drop gesture

To fully enable the drag and drop gesture, another component that indicates, that the drag and gesture was a success has
to be defined. To define accepting dropped components, the ``dropAcceptor`` property needs to be set for the receiving
component. The property should return whether this component is a valid drop target for the ``draggedComponent``
supplied in the
[DragEvent][DragEventKDoc]
passed as an argument. In this instance the ``redArea`` should only be accepting the ``redToken``, hence we only return
true if the ``draggedComponent`` is a
[Token][TokenKDoc]
and is equal to the ``redToken``.

````kotlin
redArea.dropAcceptor = { dragEvent ->
    when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == redToken
        else -> false
    }
}
````

Finally, to visually add the ``redToken`` to the ``redArea`` after it is successfully dropped, the
``onDragDropped`` of the ``redArea`` has to be adapted:

````kotlin
redArea.onDragDropped = { dragEvent ->
    redArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
}
````

The ``reposition`` function is applied to the ``draggedComponent``, because
[Area][AreaKDoc]
does not have an automatic layout algorithm.

The ``greenArea`` gets initialized accordingly:

````kotlin
greenArea.dropAcceptor = { dragEvent ->
    when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == greenToken
        else -> false
    }
}
greenArea.onDragDropped = { dragEvent ->
    greenArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
}
````

## Useful hints when dealing with drag and drop

- ``dropAccpetor`` should not modify any state and only evaluate if the drag and drop gesture is valid
- The order of invocation is as follows:
  - ``onDragGestureStarted`` on the dragged component
  - ``onDragGestureMoved`` on the dragged component, while the drag and drop gesture is in motion
  - ``dropAcceptor`` on all possible drop targets (components that are at the position of the mouse)
  - ``onDragDropped`` on all valid drop targets (``dropAcceptor`` returned ``true``)
  - ``onDragGestureEnded`` on the dragged component

- After a failed drag and drop gesture (no ``dropAcceptor`` returned ``true``), the dragged component snaps back to the
  previous container.
- If the dragged component does not get added to the scene, container or layout after a valid drag and drop gesture, it
  is no longer contained anywhere in the scene.
- Keep in mind that when dealing with situations, where multiple ``dropAcceptor`` invocations might return ``true``,
  that the event ``onDragDropped`` gets invoked on multiple components and no guarantee is given for the order of
  invocations.

## Complete source code for the example

[View it on GitHub](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-docs-examples/src/main/kotlin/examples/concepts/draganddrop/DragAndDropExample.kt){:
.btn }

````kotlin
fun main() {
  DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
  private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

  private val redArea: Area<TokenView> = Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 20,
      visual = ColorVisual(255, 0, 0, 100)
    )

  private val greenArea: Area<TokenView> = Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 200,
      visual = ColorVisual(0, 255, 0, 100)
    )

  init {
    redToken.isDraggable = true
    redToken.onDragGestureEnded = { _, success ->
      if (success) {
        redToken.isDraggable = false
      }
    }

    greenToken.isDraggable = true
    greenToken.onDragGestureEnded = { _, success ->
      if (success) {
        greenToken.isDraggable = false
      }
    }

    redArea.dropAcceptor = { dragEvent ->
      when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == redToken
        else -> false
      }
    }
    redArea.onDragDropped = { dragEvent ->
      redArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
    }

    greenArea.dropAcceptor = { dragEvent ->
      when (dragEvent.draggedComponent) {
        is TokenView -> dragEvent.draggedComponent == greenToken
        else -> false
      }
    }
    greenArea.onDragDropped = { dragEvent ->
      greenArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
    }

    gameScene.addComponents(redToken, greenToken, redArea, greenArea)
    showGameScene(gameScene)
    show()
  }
}
````
