[BoardGameApplicationKDoc]: //docs/tools.aqua.bgw.core/-board-game-application/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[ComponentViewKDoc]: //docs/tools.aqua.bgw.components/-component-view/index.html
[DynamicComponentViewKDoc]: /docs/tools.aqua.bgw.components/-dynamic-component-view/index.html
[UIComponentViewKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html
[MouseEventKDoc]: /docs/tools.aqua.bgw.event/-mouse-event/index.html
[MouseButtonTypeKDoc]: /docs/tools.aqua.bgw.event/-mouse-button-type/index.html
[KeyEventKDoc]: /docs/tools.aqua.bgw.event/-key-event/index.html
[DnDExample]: /guides/drag-and-drop/DragAndDropExample
[UIComponentViewDoc]: /guides/components/uicomponents/uicomponents

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# User Input

The BGW framework uses events to communicate user input to Components. To execute code when a specific event is fired, a
function reference, or a function literal can be set in
[ComponentView][ComponentViewKDoc]s. If /components can be enabled for drag and drop, some additional handlers can be set.
Components can be enabled for drag and drop whenever they extend
[DynamicComponentView][DynamicComponentViewKDoc].

For a more detailed introduction for Drag and Drop
head [here][DnDExample].

## Component declaration

To showcase the user input handling, the following /components are declared and wrapped inside a
[BoardGameApplication][BoardGameApplicationKDoc],
to create a running example.

```kotlin
class UserInputExample : BoardGameApplication("User input example") {
    val button: Button = Button(height = 150, width = 300, posX = 30, posY = 30).apply {
    visual = ColorVisual.GREEN
  }

  val token: TokenView = TokenView(posX = 500, posY = 30, visual = ColorVisual.RED)
  val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
}
```

## MouseEvents

There are five types of [MouseEvents][MouseEventKDoc]. The event contains the [MouseButtonType][MouseButtonTypeKDoc] that was in action.

- **onMousePressed**: The user pressed a button while the mouse was above this component.
- **onMouseReleased**: The user released a button while the mouse was above this component.
- **onMouseClicked**: The user clicked above this component, i.e. pressed and released.
- **onMouseEntered**: The mouse entered this component. Active button is UNSPECIFIED in this case.
- **onMouseExited**: The mouse left this component. Active button is UNSPECIFIED in this case.

## Key events

- **onKeyPressed**: The user pressed a key while this component had focus.
- **onKeyReleased**: The user released a key while this component had focus.
- **onKeyTyped**: The user typed a key, i.e. pressed and released or pressed and held, while this component had focus.
  `keyCode` will be Undefined in this case and the character field has to be used instead.

## Function reference vs. function literal

There are two ways to specify code that should get executed when a specific event is fired.

The first option is assigning a function with a fitting signature. In the following example the `handleMouseClicked`
function is declared and gets assigned to `onMouseClicked` on the `button`. This is useful if the same code should
be assigned on different /components, or if the reference gets removed and re-added frequently.

```kotlin
private fun handleMouseClicked(mouseEvent: MouseEvent) {
  button.text = "someone clicked on me!"
}

init {
  button.onMouseClicked = this::handleMouseClicked
}
```

The second option is assigning a function literal. In the following example a function literal is assigned to
the `onMousePressed` on the `button`. This is useful if the functionality is set only once and only on one
component.

```kotlin
button.onMousePressed = { mouseEvent ->
  button.text = "pressed ${mouseEvent.button}"
}
```

## Distinction between ComponentViews, DynamicComponentViews and UIComponentViews

There are three types of /components, that can handle user input differently.

- [ComponentViews][ComponentViewKDoc] have function references that can deal with mouse, key and drop events.

- [DynamicComponentViews][DynamicComponentViewKDoc] are ComponentViews that can be enabled for drag and drop, so they have additional
  function references to deal with that.
- [UIComponentViews][UIComponentViewKDoc] are ComponentViews that may have additional ways of dealing with user input, for example text input. Please refer to the UIComponentView [doc][UIComponentViewKDoc] or [guide][UIComponentViewDoc] to find additional information.

## Global key listeners

Global key listeners may become helpful to show menus or move playing pieces by the arrow or WASD keys. Scene-scoped listeners may be implemented on the [BoardGameScene][BoardGameSceneKDoc].

```kotlin
gameScene.onKeyPressed = { event ->
  if (event.keyCode == KeyCode.ESCAPE)
    exit()
}
```
