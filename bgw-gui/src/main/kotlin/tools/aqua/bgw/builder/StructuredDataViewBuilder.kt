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

package tools.aqua.bgw.builder

import javafx.beans.property.ReadOnlyStringWrapper
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.scene.control.ListCell
import javafx.scene.control.ListView as FXListView
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn as FXTableColumn
import javafx.scene.control.TableRow
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

/** UINodeBuilder. Factory for all BGW UI components. */
object StructuredDataViewBuilder {
  /** Switches between [UIComponent]s. */
  internal fun buildStructuredDataView(node: StructuredDataView<*>): Region =
      when (node) {
        is ListView<*> -> buildListView(node)
        is TableView<*> -> buildTableView(node)
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

  // TODO: extract
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

        if (node.selectionMode != SelectionMode.NONE && isSelected) {
          node.selectionBackgroundProperty.setGUIListenerAndInvoke(node.selectionBackground) { _, nV
            ->
            background =
                Background(BackgroundFill(nV.color.toFXColor(), CornerRadii.EMPTY, Insets.EMPTY))
          }
          node.selectionStyleProperty.setGUIListenerAndInvoke(node.selectionStyle) { _, nV ->
            if (style.isNotBlank()) style = nV
          }
        }
      }
    }
  }
  // endregion

  // region TableView
  /** Builds [TableView]. */
  private fun <T> buildTableView(tableView: TableView<T>): Region =
      FXTableView<T>().apply {
        tableView.columns.setGUIListenerAndInvoke(tableView.columns.toList()) { _, nV ->
          columns.setAll(nV.map { buildColumn(tableView, it) })
        }

        tableView.items.setGUIListenerAndInvoke(tableView.items.toList()) { _, nV ->
          items.setAll(nV)
        }

        tableView.setSelectionHandlers(selectionModel)

        isEditable = false
        background = Background.EMPTY
        rowFactory = buildTableViewRowFactory()
      }

  /** Builds [TableView] columns. */
  private fun <T> buildColumn(tableView: TableView<T>, column: TableColumn<T>) =
      FXTableColumn<T, String>(column.title).apply {
        minWidth = column.width.toDouble()
        isResizable = false
        style = tableView.font.toFXFontCSS()

        cellFactory = buildTableViewCellFactory(tableView, column)
        cellValueFactory =
            Callback { data ->
              ReadOnlyStringWrapper(
                  column.formatFunction?.invoke(data.value) ?: data.value.toString())
            }
      }

  /** Builds [ListView] row factory. */
  private fun <T> buildTableViewRowFactory(): Callback<FXTableView<T>, TableRow<T>> = Callback {
    object : TableRow<T>() {
      override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        background = Background.EMPTY // TODO: Row selection mode?
      }
    }
  }

  /** Builds [TableView] cell factory. */
  private fun <T> buildTableViewCellFactory(
      node: TableView<T>,
      column: TableColumn<T>
  ): Callback<FXTableColumn<T, String>, TableCell<T, String>> = Callback {
    object : TableCell<T, String>() {
      override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        background = Background.EMPTY

        column.fontProperty.setGUIListenerAndInvoke(column.font) { _, font ->
          style = font.toFXFontCSS()
          textFill = font.color.toFXColor()
        }

        column.formatFunctionProperty.setGUIListenerAndInvoke(column.formatFunction) { _, nV ->
          text = if (empty) "" else item.toString()
        }

        if (node.selectionMode != SelectionMode.NONE &&
            isSelected) { // TODO: Test with cell selection mode
          node.selectionBackgroundProperty.setGUIListenerAndInvoke(node.selectionBackground) { _, nV
            ->
            background =
                Background(BackgroundFill(nV.color.toFXColor(), CornerRadii.EMPTY, Insets.EMPTY))
          }
          node.selectionStyleProperty.setGUIListenerAndInvoke(node.selectionStyle) { _, nV ->
            if (style.isNotBlank()) style = nV
          }
        }
      }
    }
  }
  // endregion

  // region Helper
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

  // endregion
}
