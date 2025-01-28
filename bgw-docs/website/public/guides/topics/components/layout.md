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
[setRowHeightKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-height.html
[setRowHeightsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-heights.html
[setColumnWidthKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-width.html
[setColumnWidthsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-widths.html
[setAutoRowHeightKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-height.html
[setAutoRowHeightsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-heights.html
[setAutoColumnWidthKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-width.html
[setAutoColumnWidthsKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-widths.html
[setCellCenterModeKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-cell-center-mode.html
[setRowCenterModeKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-center-mode.html
[setColumnCenterModeKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-center-mode.html
[setCenterModeKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/set-center-mode.html
[GridIteratorElementKDoc]: /docs/tools.aqua.bgw.util/-grid-iterator-element/index.html
[AUTO_ROW_HEIGHT]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-r-o-w_-h-e-i-g-h-t_-a-u-t-o.html
[AUTO_COLUMN_WIDTH]: /docs/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-c-o-l-u-m-n_-w-i-d-t-h_-a-u-t-o.html
[CameraPaneKDoc]: /docs/tools.aqua.bgw.components.layoutviews/-camera-pane/index.html
[ComponentViewDoc]: /guides/components/componentview
[ContainerDoc]: /guides/container/container
[AreaDoc]: /guides/container/container#area

# LayoutViews

<tldr>
    <p><format style="bold">Components especially for laying out user interface elements</format></p>
    <p>→ &nbsp; <a href="http://">StaticComponentView</a></p>
</tldr>

<chapter title="LayoutViews" collapsible="true" default-state="expanded">
    <table style="header-column">
    <tr>
        <td width="20%">CameraPane</td>
        <td>Layout container to use zoom and pan functionality in an infinite coordinate space</td>
    </tr>
    <tr>
        <td>GridPane</td>
        <td>Layout container to define a flexible grid with a dynamic size of columns and rows</td>
    </tr>
    <tr>
        <td>Pane</td>
        <td>Layout container to define a new coordinate space for absolute positioning</td>
    </tr>
    </table>
</chapter>

## Prior knowledge

All layout views inherit from [ComponentView][ComponentViewDoc].
It is therefore helpful to read this documentation first as the features from this superclass doesn't get repeated here.

## Introduction

[LayoutViews][LayoutViewKDoc] are used to arrange /components.
There are two available LayoutViews: [Pane](#pane.) and [GridPane](#gridpane.).

## [Pane][PaneKDoc]

The [Pane][PaneKDoc] arranges its contents in a new coordinate space in the size of the Pane.
Elements added to the Pane align themselves relative to the top-left corner of the Pane.
This can be used to group a set of /components to move them simultaneously by altering the Pane's position.
A Pane is therefore quite similar to an [Area][AreaDoc] but can be used in [MenuScenes][MenuSceneKDoc].

> A Pane is not a [Container][ContainerDoc] and therefore not draggable. Use an [Area][AreaDoc] to combine [Components][ComponentViewDoc] to a drag group.
> {style="note"}

## [GridPane][GridPaneKDoc]

The [GridPane][GridPaneKDoc] arranges its contents in rows and columns.
All rows and columns adjust their size automatically based on the largest element in this row / column.
This implies that empty rows and column get width / height of 0.
Fixed sizes can be specified as well as a spacing between rows and columns.

### Constructor

The grid needs an initial row and column count.
These can be altered as described [later](#adding-rows--columns.).
By default, the grid gets anchored at its center.
This means that `posX` and `posY` refer to the grids center point and the grid expands equally in all directions.
This can be altered by passing `layoutFromCenter = false` to the constructor.

### Adding and removing elements

The Grid can be array-like indexed with `grid[columnIndex, rowIndex]` for all get and set operations.<br>

```kotlin
val content = grid[0,2]
```

will retrieve the content of cell `[0,2]`. Empty cells contain `null`.

```kotlin
grid[0,2] = Button(text="Hello")
```

will replace the content of cell `[0,2]` with a button. To remove elements use

```kotlin
grid[0,2] = null
```

### Adding rows / columns

Rows and columns can be added by calling [addRows][addRowsKDoc] and [addColumns][addColumnsKDoc].
In both cases the desired index to insert and the amount of rows / columns to add can be passed.

To grow the grid in all directions use [grow][growKDoc].

### Removing rows / columns

Rows and columns can be removed by calling [removeRow][removeRowKDoc] and [removeColumn][removeColumnKDoc].
In both cases the desired index to delete has to be passed.

To automatically remove all empty rows / columns use [removeEmptyRows][removeEmptyRowsKDoc]
and [removeEmptyColumns][removeEmptyColumnsKDoc].

Using [trim][trimKDoc] removes all empty outer rows and columns.

### Iterating grid /components

The grid implements the Iterable interface which means that it can be iterated by using its iterator, in foreach loops, and in streams.

The iterator returns [GridIteratorElements][GridIteratorElementKDoc] that contain the component and the current columnIndex and rowIndex.

> The iterator iterates through all cells including those that are empty. Therefore, the returned GridIteratorElement may contain `null` as component.
> {style="note"}

### Setting fixed row heights and widths

By default, all rows and columns get rendered automatically according to the largest element in this row or column.
To set fixed values use [setRowHeight][setRowHeightKDoc] / [setRowHeights][setRowHeightsKDoc] and
[setColumnWidth][setColumnWidthKDoc] / [setColumnWidths][setColumnWidthsKDoc].

To restore automatic behaviour pass [AUTO_ROW_HEIGHT] / [AUTO_COLUMN_WIDTH] or use
[setAutoRowHeight][setAutoRowHeightKDoc] / [setAutoRowHeights][setAutoRowHeightsKDoc] and
[setAutoColumnWidth][setAutoColumnWidthKDoc] / [setAutoColumnWidths][setAutoColumnWidthsKDoc]

### Changing centering behaviour

By default, /components get centered in their cells if the cell's size is larger than the component.
This can be changed for each cell by calling [setCellCenterMode][setCellCenterModeKDoc].

To change behaviour for entire rows or columns use [setRowCenterMode][setRowCenterModeKDoc] and [setColumnCenterMode][setColumnCenterModeKDoc].

To set centering behaviour for the entire grid use [setCenterMode][setCenterModeKDoc].

## [CameraPane][CameraPaneKDoc]

A pane representing a camera view that can be used to display a target layout view.

### Example

```kotlin
val targetLayout = Pane<*>(width = 1000, height = 1000)
val cameraPane = CameraPane(width = 500, height = 500 ,target = targetLayout)

// Zoom in by setting the zoom factor to 2
cameraPane.zoom = 2.0

// Make the camera pane interactive
cameraPane.interactive = true

// Pan the camera to specific coordinates
cameraPane.pan(100, 200)

// Pan the camera by offset values
cameraPane.panBy(-50, 100)
```

In the example above, a `CameraPane` is created with a pane as its target.
The zoom factor is set to 2, making the view twice as large.
The camera pane is set to be interactive, allowing zooming and panning with the mouse.
The camera is then panned to specific coordinates and panned again by offset values.

Note how the `CameraPane` is smaller than the pane it is displaying.

> A `CameraPane` is especially useful if you want to display larger content in a smaller area.
> {style="note"}
