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

@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.application.Constants
import tools.aqua.bgw.components.uicomponents.*

internal object UIComponentBuilder {
  fun build(uiComponent: UIComponent) {
    uiComponent.fontProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(uiComponent)
    }
    when (uiComponent) {
      is LabeledUIComponent -> buildLabeledUIComponent(uiComponent)
      is TextInputUIComponent -> buildTextInputUIComponent(uiComponent)
      is ComboBox<*> -> buildComboBox(uiComponent)
      is ColorPicker -> buildColorPicker(uiComponent)
      is ProgressBar -> buildProgressBar(uiComponent)
      is StructuredDataView<*> -> buildStructuredDataView(uiComponent)
    }
  }

  private fun buildLabeledUIComponent(labeledUIComponent: LabeledUIComponent) {
    labeledUIComponent.isWrapTextProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(labeledUIComponent)
    }
    labeledUIComponent.textProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(labeledUIComponent)
    }
    labeledUIComponent.alignmentProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(labeledUIComponent)
    }
    when (labeledUIComponent) {
      is Button -> buildButton(labeledUIComponent)
      is CheckBox -> buildCheckBox(labeledUIComponent)
      is Label -> buildLabel(labeledUIComponent)
      is BinaryStateButton -> buildBinaryStateButton(labeledUIComponent)
    }
  }

  private fun buildTextInputUIComponent(textInputUIComponent: TextInputUIComponent) {
    textInputUIComponent.textProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(textInputUIComponent)
    }
    textInputUIComponent.promptProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(textInputUIComponent)
    }
    textInputUIComponent.isReadonlyProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(textInputUIComponent)
    }
    when (textInputUIComponent) {
      is TextArea -> buildTextArea(textInputUIComponent)
      is TextField -> buildTextField(textInputUIComponent)
      is PasswordField -> buildPasswordField(textInputUIComponent)
    }
  }

  private fun buildBinaryStateButton(binaryStateButton: BinaryStateButton) {
    // TODO: Add property for toggle group
    binaryStateButton.selectedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(binaryStateButton)
    }
    when (binaryStateButton) {
      is ToggleButton -> buildToggleButton(binaryStateButton)
      is RadioButton -> buildRadioButton(binaryStateButton)
    }
  }

  private fun buildButton(button: Button) {}

  private fun buildCheckBox(checkBox: CheckBox) {
    checkBox.isCheckedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(checkBox)
    }
    checkBox.isIndeterminateAllowedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(checkBox)
    }
    checkBox.isIndeterminateProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(checkBox)
    }
  }

  private fun buildLabel(label: Label) {}

  private fun buildComboBox(comboBox: ComboBox<*>) {
    comboBox.observableItemsList.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(comboBox)
    }
    comboBox.selectedItemProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(comboBox)
    }
    comboBox.formatFunctionProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(comboBox)
    }
    comboBox.itemVisualProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(comboBox)
    }
  }

  private fun buildTextArea(textArea: TextArea) {}

  private fun buildTextField(textField: TextField) {}

  private fun buildPasswordField(passwordField: PasswordField) {}

  private fun buildToggleButton(toggleButton: ToggleButton) {}

  private fun buildRadioButton(radioButton: RadioButton) {}

  private fun buildColorPicker(colorPicker: ColorPicker) {
    colorPicker.selectedColorProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(colorPicker)
    }
  }

  private fun buildProgressBar(progressBar: ProgressBar) {
    progressBar.progressProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(progressBar)
    }
    progressBar.barVisualProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(progressBar)
    }
  }

  private fun buildStructuredDataView(structuredDataView: StructuredDataView<*>) {
    structuredDataView.items.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(structuredDataView)
    }
    structuredDataView.selectionModeProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(structuredDataView)
    }
    structuredDataView.selectionBackgroundProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(structuredDataView)
    }
    structuredDataView.selectedItemsList.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(structuredDataView)
    }
    structuredDataView.selectedIndicesList.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(structuredDataView)
    }
  }
}
