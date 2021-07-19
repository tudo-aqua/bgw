---
parent: Concepts
title: Drag and Drop
has_toc: true
nav_order: 2
layout: default
---

# Drag and Drop
{: .no_toc }

## Table of contents
{: .no_toc .text-delta }
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

## Component Declaration

We declare the components that we are going to need in a subclass of 
[BoardGameApplication](kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application)

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

## Initialization for drag and drop on areas

First we want to set the ``dropAcceptor``. It should return whether this element is a valid drop target for the dragged 
element supplied in the DragEvent passed as an argument. 
In this instance we want the redArea to only be a valid target for the redToken, 
hence we only return true if the draggedElement is a TokenView and is equal to the redToken.
````kotlin
redArea.dropAcceptor = { dragEvent ->
    when (dragEvent.draggedElement) {
        is TokenView -> dragEvent.draggedElement == redToken
        else -> false
    }
}
````
Now we also want to add the redToken to the redArea if it gets dropped on the redArea.
To do that we set the ``onDragElementDropped`` in the redArea as follows:
````kotlin
redArea.onDragElementDropped = { dragEvent ->
    redArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
}
````
We apply the ``reposition`` function to the ``draggedElement``, 
because [AreaContainerView](kotlin-docs/bgw-core/tools.aqua.bgw.elements.container/-area-container-view) 
does not have automatic layout.

The greenArea gets initialized similarly with the following code:
````kotlin
greenArea.dropAcceptor = {
    when (it.draggedElement) {
        is TokenView -> it.draggedElement == greenToken
        else -> false
    }
}

greenArea.onDragElementDropped = {
    greenArea.add((it.draggedElement as TokenView).apply { reposition(0,0) })
}
````

## Initialization for drag and drop on tokens

The tokens need to be draggable in order to register drag gestures. We do that as follows:
````kotlin
redToken.isDraggable = true
````
We also want the tokens to be non-draggable, whenever the drag and drop gesture was successful.
In order to achive that we set the ``onDragGestureEnded``, where we set ``isDraggable`` to ``false`` when the boolean indicates success.
````kotlin
redToken.onDragGestureEnded = { _, success ->
    if (success) {
        redToken.isDraggable = false 
    }
}
````
The greenToken gets initialized similarly with the following code:
````kotlin
greenToken.isDraggable = true
greenToken.onDragGestureEnded = { _, success ->
    if (success) {
        greenToken.isDraggable = false
    }
}
````

## Useful Hints when dealing with drag and drop

//Todo

## Complete source code for the example

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
        //initialize areas for drag and drop
        redArea.dropAcceptor = { dragEvent ->
            when (dragEvent.draggedElement) {
                is TokenView -> dragEvent.draggedElement == redToken
                else -> false
            }
        }
        redArea.onDragElementDropped = { dragEvent ->
            redArea.add((dragEvent.draggedElement as TokenView).apply { reposition(0,0) })
        }

        greenArea.dropAcceptor = {
            when (it.draggedElement) {
                is TokenView -> it.draggedElement == greenToken
                else -> false
            }
        }
        greenArea.onDragElementDropped = {
            greenArea.add((it.draggedElement as TokenView).apply { reposition(0,0) })
        }


        //initialize Tokens for drag and drop
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

        //add elements to scene and show scene
        gameScene.addElements(redToken, greenToken, redArea, greenArea)
        showGameScene(gameScene)
        show()
    }
}
````