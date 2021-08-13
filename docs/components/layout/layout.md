---
parent: Components
title: LayoutViews
nav_order: 4
layout: default
---

[LayoutViewDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-layout-view/index.html
[PaneDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-pane/index.html
[GridPaneDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/index.html
[addRowsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/add-rows.html
[addColumnsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/add-columns.html
[growDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/grow.html

[removeRowDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-row.html
[removeColumnDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-column.html
[removeEmptyRowsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-rows.html
[removeEmptyColumnsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/remove-empty-columns.html
[trimDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/trim.html

[setRowHeightDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-height.html
[setRowHeightsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-heights.html
[setColumnWidthDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-width.html
[setColumnWidthsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-widths.html

[setAutoRowHeightDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-height.html
[setAutoRowHeightsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-row-heights.html
[setAutoColumnWidthDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-width.html
[setAutoColumnWidthsDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-auto-column-widths.html

[setCellCenterModeDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-cell-center-mode.html
[setRowCenterModeDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-row-center-mode.html
[setColumnCenterModeDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-column-center-mode.html
[setCenterModeDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/set-center-mode.html

[GridIteratorElementDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-grid-iterator-element/index.html
[AUTO_ROW_HEIGHT]:https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-r-o-w_-h-e-i-g-h-t_-a-u-t-o.html
[AUTO_COLUMN_WIDTH]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.layoutviews/-grid-pane/-companion/-c-o-l-u-m-n_-w-i-d-t-h_-a-u-t-o.html
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

[LayoutViews][LayoutViewDoc] are used to arrange components.
There are two available LayoutViews: [Pane](#pane) and [GridPane](#gridpane).

## [Pane][PaneDoc]



## [GridPane][GridPaneDoc]
The [GridPane][GridPaneDoc] arranges its contents in rows and columns. 
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
grid[0,2] = Button("Hello")
````
will replace the content of cell ``[0,2]`` with a button. To remove elements use
````kotlin
grid[0,2] = null
````

### Adding rows / columns
Rows and columns can be added by calling [addRows][addRowsDoc] and [addColumns][addColumnsDoc].
In both cases the desired index to insert and the amount of rows / columns to add can be passed.

To grow the grid in all directions use [grow][growDoc].

### Removing rows / columns
Rows and columns can be removed by calling [removeRow][removeRowDoc] and [removeColumn][removeColumnDoc].
In both cases the desired index to delete has to be passed.

To automatically remove all empty rows / columns use [removeEmptyRows][removeEmptyRowsDoc]
 and [removeEmptyColumns][removeEmptyColumnsDoc].

Using [trim][trimDoc] removes all empty outer rows and columns.

### Iterating grid components
The grid implements the Iterable interface which means that it can be iterated by using its iterator, in foreach loops, and in streams.

The iterator returns [GridIteratorElements][GridIteratorElementDoc] that contain the component and the current columnIndex and rowIndex.
Note that the iterator iterates through all cells including those that are empty.
Therefore, the returned GridIteratorElement may contain ``null`` as component.

### Setting fixed row heights and widths
By default, all rows and columns get rendered automatically according to the largest element in this row or column.
To set fixed values use [setRowHeight][setRowHeightDoc] / [setRowHeights][setRowHeightsDoc] and
[setColumnWidth][setColumnWidthDoc] / [setColumnWidths][setColumnWidthsDoc].

To restore automatic behaviour pass [AUTO_ROW_HEIGHT][AUTO_ROW_HEIGHT] / [AUTO_COLUMN_WIDTH][AUTO_COLUMN_WIDTH] or use
[setAutoRowHeight][setAutoRowHeightDoc] / [setAutoRowHeights][setAutoRowHeightsDoc] and
[setAutoColumnWidth][setAutoColumnWidthDoc] / [setAutoColumnWidths][setAutoColumnWidthsDoc]

### Changing centering behaviour
By default, components get centered in their cells if the cell's size is larger than the component.
This can be changed for each cell by calling [setCellCenterMode][setCellCenterModeDoc]. 

To change behaviour for entire rows or columns use [setRowCenterMode][setRowCenterModeDoc] and [setColumnCenterMode][setColumnCenterModeDoc].

To set centering behaviour for the entire grid use [setCenterMode][setCenterModeDoc].