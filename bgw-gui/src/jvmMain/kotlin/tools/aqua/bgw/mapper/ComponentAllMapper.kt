/*
 * Copyright 2025 The BoardGameWork Authors
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

import ComponentMapper.fillData
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.mapper.VisualMapper

internal object ComponentMapper {
  fun ComponentViewData.fillData(componentView: ComponentView): ComponentViewData {
    return this.apply {
      id = componentView.id
      posX = componentView.posX.toInt()
      posY = componentView.posY.toInt()
      width = componentView.width.toInt()
      height = componentView.height.toInt()
      visual = VisualMapper.map(componentView.visual)
      zIndex = componentView.zIndex
      opacity = componentView.opacity
      isVisible = componentView.isVisible
      isDisabled = componentView.isDisabled
      // isFocusable
      scaleX = componentView.scaleX
      scaleY = componentView.scaleY
      rotation = componentView.rotation

      if (componentView.dropAcceptor != null) {
        isDroppable = true
      }
      hasMouseEnteredEvent = componentView.onMouseEntered != null
      hasMouseExitedEvent = componentView.onMouseExited != null
    }
  }

  private fun mapSpecific(componentView: ComponentView): ComponentViewData {
    return when (componentView) {
      is ListView<*> ->
          (ListViewData().fillData(componentView) as ListViewData).apply {
            items = componentView.items.map { item -> componentView.formatItem(item) }
            selectionMode = componentView.selectionMode.name.lowercase()
            selectionBackground = componentView.selectionBackground.color.toHex()
            selectedItems = componentView.selectedIndicesList.toList()
            font = FontMapper.map(componentView.font)
          }
      is TableView<*> ->
          (TableViewData().fillData(componentView) as TableViewData).apply {
            items = componentView.items.map { it.toString() }
            columns =
                componentView.columns.map {
                  TableColumnData().apply {
                    title = it.title
                    width = it.width.toInt()
                    font = FontMapper.map(it.font)
                    items = componentView.items.map { item -> it.formatItem(item) }
                  }
                }
            selectionMode = componentView.selectionMode.name.lowercase()
            selectionBackground = componentView.selectionBackground.color.toHex()
            selectedItems = componentView.selectedIndicesList.toList()
            font = FontMapper.map(componentView.font)
          }
      is LabeledUIComponent -> {
        when (componentView) {
          is Button -> ButtonData().fillData(componentView) as ButtonData
          is CheckBox -> CheckBoxData().fillData(componentView) as CheckBoxData
          is Label -> LabelData().fillData(componentView) as LabelData
          is RadioButton -> RadioButtonData().fillData(componentView) as RadioButtonData
          is ToggleButton -> ToggleButtonData().fillData(componentView) as ToggleButtonData
          else ->
              throw IllegalArgumentException(
                  "Unknown component type: ${componentView::class.simpleName}")
        }.apply {
          font = FontMapper.map(componentView.font)
          alignment =
              Pair(
                  componentView.alignment.horizontalAlignment.name.lowercase(),
                  componentView.alignment.verticalAlignment.name.lowercase())
          text = componentView.text
          isWrapText = componentView.isWrapText
        }
      }
      is TextInputUIComponent -> {
        when (componentView) {
          is PasswordField -> PasswordFieldData().fillData(componentView) as PasswordFieldData
          is TextField -> TextFieldData().fillData(componentView) as TextFieldData
          is TextArea -> TextAreaData().fillData(componentView) as TextAreaData
          else ->
              throw IllegalArgumentException(
                  "Unknown component type: ${componentView::class.simpleName}")
        }.apply {
          font = FontMapper.map(componentView.font)
          text = componentView.text
          prompt = componentView.prompt
          isReadonly = componentView.isReadonly
        }
      }
      is UIComponent -> {
        when (componentView) {
          is ComboBox<*> -> ComboBoxData().fillData(componentView) as ComboBoxData
          is ColorPicker -> ColorPickerData().fillData(componentView) as ColorPickerData
          is ProgressBar -> ProgressBarData().fillData(componentView) as ProgressBarData
          else ->
              throw IllegalArgumentException(
                  "Unknown component type: ${componentView::class.simpleName}")
        }.apply { font = FontMapper.map(componentView.font) }
      }
      is CameraPane<*> ->
          (CameraPaneData().fillData(componentView) as CameraPaneData).apply {
            target = LayoutAllMapper.map(componentView.target)
            interactive = componentView.interactive
            internalPanData = componentView.panData
            panButton = componentView.panMouseButton.name.lowercase()
            limitBounds = componentView.limitBounds
            isVerticalLocked = componentView.isVerticalLocked
            isHorizontalLocked = componentView.isHorizontalLocked
            isZoomLocked = componentView.isZoomLocked
          }
      is GameComponentView -> {
        when (componentView) {
          is CardView -> CardViewData().fillData(componentView) as CardViewData
          is DiceView -> DiceViewData().fillData(componentView) as DiceViewData
          is HexagonView -> HexagonViewData().fillData(componentView) as HexagonViewData
          is TokenView -> TokenViewData().fillData(componentView) as TokenViewData
          else ->
              throw IllegalArgumentException(
                  "Unknown component type: ${componentView::class.simpleName}")
        }.apply {
          isDraggable = componentView.isDraggable
          isDragged = componentView.isDragged
        }
      }
      else ->
          throw IllegalArgumentException(
              "Unknown component type: ${componentView::class.simpleName}")
    }
  }

  fun map(componentView: ComponentView): ComponentViewData {
    return when (componentView) {

      // TODO - LabeledUIComponent
      is Button -> (mapSpecific(componentView) as ButtonData)
      is CheckBox ->
          (mapSpecific(componentView) as CheckBoxData).apply {
            isChecked = componentView.isChecked
            allowIndeterminate = componentView.isIndeterminateAllowed
            isIndeterminate = componentView.isIndeterminate
          }
      is Label -> (mapSpecific(componentView) as LabelData)
      is RadioButton ->
          (mapSpecific(componentView) as RadioButtonData).apply {
            isSelected = componentView.isSelected
            group = componentView.toggleGroup.id
          }
      is ToggleButton ->
          (mapSpecific(componentView) as ToggleButtonData).apply {
            isSelected = componentView.isSelected
            group = componentView.toggleGroup.id
          }

      // TODO - TextInputUIComponent
      is PasswordField -> (mapSpecific(componentView) as PasswordFieldData)
      is TextField -> (mapSpecific(componentView) as TextFieldData)
      is TextArea -> (mapSpecific(componentView) as TextAreaData)

      // TODO - UIComponent
      is ComboBox<*> -> mapComboBox(componentView)
      is ColorPicker ->
          (mapSpecific(componentView) as ColorPickerData).apply {
            selectedColor = componentView.selectedColor.toHex()
          }
      is ProgressBar ->
          (mapSpecific(componentView) as ProgressBarData).apply {
            progress = componentView.progress.coerceIn(0.0, 1.0)
            barVisual = VisualMapper.map(componentView.barVisual)
          }

      // TODO - StructuredDataView
      is ListView<*> ->
          (mapSpecific(componentView) as ListViewData).apply {
            orientation = componentView.orientation.name.lowercase()
          }
      is TableView<*> ->
          (mapSpecific(componentView) as TableViewData).apply {
            // columns (as TableColumnData)
          }

      // TODO - ComponentView
      is CameraPane<*> -> (mapSpecific(componentView) as CameraPaneData)

      // TODO - GameComponentView
      is CardView ->
          (mapSpecific(componentView) as CardViewData).apply {
            front = VisualMapper.map(componentView.frontVisual)
            back = VisualMapper.map(componentView.backVisual)
            currentVisual =
                if (componentView.currentSide == CardView.CardSide.BACK)
                    VisualMapper.map(componentView.backVisual)
                else VisualMapper.map(componentView.frontVisual)
          }
      is DiceView ->
          (mapSpecific(componentView) as DiceViewData).apply {
            currentSide = componentView.currentSide
            visuals = componentView.visuals.map { VisualMapper.map(it) }
          }
      is HexagonView ->
          (mapSpecific(componentView) as HexagonViewData).apply {
            size = componentView.size.toInt()
            orientation = componentView.orientation.name.lowercase()
          }
      is TokenView -> (mapSpecific(componentView) as TokenViewData)
      else -> TODO("Not implemented")
    }
  }

  fun <T> mapComboBox(comboBox: ComboBox<T>): ComboBoxData {
    return (mapSpecific(comboBox) as ComboBoxData).apply {
      val selItem = comboBox.selectedItem
      prompt = comboBox.prompt
      items =
          comboBox.items.mapIndexed { index, it ->
            Pair(index, comboBox.formatFunction?.invoke(it) ?: it.toString())
          }
      selectedItem =
          if (selItem == null) null
          else
              Pair(
                  comboBox.getSelectedIndex(),
                  comboBox.formatFunction?.invoke(selItem) ?: comboBox.selectedItem.toString())
      disallowUnselect = comboBox.disallowUnselect
      itemVisuals =
          comboBox.items.mapIndexed { index, _ ->
            VisualMapper.map(comboBox.itemVisuals.getOrElse(index) { comboBox.visual })
          }
    }
  }
}

internal object LayoutAllMapper {
  fun map(layout: LayoutView<*>): LayoutViewData {
    return when (layout) {
      is Pane<*> ->
          (PaneData().fillData(layout) as PaneData).apply {
            components = layout.components.map { RecursiveMapper.map(it) }.toMutableList()
            if (layout.dropAcceptor != null) {
              isDroppable = true
            }
          }
      is GridPane<*> ->
          (GridPaneData().fillData(layout) as GridPaneData).apply {
            val grid =
                layout.grid.clone().apply {
                  removeEmptyColumns()
                  removeEmptyRows()
                }
            columns = grid.columns
            rows = grid.rows
            this.grid =
                grid.map {
                  val alignment = layout.getCellCenterMode(it.columnIndex, it.rowIndex)
                  GridElementData(
                      it.columnIndex,
                      it.rowIndex,
                      if (it.component != null) RecursiveMapper.map(it.component) else null,
                      alignment =
                          alignment.horizontalAlignment.name.lowercase() to
                              alignment.verticalAlignment.name.lowercase())
                }
            spacing = layout.spacing.toInt()
            layoutFromCenter = layout.isLayoutFromCenter
            if (layout.dropAcceptor != null) {
              isDroppable = true
            }
          }
      else -> throw IllegalArgumentException("Unknown layout type: ${layout::class.simpleName}")
    }
  }
}

internal object ContainerAllMapper {
  fun map(container: GameComponentContainer<*>): GameComponentContainerData {
    return when (container) {
      is Area<*> ->
          (AreaData().fillData(container) as AreaData).apply {
            components =
                container.components.map { RecursiveMapper.map(it) }.toMutableList()
                    as MutableList<GameComponentViewData>
            if (container.dropAcceptor != null) {
              isDroppable = true
            }
          }
      is CardStack<*> ->
          (CardStackData().fillData(container) as CardStackData).apply {
            components =
                container.components.map { RecursiveMapper.map(it) }.toMutableList()
                    as MutableList<GameComponentViewData>
            if (container.dropAcceptor != null) {
              isDroppable = true
            }
            alignment =
                Pair(
                    container.alignment.horizontalAlignment.name.lowercase(),
                    container.alignment.verticalAlignment.name.lowercase())
          }
      is HexagonGrid<*> -> {
        val tempMap = mutableMapOf<String, HexagonViewData>()
        container.map.forEach { (key, value) ->
          tempMap["${key.first}/${key.second}"] =
              (HexagonViewData().fillData(value) as HexagonViewData).apply {
                id = value.id
                posX = value.posX.toInt()
                posY = value.posY.toInt()
                visual = VisualMapper.map(value.visual)
                size = value.size.toInt()
                orientation = container.orientation.name.lowercase()
                // isDraggable = value.isDraggable          // TODO - Element has no root node
                // dragging out
              }
        }

        (HexagonGridData().fillData(container) as HexagonGridData).apply {
          coordinateSystem = container.coordinateSystem.name.lowercase()
          map = tempMap
          spacing = 0
          orientation = container.orientation.name.lowercase()
          // components ?!

          if (container.dropAcceptor != null) {
            isDroppable = true
          }
        }
      }
      is LinearLayout<*> ->
          (LinearLayoutData().fillData(container) as LinearLayoutData).apply {
            components =
                container.components.map { RecursiveMapper.map(it) }.toMutableList()
                    as MutableList<GameComponentViewData>
            spacing = container.spacing.toInt()
            orientation = container.orientation.name.lowercase()
            alignment =
                Pair(
                    container.alignment.horizontalAlignment.name.lowercase(),
                    container.alignment.verticalAlignment.name.lowercase())

            if (container.dropAcceptor != null) {
              isDroppable = true
            }
          }
      is Satchel ->
          (SatchelData().fillData(container) as SatchelData).apply {
            components =
                container.components.map { RecursiveMapper.map(it) }.toMutableList()
                    as MutableList<GameComponentViewData>

            if (container.dropAcceptor != null) {
              isDroppable = true
            }
          }
    }
  }
}

internal object RecursiveMapper {
    fun map(component: ComponentView): ComponentViewData {
        return when (component) {
            is LayoutView<*> -> {
                LayoutAllMapper.map(component)
            }
            is GameComponentContainer<*> -> {
                ContainerAllMapper.map(component).apply {
                    isDraggable = component.isDraggable
                    isDragged = component.isDragged
                }
            }
            else -> {
                ComponentMapper.map(component)
            }
        }
    }
}