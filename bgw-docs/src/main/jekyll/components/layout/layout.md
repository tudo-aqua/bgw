---
parent: Components
title: LayoutViews
nav_order: 6
---

<!-- KDoc -->
[LayoutViewKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-layout-view/index.html
[PaneKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-pane/index.html
[MenuSceneKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.core/-menu-scene/index.html

[GridPaneKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/index.html
[growKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/grow.html

[addRowsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/add-rows.html
[addColumnsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/add-columns.html
[trimKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/trim.html

[removeRowKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-row.html
[removeColumnKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-column.html
[removeEmptyRowsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-rows.html
[removeEmptyColumnsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-columns.html

[setRowHeightKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-height.html
[setRowHeightsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-heights.html
[setColumnWidthKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-width.html
[setColumnWidthsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-widths.html

[setAutoRowHeightKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-height.html
[setAutoRowHeightsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-heights.html
[setAutoColumnWidthKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-width.html
[setAutoColumnWidthsKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-widths.html

[setCellCenterModeKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-cell-center-mode.html
[setRowCenterModeKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-center-mode.html
[setColumnCenterModeKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-center-mode.html
[setCenterModeKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/set-center-mode.html

[GridIteratorElementKDoc]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.util/-grid-iterator-element/index.html
[AUTO_ROW_HEIGHT]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-r-o-w_-h-e-i-g-h-t_-a-u-t-o.html
[AUTO_COLUMN_WIDTH]: ../../bgw-gui-kdoc/bgw-gui/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-c-o-l-u-m-n_-w-i-d-t-h_-a-u-t-o.html

<!-- GH-Pages Doc -->
[ComponentViewDoc]: ../../components/componentview/componentview.md
[ContainerDoc]: ../../components/container/container.md
[AreaDoc]: ../../components/container/container.md#area

<!-- Start Page -->
# LayoutViews
{: .no_toc}

<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

## Prior knowledge
All layout views inherit from [ComponentView][ComponentViewDoc].
It is therefore helpful to read this documentation first as the features from this superclass doesn't get repeated here.

## Introduction
[LayoutViews][LayoutViewKDoc] are used to arrange /components.
There are two available LayoutViews: [Pane](#pane) and [GridPane](#gridpane).

## [Pane][PaneKDoc]
The [Pane][PaneKDoc] arranges its contents in a new coordinate space in the size of the Pane.
Elements added to the Pane align themselves relative to the top-left corner of the Pane.
This can be used to group a set of /components to move them simultaneously by altering the Pane's position. 
A Pane is therefore quite similar to an [Area][AreaDoc] but can be used in [MenuScenes][MenuSceneKDoc]. 

*Note that a Pane is not a [Container][ContainerDoc] and therefore not draggable. 
Use an [Area][AreaDoc] to combine /components to a drag group.*

## [GridPane][GridPaneKDoc]
The [GridPane][GridPaneKDoc] arranges its contents in rows and columns. 
All rows and columns adjust their size automatically based on the largest element in this row / column. 
This implies that empty rows and column get width / height of 0. 
Fixed sizes can be specified as well as a spacing between rows and columns.

### Constructor
The grid needs an initial row and column count. 
These can be altered as described [later](#adding-rows--columns).
By default, the grid gets anchored at its center. 
This means that ``posX`` and ``posY`` refer to the grids center point and the grid expands equally in all directions.
This can be altered by passing ``layoutFromCenter = false`` to the constructor.

### Adding and removing elements
The Grid can be array-like indexed with ``grid[columnIndex, rowIndex]`` for all get and set operations.<br>
````kotlin
val content = grid[0,2]
````
will retrieve the content of cell ``[0,2]``. Empty cells contain ``null``.

````kotlin
grid[0,2] = Button(text="Hello")
````
will replace the content of cell ``[0,2]`` with a button. To remove elements use
````kotlin
grid[0,2] = null
````

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
Note that the iterator iterates through all cells including those that are empty.
Therefore, the returned GridIteratorElement may contain ``null`` as component.

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