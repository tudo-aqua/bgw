[DynamicComponentViewKDoc]: /docs/tools.aqua.bgw.components/-dynamic-component-view/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[DragEventKDoc]: /docs/tools.aqua.bgw.event/-drag-event/index.html

# Dynamic Components

<tldr>
    <p><format style="bold">Dynamic components for use only in BoardGameScenes</format></p>
    <p>→ &nbsp; <a href="http://">ComponentView</a></p>
</tldr>

## Introduction

[DynamicComponentView][DynamicComponentViewKDoc] is the baseclass for all [ComponentViews][ComponentViewKDoc] that are draggable.
It introduces properties and functions referring to the drag and drop feature of the BGW.

### Properties

`isDraggable: Boolean` / `isDraggableProperty: BooleanProperty`:

Set this attribute / property to `true` to enable the drag and drop feature for this [DynamicComponentView][DynamicComponentViewKDoc].
Draggable /components can be picked up by holding the left mouse button and follow the cursor until the mouse button gets released.

`isDragged: Boolean` `isDraggedProperty: BooleanProperty`:
Represents whether this [DynamicComponentView][DynamicComponentViewKDoc] is currently dragged or not.
Gets set to `true` when the drag begins and to `false` after mouse button was released.

### Functions

`onDragGestureStarted: ((DragEvent) -> Unit)?`: Gets invoked with a [DragEvent][DragEventKDoc] whenever a drag gesture is started on this component.

`onDragGestureMoved: ((DragEvent) -> Unit)?`: Gets invoked with a [DragEvent][DragEventKDoc] whenever the mouse was moved during a drag gesture.

`onDragGestureEnded: ((DropEvent, Boolean) -> Unit)?`: Gets invoked with a [DragEvent][DragEventKDoc] after the drag gesture ended.
