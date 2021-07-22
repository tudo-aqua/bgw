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

In this section we are going to create a fully functional step-by-step example on the drag and drop 
feature from BGW.

We want to have a scene with a red token, a green token, a red area and a green area.
It should be possible to drag the red token into the red area but not into the green area.
Vice versa for the green token and area. Once the tokens have been dragged into a correct area, 
they should become non-draggable.

The complete source code for this example can be found at the bottom of the page.

## Prior knowledge
For this tutorial we assume, that you have knowledge of the following components and concepts.

- [BoardGameApplication]()
- [DynamicView]()
- [AreaContainerView]()
- [TokenView]()


## Component declaration

We declare the components that we are going to need in a subclass of 
[BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/).


````kotlin
class DragAndDropExample : BoardGameApplication("Drag and drop example") {
    val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

    val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
    val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

    val redArea: AreaContainerView<TokenView> =
        AreaContainerView(
            height = 50, 
            width = 50, 
            posX = 200, 
            posY = 20, 
            visual = ColorVisual(255, 0, 0, 100)
        )

    val greenArea: AreaContainerView<TokenView> =
        AreaContainerView(
            height = 50,
            width = 50,
            posX = 200,
            posY = 200,
            visual = ColorVisual(0, 255, 0, 100)
        )

    init {
        //the code referenced further down goes here
        
        gameScene.addElements(redToken, greenToken, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}
````

## Make an element draggable

The first thing we are always going to need, when dealing with drag and drop is a draggable element. 
In this case we want the ``redToken`` to be draggable. We can achieve that as follows:
````kotlin
redToken.isDraggable = true
````
After the desired drag and drop gesture was performed on the ``redToken`` we want it to be non-draggable.
In order to achieve that we set the ``onDragGestureEnded``, where we set ``isDraggable`` to ``false`` when the boolean indicates success.
````kotlin
redToken.onDragGestureEnded = { _, success ->
    if (success) {
        redToken.isDraggable = false 
    }
}
````
The ``greenToken`` gets initialized similarly with the following code:
````kotlin
greenToken.isDraggable = true
greenToken.onDragGestureEnded = { _, success ->
    if (success) {
        greenToken.isDraggable = false
    }
}
````

## Make an element act as a target for a drag and drop gesture

To make it possible that a drag and drop gesture is valid, we need another element that indicates, 
that the drag and gesture was a success.
If we want to allow an element to indicate success for a drag and drop gesture, we need to set the ``dropAcceptor``. 
It should return whether this element is a valid drop target for the dragged
element supplied in the 
[DragEvent](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-drag-event/) 
passed as an argument.
In this instance we want the ``redArea`` to only be a valid target for the ``redToken``,
hence we only return true if the ``draggedElement`` is a 
[TokenView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.elements.gameelements/-token-view/) 
and is equal to the ``redToken``.
````kotlin
redArea.dropAcceptor = { dragEvent ->
    when (dragEvent.draggedElement) {
        is TokenView -> dragEvent.draggedElement == redToken
        else -> false
    }
}
````
Now we also want to add the ``redToken`` to the ``redArea`` if it gets dropped on the ``redArea``.
To do that we set the ``onDragElementDropped`` in the ``redArea`` as follows:
````kotlin
redArea.onDragElementDropped = { dragEvent ->
    redArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
}
````
We apply the ``reposition`` function to the ``draggedElement``, because 
[AreaContainerView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.elements.container/-area-container-view/)
does not have automatic layout.

The ``greenArea`` gets initialized similarly with the following code:
````kotlin
greenArea.dropAcceptor = { dragEvent ->
    when (dragEvent.draggedElement) {
        is TokenView -> dragEvent.draggedElement == greenToken
        else -> false
    }
}

greenArea.onDragElementDropped = { dragEvent ->
    greenArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
}
````

## Useful hints when dealing with drag and drop

- ``dropAccpetor`` should not modify any state and only evaluate if the drag and drop gesture is valid.
- The order of invocation is as follows:
    - ``onDragGestureStarted`` on the dragged element.
    - ``onDragGestureMoved`` on the dragged element, while the drag and drop gesture is in motion.
    - ``dropAcceptor`` on all possible drop targets (elements that are under the mouse position).
    - ``onDragElementDropped`` on all valid drop targets (``dropAcceptor`` returned ``true``).
    - ``onDragGestureEnded`` on the dragged element.
  
- After a failed drag and drop gesture (no ``dropAcceptor`` returned true), the dragged element snaps back to the initial scene, container or layout.
- If the dragged element does not get added to the scene, a container or layout after a valid drag and drop gesture, it is no longer contained anywhere in the scene.
- Be careful when dealing with situations, where multiple ``dropAcceptors`` might return ``true``, because ``onDragElementDropped`` gets invoked on multiple elements and no 
    guarantee is given for the order of invocations.



## Complete source code for the example

[View it on GitHub](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/examples/concepts/draganddrop/DragAndDropExample.kt){: .btn }

````kotlin
fun main() {
    DragAndDropExample()
}

class DragAndDropExample : BoardGameApplication("Drag and drop example") {
    val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

    val redToken: TokenView = TokenView(posX = 20, posY = 20, visual = ColorVisual.RED)
    val greenToken: TokenView = TokenView(posX = 20, posY = 200, visual = ColorVisual.GREEN)

    val redArea: AreaContainerView<TokenView> =
        AreaContainerView(
            height = 50,
            width = 50,
            posX = 200,
            posY = 20,
            visual = ColorVisual(255, 0, 0, 100)
        )

    val greenArea: AreaContainerView<TokenView> =
        AreaContainerView(
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
            when (dragEvent.draggedElement) {
                is TokenView -> dragEvent.draggedElement == redToken
                else -> false
            }
        }
        redArea.onDragElementDropped = { dragEvent ->
            redArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
        }

        greenArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedElement) {
                is TokenView -> dragEvent.draggedElement == greenToken
                else -> false
            }
        }
        greenArea.onDragElementDropped = { dragEvent ->
            greenArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
        }

        gameScene.addElements(redToken, greenToken, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}
````