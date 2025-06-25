[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[DragEventKDoc]: /docs/tools.aqua.bgw.event/-drag-event/index.html
[GameComponentContainerKDoc]: /docs/tools.aqua.bgw.components.container/-game-component-container/index.html
[LayoutViewKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-layout-view/index.html
[SceneKDoc]: /docs/tools.aqua.bgw.core/-scene/index.html
[RootComponentKDoc]: /docs/tools.aqua.bgw.components/-root-component/index.html
[VisualKDoc]: /docs/tools.aqua.bgw.visual/-visual/index.html
[MouseEventKDoc]: /docs/tools.aqua.bgw.event/-mouse-event/index.html
[KeyEventKDoc]: /docs/tools.aqua.bgw.event/-key-event/index.html
[DragEventKDoc]: /docs/tools.aqua.bgw.event/-drag-event/index.html
[MovementAnimationKDoc]: /docs/tools.aqua.bgw.animation/-movement-animation/index.html
[RotationAnimationKDoc]: /docs/tools.aqua.bgw.animation/-rotation-animation/index.html
[UIComponentsKDoc]: /docs/tools.aqua.bgw.components.uicomponents/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[WheelEventKDoc]: /docs/tools.aqua.bgw.event/-wheel-event/index.html
[ScaleAnimationKDoc]: /docs/tools.aqua.bgw.animation/-scale-animation/index.html

<!-- GH-Pages Doc -->

[VisualDoc]: /guides/concepts/visual
[DragDropDoc]: /guides/concepts/drag-and-drop
[AnimationDocs]: /guides/concepts/animations
[UserInputDoc]: /guides/concepts/user-input

# ComponentView

<tldr>Baseclass of all components</tldr>

## Introduction

[ComponentView][ComponentViewKDoc] is the abstract baseclass of all elements to be placed into [Scenes][SceneKDoc].
It defines important fields and functions that are necessary to every inheriting component.

This guide will introduce you to the most important properties, listeners and functions of a components.

---

## Properties

Because some properties bare special functionality, they are explained in more detail below. For a complete list of all properties refer to the [ComponentView][ComponentViewKDoc] API reference.

<signature>
<code-block lang="kotlin" copy="false">
scale: Double
</code-block>

This setter sets both `scaleX` and `scaleY` to the provided value. The matching getter however returns scale if `scaleX == scaleY` or throws an `IllegalStateException` if scaleX and scaleY differ.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
rotation: Double
</code-block>

The rotation of a component in degrees will rotate the component around its center (anchor point). It can be set between 0 (incl.) and 360 (excl.) degrees.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
visual: Visual
</code-block>

To set the background of a component, a [Visual][VisualKDoc] is used. This can range from a simple color to images and even supports layering multiple visuals.

> Read more about Visuals [here][VisualDoc].

</signature>

<signature>
<code-block lang="kotlin" copy="false">
opacity: Double
</code-block>

Decreasing the opacity of a component will make it more and more transparent. 0.0 thus corresponds to 0% opacity, where 1.0 corresponds to 100% opacity. The value must be in the range of 0.0 to 1.0.

<br>

<code-block lang="kotlin" copy="false">
isVisible: Boolean
</code-block>

To completely hide a component, set its visibility to false. This will also prevent it from receiving any user input events.

> Invisible components no longer trigger mouse click events, while components with `opacity = 0.0` still do.
> {style="warning"}

</signature>

<signature>
<code-block lang="kotlin" copy="false">
isDisabled: Boolean
</code-block>

Disabling a component will also prevent it from receiving any user input events. Some elements like [Buttons][ButtonKDoc] will change their appearance to indicate that they are disabled.
This property however does not affect the visibility of the component.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
parent: ComponentView?
</code-block>

The parent of this ComponentView. Its value is `null` if the component is not contained in any [GameComponentContainer][GameComponentContainerKDoc], [LayoutView][LayoutViewKDoc] or a [Scene][SceneKDoc].
If the component has been added directly to a scene, `parent` is equal to the scene's [RootComponent][RootComponentKDoc].

</signature>

---

## Listeners

Listeners are used to react to user input events like mouse clicks or key presses. They are assigned as lambda functions and can be removed by reassigning `null`. Read more about user input [here][UserInputDoc].

##### Mouse movement events

<signature>
<code-block lang="kotlin" copy="false">
onMouseEntered: ((MouseEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse enters this component.

<br>

<code-block lang="kotlin" copy="false">
onMouseExited: ((MouseEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse leaves this component.

> A hover effect can be implemented by changing the component's appearance in these functions.
> {style="note"}

</signature>

##### Mouse interaction events

<signature>
<code-block lang="kotlin" copy="false">
onMousePressed: ((MouseEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse is pressed inside this component.

<br>

<code-block lang="kotlin" copy="false">
onMouseReleased: ((MouseEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse is released inside this component.

<br>

<code-block lang="kotlin" copy="false">
onMouseClicked: ((MouseEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse is clicked, i.e. pressed and released, inside
this component.

> The [MouseEvent's][MouseEventKDoc] `button` attribute contains the pressed mouse button.
> {style="note"}

</signature>

<signature>
<code-block lang="kotlin" copy="false">
onMouseWheel: ((WheelEvent) -> Unit)?
</code-block>

This listener gets invoked when the mouse wheel is rotated while the mouse is in this component.

> The [WheelEvent][WheelEventKDoc] contains the direction of turning as well as pressed special keys like `Ctrl`, `Alt` or `Shift`.
> {style="note"}

</signature>

##### Key events

<signature>
<code-block lang="kotlin" copy="false">
onKeyPressed: ((KeyEvent) -> Unit)?
</code-block>

This listener gets invoked when a keyboard key is pressed while this component has focus.

<br>

<code-block lang="kotlin" copy="false">
onKeyReleased: ((KeyEvent) -> Unit)?
</code-block>

This listener gets invoked when a keyboard key is released while this component has focus.

> The [KeyEvent][KeyEventKDoc] contains the pressed key combination.
> {style="note"}

</signature>

##### Drag events

<signature>
<code-block lang="kotlin" copy="false">
onDragGestureEntered: ((DragEvent) -> Unit)?
</code-block>

This listener gets invoked with a [DragEvent][DragEventKDoc] containing the dragged
component when the mouse enters this component while dragging and the `dropAcceptor` returns `true`.

<br>

<code-block lang="kotlin" copy="false">
onDragGestureExited: ((DragEvent) -> Unit)?
</code-block>

This listener gets invoked with a [DragEvent][DragEventKDoc] containing the dragged
component when the mouse leaves this component while dragging and the `dropAcceptor` returns `true`.

<br>

<code-block lang="kotlin" copy="false">
onDragDropped: ((DragEvent) -> Unit)?
</code-block>

This listener gets invoked with a [DragEvent][DragEventKDoc] containing the dragged
component after a successful drag and drop gesture and the `dropAcceptor` returns `true`.

<br>

<code-block lang="kotlin" copy="false">
dropAcceptor: ((DragEvent) -> Boolean)?
</code-block>

This listener gets invoked when a dragged component is inside this component.
Implement this function in such a way that it returns `true` if this component accepts the drop of the given component (passed with the [DragEvent][DragEventKDoc]) or `false` if a drop is not valid.
The dragged component will snap back to its original position if the available drop target returns `false`.

It is advised not to modify the scene or its children in this function. The modification, for example adding the
component to a container should be done in `onDragDropped` as that function only gets invoked if the `dropAcceptor` returns `true`.

> Read more about implementing drag and drop [here][DragDropDoc].

</signature>

---

## Functions

The ComponentView class provides a variety of functions to manipulate the component's appearance and position without the need to directly access its properties.
Accessing the properties directly is still possible and supported in cases where the provided functions do not suffice.

##### Positioning

<signature>
<code-block lang="kotlin" copy="false">
reposition(posX: Number, posY: Number)
</code-block>

Repositions this component to the specified coordinates.
To move it with an animation refer to the [MovementAnimation][MovementAnimationKDoc] tutorial [here][AnimationDocs].

<br>

<code-block lang="kotlin" copy="false">
offset(offsetX: Number, offsetY: Number)
</code-block>

Moves this component by relative offset.
To move it with an animation refer to the [MovementAnimation][MovementAnimationKDoc] tutorial [here][AnimationDocs].

</signature>

##### Transformation

<signature>
<code-block lang="kotlin" copy="false">
rotate(degrees: Number)
</code-block>

Rotates this component by the given number of degrees.
To rotate it with an animation refer to the [RotationAnimation][RotationAnimationKDoc] tutorial [here][AnimationDocs].

</signature>

<signature>
<code-block lang="kotlin" copy="false">
resize(width: Number, height: Number)
</code-block>

Resizes this component to the specified dimensions. This will change the `width` and `height` properties of the component.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
scale(scalar: Number)
</code-block>

The function uniformly scales this component by the given scalar. This is equivalent to setting both `scaleX` and `scaleY` to the same value.
To scale it with an animation refer to the [ScaleAnimation][ScaleAnimationKDoc] tutorial [here][AnimationDocs].

> The `scale` functions modify the component's `scale` properties. They do however not effect the `width` and `height` properties.
> If you want to get the actual width and height of the component after scaling, use the `actualWidth` and `actualHeight` properties.
> {style="warning"}

<br>

<code-block lang="kotlin" copy="false">
scaleX(scalar: Number)
</code-block>

Only scales this component's width by setting the `scaleX` property to the provided scalar.

<br>

<code-block lang="kotlin" copy="false">
scaleY(scalar: Number)
</code-block>

Only scales this component's height by setting the `scaleY` property to the provided scalar.

> The provided scalar must always be greater than or equal to 0.0 or an `IllegalArgumentException` will be thrown.
> {style="warning"}

</signature>

##### Scene hierarchy

<signature>
<code-block lang="kotlin" copy="false">
removeFromParent(): ComponentView
</code-block>

Removes this component from its parent. Returns the former parent or throws an `IllegalStateException` if the parent was null, i.e. this component was not contained in any [GameComponentContainer][GameComponentContainerKDoc], [LayoutView][LayoutViewKDoc] or [Scene][SceneKDoc].

</signature>
