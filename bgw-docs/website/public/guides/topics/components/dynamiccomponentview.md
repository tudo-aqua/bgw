[DynamicComponentViewKDoc]: /docs/tools.aqua.bgw.components/-dynamic-component-view/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html
[DragEventKDoc]: /docs/tools.aqua.bgw.event/-drag-event/index.html
[DropEventKDoc]: /docs/tools.aqua.bgw.event/-drop-event/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html

# DynamicComponentView

<tldr>Superclass for dynamic game components with drag-and-drop capabilities</tldr>

## Introduction

The [DynamicComponentView][DynamicComponentViewKDoc] is an abstract superclass that extends [ComponentView][ComponentViewKDoc]. It provides essential functionality for interactive game elements, focusing on drag-and-drop operations in BGW.

> Subclasses of [DynamicComponentView][DynamicComponentViewKDoc] can only be used in [BoardGameScenes][BoardGameSceneKDoc].
> {style="warning"}

---

## Properties

Because [DynamicComponentView][DynamicComponentViewKDoc] extends [ComponentView][ComponentViewKDoc], it inherits all of its properties. The following section describes the important additional properties that are unique to [DynamicComponentViews][DynamicComponentViewKDoc].

<signature>
<code-block lang="kotlin" copy="false">
isDraggable: Boolean
</code-block>

Enable the drag-and-drop feature for this [DynamicComponentView][DynamicComponentViewKDoc]. When enabled, components can be picked up by holding the left mouse button and will follow the cursor until released.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
isDragged: Boolean
</code-block>

Indicates if this [DynamicComponentView][DynamicComponentViewKDoc] is currently being dragged. Value is `true` during an active drag operation and `false` otherwise.

</signature>

---

## Listeners

The following additional event handlers are available for elements inheriting [DynamicComponentView][DynamicComponentViewKDoc].

<signature>
<code-block lang="kotlin" copy="false">
onDragGestureStarted: ((DragEvent) -> Unit)?
</code-block>

This listener gets invoked with a [DragEvent][DragEventKDoc] when a drag gesture starts on this component.

<br>

<code-block lang="kotlin" copy="false">
onDragGestureMoved: ((DragEvent) -> Unit)?
</code-block>

This listener gets invoked with a [DragEvent][DragEventKDoc] when the mouse moves during a drag gesture.

> The [DragEvent][DragEventKDoc] contains the currently dragged component.
> {style="note"}

</signature>

<signature>
<code-block lang="kotlin" copy="false">
onDragGestureEnded: ((DropEvent, Boolean) -> Unit)?
</code-block>

This listener gets invoked with a [DropEvent][DropEventKDoc] and a boolean indicating if the drop was successful when a drag gesture ends.

> The [DropEvent][DropEventKDoc] contains the currently dragged component and the drop target.
> {style="note"}

</signature>
