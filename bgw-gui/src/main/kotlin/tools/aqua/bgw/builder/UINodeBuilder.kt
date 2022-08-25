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

import com.jfoenix.controls.*
import java.awt.Color
import javafx.beans.property.BooleanProperty as FXBooleanProperty
import javafx.beans.property.ObjectProperty as FXObjectProperty
import javafx.beans.property.StringProperty as FXStringProperty
import javafx.geometry.Pos
import javafx.scene.control.ColorPicker as FXColorPicker
import javafx.scene.control.Label as FXLabel
import javafx.scene.control.Labeled
import javafx.scene.control.ListCell
import javafx.scene.control.PasswordField as FXPasswordField
import javafx.scene.control.ProgressBar as FXProgressBar
import javafx.scene.control.TextArea as FXTextArea
import javafx.scene.control.TextField as FXTextField
import javafx.scene.control.TextInputControl as FXTextInputControl
import javafx.scene.input.KeyCode as FXKeyCode
import javafx.scene.input.KeyEvent as FXKeyEvent
import javafx.scene.layout.Region
import javafx.util.Callback
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.toFXPos
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.observable.properties.BooleanProperty

/** UINodeBuilder. Factory for all BGW UI components. */
object UINodeBuilder {
  /** Switches between [UIComponent]s. */
  internal fun buildUIComponent(uiComponent: UIComponent): Region =
      when (uiComponent) {
        is Button -> buildButton(uiComponent)
        is CheckBox -> buildCheckBox(uiComponent)
        is ComboBox<*> -> buildComboBox(uiComponent)
        is Label -> buildLabel(uiComponent)
        is TextArea -> buildTextArea(uiComponent)
        is TextField -> buildTextField(uiComponent)
        is PasswordField -> buildPasswordField(uiComponent)
        is ToggleButton -> buildToggleButton(uiComponent)
        is RadioButton -> buildRadioButton(uiComponent)
        is ColorPicker -> buildColorPicker(uiComponent)
        is ProgressBar -> buildProgressBar(uiComponent)
        is StructuredDataView<*> -> StructuredDataViewBuilder.buildStructuredDataView(uiComponent)
      }

  // region Build components
  /** Builds [Labeled]. */
  private fun buildLabel(label: Label): Region =
      FXLabel().apply {
        textProperty().bindTextProperty(label)
        alignmentProperty().bindAlignmentProperty(label)
        bindWrapTextProperty(label.isWrapTextProperty)
      }

  /** Builds [Button]. */
  private fun buildButton(button: Button): Region =
      JFXButton().apply {
        textProperty().bindTextProperty(button)
        alignmentProperty().bindAlignmentProperty(button)
        bindWrapTextProperty(button.isWrapTextProperty)
      }

  /** Builds [TextArea]. */
  private fun buildTextArea(textArea: TextArea): Region =
      FXTextArea(textArea.textProperty.value).apply {
        textProperty().bindTextProperty(textArea)
        promptTextProperty().bindPromptProperty(textArea)
        disableUndo()
      }

  /** Builds [TextField]. */
  private fun buildTextField(textField: TextField): Region =
      FXTextField(textField.textProperty.value).apply {
        textProperty().bindTextProperty(textField)
        promptTextProperty().bindPromptProperty(textField)
        disableUndo()
      }

  /** Builds [PasswordField]. */
  private fun buildPasswordField(passwordField: PasswordField): Region =
      FXPasswordField().apply {
        text = passwordField.textProperty.value
        textProperty().bindTextProperty(passwordField)
        promptTextProperty().bindPromptProperty(passwordField)
        disableUndo()
      }

  /** Builds [ComboBox]. */
  private fun <T> buildComboBox(comboBox: ComboBox<T>): Region =
      JFXComboBox<T>().apply {
        comboBox.observableItemsList.setGUIListenerAndInvoke(emptyList()) { _, _ ->
          items.clear()
          comboBox.items.forEach { items.add(it) }
          setCellFactory(comboBox)
        }
        selectionModel.selectedItemProperty().addListener { _, _, newValue ->
          comboBox.selectedItem = newValue
        }
        comboBox.selectedItemProperty.setInternalListenerAndInvoke(comboBox.selectedItem) {
            _,
            newValue,
          ->
          if (newValue != null) {
            selectionModel.select(newValue)
          }
        }
        promptText = comboBox.prompt
        // font
        comboBox.fontProperty.setGUIListenerAndInvoke(comboBox.font) { _, _ ->
          buttonCell.style = comboBox.font.toFXFontCSS()
          buttonCell.textFill = comboBox.font.color.toFXColor()
          setCellFactory(comboBox)
        }

        comboBox.formatFunctionProperty.guiListener = { _, _ -> setCellFactory(comboBox) }
      }

  /** Builds [CheckBox]. */
  private fun buildCheckBox(checkBox: CheckBox): Region =
      JFXCheckBox().apply {
        textProperty().bindTextProperty(checkBox)
        allowIndeterminateProperty().bindBooleanProperty(checkBox.isIndeterminateAllowedProperty)
        indeterminateProperty().bindBooleanProperty(checkBox.isIndeterminateProperty)
        selectedProperty().bindBooleanProperty(checkBox.isCheckedProperty)
        alignmentProperty().bindAlignmentProperty(checkBox)
        bindWrapTextProperty(checkBox.isWrapTextProperty)
      }

  /** Builds [ColorPicker]. */
  private fun buildColorPicker(colorPicker: ColorPicker): Region =
      FXColorPicker().apply {
        colorPicker.selectedColorProperty.setGUIListenerAndInvoke(colorPicker.selectedColor) {
            _,
            newValue,
          ->
          value = newValue.toFXColor()
        }
        valueProperty().addListener { _, _, nV ->
          colorPicker.selectedColor =
              Color(nV.red.toFloat(), nV.green.toFloat(), nV.blue.toFloat(), nV.opacity.toFloat())
        }
      }

  /** Builds [ToggleButton]. */
  private fun buildToggleButton(toggleButton: ToggleButton): Region =
      JFXToggleButton().apply {
        textProperty().bindTextProperty(toggleButton)
        alignmentProperty().bindAlignmentProperty(toggleButton)
        selectedProperty().bindBooleanProperty(toggleButton.selectedProperty)
        bindWrapTextProperty(toggleButton.isWrapTextProperty)
      }

  /** Builds [RadioButton]. */
  private fun buildRadioButton(radioButton: RadioButton): Region =
      JFXRadioButton().apply {
        textProperty().bindTextProperty(radioButton)
        alignmentProperty().bindAlignmentProperty(radioButton)
        selectedProperty().addListener { _, _, nV -> radioButton.isSelected = nV }

        radioButton.selectedProperty.setGUIListenerAndInvoke(radioButton.isSelected) { _, nV ->
          selectedProperty().value =
              if (nV || !radioButton.toggleGroup.buttons.none { it.isSelected }) nV
              else true // Reselect if attempting to deselect last radio button
        }
      }

  /** Builds [ProgressBar]. */
  private fun buildProgressBar(progressBar: ProgressBar): Region =
      FXProgressBar().apply {
        progressBar.progressProperty.setGUIListenerAndInvoke(progressBar.progress) { _, nV ->
          progress = if (nV < 0.0) 0.0 else nV
        }
        progressBar.barColorProperty.setGUIListenerAndInvoke(progressBar.barColor) { _, nV ->
          progressBar.internalCSS =
              "-fx-accent: rgba(${nV.red},${nV.green},${nV.blue},${nV.alpha});"
        }
      }
  // endregion

  // region Bind properties
  /** Binds [TextInputUIComponent.textProperty]. */
  private fun FXStringProperty.bindTextProperty(labeled: TextInputUIComponent) {
    // Framework -> JavaFX
    labeled.textProperty.setGUIListenerAndInvoke(labeled.text) { _, nV -> value = nV }
    // JavaFX -> Framework
    addListener { _, _, new -> labeled.text = new }
  }

  /** Binds [LabeledUIComponent.textProperty]. */
  private fun FXStringProperty.bindTextProperty(labeled: LabeledUIComponent) {
    // Framework -> JavaFX
    labeled.textProperty.setGUIListenerAndInvoke(labeled.text) { _, nV -> value = nV }
    // JavaFX -> Framework
    addListener { _, _, new -> labeled.text = new }
  }

  /** Binds [TextInputUIComponent.promptProperty]. */
  private fun FXStringProperty.bindPromptProperty(labeled: TextInputUIComponent) {
    // Framework -> JavaFX
    labeled.promptProperty.setGUIListenerAndInvoke(labeled.prompt) { _, nV -> value = nV }
  }

  /** Binds [LabeledUIComponent.alignmentProperty]. Framework -> JavaFX only. */
  private fun FXObjectProperty<Pos>.bindAlignmentProperty(labeled: LabeledUIComponent) {
    // Framework -> JavaFX
    labeled.alignmentProperty.setGUIListenerAndInvoke(labeled.alignment) { _, nV ->
      value = nV.toFXPos()
    }
  }

  /** Binds [LabeledUIComponent.isWrapTextProperty]. */
  private fun Labeled.bindWrapTextProperty(booleanProperty: BooleanProperty) {
    // Framework -> JavaFX
    booleanProperty.setGUIListenerAndInvoke(booleanProperty.value) { _, newValue ->
      isWrapText = newValue
    }
  }

  /** Binds [BooleanProperty]. */
  private fun FXBooleanProperty.bindBooleanProperty(booleanProperty: BooleanProperty) {
    // Framework -> JavaFX
    booleanProperty.setGUIListenerAndInvoke(booleanProperty.value) { _, nV -> value = nV }

    // JavaFX -> Framework
    addListener { _, _, nV -> booleanProperty.value = nV }
  }
  // endregion

  // region Helper
  /** Disables the default undo operation on [FXTextInputControl]s on ctrl + z. */
  private fun FXTextInputControl.disableUndo() {
    addEventFilter(FXKeyEvent.ANY) { if (it.code == FXKeyCode.Z && it.isShortcutDown) it.consume() }
  }

  /** Sets [ComboBox] cell factory . */
  private fun <T> JFXComboBox<T>.setCellFactory(comboBox: ComboBox<T>) {
    cellFactory = Callback { buildListCell(comboBox) }
    buttonCell = buildListCell(comboBox)
  }

  /** Creates [ComboBox] cell factory . */
  private fun <T> buildListCell(comboBox: ComboBox<T>): ListCell<T> =
      object : ListCell<T>() {
        override fun updateItem(item: T, empty: Boolean) {
          super.updateItem(item, empty)

          if (empty) {
            this.textFill = Color(0F, 0F, 0F, 0F).toFXColor()
          } else {
            this.style = comboBox.font.toFXFontCSS()
            this.textFill = comboBox.font.color.toFXColor()
            this.text = comboBox.formatFunction?.invoke(item) ?: item.toString()
          }
        }
      }
  // endregion
}
