/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.control.ListView as FXListView
import javafx.scene.control.TableColumn as FXTableColumn
import javafx.scene.control.TableView as FXTableView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.Region
import javafx.util.Callback
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.toFXSelectionMode
import tools.aqua.bgw.builder.FXConverters.toJavaFXOrientation
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.components.uicomponents.SelectionMode

/** UINodeBuilder. Factory for all BGW UI components. */
object StructuredDataViewBuilder {
  /** Switches between [UIComponent]s. */
  internal fun buildStructuredDataView(node: StructuredDataView<*>): Region =
      when (node) {
        is ListView<*> -> buildListView(node)
        is TableView<*> -> buildTableView(node)
        else -> throw IllegalArgumentException("Unknown StructuredDataView type.")
      }

  // region ListView
  /** Builds [ListView]. */
  private fun <T> buildListView(listView: ListView<T>): Region =
      FXListView<T>().apply {
        cellFactory = buildListViewCellFactory(listView)

        listView.items.setGUIListenerAndInvoke(listView.items.toList()) { _, nV ->
          items.setAll(nV)
        }

        listView.orientationProperty.setGUIListenerAndInvoke(listView.orientation) { _, nV ->
          orientationProperty().value = nV.toJavaFXOrientation()
        }

        listView.setSelectionHandlers(selectionModel)

        background = Background.EMPTY
      }

  /** Builds [ListView] cell factory. */
  private fun <T> buildListViewCellFactory(
      node: ListView<T>
  ): Callback<javafx.scene.control.ListView<T>, ListCell<T>> = Callback {
    object : ListCell<T>() {
      override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        background = Background.EMPTY

        node.fontProperty.setGUIListenerAndInvoke(node.font) { _, font ->
          style = font.toFXFontCSS()
          textFill = font.color.toFXColor()
        }

        node.formatFunctionProperty.setGUIListenerAndInvoke(node.formatFunction) { _, nV ->
          text = if (empty) "" else nV?.invoke(item) ?: item.toString()
        }

        setSelectionCallback(node)
      }
    }
  }
  // endregion

  // region TableView
  /** Builds [TableView]. */
  private fun <T> buildTableView(tableView: TableView<T>): Region =
      FXTableView<T>().apply {
        tableView.columns.setGUIListenerAndInvoke(tableView.columns.toList()) { _, nV ->
          columns.setAll(nV.map { buildTableViewColumn(it) })
        }

        tableView.items.setGUIListenerAndInvoke(tableView.items.toList()) { _, nV ->
          items.setAll(nV)
        }

        tableView.setSelectionHandlers(selectionModel)

        isEditable = false
        background = Background.EMPTY
        rowFactory = buildTableViewRowFactory(tableView)
      }

  /** Builds [TableView] columns. */
  private fun <T> buildTableViewColumn(column: TableColumn<T>) =
      FXTableColumn<T, String>().apply {
        isResizable = false

        column.titleProperty.setGUIListenerAndInvoke(column.title) { _, nV -> text = nV }

        column.widthProperty.setGUIListenerAndInvoke(column.width) { _, nV ->
          minWidth = nV
          maxWidth = nV
          tableView?.refresh()
        }

        cellFactory = buildTableViewCellFactory(column)

        column.formatFunctionProperty.setGUIListenerAndInvoke(column.formatFunction) { _, nV ->
          cellValueFactory = Callback {
            ReadOnlyStringWrapper(nV?.invoke(it.value) ?: it.value.toString())
          }
          tableView?.refresh()
        }
      }

  /** Builds [ListView] row factory. */
  private fun <T> buildTableViewRowFactory(
      node: TableView<T>
  ): Callback<FXTableView<T>, TableRow<T>> = Callback {
    object : TableRow<T>() {
      override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        background = Background.EMPTY

        setSelectionCallback(node)
      }
    }
  }

  /** Builds [TableView] cell factory. */
  private fun <T> buildTableViewCellFactory(
      column: TableColumn<T>
  ): Callback<FXTableColumn<T, String>, TableCell<T, String>> = Callback {
    object : TableCell<T, String>() {
      override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        background = Background.EMPTY
        text = if (empty) "" else item

        column.fontProperty.setGUIListenerAndInvoke(column.font) { _, font ->
          style = font.toFXFontCSS()
          textFill = font.color.toFXColor()
          this.tableView?.refresh()
        }
      }
    }
  }
  // endregion

  // region Helper
  /** Adds selection handlers to [StructuredDataView]. */
  private fun <T> StructuredDataView<T>.setSelectionHandlers(
      selectionModel: MultipleSelectionModel<T>
  ) {
    selectionModeProperty.setGUIListenerAndInvoke(selectionMode) { oV, nV ->
      selectionModel.selectionMode = nV.toFXSelectionMode()

      if (oV == SelectionMode.NONE || nV == SelectionMode.NONE) selectionModel.clearSelection()
    }

    selectionModel.selectedItems.addListener(
        ListChangeListener {
          if (selectionMode != SelectionMode.NONE)
              selectedItemsList.setAll(selectionModel.selectedItems.toList())
        })

    selectionModel.selectedIndices.addListener(
        ListChangeListener {
          if (selectionMode != SelectionMode.NONE)
              selectedIndicesList.setAll(selectionModel.selectedIndices.toList())
        })

    onSelectionEvent = { selectionModel.clearAndSelect(it) }
    onSelectAllEvent = { selectionModel.selectAll() }
    onSelectNoneEvent = { selectionModel.clearSelection() }
  }

  /** Adds selection callback to [ListCell] / [TableRow]. */
  private fun <T> IndexedCell<T>.setSelectionCallback(node: StructuredDataView<T>) {
    if (node.selectionMode != SelectionMode.NONE && isSelected) {
      node.selectionBackgroundProperty.setGUIListenerAndInvoke(node.selectionBackground) { _, nV ->
        background =
            Background(BackgroundFill(nV.color.toFXColor(), CornerRadii.EMPTY, Insets.EMPTY))
      }
      node.selectionStyleProperty.setGUIListenerAndInvoke(node.selectionStyle) { _, nV ->
        style = nV
      }
    }
  }
  // endregion
}
