---
parent: Concepts 
title: Drag and Drop 
has_toc: true 
nav_order: 2 
layout: default
---

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
found [here](/bgw/concepts/drag-and-drop/DragAndDropExample.html#complete-source-code-for-the-example).

## Prior knowledge

Knowledge about the following components and concepts is necessary for this tutorial.

- [BoardGameApplication]()
- [DynamicComponent]()
- [Area]()
- [TokenView]()

## Component declaration

To create a running example, the described elements are wrapped in a
[BoardGameApplication](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/).

````kotlin
class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
  private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

  private val redArea: Area<TokenView> =
    Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 20,
      visual = ColorVisual(255, 0, 0, 100)
    )

  private val greenArea: Area<TokenView> =
    Area(
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

## Make an element draggable

To start of with this drag and drop example, a draggable element has to be declared. In this case the ``redToken``
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

## Make an element act as a target for a drag and drop gesture

To fully enable the drag and drop gesture, another element that indicates, that the drag and gesture was a success has
to be defined. To define accepting dropped elements, the ``dropAcceptor`` property needs to be set for the receiving
element. The property should return whether this element is a valid drop target for the ``draggedElement`` supplied in
the
[DragEvent](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-drag-event/)
passed as an argument. In this instance the ``redArea`` should only be accepting the ``redToken``, hence we only return
true if the ``draggedElement`` is a
[TokenView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.elements.gameelements/-token-view/)
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
``onDragElementDropped`` of the ``redArea`` has to be adapted:

````kotlin
redArea.onDragDropped = { dragEvent ->
    redArea.add((dragEvent.draggedComponent as TokenView).apply { reposition(0, 0) })
}
````

The ``reposition`` function is applied to the ``draggedElement``, because
[Area](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-area/)
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
    - ``onDragGestureStarted`` on the dragged element
    - ``onDragGestureMoved`` on the dragged element, while the drag and drop gesture is in motion
    - ``dropAcceptor`` on all possible drop targets (elements that are at the position of the mouse)
    - ``onDragElementDropped`` on all valid drop targets (``dropAcceptor`` returned ``true``)
    - ``onDragGestureEnded`` on the dragged element

- After a failed drag and drop gesture (no ``dropAcceptor`` returned ``true``), the dragged element snaps back to the
  previous container.
- If the dragged element does not get added to the scene, container or layout after a valid drag and drop gesture, it is
  no longer contained anywhere in the scene.
- Keep in mind that when dealing with situations, where multiple ``dropAcceptor`` invocations might return ``true``,
  that the event ``onDragElementDropped`` gets invoked on multiple elements and no guarantee is given for the order of
  invocations.

## Complete source code for the example

[View it on GitHub](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/examples/concepts/draganddrop/DragAndDropExample.kt){:
.btn }

````kotlin
fun main() {
  DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
  private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

  private val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
  private val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

  private val redArea: Area<TokenView> =
    Area(
      height = 50,
      width = 50,
      posX = 200,
      posY = 20,
      visual = ColorVisual(255, 0, 0, 100)
    )

  private val greenArea: Area<TokenView> =
    Area(
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
