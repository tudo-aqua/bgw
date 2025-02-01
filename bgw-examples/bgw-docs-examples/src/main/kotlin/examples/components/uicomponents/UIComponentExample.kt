/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package examples.components.uicomponents

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

fun main() {
  UIComponentExample()
}

class UIComponentExample : BoardGameApplication("UIComponent Example") {
  private val menuScene = MenuScene(width = 800).apply { this.opacity = 1.0 }

  private val outputLabel =
      Label(
          posX = 50,
          posY = 50,
          width = 300,
          text = "I am a Label.",
          alignment = Alignment.CENTER,
          isWrapText = true)

  init {
    menuScene.addComponents(outputLabel)

    // Button
    val button =
        Button(posX = 450, posY = 50, text = "I am a Button.", visual = ColorVisual.LIGHT_GRAY)

    button.onMouseClicked = { outputLabel.text = "Someone pressed the Button!" }

    menuScene.addComponents(button)

    // CheckBox
    val checkBox =
        CheckBox(
            posX = 50,
            posY = 150,
            width = 300,
            text = "I am a CheckBox.",
            alignment = Alignment.CENTER_LEFT)

    checkBox.isIndeterminateAllowed = true

    checkBox.onCheckedChanged = { newValue ->
      outputLabel.text =
          if (newValue) "The check box is checked!" else "The check box is unchecked!"
    }

    checkBox.onIndeterminateChanged = { newValue ->
      if (newValue) outputLabel.text = "The check box is indeterminate!"
    }

    menuScene.addComponents(checkBox)

    // ColorPicker
    val colorPicker = ColorPicker(posX = 450, posY = 200, width = 300, initialColor = Color.BLACK)

    val colorPickerLabel =
        Label(
                posX = colorPicker.posX,
                posY = colorPicker.posY - 50,
                width = colorPicker.width,
                height = 50,
                alignment = Alignment.CENTER,
                font = Font(color = colorPicker.selectedColor),
                text = "This is a ColorPicker. Use it to change the colour of this text!")
            .apply { isWrapText = true }

    colorPicker.onColorSelected = { newValue -> colorPickerLabel.font = Font(color = newValue) }

    menuScene.addComponents(colorPicker, colorPickerLabel)

    // ComboBox
    val comboBox =
        ComboBox<Double>(
            posX = 50, posY = 350, width = 300, prompt = "Select an option! This is the prompt.")

    comboBox.formatFunction = { "Option ${it.toInt()}" }

    comboBox.items = listOf(0.0, 1.0, 2.0)

    comboBox.onItemSelected = { newValue ->
      outputLabel.text = "Combo box selection is : $newValue"
    }

    val comboBoxLabel =
        Label(
            posX = comboBox.posX,
            posY = comboBox.posY - 50,
            width = comboBox.width,
            height = comboBox.height,
            alignment = Alignment.CENTER,
            text = "This is a ComboBox")

    menuScene.addComponents(comboBox, comboBoxLabel)

    // ProgressBar
    val progressBar =
        ProgressBar(posX = 450, posY = 350, width = 300, progress = 0.5, barColor = Color.GREEN)

    progressBar.onMouseClicked = {
      progressBar.progress = if (progressBar.progress > 1.0) 0.0 else progressBar.progress + 0.05
    }

    progressBar.onProgressed = { newValue ->
      when {
        newValue > 0.8 -> progressBar.barColor = Color.RED
        newValue > 0.5 -> progressBar.barColor = Color.YELLOW
        else -> progressBar.barColor = Color.GREEN
      }
    }

    val progressBarLabel =
        Label(
            posX = progressBar.posX,
            posY = progressBar.posY - 50,
            width = progressBar.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a ProgressBar. Click it to progress it!")

    menuScene.addComponents(progressBar, progressBarLabel)

    // RadioButton and ToggleButton
    val toggleGroup = ToggleGroup()

    val radioButton = RadioButton(posX = 50, posY = 450, toggleGroup = toggleGroup)

    val radioLabel =
        Label(
                posX = radioButton.posX + radioButton.width,
                posY = radioButton.posY,
                width = 300 - radioButton.width,
                height = radioButton.height,
                text = "This is a RadioButton.",
                alignment = Alignment.CENTER_LEFT)
            .apply { isWrapText = true }

    radioButton.onSelected = { radioLabel.text = "This is a selected radio button!" }

    radioButton.onDeselected = { radioLabel.text = "This is a deselected radio button!" }

    menuScene.addComponents(radioButton, radioLabel)

    val toggleButton = ToggleButton(posX = 450, posY = 450, toggleGroup = toggleGroup)

    val toggleLabel =
        Label(
                posX = toggleButton.posX + toggleButton.width,
                posY = toggleButton.posY,
                width = 300 - toggleButton.width,
                height = toggleButton.height,
                text = "This is a ToggleButton.",
                alignment = Alignment.CENTER_LEFT)
            .apply { isWrapText = true }

    toggleButton.onSelected = { -> toggleLabel.text = "This is a selected toggle button!" }

    toggleButton.onDeselected = { toggleLabel.text = "This is a deselected toggle button!" }

    menuScene.addComponents(toggleButton, toggleLabel)

    // TextArea
    val textArea = TextArea(posX = 50, posY = 600, prompt = "Type something! This is the prompt.")
    textArea.onTextChanged = { newValue -> outputLabel.text = newValue }

    val textAreaLabel =
        Label(
            posX = textArea.posX,
            posY = textArea.posY - 50,
            width = textArea.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a TextArea.")

    menuScene.addComponents(textArea, textAreaLabel)

    // TextField
    val textField =
        TextField(
            posX = 450, posY = 600, width = 300, prompt = "Type something! This is the prompt.")
    textField.onTextChanged = { newValue -> outputLabel.text = newValue }

    val textFieldLabel =
        Label(
            posX = textField.posX,
            posY = textField.posY - 50,
            width = textField.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a TextField.")

    menuScene.addComponents(textField, textFieldLabel)

    // ListView
    val listView =
        ListView(
                posX = 50,
                posY = 800,
                width = 300,
                height = 200,
                items = mutableListOf(42, 1337, 1, 2, 3))
            .apply { formatFunction = { "Value for this cell is $it" } }

    listView.onSelectionChanged = { newValue -> println(newValue) }

    val listViewLabel =
        Label(
            posX = listView.posX,
            posY = listView.posY - 50,
            width = listView.width,
            height = 50,
            text = "This is a ListView.",
            alignment = Alignment.CENTER)

    menuScene.addComponents(listView, listViewLabel)

    // TableView
    val table = TableView<Int>(posX = 450, posY = 800, width = 300, height = 200,
        selectionMode = SelectionMode.SINGLE)

      table.onSelectionChanged = { newValue -> println(newValue) }

    table.columns.add(TableColumn(title = "Value", width = 100) { "$it" })

    table.columns.add(TableColumn(title = "Squared", width = 100) { "${it * it}" })

    table.columns.add(TableColumn(title = "Even?", width = 100) { "${it % 2 == 0}" })

    table.items.addAll(mutableListOf(42, 1337, 1, 2, 3))

    val tableLabel =
        Label(
            posX = table.posX,
            posY = table.posY - 50,
            width = table.width,
            height = 50,
            text = "This is a TableView.",
            alignment = Alignment.CENTER)

    menuScene.addComponents(table, tableLabel)

    showMenuScene(menuScene)
    show()
  }
}
