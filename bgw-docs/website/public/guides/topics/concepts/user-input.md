[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[DynamicComponentViewKDoc]: /docs/tools.aqua.bgw.components/-dynamic-component-view/index.html
[SceneKDoc]: /docs/tools.aqua.bgw.core/-scene/index.html
[MouseEventKDoc]: /docs/tools.aqua.bgw.event/-mouse-event/index.html
[MouseButtonTypeKDoc]: /docs/tools.aqua.bgw.event/-mouse-button-type/index.html
[KeyEventKDoc]: /docs/tools.aqua.bgw.event/-key-event/index.html
[KeyCodeKDoc]: /docs/tools.aqua.bgw.event/-key-code/index.html
[WheelEventKDoc]: /docs/tools.aqua.bgw.event/-wheel-event/index.html

<!-- GH-Pages Doc -->

[ComponentViewDoc]: /guides/components/componentview
[DragDropDoc]: /guides/concepts/drag-and-drop
[UIComponentsDoc]: /guides/components/uicomponents
[HandlingEvents]: /guides/handling-events
[DeclaringScenes]: /guides/declaring-scenes

# User Input

<tldr>React to mouse, keyboard and mouse wheel</tldr>

## Introduction

BGW reports user input with **events**. Every [ComponentView][ComponentViewKDoc] offers a set of nullable handler properties. Assign a lambda or a function reference and BGW calls it when the event occurs, assign `null` to stop listening. The basics are explained in [Handling Events][HandlingEvents], this guide is the reference for all input events.

The preview shows the component that is used in the examples below.

<preview key="tools.aqua.bgw.main.examples.ExampleInteractionScene.interactiveButtonExample">
val interactiveButton = Button(
    width = 300,
    height = 100,
    text = "Click, hover or focus me",
    font = Font(20.0, Color(0x0F141F)),
    visual = ColorVisual(Color(0xFFC656)).apply {
        style.borderRadius = BorderRadius.MEDIUM
    }
)
</preview>

---

## Mouse events

All mouse handlers receive a [MouseEvent][MouseEventKDoc]:

<signature>
<code-block lang="kotlin" copy="false">
button: MouseButtonType
posX: Number
posY: Number
</code-block>

The [button][MouseButtonTypeKDoc] that is involved (`LEFT_BUTTON`, `RIGHT_BUTTON`, `MOUSE_WHEEL` or `OTHER`) and the position of the mouse as **absolute** coordinates of the active scene. For enter and exit events no button is involved.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
onMousePressed: ((MouseEvent) -&gt; Unit)?
onMouseReleased: ((MouseEvent) -&gt; Unit)?
onMouseClicked: ((MouseEvent) -&gt; Unit)?
</code-block>

Invoked when a mouse button is pressed or released above the component, and when it was clicked, i.e. pressed and released. `onMouseClicked` is invoked after `onMousePressed` and `onMouseReleased`.

<br>

<code-block lang="kotlin" copy="false">
onMouseEntered: ((MouseEvent) -&gt; Unit)?
onMouseExited: ((MouseEvent) -&gt; Unit)?
</code-block>

Invoked when the mouse enters or leaves the component. Use them for hover effects.

</signature>

A typical hover effect changes the `visual` in both handlers:

```kotlin
button.onMouseEntered = { button.visual = ColorVisual.MAGENTA }
button.onMouseExited = { button.visual = ColorVisual.GREEN }
```

Because every click reports the `button`, the same component can react differently to the left and the right mouse button:

```kotlin
button.onMouseClicked = { event ->
    when (event.button) {
        MouseButtonType.LEFT_BUTTON -> button.text = "Left click"
        MouseButtonType.RIGHT_BUTTON -> button.text = "Right click"
        else -> {}
    }
}
```

### Mouse wheel

React to the player turning the mouse wheel.

<signature>
<code-block lang="kotlin" copy="false">
onMouseWheel: ((WheelEvent) -&gt; Unit)?
</code-block>

Invoked when the wheel is turned while the mouse is above the component. The [WheelEvent][WheelEventKDoc] contains the `direction` (`UP` or `DOWN`), the `scrollOffset` since the last event as positive number and the state of the modifier keys `isControlDown`, `isShiftDown` and `isAltDown`.

</signature>

`WheelDirection` can be multiplied with a number, which makes it easy to turn a scroll into a signed value:

```kotlin
token.onMouseWheel = { event ->
    token.rotate(event.direction * event.scrollOffset)
}
```

---

## Keyboard events

React to keys that are pressed while a component has focus.

<signature>
<code-block lang="kotlin" copy="false">
onKeyPressed: ((KeyEvent) -&gt; Unit)?
onKeyReleased: ((KeyEvent) -&gt; Unit)?
</code-block>

Invoked when a key is pressed or released **while the component has focus**. The [KeyEvent][KeyEventKDoc] contains

- `keyCode`: the pressed key as [KeyCode][KeyCodeKDoc], for example `KeyCode.ENTER` or `KeyCode.A`,
- `character`: the typed character as string,
- `isControlDown`, `isShiftDown` and `isAltDown`: the state of the modifier keys.

</signature>

```kotlin
button.onKeyPressed = { event ->
    if (event.keyCode == KeyCode.ENTER && event.isControlDown) {
        button.text = "Submitted"
    }
}
```

> `onKeyTyped` is deprecated. Use `onKeyPressed` and read the `character` of the event instead.
> {style="warning"}

### Focus

Key events are only delivered to the component that has the focus. A component receives the focus when the player clicks it or navigates to it with the keyboard. Two properties influence this behavior:

<signature>
<code-block lang="kotlin" copy="false">
isFocusable: Boolean
isDisabled: Boolean
</code-block>

A component with `isFocusable = false` can never get the focus. A **disabled** component does not receive any input events at all, mouse or keyboard, and cannot be focused.

</signature>

### Global key listeners

To react to keys independent of focus, for example to open a menu with _Escape_ or to move a piece with the arrow keys, register a listener on the [scene][SceneKDoc]:

<signature>
<code-block lang="kotlin" copy="false">
scene.onKeyPressed: ((KeyEvent) -&gt; Unit)?
scene.onKeyReleased: ((KeyEvent) -&gt; Unit)?
</code-block>

Invoked for every key that is pressed or released while the scene is shown. See [Declaring Scenes][DeclaringScenes].

</signature>

```kotlin
gameScene.onKeyPressed = { event ->
    when (event.keyCode) {
        KeyCode.ESCAPE -> showMenuScene(menuScene)
        KeyCode.R -> rollDice()
        else -> {}
    }
}
```

---

## Visibility and input

Whether a component receives input depends on its state:

| State | Receives input |
| --- | --- |
| `isVisible = false` | No. Invisible components do not trigger any events. |
| `isDisabled = true` | No. Some UI components additionally change their appearance. |
| `opacity = 0.0` | Yes. The component is invisible, but still reacts to clicks. |
| Scene is locked | No. A `BoardGameScene` ignores input while it is `lock()`ed and while a menu scene is shown. |

---

## Function reference or lambda

There are two ways to assign a handler. A **function reference** is useful if the same function should react on several components, or if the handler is exchanged at runtime:

```kotlin
private fun handleMouseClicked(mouseEvent: MouseEvent) {
    button.text = "someone clicked on me!"
}

init {
    button.onMouseClicked = this::handleMouseClicked
    otherButton.onMouseClicked = this::handleMouseClicked
}
```

A **lambda** is the shorter form for handlers that are only used once:

```kotlin
button.onMousePressed = { mouseEvent ->
    button.text = "pressed ${mouseEvent.button}"
}
```

---

## Which component supports what

Different kinds of components offer different events.

<table orientation="horizontal">
    <tr>
        <td width="30%"><code>ComponentView</code></td>
        <td>Mouse, wheel and key events as well as the drop target events <code>dropAcceptor</code>, <code>onDragDropped</code>, <code>onDragGestureEntered</code> and <code>onDragGestureExited</code>. Every component has them.</td>
    </tr>
    <tr>
        <td><code>DynamicComponentView</code></td>
        <td>Additionally <code>isDraggable</code>, <code>onDragGestureStarted</code>, <code>onDragGestureMoved</code> and <code>onDragGestureEnded</code>. All game components are dynamic components.</td>
    </tr>
    <tr>
        <td>UI components</td>
        <td>Own events for their specific kind of input, like <code>onTextChanged</code> of text fields, <code>onItemSelected</code> of a combo box, <code>onValueChanged</code> of a slider, <code>onCheckedChanged</code> of a check box or <code>onSelectionChanged</code> of lists and tables.</td>
    </tr>
    <tr>
        <td>Containers and layouts</td>
        <td><code>onAdd</code> and <code>onRemove</code> when components are added or removed, and <code>onZoomed</code> for the <code>CameraPane</code>.</td>
    </tr>
</table>

> Drag and drop is covered in detail in its own guide: [Drag and Drop][DragDropDoc]. The events of UI components are listed in [UI Components][UIComponentsDoc].
> {style="note"}
