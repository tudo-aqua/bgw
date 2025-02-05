[LayoutViewKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-layout-view/index.html
[PaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-pane/index.html
[MenuSceneKDoc]: /docs/tools.aqua.bgw.core/-menu-scene/index.html
[GridPaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/index.html
[growKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/grow.html
[addRowsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/add-rows.html
[addColumnsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/add-columns.html
[trimKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/trim.html
[removeRowKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-row.html
[removeColumnKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-column.html
[removeEmptyRowsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-rows.html
[removeEmptyColumnsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-columns.html
[GridIteratorElementKDoc]: /docs/tools.aqua.bgw.util/-grid-iterator-element/index.html
[CameraPaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-camera-pane/index.html
[PaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-pane/index.html
[ComponentViewDoc]: /guides/components/componentview
[AreaDoc]: /guides/container/container#area
[GameComponentViewsDoc]: /guides/components/gamecomponentview
[GameComponentsDoc]: /guides/components/gamecomponents
[LayoutViewDoc]: /guides/components/layout
[UIComponentsDoc]: /guides/components/uicomponents
[ContainerDoc]: /guides/components/container

# LayoutViews

<tldr>Components especially for laying out user interface elements</tldr>

## Introduction

[LayoutViews][LayoutViewKDoc] are used to arrange [GameComponentViews][GameComponentViewsDoc] and [UIComponents][UIComponentsDoc]. The three LayoutViews [Pane](PaneKDoc), [GridPane](GridPaneKDoc) and [CameraPane](CameraPaneKDoc) are available. Different from [Containers][ContainerDoc], LayoutViews can also be used in [MenuScenes][MenuSceneKDoc].

All [LayoutViews][LayoutViewKDoc] inherit from [ComponentView][ComponentViewDoc]. It is therefore helpful to read this documentation first as the features from this superclass don't get repeated here.

Please also take a look at corresponding [Containers][ContainerDoc], [GameComponents][GameComponentsDoc] and [UIComponents][UIComponentsDoc].

---

> The following examples are visually accurate representations of BGW components based on the provided code snippets.
> All example listeners however are purely illustrative and do not execute any code when interacting with the components in this guide.
> {style="warning"}

<br>

## Pane

<tldr>Component for grouping elements in a new absolute coordinate space</tldr>

The [Pane][PaneKDoc] arranges its contents in a new coordinate space in the size of the Pane.
Elements added to the Pane align themselves relative to the top-left corner of the Pane.
This can be used to group a set of components to move them simultaneously by altering the Pane's position.

The example shows to labels without text with the first positioned relative to the scene, while the second is positioned relative to a pane using the exact same coordinates. The pane itself is shifted to the right thus the second label is also shifted to the right.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.paneExample">
val absoluteLabel = Label(
    posX = 75,
    posY = 75,
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xc6ff6e))
)
&#13;
val paneLabel = Label(
    posX = 75,
    posY = 75,
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0x6dbeff))
)
&#13;
val pane = Pane&lt;ComponentView&gt;(
    posX = 225,
    posY = 0,
    width = 225,
    height = 225,
    visual = ColorVisual(Color(0x0f141f))
)
&#13;
pane.add(paneLabel)
</preview>

> Panes are not [Containers][ContainerDoc] and thus not draggable. Use the almost equivalent [Area][AreaDoc] to combine [GameComponentViews][GameComponentViewsDoc] to a drag group.
> {style="note"}

##### Listeners

It is possible to track when components are added to or removed from the Pane by adding the `onAdd` and `onRemove` listeners.

<signature>
<code-block lang="kotlin" copy="false">
onAdd: (T.() -> Unit)? = null
</code-block>

This listener gets invoked when a component is added to the Pane. The added component is accessible through `this`.

<br>

<code-block lang="kotlin" copy="false">
onRemove: (T.() -> Unit)? = null
</code-block>

This listener gets invoked when a component is removed from the Pane. The removed component is accessible through `this`.

</signature>

---

## GridPane

<tldr>Component for arranging elements in rows and columns</tldr>

The [GridPane][GridPaneKDoc] arranges its contents in rows and columns.
All rows and columns adjust their size automatically based on the largest element in this row / column.
This implies that empty rows and column get width / height of 0. The spacing between rows and columns can be adjusted using the `spacing` property.

A grid needs an initial row and column count. These can however be altered after instantiation.
By default, the grid gets anchored at its center. This means that `posX` and `posY` refer to the grids center point and the grid expands equally in all directions.
This can be prevented by passing `layoutFromCenter = false` to the constructor.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.gridPaneExample">
val purpleLabel = Label(
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xbb6dff))
)
&#13;
val redLabel = Label(
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xef4444))
)
&#13;
val orangeLabel = Label(
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xfa6c56))
)
&#13;
val yellowLabel = Label(
    width = 75,
    height = 75,
    visual = ColorVisual(Color(0xffc656))
)
&#13;
val gridPane = GridPane<ComponentView>(
    posX = 225,
    posY = 100,
    layoutFromCenter = true,
    rows = 3,
    columns = 3,
    spacing = 10
)
&#13;
gridPane[0, 0] = purpleLabel
gridPane[1, 1] = redLabel
gridPane[2, 1] = orangeLabel
gridPane[2, 2] = yellowLabel
</preview>

> Changing `layoutFromCenter` after instantiation has no effect.
> {style="warning"}

##### Cells

The Grid can be array-like indexed with `[columnIndex, rowIndex]` for all get and set operations. The indices start at 0, so `[1, 1]` will extract the content from the cell in the second column and second row. Empty cells contain `null`.

```kotlin
val redLabelRef = gridPane[1, 1]
```

To set or replace the content of a cell, simply assign a new component to it:

```kotlin
val greenLabel = Label(width = 75, height = 75, visual = ColorVisual(Color(0xc6ff6e)))
gridPane[0, 2] = greenLabel
```

Finally to remove the content of a cell, assign `null` to it. In the example below, the content of cell `[2, 1]` (orange label) is removed.

```kotlin
gridPane[2, 1] = null
```

##### Adding cells

Additional cells can be added by expanding the grid using [addRows()][addRowsKDoc] and [addColumns()][addColumnsKDoc].
In both cases the desired index to insert and the amount of rows / columns to add can be passed.

<signature>
<code-block lang="kotlin" copy="false">
fun addRows(rowIndex: Int, count: Int = 1)
</code-block>

This function adds `count` rows starting after `rowIndex`. If `count` is not specified, one row is added.

<br>

<code-block lang="kotlin" copy="false">
fun addColumns(columnIndex: Int, count: Int = 1)
</code-block>

This function adds `count` columns starting after `columnIndex`. If `count` is not specified, one column is added.

</signature>

To grow the grid in all directions at the same time use [grow()][growKDoc]. This function adds rows and columns to all sides of the grid.

<signature>
<code-block lang="kotlin" copy="false">
fun grow(left: Int = 0, right: Int = 0, top: Int = 0, bottom: Int = 0) : Boolean
</code-block>

This function adds `left` columns to the left, `right` columns to the right, `top` rows to the top and `bottom` rows to the bottom of the grid.
It is thus creating a padding of empty cells around the grid.

</signature>

##### Removing cells

Analogous to adding cells, unwanted cells can be removed by removing rows and columns using [removeRow()][removeRowKDoc] and [removeColumn()][removeColumnKDoc].

<signature>
<code-block lang="kotlin" copy="false">
fun removeRow(rowIndex: Int)
</code-block>

This function removes the row at `rowIndex`.

<br>

<code-block lang="kotlin" copy="false">
fun removeColumn(columnIndex: Int)
</code-block>

This function removes the column at `columnIndex`.

</signature>

Empty rows and columns can be removed automatically using [removeEmptyRows()][removeEmptyRowsKDoc] and [removeEmptyColumns()][removeEmptyColumnsKDoc] or by calling [trim()][trimKDoc] which removes all empty outer rows and columns at once.

<signature>
<code-block lang="kotlin" copy="false">
fun removeEmptyRows()
</code-block>

This function removes all empty rows from the grid.

<br>

<code-block lang="kotlin" copy="false">
fun removeEmptyColumns()
</code-block>

This function removes all empty columns from the grid.

<br>

<code-block lang="kotlin" copy="false">
fun trim() : Boolean
</code-block>

This function removes all empty outer rows and columns from the grid. It returns `true` if any rows or columns were removed. If the grid is empty, the resulting grid will have a size of 0x0.

</signature>

##### Iterating

The GridPane implements the Iterable interface, allowing iteration through its cells using standard Kotlin iteration methods. You can use it with iterators, foreach loops, or streams. During iteration, [GridIteratorElements][GridIteratorElementKDoc] are returned, each containing the `component` in the cell, its `columnIndex` and its `rowIndex`.

> The iterator iterates through all cells including those that are empty. Therefore, the returned GridIteratorElement may contain `null` as component.
> {style="note"}

---

## CameraPane

<tldr>Component for displaying a pannable and zoomable camera view</tldr>

The [CameraPane][CameraPaneKDoc] is a special layout view that displays a cutout view of a specific target layout. Overflows of the target layout are clipped by the camera pane and will be viewable by panning or zooming if the CameraPane has the `interactive` property set to `true`. As the `target` needs to also be of type LayoutView, it can be either [GridPane][GridPaneKDoc] or [Pane][PaneKDoc].

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.cameraPaneExample">
val panLabel = Label(
    posX = 300,
    posY = 410,
    width = 400,
    height = 200,
    text = "Drag to pan the camera. Scroll to zoom.",
    alignment = Alignment.CENTER,
    font = Font(20.0, Color.WHITE)
)
&#13;
val targetLayout = Pane&lt;ComponentView&gt;(
    width = 1000,
    height = 1000,
    visual = ImageVisual("background.png")
)
&#13;
targetLayout.add(panLabel)
&#13;
val cameraPane = CameraPane(
    width = 500,
    height = 250,
    target = targetLayout,
    limitBounds = true
)
&#13;
cameraPane.interactive = true
</preview>

> Note how in this example the `CameraPane` is stretching the entirety of the container but is smaller than the pane it is displaying. A `CameraPane` is especially useful if you want to display larger content in a smaller area.

##### Interactivity

The `interactive` property controls whether the camera pane can be panned and zoomed interactively. If set to `true`, the camera pane can be panned by dragging the mouse and zoomed by scrolling. If set to `false`, the camera pane is static and cannot be panned or zoomed manually. Zooming and panning can still be done programmatically by calling the respective functions.

##### Bounds

The CameraPane can be limited to panning inside its bounds. Doing so will snap the edges of the target layout to the edges of the camera pane.
In the example above, the `limitBounds` property is set to `true`, preventing the user to move the camera too far beyond the target layouts bounds.

<signature>
<code-block lang="kotlin" copy="false">
limitBounds : Boolean
</code-block>

The `limitBounds` property is set to `true` by default, preventing the camera from panning outside the target layout. This is especially useful if the target layout is larger than the camera pane.

If `limitBounds` is set to `false`, the camera can pan outside the target layout. This can be useful if the target layout is smaller than the camera pane or if the camera should be able to focus on a specific area at the edge of the target layout.

</signature>

##### Zoom

The `zoom` property controls the zoom factor of the camera and is `1.0` by default. Adjusting the zoom factor will zoom in or out.

<signature>
<code-block lang="kotlin" copy="false">
zoom : Double
</code-block>

A zoom factor of `1.0` displays the target layout at its original size. A zoom factor greater than `1.0` zooms in, while a zoom factor less than `1.0` zooms out.

</signature>

##### Panning

The camera pane can be panned programmatically by calling the `pan` or `panBy` functions. This will center the specified coordinates in the camera pane.

<signature>
<code-block lang="kotlin" copy="false">
fun pan(x: Number, y: Number, zoom: Double, smooth: Boolean = true)
</code-block>

This function pans the camera to focus the specified coordinates `(x, y)` and adjust the `zoom` factor to the one specified. If `smooth` is set to `true`, the camera pans smoothly to the target coordinates.

<br>

<code-block lang="kotlin" copy="false">
fun panBy(xOffset: Number, yOffset: Number, zoom: Double, smooth: Boolean = true)
</code-block>

This function pans the camera by the specified `xOffset` and `yOffset` and adjusts the `zoom` factor to the one specified. If `smooth` is set to `true`, the camera pans smoothly to the target coordinates.

> Both functions are also available without the `zoom` parameter. In this case, the zoom factor remains unchanged.
> {style="note"}

</signature>
