/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

import ButtonData
import CameraPaneData
import CardViewData
import CheckBoxData
import ColorPickerData
import ComboBoxData
import ComponentViewData
import DiceViewData
import GameComponentContainerData
import HexagonGridData
import HexagonViewData
import LabelData
import LayoutViewData
import ListViewData
import PasswordFieldData
import ProgressBarData
import RadioButtonData
import SliderData
import TableViewData
import TextAreaData
import TextFieldData
import ToggleButtonData
import TokenViewData
import react.*
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.container.HexagonGrid as ReactHexagonGrid
import tools.aqua.bgw.elements.gamecomponentviews.CardView as ReactCardView
import tools.aqua.bgw.elements.gamecomponentviews.DiceView as ReactDiceView
import tools.aqua.bgw.elements.gamecomponentviews.HexagonView as ReactHexagonView
import tools.aqua.bgw.elements.gamecomponentviews.TokenView as ReactTokenView
import tools.aqua.bgw.elements.layoutviews.CameraPane as ReactCameraPane
import tools.aqua.bgw.elements.uicomponents.Button as ReactButton
import tools.aqua.bgw.elements.uicomponents.CheckBox as ReactCheckBox
import tools.aqua.bgw.elements.uicomponents.ColorPicker as ReactColorPicker
import tools.aqua.bgw.elements.uicomponents.ComboBox as ReactComboBox
import tools.aqua.bgw.elements.uicomponents.Label as ReactLabel
import tools.aqua.bgw.elements.uicomponents.ListView as ReactListView
import tools.aqua.bgw.elements.uicomponents.PasswordField as ReactPasswordField
import tools.aqua.bgw.elements.uicomponents.ProgressBar as ReactProgressBar
import tools.aqua.bgw.elements.uicomponents.RadioButton as ReactRadioButton
import tools.aqua.bgw.elements.uicomponents.Slider as ReactSlider
import tools.aqua.bgw.elements.uicomponents.TableView as ReactTableView
import tools.aqua.bgw.elements.uicomponents.TextArea as ReactTextArea
import tools.aqua.bgw.elements.uicomponents.TextField as ReactTextField
import tools.aqua.bgw.elements.uicomponents.ToggleButton as ReactToggleButton
import web.cssom.*

internal external interface NodeProps : Props {
  var data: ComponentViewData
  var isOverlayPreview: Boolean
}

/** Structural equality of two decoded data objects. */
internal val dataEquals: (Any?, Any?) -> Boolean =
    js(
        """
        (function deepEqual(a, b) {
          if (a === b) return true;
          if (a === null || b === null || typeof a !== 'object' || typeof b !== 'object') return false;
          if (Object.getPrototypeOf(a) !== Object.getPrototypeOf(b)) return false;
          var keysA = Object.keys(a);
          if (keysA.length !== Object.keys(b).length) return false;
          for (var i = 0; i < keysA.length; i++) {
            var key = keysA[i];
            if (!deepEqual(a[key], b[key])) return false;
          }
          return true;
        })
        """)

/**
 * Renders a component and skips re-rendering it (and its whole subtree) if its data did not change.
 * Components still re-render on their own state changes, e.g. animated visuals or drag state.
 */
private val MemoNode: FC<NodeProps> =
    memo(
        FC<NodeProps> { props -> +NodeBuilder.buildNode(props.data, props.isOverlayPreview) },
        { previous, next ->
          previous.isOverlayPreview == next.isOverlayPreview && dataEquals(previous.data, next.data)
        })

internal object NodeBuilder {
  fun build(
      componentViewData: ComponentViewData,
      isOverlayPreview: Boolean = false
  ): ReactElement<*> =
      MemoNode.create {
        data = componentViewData
        this.isOverlayPreview = isOverlayPreview
      }

  internal fun buildNode(
      componentViewData: ComponentViewData,
      isOverlayPreview: Boolean = false
  ): ReactElement<*> {
    return when (componentViewData) {
      is LayoutViewData -> LayoutNodeBuilder.build(componentViewData, isOverlayPreview)
      is LabelData -> ReactLabel.create { data = componentViewData }
      is ButtonData -> ReactButton.create { data = componentViewData }
      is TextFieldData -> ReactTextField.create { data = componentViewData }
      is ComboBoxData -> ReactComboBox.create { data = componentViewData }
      is HexagonGridData ->
          ReactHexagonGrid.create {
            data = componentViewData
            this.isOverlayPreview = isOverlayPreview
          }
      is CameraPaneData -> ReactCameraPane.create { data = componentViewData }
      is ProgressBarData -> ReactProgressBar.create { data = componentViewData }
      is CheckBoxData -> ReactCheckBox.create { data = componentViewData }
      is RadioButtonData -> ReactRadioButton.create { data = componentViewData }
      is SliderData -> ReactSlider.create { data = componentViewData }
      is PasswordFieldData -> ReactPasswordField.create { data = componentViewData }
      is ToggleButtonData -> ReactToggleButton.create { data = componentViewData }
      is ColorPickerData -> ReactColorPicker.create { data = componentViewData }
      is TextAreaData -> ReactTextArea.create { data = componentViewData }
      is TableViewData -> ReactTableView.create { data = componentViewData }
      is ListViewData -> ReactListView.create { data = componentViewData }
      is GameComponentContainerData -> ContainerBuilder.build(componentViewData, isOverlayPreview)
      is CardViewData ->
          ReactCardView.create {
            data = componentViewData
            this.isOverlayPreview = isOverlayPreview
          }
      is DiceViewData ->
          ReactDiceView.create {
            data = componentViewData
            this.isOverlayPreview = isOverlayPreview
          }
      is HexagonViewData ->
          ReactHexagonView.create {
            data = componentViewData
            this.isOverlayPreview = isOverlayPreview
          }
      is TokenViewData ->
          ReactTokenView.create {
            data = componentViewData
            this.isOverlayPreview = isOverlayPreview
          }
      else ->
          throw IllegalArgumentException(
              "Unknown component type: ${componentViewData::class.simpleName}")
    }
  }
}
