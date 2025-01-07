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
import GridElementData
import HexagonGridData
import HexagonViewData
import LabelData
import LayoutViewData
import ListViewData
import PasswordFieldData
import ProgressBarData
import RadioButtonData
import TableViewData
import TextAreaData
import TextFieldData
import ToggleButtonData
import TokenViewData
import emotion.react.css
import react.*
import react.ReactElement
import react.create
import react.dom.html.HTMLAttributes
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
import tools.aqua.bgw.elements.uicomponents.TableView as ReactTableView
import tools.aqua.bgw.elements.uicomponents.TextArea as ReactTextArea
import tools.aqua.bgw.elements.uicomponents.TextField as ReactTextField
import tools.aqua.bgw.elements.uicomponents.ToggleButton as ReactToggleButton
import web.cssom.*
import web.cssom.integer
import web.dom.Element

internal object NodeBuilder {
  fun build(componentViewData: ComponentViewData): ReactElement<*> {
    return when (componentViewData) {
      is LayoutViewData -> LayoutNodeBuilder.build(componentViewData)
      is LabelData -> ReactLabel.create { data = componentViewData }
      is ButtonData -> ReactButton.create { data = componentViewData }
      is TextFieldData -> ReactTextField.create { data = componentViewData }
      is ComboBoxData -> ReactComboBox.create { data = componentViewData }
      is HexagonGridData -> ReactHexagonGrid.create { data = componentViewData }
      is CameraPaneData -> ReactCameraPane.create { data = componentViewData }
      is ProgressBarData -> ReactProgressBar.create { data = componentViewData }
      is CheckBoxData -> ReactCheckBox.create { data = componentViewData }
      is RadioButtonData -> ReactRadioButton.create { data = componentViewData }
      is PasswordFieldData -> ReactPasswordField.create { data = componentViewData }
      is ToggleButtonData -> ReactToggleButton.create { data = componentViewData }
      is ColorPickerData -> ReactColorPicker.create { data = componentViewData }
      is TextAreaData -> ReactTextArea.create { data = componentViewData }
      is TableViewData -> ReactTableView.create { data = componentViewData }
      is ListViewData -> ReactListView.create { data = componentViewData }
      is GameComponentContainerData -> ContainerBuilder.build(componentViewData)
      is CardViewData -> ReactCardView.create { data = componentViewData }
      is DiceViewData -> ReactDiceView.create { data = componentViewData }
      is HexagonViewData -> ReactHexagonView.create { data = componentViewData }
      is TokenViewData -> ReactTokenView.create { data = componentViewData }
      else ->
          throw IllegalArgumentException(
              "Unknown component type: ${componentViewData::class.simpleName}")
    }
  }

  fun build(gridElementData: GridElementData): ReactElement<*> {
    return FC<GridPaneElementProps> { props ->
          bgwGridElement {
            css {
              gridColumn = integer(gridElementData.column + 1)
              gridRow = integer(gridElementData.row + 1)
              justifySelf =
                  when (gridElementData.alignment.first) {
                    "left" -> JustifySelf.flexStart
                    "center" -> JustifySelf.center
                    "right" -> JustifySelf.flexEnd
                    else -> JustifySelf.center
                  }
              alignSelf =
                  when (gridElementData.alignment.second) {
                    "top" -> AlignSelf.flexStart
                    "center" -> AlignSelf.center
                    "bottom" -> AlignSelf.flexEnd
                    else -> AlignSelf.center
                  }
            }
            +props.data.component?.let { build(it) }
          }
        }
        .create { data = gridElementData }
  }
}

internal external interface GridPaneElementProps : Props {
  var data: GridElementData
}

internal inline val bgwGridElement: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_grid_element".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
