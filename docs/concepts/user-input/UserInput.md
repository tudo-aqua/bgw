---
parent: Concepts 
title: User Input 
has_toc: true 
nav_order: 3 
layout: default
---

[CompDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-component-view/index.html

# User Input
{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

The BGW framework uses events to communicate user input to Components. To execute code when a specific event is fired, a
function reference, or a function literal can be set in
[ComponentView][CompDoc]s. If components can be enabled for drag and drop, some additional handlers can be set.
Components can be enabled for drag and drop whenever they extend
[DynamicComponentView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-component-view/index.html).

For a more detailed introduction for Drag and Drop
head [here](https://tudo-aqua.github.io/bgw/concepts/drag-and-drop/DragAndDropExample.html).

The full source code for this example can be found [here](https://tudo-aqua.github.io/bgw/concepts/user-input/UserInput.html#full-example-on-all-available-methods-of-dealing-with-user-input).

## Component declaration

To showcase the user input handling, the following components are declared and wrapped inside a 
[BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html), 
to create a running example.

````kotlin
class UserInputExample : BoardGameApplication("User input example") {
    val button: Button = Button(height = 150, width = 300, posX = 30, posY = 30).apply {
		visual = ColorVisual.GREEN
	} 
    val token: TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)
    val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
}
````

## Function reference vs. function literal

There are two ways to specify code that should get executed when a specific event is fired.

The first option is assigning a function with a fitting signature. In the following example the ``handleMouseClicked``
function is declared and gets assigned to ``onMouseClicked`` on the ``button``. This is useful if the same code should
be assigned on different components, or if the reference gets removed and re-added frequently.

````kotlin
private fun handleMouseClicked(mouseEvent: MouseEvent) {
	button.text = "someone clicked on me!"
}

init {
	button.onMouseClicked = this::handleMouseClicked
}
````

The second option is assigning a function literal. In the following example a function literal is assigned to
the ``onMousePressed`` on the ``button``. This is useful if the functionality is set only once and only on one
component.

````kotlin
button.onMousePressed = { mouseEvent ->
	button.text = "pressed ${mouseEvent.button}"
}
````

## Distinction between ComponentViews, DynamicComponentViews and UIComponentViews

There are three types of components, that can handle user input differently. 

- [ComponentViews](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-component-view/index.html) have function references that can deal with mouse, key and drop events.

- [DynamicComponentViews](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-dynamic-component-view/index.html) are ComponentViews that can be enabled for drag and drop, so they have additional 
    function references to deal with that.
    
- [UIComponentViews](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html) are ComponentViews that may have additional ways of dealing with user input, 
    for example text input. Please refer to the UIComponentView 
  [doc](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html) 
  or [guide](https://tudo-aqua.github.io/bgw/components/uicomponents/uicomponents.html) 
  to find additional information.
  
## Full example on all available methods of dealing with user input

This example uses all available fields that can be set to handle user input on [ComponentViews](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-component-view/index.html) and [DynamicComponentViews](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-dynamic-component-view/index.html). 

[View it on GitHub](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/examples/concepts/userinput/UserInputExample.kt){:
.btn }

````kotlin
fun main() {
	UserInputExample()
}

class UserInputExample: BoardGameApplication("User input example") {

	val button : Button = Button(height = 150, width = 300, posX = 30, posY = 30).apply {
		visual = ColorVisual.GREEN
	}

	val token : TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)

	val gameScene : BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)

	private fun handleMouseClicked(mouseEvent: MouseEvent) {
		button.text = "someone clicked on me!"
	}

	init {
		//handling user input on ComponentView

		button.onMouseClicked = this::handleMouseClicked

		button.onMousePressed = { mouseEvent ->
			button.text = "pressed ${mouseEvent.button}"
		}
		button.onMouseReleased = { mouseEvent ->
			button.text = "released ${mouseEvent.button}"
		}
		button.onMouseEntered = {
			button.visual = ColorVisual.MAGENTA
		}
		button.onMouseExited = {
			button.visual = ColorVisual.GREEN
		}
		button.onKeyPressed = { keyEvent ->
			button.text = "pressed key: ${keyEvent.keyCode}"
		}
		button.onKeyReleased = { keyEvent ->
			button.text = "released key: ${keyEvent.keyCode}"
		}
		button.onKeyTyped = { keyEvent ->
			button.text = "typed key: ${keyEvent.character}"
		}
		button.dropAcceptor = { true }
		button.onDragDropped = {
			it.draggedComponent.reposition(500,30)
			it.draggedComponent.rotation = 0.0
			gameScene.addComponents(token)
		}
		button.onDragGestureEntered = { dragEvent ->
			button.visual = dragEvent.draggedComponent.visual
		}
		button.onDragGestureExited = {
			button.visual = ColorVisual.GREEN
		}

		//Additional function references available only to DynamicComponentViews

		token.isDraggable = true

		token.onDragGestureMoved = { token.rotate(5) }
		token.onDragGestureStarted = { token.scale(1.2) }
		token.onDragGestureEnded = { dropEvent, success ->
			if (success) token.resize(50,50)
		}

		showGameScene(gameScene.apply { addComponents(button, token) })
		show()
	}
}
````




