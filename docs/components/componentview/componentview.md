---
parent: Components
title: ComponentView
nav_order: 1
layout: default
---

<!-- KDoc -->
[ComponentViewKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-component-view/index.html
[GameComponentContainerKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-game-component-container/index.html
[LayoutViewKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-layout-view/index.html
[SceneKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-scene/index.html
[RootComponentKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components/-root-component/index.html
[VisualKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-visual/index.html

[MouseEventKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-mouse-event/index.html
[KeyEventKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-key-event/index.html
[DragEventKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.event/-drag-event/index.html

[MovementAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-movement-animation/index.html
[RotationAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-rotation-animation/index.html

<!-- GH-Pages Doc -->
[VisualDoc]: https://tudo-aqua.github.io/bgw/concepts/visual/visual.html
[DragDropDoc]: https://tudo-aqua.github.io/bgw/concepts/drag-and-drop/DragAndDropExample.html
[AnimationDocs]: https://tudo-aqua.github.io/bgw/concepts/animations/Animations.html
[UserInputDoc]: https://tudo-aqua.github.io/bgw/concepts/user-input/UserInput.html

<!-- Start Page -->
# Component View

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

## Introduction
[ComponentView][ComponentViewKDoc] is the abstract baseclass of all framework components. 
It defines important fields and functions that are necessary to visualize inheriting components.
Most fields come as a pair of attribute and observable property.

## Properties
``posX: Double``/``posXProperty: DoubleProperty``: The horizontal position.

``posY: Double``/``posYProperty: DoubleProperty``: The vertical position.
	
``width: Double``/``widthProperty: DoubleProperty``: The width.

``height: Double``/``heightProperty: DoubleProperty``: The height.
	
``scaleX: Double``/``scaleXProperty: DoubleProperty``: The horizontal scale

``scaleY: Double``/``scaleYProperty: DoubleProperty``: The vertical scale

``scale: Double``:  Scale of this component. Setter sets both scaleX and scaleY.
Getter returns scale if ``scaleX == scaleY`` or throws an *IllegalStateException* if scaleX and scaleY differ.

``rotation: Double``/``rotationProperty: DoubleProperty``: Rotation of this component in degrees. 
Between 0 (incl.) and 360 (excl.).

``visual: Visual``/`` visualProperty: Property<Visual>``: The current [Visual][VisualKDoc]. Read more about Visuals 
[here][VisualDoc].

``opacity: Double``/``opacityProperty: DoubleProperty``: The current opacity. 
Must be in range 0.0 to 1.0. 
0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.

``isVisible``/``isVisibleProperty: BooleanProperty``: Visibility of this component. 
Visible components may still be opaque due to opacity property.

*Note that invisible components no longer trigger mouse click events, components with opacity = 0.0 still do.*
	
``isDisabled: Boolean``/``isDisabledProperty: BooleanProperty``:Controls if user input events cause input functions of 
this component to get invoked

``isFocusable: Boolean``/``isFocusableProperty: BooleanProperty``: Controls whether this component is focusable or not.

``name: String``:
Name field only for debugging purposes.
Has no effect on rendering.
Use this to label components for easier debugging and printing

``parent: ComponentView?``:
The parent of this ComponentView.
Its value is null if the component is not contained in any [GameComponentContainer][GameComponentContainerKDoc],
[LayoutView][LayoutViewKDoc], or a [Scene][SceneKDoc].
If the component has been added directly to a scene, *parent* is equal to the scene's [RootComponent][RootComponentKDoc].

### Event handler
Event handlers et called by the framework if the associated event happened.
Handlers can be assigned like attribute values.
To remove a handler assign ``null``. Read more about this topic [here][UserInputDoc].
	
#### Mouse movement events
``onMouseEntered: ((MouseEvent) -> Unit)?``: Gets invoked when the mouse enters this component.
	
``onMouseExited: ((MouseEvent) -> Unit)?``: Gets invoked when the mouse leaves this component.

#### Mouse interaction events 
``onMousePressed: ((MouseEvent) -> Unit)?``:Gets invoked when the mouse is pressed inside this component.

``onMouseReleased: ((MouseEvent) -> Unit)?``:Gets invoked when the mouse is released inside this component.

``onMouseClicked: ((MouseEvent) -> Unit)?``:Gets invoked when the mouse is clicked, i.e. pressed and released, inside
this component.

*Note: The [MouseEvent][MouseEventKDoc]'s ``button`` attribute always contains the mouse button.*

#### Key events
``onKeyPressed: ((KeyEvent) -> Unit)?``:Gets invoked when a keyboard key is pressed while this component has focus.

``onKeyReleased: ((KeyEvent) -> Unit)?``:Gets invoked when a keyboard key is released while this component has focus.

``onKeyTyped: ((KeyEvent) -> Unit)?``:Gets invoked when a keyboard key is typed, i.e. pressed and released, while this 
component has focus.

*Note: The [KeyEvent][KeyEventKDoc] always contains the key combination.*
	
#### Drag events
*Read more about drag and drop [here][DragDropDoc]*
	
``onDragGestureEntered: ((DragEvent) -> Unit)?``: Gets invoked with a [DragEvent][DragEventKDoc] containing the dragged 
component when the mouse enters this component while dragging.

``onDragGestureExited: ((DragEvent) -> Unit)?``:  Gets invoked with a [DragEvent][DragEventKDoc] containing the dragged
component when the mouse leaves this component while dragging.

``onDragDropped: ((DragEvent) -> Unit)?``:  Gets invoked with a [DragEvent][DragEventKDoc] containing the dragged 
component after a successful drag and drop gesture.

``dropAcceptor: ((DragEvent) -> Boolean)?``: Gets invoked when a dragged component was released inside this component.
Implement this function in such a way that it returns `true` if this component accepts the drop of the given component, 
passed with the [DragEvent][DragEventKDoc],, or `false` if a drop is not valid. The dragged component will snap back if 
all available drop targets return `false`.
It is advised not to modify the Scene or its children in this function. The modification, for example adding the 
component to a container should be done in ``onDragDropped`` as this function only gets invoked if this ``dropAccessor``
returns true.


## Functions

``reposition(posX: Number, posY: Number)``: Repositions this component to the specified coordinates. 
To move it with an animation refer to the [MovementAnimation][MovementAnimationKDoc] tutorial [here][AnimationDocs].
	
``offset(offsetX: Number, offsetY: Number)``: Moves this component by relative offset. 
To move it with an animation refer to the [MovementAnimation][MovementAnimationKDoc] tutorial [here][AnimationDocs].

``rotate(degrees: Number)``: Rotates this component by the given number of degrees.
To rotate it with an animation refer to the [RotationAnimation][RotationAnimationKDoc] tutorial [here][AnimationDocs].

``resize(width: Number, height: Number)``: Resizes this component to the specified dimensions.

``scale(scalar: Number)``: Scales this component by given scalar.

``scaleX(scalar: Number)``: Scales this component's width by given scalar.

``scaleY(scalar: Number)``: Scales this component's height by given scalar.

``removeFromParent(): ComponentView``: Removes this component from its parent.
Returns the former parent. Throws an *IllegalStateException* if the parent was null, i.e. this component was not 
contained in any scene.