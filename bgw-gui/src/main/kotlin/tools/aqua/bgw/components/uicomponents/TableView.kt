/*
 * Copyright 2021-2022 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.DEFAULT_TABLE_VIEW_HEIGHT
import tools.aqua.bgw.core.DEFAULT_TABLE_VIEW_WIDTH
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [TableView] may be used to visualize a data table.
 *
 * The items list is used as the data model. The columns list defines how the data is represented.
 *
 * Simplified example on how the columns list is used to represent the data:
 *
 * items: 1, 2, 3
 *
 * columns: ("first", {x -> x+1}, 10),
 * ```
 *          ("second", {x -> "nice string " + x}, 14),
 *
 *          ("third", {x -> "" + x*x + "!"}, 10)
 * ```
 * Representation:
 *
 * |first |second |third |
 *
 * |----------|--------------|----------|
 *
 * |2 |nice string 1 |1! |
 *
 * |3 |nice string 2 |4! |
 *
 * |4 |nice string 3 |9! |
 *
 * @constructor Creates a [TableView].
 *
 * @param T Generic [TableView] content.
 * @param posX Horizontal coordinate for this [TableView]. Default: 0.
 * @param posY Vertical coordinate for this [TableView]. Default: 0.
 * @param width Width for this [TableView]. Default: [DEFAULT_TABLE_VIEW_WIDTH].
 * @param height Height for this [TableView]. Default: [DEFAULT_TABLE_VIEW_HEIGHT].
 * @param items Initial list of items for this [TableView]. Default: empty list.
 * @param font The [Font] for this [TableView]. Default: default [Font] constructor.
 * @param visual Background [Visual]. Default: [ColorVisual.WHITE].
 * @param selectionMode Selection mode to be used for this [TableView]. Default:
 * [SelectionMode.SINGLE].
 * @param selectionBackground Background for selected items in this [TableView]. Default:
 * [ColorVisual.BLUE].
 *
 * @see TableColumn
 */
open class TableView<T>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_TABLE_VIEW_WIDTH,
    height: Number = DEFAULT_TABLE_VIEW_HEIGHT,
    items: List<T> = emptyList(),
    font: Font = Font(),
    visual: Visual = ColorVisual.WHITE,
    selectionMode: SelectionMode = SelectionMode.SINGLE,
    selectionBackground: ColorVisual = ColorVisual.BLUE,
) :
    StructuredDataView<T>(
        posX = posX,
        posY = posY,
        width = width,
        height = height,
        items = items,
        font = font,
        visual = visual,
        selectionMode = selectionMode,
        selectionBackground = selectionBackground) {
  /**
   * An [ObservableList] that contains [TableColumn]s which specify how the data is represented in
   * that column. The first [TableColumn] in this [ObservableList] will be the leftmost column in
   * the rendered [TableView].
   * @see TableColumn
   */
  val columns: ObservableList<TableColumn<T>> = ObservableArrayList()
}
